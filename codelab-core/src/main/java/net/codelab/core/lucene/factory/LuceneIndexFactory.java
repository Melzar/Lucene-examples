package net.codelab.core.lucene.factory;

import net.codelab.core.lucene.index.generic.*;
import net.codelab.core.lucene.index.generic.providers.*;

import java.io.IOException;

/**
 * Created by Melzarek on 29/11/13.
 */
public interface LuceneIndexFactory {

    public <T> LuceneIndex<T> createLuceneIndex(String directoryPath, IndexDataProvider indexDataProvider ,DirectoryProvider directoryProvider, MappingProvider mappingProvider,
                                          AnalyzerProvider analyzerProvider, SearchProvider searchProvider, SuggestingProvider suggestingProvider) throws IOException;

}
