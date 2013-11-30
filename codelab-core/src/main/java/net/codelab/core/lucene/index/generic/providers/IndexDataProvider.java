package net.codelab.core.lucene.index.generic.providers;

import java.util.Collection;

/**
 * Created by Melzarek on 29/11/13.
 */
public interface IndexDataProvider<T> {

    public Collection<T> fetchAndParseXMLData();

}
