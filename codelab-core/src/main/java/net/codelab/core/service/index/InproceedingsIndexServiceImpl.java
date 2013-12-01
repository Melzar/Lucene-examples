package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Inproceedings;
import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.lucene.index.generic.xml.GenericXMLHandler;
import net.codelab.core.lucene.factory.LuceneIndexFactoryImpl;
import net.codelab.core.lucene.index.generic.LuceneIndex;
import net.codelab.core.lucene.index.generic.providers.AnalyzerProvider;
import net.codelab.core.lucene.index.generic.providers.DirectoryProvider;
import net.codelab.core.lucene.index.generic.providers.SearchProvider;
import net.codelab.core.lucene.inproceedings.providers.InproceedingsMappingProvider;
import net.codelab.core.service.parse.XMLParsingService;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

/**
 * Created by Melzarek on 01/12/13.
 */
@Service
public class InproceedingsIndexServiceImpl implements InproceedingsIndexService {

    public final String INDEX_DIRECTORY = "index/inproceedings";

    private LuceneIndex<Inproceedings> index;

    @Autowired
    private AnalyzerProvider defaultAnalyzerProvider;

    @Autowired
    private DirectoryProvider directoryProvider;

    @Autowired
    private SearchProvider defaultSearchProvider;

    @Autowired
    private InproceedingsMappingProvider inproceedingsMappingProvider;

    @Autowired
    private XMLParsingService parsingService;

    public InproceedingsIndexServiceImpl() {
    }

    @PostConstruct
    private void getOrCreateIndex() {
        try {
            index = new LuceneIndexFactoryImpl().createLuceneIndex(INDEX_DIRECTORY,this,directoryProvider,inproceedingsMappingProvider,defaultAnalyzerProvider,defaultSearchProvider,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchAndParseXMLData() throws IOException {
        index.initializeFetchingDirectlyToIndex();
        long start = System.currentTimeMillis();
        parsingService.parseXmlFromURL(DATA_URL, new GenericXMLHandler(Inproceedings.class, index));
        long end = System.currentTimeMillis();
        System.out.println("Directly indexing time : " + (end-start) + " ms");
        index.terminateFetchingDirectlyToIndex();
    }

    @Override
    public ResultsDTO<Inproceedings> getInproceedingByTitle(String query) throws IOException {
        BooleanQuery booleanQuery = new BooleanQuery();
        TokenStream tokenStream = defaultAnalyzerProvider.getIndexAnalyzer().tokenStream("title", new StringReader(query));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while(tokenStream.incrementToken())
        {
            Term term = new Term("title", charTermAttribute.toString());
            booleanQuery.add(new PrefixQuery(term), BooleanClause.Occur.SHOULD);
        }
        tokenStream.end();
        tokenStream.close();
        return index.searchItems(booleanQuery,null,50);
    }

}
