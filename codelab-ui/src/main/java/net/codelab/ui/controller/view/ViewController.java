package net.codelab.ui.controller.view;

import net.codelab.core.service.parse.XMLParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Melzarek on 20/11/13.
 */

@Controller
public class ViewController
{
    @Autowired
    XMLParsingService parsingService;

    @RequestMapping("/")
    public String prepareIndex(Model model)
    {
         return "index";
    }

}
