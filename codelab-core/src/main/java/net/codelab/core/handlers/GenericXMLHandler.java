package net.codelab.core.handlers;

import net.codelab.core.entity.dto.Course;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
 * !IMPORTANT this implementation loads all chosen nodes to list and then returns it. It is memory consuming so this
 * is rather soultion for smaller files or for system with enough memory. If you don't like this solution you should
 * try child implementation "GenericIndexXmlHandler" which you can extend and adjust to your needs
 *
 * 1.12.2013 - add support for multivalued fields, your multivalued field has to be instanced as Collections object
 * - also add support for dirty sources (if you have other "xml" objects than you predict)
 *
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

    protected Class<T> getClazz(){ return clazz ;}

    protected T getParsedObject() { return classobject ;}

    protected void setParsedObject (T object) { classobject = object ;}

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
            classobjects.add(classobject);
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
