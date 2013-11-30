package net.codelab.core.lucene.index.generic.providers;

import net.codelab.core.entity.dto.ResultsDTO;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Created by Melzarek on 28/11/13.
 */
public interface SearchProvider {

    public ResultsDTO<Document> performSearch(IndexSearcher searcher, Filter filter, Query query, int hitsnumber) throws IOException;
}
