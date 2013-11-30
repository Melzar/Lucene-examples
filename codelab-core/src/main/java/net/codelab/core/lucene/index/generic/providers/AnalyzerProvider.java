package net.codelab.core.lucene.index.generic.providers;

import org.apache.lucene.analysis.Analyzer;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface AnalyzerProvider {

    public Analyzer getIndexAnalyzer();

}
