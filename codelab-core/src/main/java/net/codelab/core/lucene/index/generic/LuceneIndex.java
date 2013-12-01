package net.codelab.core.lucene.index.generic;

import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.lucene.index.generic.providers.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Melzarek on 25/11/13.
 */
public class LuceneIndex <T>{

    public static final Version LUCENE_VERSION = Version.LUCENE_46;

    private MappingProvider mappingProvider;

    private AnalyzerProvider analyzerProvider;

    private SearchProvider searchProvider;

    private IndexIOProvider indexIOProvider;

    private SearcherManager searcherManager;

    private IndexDataProvider indexDataProvider;

    //private SuggestingProvider suggestingProvider;

    //TODO Add log implementation to places where exceptions are being catched
    //TODO Add concurency writers for indexing

    public LuceneIndex(IndexIOProvider indexIOProvider, IndexDataProvider indexDataProvider, MappingProvider mappingProvider,
                       AnalyzerProvider analyzerProvider, SearchProvider searchProvider) throws IOException {
        this.mappingProvider = mappingProvider;
        this.analyzerProvider = analyzerProvider;
        this.searchProvider = searchProvider;
        this.searcherManager = new SearcherManager(indexIOProvider.getDirectory(),null);
        this.indexDataProvider = indexDataProvider;
        this.indexIOProvider = indexIOProvider;
    }

    public ResultsDTO<T> searchItems(Query query, Filter filter, int hitsnum) throws IOException {
        ResultsDTO<T> results = new ResultsDTO<>();
        IndexSearcher indexSearcher = searcherManager.acquire();
        ResultsDTO<Document> rawResults = searchProvider.performSearch(indexSearcher, null, query, hitsnum);
        searcherManager.release(indexSearcher);
        results.setSearchtime(rawResults.getSearchtime());
        results.setTotalhits(rawResults.getTotalhits());
        results.setHiglights(rawResults.getHiglights());
        results.setSuggestions(rawResults.getSuggestions());
        results.setResults(mappingProvider.convertDocumentsToItems(rawResults.getResults()));
        return results;
    }

    public void initializeFetchingDirectlyToIndex() throws IOException {
        indexIOProvider.initializeWriter(analyzerProvider).deleteAll();
    }

    public void terminateFetchingDirectlyToIndex() throws IOException {
        indexIOProvider.saveChanges();
        indexIOProvider.terminateWriter();
        searcherManager.maybeRefresh();
    }

    public void addWithoutCommit(T item) throws IOException {
        IndexWriter writer = indexIOProvider.getWriter();
            try
            {
                writer.addDocument(mappingProvider.convertItemToDocument(item));
            }catch(IOException e)
            {
                writer.rollback();
                throw e;
            }
    }

    public void addItem(T item) throws IOException {
        addItems(Collections.singletonList(item));
    }

    public void addItems(List<T> list) throws IOException {
        try(IndexWriter indexWriter = indexIOProvider.initializeWriter(analyzerProvider)) {
          try
          {
            indexWriter.addDocuments(mappingProvider.convertItemsToDocument(list));
            indexIOProvider.saveChanges();
          } catch (IOException e) {
            indexWriter.rollback();
            throw e;
          }
        } finally {
            indexIOProvider.terminateWriter();
        }
        searcherManager.maybeRefresh();
    }

    public void updateItem(T item) throws IOException {
       updateItems(Collections.singletonList(item));
    }

    public void updateItems(List<T> list) throws IOException {
        try(IndexWriter indexWriter = indexIOProvider.initializeWriter(analyzerProvider)) {
            try
            {
                for(T item : list)
                {
                    indexWriter.updateDocument(mappingProvider.getIdentifier(item), mappingProvider.convertItemToDocument(item));
                }
                indexIOProvider.saveChanges();
            } catch (IOException e) {
                indexWriter.rollback();
                throw e;
            }
        } finally {
            indexIOProvider.terminateWriter();
        }
        searcherManager.maybeRefresh();
    }

    public void deleteItem(T item) throws IOException {
        deleteItems(Collections.singletonList(item));
    }

    public void deleteItems(List<T> list) throws IOException {
        try(IndexWriter indexWriter = indexIOProvider.initializeWriter(analyzerProvider)) {
            try
            {
                for(T item : list)
                {
                    indexWriter.deleteDocuments(mappingProvider.getIdentifier(item));
                }
                indexIOProvider.saveChanges();
            } catch (IOException e) {
                indexWriter.rollback();
                throw e;
            }
        } finally {
            indexIOProvider.terminateWriter();
        }
        searcherManager.maybeRefresh();
    }

}
