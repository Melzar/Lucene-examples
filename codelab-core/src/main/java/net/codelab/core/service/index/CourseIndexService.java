package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.entity.dto.ResultsDTO;
import net.codelab.core.lucene.index.course.providers.CourseDataProvider;

import java.io.IOException;

/**
 * Created by Melzarek on 21/11/13.
 */
public interface CourseIndexService extends CourseDataProvider {

       public ResultsDTO<Course> getCourseByTitle(String query) throws IOException;

}
