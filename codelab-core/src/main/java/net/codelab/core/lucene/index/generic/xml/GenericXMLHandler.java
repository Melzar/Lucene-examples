package net.codelab.core.lucene.index.generic.xml;

import net.codelab.core.lucene.index.generic.LuceneIndex;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Melzarek on 29/11/13.
 *
 * GenericXMLHandler implementation to preserve dry code and
 * minimize the boilerplate of SaxParser methods
 *
 * This handler uses reflection to parse xml to given class type, its fields etc.
 * It is important to preserve class and fields name corresponding to node in xml source.
 *
 * For example if you have <course><id>1</id><name>example</name></course> in xml, your class
 * name has to be "Course" and fields should be "id" and "name". If you do not include given field in
 * object class template it won't be parsed (will remain null).
 *
 * Even if this implementation uses reflection it is about 20 - 40 % faster than "standard" implementation using
 * many comparisons and booleans.
 *
 *
 * 1.12.2013 - add support for multivalued fields, your multivalued field has to be instanced as Collections object
 * - also add support for dirty sources (if you have other "xml" objects than you predict)
 *
 * @param <T>
 */
public class GenericXMLHandler<T> extends DefaultHandler {

    private Class<T> clazz;
    private T classobject ;
    private Map<String,Boolean> classfields;
    private LuceneIndex<T> index;

    public GenericXMLHandler(Class<T> clazz, LuceneIndex<T> index) {
        this.clazz = clazz;
        this.classfields = new HashMap<>();
        this.index = index;
        prepareFields();
    }

    private void prepareFields()
    {
        for(Field f : clazz.getDeclaredFields())
        {
           classfields.put(f.getName(),false);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        if(qName.equalsIgnoreCase(clazz.getSimpleName()))
        {
            try {
                classobject = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new SAXException("Failed to instantiate generic type", e);
            } catch (IllegalAccessException e) {
                throw new SAXException("Failed to instantiate generic type", e);
            }
        }
        else if(classfields.keySet().contains(qName) && classobject != null)
        {
            classfields.put(qName,true);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(clazz.getSimpleName())) {
            try {
                index.addWithoutCommit(classobject);
            } catch (IOException e) {
                throw new SAXException("Failed to add generic object to index", e);
            }
            classobject = null;
        }
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
        for(String key : classfields.keySet())
        {
            if(classfields.get(key))
            {
                try {
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    if(field.getType() == Collection.class)
                    {
                       Collections.addAll((Collection<String>) field.get(classobject),new String(ch, start, length));
                    }
                    else
                    {
                        field.set(classobject, new String(ch, start, length));
                    }
                    classfields.put(key, false);
                    break;
                } catch (IllegalAccessException e) {
                    throw new SAXException("Failed to access generic field", e);
                } catch (NoSuchFieldException e) {
                    throw new SAXException("Failed to find generic method", e);
                }
            }
        }
    }
}
