package net.codelab.core.lucene.factory;

import net.codelab.core.lucene.index.generic.*;
import net.codelab.core.lucene.index.generic.providers.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Melzarek on 29/11/13.
 */

@Service
public class LuceneIndexFactoryImpl implements LuceneIndexFactory {

    @Override
    public <T> LuceneIndex<T> createLuceneIndex(String directorypath, IndexDataProvider indexDataProvider,DirectoryProvider directoryProvider, MappingProvider mappingProvider, AnalyzerProvider analyzerProvider, SearchProvider searchProvider, SuggestingProvider suggestingProvider) throws IOException {
        Directory dir = directoryProvider.openOrCreateIndexDirectory(getClass().getClassLoader().getResource("resources").getPath().concat(directorypath));
        if(dir.listAll().length == 0)
        {
            IndexWriterConfig conf = new IndexWriterConfig(LuceneIndex.LUCENE_VERSION, analyzerProvider.getIndexAnalyzer());
            IndexWriter indexWriter = new IndexWriter(dir, conf);
            indexWriter.close();
        }
        IndexIOProvider indexIOProvider = new IndexIOProvider(dir);
        return new LuceneIndex<>(indexIOProvider, indexDataProvider, mappingProvider,analyzerProvider,searchProvider);
    }

}
