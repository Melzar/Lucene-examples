package net.codelab.core.lucene.index.generic.providers;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Melzarek on 29/11/13.
 */
public interface IndexDataProvider {

    public void fetchAndParseXMLData() throws IOException;

}
