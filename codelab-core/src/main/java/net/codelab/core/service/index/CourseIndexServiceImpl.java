package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.handlers.xml.CourseXMLHandler;
import net.codelab.core.lucene.index.CourseIndex;
import net.codelab.core.lucene.index.ResultsDTO;
import net.codelab.core.service.parse.XMLParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Melzarek on 21/11/13.
 */

@Service
public class CourseIndexServiceImpl implements CourseIndexService {

    private final CourseIndex courseIndex;

    @Autowired
    private XMLParsingService parsingService;

    private final String courseDataURL = "http://www.cs.washington.edu/research/xmldatasets/data/courses/reed.xml";

    public CourseIndexServiceImpl() {
        courseIndex = new CourseIndex(CourseIndex.COURSE_INDEX_PATH);
    }

    @Override
    public void reciveCourseData() {
           CourseXMLHandler datahandler = parsingService.parseXmlFromURL(courseDataURL, new CourseXMLHandler());
           courseIndex.startCreateNewIndex();
           courseIndex.addItemToIndexBulk(datahandler.getCourses());
           courseIndex.endIndexOperations();
    }

    @Override
    public ResultsDTO<Course> getCourseIndexData(String query) {
          return courseIndex.searchIndex(query);
    }
}
