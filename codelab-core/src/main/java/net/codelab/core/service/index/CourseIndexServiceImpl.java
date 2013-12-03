package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.handlers.GenericIndexXMLHandler;
import net.codelab.core.handlers.GenericXMLHandler;
import net.codelab.core.lucene.factory.LuceneIndexFactoryImpl;
import net.codelab.core.lucene.index.generic.providers.AnalyzerProvider;
import net.codelab.core.lucene.index.generic.providers.DirectoryProvider;
import net.codelab.core.lucene.index.generic.providers.SearchProvider;
import net.codelab.core.lucene.index.course.providers.CourseMappingProvider;
import net.codelab.core.lucene.index.generic.LuceneIndex;
import net.codelab.core.service.parse.XMLParsingService;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PrefixQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Melzarek on 21/11/13.
 */

@Service
public class CourseIndexServiceImpl implements CourseIndexService {

    public final String INDEX_DIRECTORY = "index/courses";

    private LuceneIndex<Course> index;

    @Autowired
    private AnalyzerProvider defaultAnalyzerProvider;

    @Autowired
    private DirectoryProvider directoryProvider;

    @Autowired
    private SearchProvider defaultSearchProvider;

    @Autowired
    private CourseMappingProvider courseMappingProvider;

    @Autowired
    private XMLParsingService xmlParsingService;

    //Suggesting provider

    public CourseIndexServiceImpl() {

    }

    @PostConstruct
    private void getOrCreateIndex() {
        try {
            index = new LuceneIndexFactoryImpl().createLuceneIndex(INDEX_DIRECTORY,this,directoryProvider,courseMappingProvider,defaultAnalyzerProvider,defaultSearchProvider,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fetchAndParseXMLData() throws IOException {
        index.initializeFetchingDirectlyToIndex();
        xmlParsingService.parseXmlFromURL(DATA_URL, new GenericIndexXMLHandler(Course.class, index));
        index.terminateFetchingDirectlyToIndex();
    }

    @Override
    public ResultsDTO<Course> getCourseByTitle(String query) throws IOException {
        BooleanQuery booleanQuery = new BooleanQuery();
        TokenStream stream = defaultAnalyzerProvider.getIndexAnalyzer().tokenStream("title", new StringReader(query));
        CharTermAttribute charterm = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        while(stream.incrementToken())
        {
            Term term = new Term("title", charterm.toString());
            booleanQuery.add(new FuzzyQuery(term, 1), BooleanClause.Occur.SHOULD);
            booleanQuery.add(new PrefixQuery(term), BooleanClause.Occur.SHOULD);
        }
        stream.end();
        stream.close();
        DuplicateFilter duplicateFilter = new DuplicateFilter("title");
        duplicateFilter.setKeepMode(DuplicateFilter.KeepMode.KM_USE_LAST_OCCURRENCE);
        return index.searchItems(booleanQuery,duplicateFilter,50);
    }

}
