package net.codelab.core.lucene.index.course.providers;

import net.codelab.core.lucene.index.generic.providers.IndexDataProvider;

/**
 * Created by Melzarek on 29/11/13.
 */
public interface CourseDataProvider extends IndexDataProvider {

    public final String DATA_URL = "http://www.cs.washington.edu/research/xmldatasets/data/courses/reed.xml";
}
