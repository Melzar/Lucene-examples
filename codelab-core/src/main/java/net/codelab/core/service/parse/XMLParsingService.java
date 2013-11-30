package net.codelab.core.service.parse;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Melzarek on 21/11/13.
 */
public interface XMLParsingService {

    public <T extends DefaultHandler> T  parseXmlFromURL(String URL, T handler);

}
