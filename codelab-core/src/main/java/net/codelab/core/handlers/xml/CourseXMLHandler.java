package net.codelab.core.handlers.xml;

import net.codelab.core.entity.dto.Course;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Melzarek on 21/11/13.
 */
public class CourseXMLHandler extends DefaultHandler {

    public Course course = null;
    public List<Course> courses = null;

    boolean breg_num;
    boolean bsub;
    boolean bcrse;
    boolean bsect;
    boolean btitle;
    boolean bunits;
    boolean binstructor;
    boolean bdays;
    boolean bstart_time;
    boolean bend_time;
    boolean bbuilding;
    boolean broom;

    public CourseXMLHandler()
    {

    }

    public List<Course> getCourses()
    {
        return courses;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {

        switch(qName)
        {
            case "course":
                course = new Course();
                if (courses == null)
                {
                    courses = new LinkedList<Course>();
                }
                ; break;
            case "reg_num":
                breg_num = true;
                ; break;
            case "subj":
                bsub = true;
                ; break;
            case "crse":
                bcrse = true;
                ; break;
            case "sect":
                bsect = true;
                ; break;
            case "title":
                btitle = true;
                ; break;
            case "units":
                bunits = true;
                ; break;
            case "instructor":
                binstructor = true;
                ; break;
            case "days":
                bdays = true;
                ; break;
            case "start_time":
                bstart_time = true;
                ; break;
            case "end_time":
                bend_time = true;
                ; break;
            case "building":
                bbuilding = true;
                ; break;
            case "room":
                broom = true;
                ; break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Course")) {
            courses.add(course);
        }
    }


    @Override
    public void characters(char ch[], int start, int length) throws SAXException
    {
            if(breg_num){
                course.setReg_num(new String(ch, start, length));
                breg_num = false;
            } else if (bsub)
            {
                course.setSubj(new String(ch, start, length));
                bsub = false;
            } else if (bcrse)
            {
                course.setCrse(new String(ch, start, length));
                bcrse = false;
            } else if (bsect)
            {
                course.setSect(new String(ch, start, length));
                bsect = false;
            } else if (btitle)
            {
                course.setTitle(new String(ch, start, length));
                btitle = false;
            } else if (bunits)
            {
                course.setUnits(new String(ch, start, length));
                bunits = false;
            } else if (binstructor)
            {
                course.setInstructor(new String(ch, start, length));
                binstructor = false;
            } else if (bdays)
            {
                course.setDays(new String(ch, start, length));
                bdays = false;
            } else if (bstart_time)
            {
                course.setStart_time(new String(ch, start, length));
                bstart_time = false;
            } else if (bend_time)
            {
                course.setEnd_time(new String(ch, start, length));
                bend_time = false;
            } else if (bbuilding)
            {
                course.setBuilding(new String(ch, start, length));
                bbuilding = false;
            } else if (broom)
            {
                course.setRoom((ch == null) ? "" : new String(ch, start, length));
                broom = false;
            }
    }

}
