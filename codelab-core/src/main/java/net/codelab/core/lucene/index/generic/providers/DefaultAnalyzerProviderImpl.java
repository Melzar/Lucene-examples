package net.codelab.core.lucene.index.generic.providers;

import net.codelab.core.lucene.index.generic.LuceneIndex;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.springframework.stereotype.Service;

/**
 * Created by Melzarek on 29/11/13.
 */

@Service
public class DefaultAnalyzerProviderImpl implements AnalyzerProvider {

    private Analyzer analyzer;

    @Override
    public Analyzer getIndexAnalyzer() {
        if(analyzer != null)
        {
            return analyzer;
        }
        return analyzer = new StandardAnalyzer(LuceneIndex.LUCENE_VERSION);
    }
}
