package net.codelab.core.handlers;

import net.codelab.core.lucene.index.generic.LuceneIndex;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Created by Melzarek on 03/12/13.
 */
public class GenericIndexXMLHandler<T> extends  GenericXMLHandler {

    private LuceneIndex<T> luceneIndex;

    public GenericIndexXMLHandler(Class<T> clazz, LuceneIndex<T> luceneIndex) {
        super(clazz);
        this.luceneIndex = luceneIndex;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        super.startElement(uri,localName,qName,attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(super.getClazz().getSimpleName())) {
            try {
                luceneIndex.addWithoutCommit((T) super.getParsedObject());
                super.setParsedObject(null);
            } catch (IOException e) {
                throw new SAXException("Failed to add generic object to index", e);
            }
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
        super.characters(ch,start,length);
    }

}
