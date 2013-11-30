package net.codelab.core.lucene.index.generic.providers;

import net.codelab.core.entity.dto.ResultsDTO;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Melzarek on 29/11/13.
 */
@Service
public class DefaultSearchProviderImpl implements SearchProvider {

    @Override
    public ResultsDTO<Document> performSearch(IndexSearcher searcher, Filter filter, Query query, int hitsnumber) throws IOException {
        ResultsDTO results = new ResultsDTO<>();
        long start = System.currentTimeMillis();
        TopDocs topDocs = (filter == null) ? searcher.search (query,hitsnumber) : searcher.search(query,filter,hitsnumber);
        long end = System.currentTimeMillis();
        results.setSearchtime(end - start);
        results.setTotalhits(topDocs.totalHits);
        for(ScoreDoc scoreDoc : topDocs.scoreDocs)
        {
            results.getResults().add(searcher.getIndexReader().document(scoreDoc.doc));
        }
        return results;
    }

}
