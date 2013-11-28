package net.codelab.core.service.parse;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.handlers.xml.CourseXMLHandler;
import org.apache.lucene.document.Document;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

/**
 * Created by Melzarek on 21/11/13.
 */
public interface XMLParsingService {

    public <T extends DefaultHandler> T  parseXmlFromURL(String URL, T handler);

}
