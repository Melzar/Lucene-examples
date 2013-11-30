package net.codelab.core.lucene.index.generic.providers;

import net.codelab.core.lucene.index.generic.LuceneIndex;
import net.codelab.core.lucene.index.generic.providers.AnalyzerProvider;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * Created by Melzarek on 29/11/13.
 */
public class IndexIOProvider {

    private Directory directory;

    private IndexReader reader;

    private IndexWriter writer;

    private IndexWriterConfig indexWriterConfig;

    private IndexSearcher searcher;

    public IndexIOProvider(Directory directory) throws IOException {
       this.directory = directory;
       this.reader = DirectoryReader.open(directory);
    }

    public IndexWriter initializeWriter(AnalyzerProvider analyzerProvider) throws IOException {
        indexWriterConfig = new IndexWriterConfig(LuceneIndex.LUCENE_VERSION, analyzerProvider.getIndexAnalyzer());
        indexWriterConfig.setRAMBufferSizeMB(512);
        return writer = new IndexWriter(directory,indexWriterConfig);
    }

    public void terminateWriter() {
            indexWriterConfig = null;
            writer = null;
    }

    public IndexReader getReader() {
        return reader;
    }

    public void setReader(IndexReader reader) {
        this.reader = reader;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
}
