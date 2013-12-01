package net.codelab.core.lucene.inproceedings.providers;

import net.codelab.core.lucene.index.generic.providers.IndexDataProvider;

/**
 * Created by Melzarek on 01/12/13.
 */
public interface InproceedingsDataProvider extends IndexDataProvider {

    public final String DATA_URL = "http://www.cs.washington.edu/research/xmldatasets/data/dblp/dblp.xml";
}
