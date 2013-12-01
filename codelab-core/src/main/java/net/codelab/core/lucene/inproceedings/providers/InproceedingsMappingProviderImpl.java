package net.codelab.core.lucene.inproceedings.providers;

import net.codelab.core.entity.dto.Inproceedings;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Melzarek on 01/12/13.
 */
@Service
public class InproceedingsMappingProviderImpl implements InproceedingsMappingProvider {

    @Override
    public Document convertItemToDocument(Inproceedings item) {
        Document document = new Document();
        for(String author: item.getAuthor())
        {
            document.add(new TextField("author", author, Field.Store.YES));
        }
        document.add(new TextField("title", item.getTitle(), Field.Store.YES));
        document.add(new StringField("pages", item.getPages(), Field.Store.YES));
        document.add(new StringField("year", item.getYear(), Field.Store.YES));
        document.add(new StringField("booktitle", item.getTitle(), Field.Store.YES));
        document.add(new StringField("ee", item.getEe(), Field.Store.YES));
        document.add(new StringField("url",item.getUrl(), Field.Store.YES));
        return document;
    }

    @Override
    public Collection<Document> convertItemsToDocument(Collection<Inproceedings> items) {
        Collection<Document> documents = new LinkedList<>();
        for(Inproceedings inproceeding : items)
        {
            documents.add(convertItemToDocument(inproceeding));
        }
        return documents;
    }

    @Override
    public Inproceedings convertDocumentToItem(Document document) {
        Inproceedings inproceedings = new Inproceedings();
        for(IndexableField field : document.getFields("author"))
        {
            inproceedings.getAuthor().add(field.stringValue());
        }
        inproceedings.setTitle(document.getField("title").stringValue());
        inproceedings.setPages(document.getField("pages").stringValue());
        inproceedings.setYear(document.getField("year").stringValue());
        inproceedings.setBooktitle(document.getField("booktitle").stringValue());
        inproceedings.setEe(document.getField("ee").stringValue());
        inproceedings.setUrl(document.getField("url").stringValue());
        return inproceedings;
    }

    @Override
    public Collection<Inproceedings> convertDocumentsToItems(Collection<Document> documents){
        Collection<Inproceedings> inproceedings = new LinkedList<>();
        for(Document document : documents)
        {
            inproceedings.add(convertDocumentToItem(document));
        }
        return inproceedings;
    }

    @Override
    public Term getIdentifier(Inproceedings item) {
        return null;
    }
}
