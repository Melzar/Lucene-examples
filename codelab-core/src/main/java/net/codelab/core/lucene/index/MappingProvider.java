package net.codelab.core.lucene.index;

import org.apache.lucene.document.Document;

import java.util.List;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface MappingProvider<T> {

    public Document convertItemToDocument(T item);

    public List<Document> convertItemsToDocument(List<T> items);

    public T convertDocumentToItem(Document document);

    public List<T> convertDocumentsToItems(List<Document> documents);
}
