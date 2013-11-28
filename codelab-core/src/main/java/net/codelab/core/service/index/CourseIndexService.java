package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.lucene.index.ResultsDTO;

import java.util.List;

/**
 * Created by Melzarek on 21/11/13.
 */
public interface CourseIndexService {

       public void reciveCourseData();

       public ResultsDTO<Course> getCourseIndexData(String query);
}
