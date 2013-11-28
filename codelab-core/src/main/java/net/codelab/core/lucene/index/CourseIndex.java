package net.codelab.core.lucene.index;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.handlers.xml.CourseXMLHandler;
import org.apache.lucene.document.*;
import org.apache.lucene.index.FieldInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 21/11/13.
 */
public class CourseIndex extends AbstractLuceneIndex<Course> {

    public static final String COURSE_INDEX_PATH = "./resources/index/course";

    public CourseIndex(String indexDirectoryPath) {
        super(indexDirectoryPath);
    }

    @Override
    public Document convertToDocument(Course course) {
            Document document = new Document();
            document.add(new StringField("reg_num", course.getReg_num(), Field.Store.YES));
            document.add(new TextField("subj", course.getSubj(), Field.Store.YES));
            document.add(new StringField("crse", course.getCrse(), Field.Store.YES));
            document.add(new StringField("sect", course.getSect(), Field.Store.YES));
            document.add(new TVectorTextField("title", course.getTitle(), TVectorTextField.TEXT_TYPE));
            document.add(new StringField("units", course.getUnits(), Field.Store.YES));
            document.add(new TextField("instructor", course.getInstructor(), Field.Store.YES));
            document.add(new StringField("days",course.getDays(),Field.Store.YES));
            document.add(new StringField("start_time", course.getStart_time(), Field.Store.YES));
            document.add(new StringField("end_time", course.getEnd_time(), Field.Store.YES));
            document.add(new StringField("building", course.getBuilding(), Field.Store.YES));
            document.add(new StringField("room", course.getRoom(), Field.Store.YES));
            return document;
    }

    @Override
    public List<Document> convertToDocumentBulk(List<Course> item) {
        List<Document> courseDocuments = new LinkedList<Document>();
        for(Course course : item)
        {
           courseDocuments.add(convertToDocument(course));
        }
        return courseDocuments;
    }

    @Override
    public Course convertFromDocument(Document doc) {
        Course course = new Course();
        course.setReg_num(doc.getField("reg_num").stringValue());
        course.setSubj(doc.getField("subj").stringValue());
        course.setCrse(doc.getField("crse").stringValue());
        course.setSect(doc.getField("sect").stringValue());
        course.setTitle(doc.getField("title").stringValue());
        course.setUnits(doc.getField("units").stringValue());
        course.setInstructor(doc.getField("instructor").stringValue());
        course.setDays(doc.getField("days").stringValue());
        course.setStart_time(doc.getField("start_time").stringValue());
        course.setEnd_time(doc.getField("end_time").stringValue());
        course.setBuilding(doc.getField("building").stringValue());
        course.setRoom(doc.getField("room").stringValue());
        return course;
    }

    @Override
    public List<Course> convertFromDocumentBulk(List<Document> doc) {
        List<Course> courses = new LinkedList<Course>();
        for(Document document : doc)
        {
            courses.add(convertFromDocument(document));
        }
        return courses;
    }
}
