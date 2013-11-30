package net.codelab.core.service.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.entity.dto.ResultsDTO;

import java.io.IOException;

/**
 * Created by Melzarek on 21/11/13.
 */
public interface CourseIndexService {

       public ResultsDTO<Course> getCourseByTitle(String query) throws IOException;

       public void reindex() throws IOException;
}
