package net.codelab.core.service.parse;

import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by Melzarek on 21/11/13.
 */

@Service
public class XMLParsingServiceImpl implements XMLParsingService {

    //TODO secure procesing final

    public XMLParsingServiceImpl()
    {

    }

    @Override
    public <T extends DefaultHandler> T parseXmlFromURL(String URL, T handler) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            parserFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
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

