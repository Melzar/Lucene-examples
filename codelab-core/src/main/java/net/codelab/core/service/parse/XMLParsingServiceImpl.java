package net.codelab.core.service.parse;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.handlers.xml.CourseXMLHandler;
import org.apache.lucene.document.*;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 21/11/13.
 */

@Service
public class XMLParsingServiceImpl implements XMLParsingService {



    public XMLParsingServiceImpl()
    {

    }

    @Override
    public <T extends DefaultHandler> T parseXmlFromURL(String URL, T handler) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(URL, handler);
            return handler;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

