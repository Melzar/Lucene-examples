package net.codelab.core.lucene.index.course.providers;

import net.codelab.core.entity.dto.Course;
import net.codelab.core.lucene.index.generic.field.TVectorTextField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Melzarek on 29/11/13.
 */

@Service
public class CourseMappingProviderImpl implements CourseMappingProvider {

    @Override
    public Document convertItemToDocument(Course item) {
        Document document = new Document();
        document.add(new StringField("reg_num", item.getReg_num(), Field.Store.YES));
        document.add(new TextField("subj", item.getSubj(), Field.Store.YES));
        document.add(new StringField("crse", item.getCrse(), Field.Store.YES));
        document.add(new StringField("sect", item.getSect(), Field.Store.YES));
        document.add(new TVectorTextField("title", item.getTitle(), TVectorTextField.TEXT_TYPE));
        if(item.getUnits() == null)
        {
            System.out.println(item.getUnits());
        }
        document.add(new StringField("units", item.getUnits(), Field.Store.YES));
        document.add(new TextField("instructor", item.getInstructor(), Field.Store.YES));
        document.add(new StringField("days",item.getDays(),Field.Store.YES));
        document.add(new StringField("start_time", item.getStart_time(), Field.Store.YES));
        document.add(new StringField("end_time", item.getEnd_time(), Field.Store.YES));
        document.add(new StringField("building", item.getBuilding(), Field.Store.YES));
        document.add(new StringField("room", item.getRoom(), Field.Store.YES));
        return document;
    }

    @Override
    public List<Document> convertItemsToDocument(Collection<Course> items) {
        List<Document> documents = new LinkedList<Document>();
        for(Course course : items)
        {
            documents.add(convertItemToDocument(course));
        }
        return documents;
    }

    @Override
    public Course convertDocumentToItem(Document document) {
        Course course = new Course();
        course.setReg_num(document.getField("reg_num").stringValue());
        course.setSubj(document.getField("subj").stringValue());
        course.setCrse(document.getField("crse").stringValue());
        course.setSect(document.getField("sect").stringValue());
        course.setTitle(document.getField("title").stringValue());
        course.setUnits(document.getField("units").stringValue());
        course.setInstructor(document.getField("instructor").stringValue());
        course.setDays(document.getField("days").stringValue());
        course.setStart_time(document.getField("start_time").stringValue());
        course.setEnd_time(document.getField("end_time").stringValue());
        course.setBuilding(document.getField("building").stringValue());
        course.setRoom(document.getField("room").stringValue());
        return course;
    }

    @Override
    public List<Course> convertDocumentsToItems(Collection<Document> list) {
        List<Course> courses = new LinkedList<Course>();
        for(Document document : list)
        {
            courses.add(convertDocumentToItem(document));
        }
        return courses;
    }

    @Override
    public Term getIdentifier(Course item) {
        return new Term("reg_num", item.getReg_num());
    }
}
