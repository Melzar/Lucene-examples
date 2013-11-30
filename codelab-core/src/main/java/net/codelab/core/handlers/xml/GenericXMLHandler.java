package net.codelab.core.handlers.xml;

import net.codelab.core.entity.dto.Course;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * @param <T>
 */
public class GenericXMLHandler<T> extends DefaultHandler {

    private Class<T> clazz;
    private T classobject ;
    private List<T> classobjects;
    private Map<String,Boolean> classfields;

    public GenericXMLHandler(Class<T> clazz) {
        this.clazz = clazz;
        this.classfields = new HashMap<>();
        this.classobjects = new LinkedList<>();
        prepareFields();
    }

    private void prepareFields()
    {
        for(Field f : clazz.getDeclaredFields())
        {
           classfields.put(f.getName(),false);
        }
    }

    public List<T> getParsedObjects()
    {
        return classobjects;
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
        else if(classfields.keySet().contains(qName))
        {
            classfields.put(qName,true);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase(clazz.getSimpleName())) {
            classobjects.add(classobject);
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
                    field.set(classobject, new String(ch, start, length));
                    classfields.put(key, false);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
