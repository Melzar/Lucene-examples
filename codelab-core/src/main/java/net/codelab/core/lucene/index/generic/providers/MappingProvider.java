package net.codelab.core.lucene.index.generic.providers;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

import java.util.Collection;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface MappingProvider<T> {

    public Document convertItemToDocument(T item);

    public Collection<Document> convertItemsToDocument(Collection<T> items);

    public T convertDocumentToItem(Document document);

    public Collection<T> convertDocumentsToItems(Collection<Document> documents);

    public Term getIdentifier(T item);
}
