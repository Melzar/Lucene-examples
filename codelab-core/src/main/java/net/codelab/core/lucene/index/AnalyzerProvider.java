package net.codelab.core.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface AnalyzerProvider {

    public Analyzer STANDARD_ANALYZER = new StandardAnalyzer(Version.LUCENE_46);

    public Analyzer WHITESPACE_ANALYZER = new WhitespaceAnalyzer(Version.LUCENE_46);

    public Analyzer STOP_ANALYZER = new StopAnalyzer(Version.LUCENE_46);

    public Analyzer SIMPLE_ANALYZER = new SimpleAnalyzer(Version.LUCENE_46);

    public Analyzer getIndexAnalyzer();

}
