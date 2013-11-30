package net.codelab.core.lucene.index.generic.field;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.search.vectorhighlight.FieldQuery;

import java.io.Reader;

/**
 * Created by Melzarek on 27/11/13.
 */
public class TVectorTextField extends Field {

    public static final FieldType TEXT_TYPE = new FieldType();

    static{
        TEXT_TYPE.setIndexed(true);
        TEXT_TYPE.setStored(true);
        TEXT_TYPE.setTokenized(true);
        TEXT_TYPE.setStoreTermVectors(true);
        TEXT_TYPE.setStoreTermVectorPositions(true);
        TEXT_TYPE.setStoreTermVectorOffsets(true);
        TEXT_TYPE.setStoreTermVectorPayloads(true);
        TEXT_TYPE.freeze();
    }


    protected TVectorTextField(String name, FieldType type) {
        super(name, type);
    }

    public TVectorTextField(String name, Reader reader, FieldType type) {
        super(name, reader, type);
    }

    public TVectorTextField(String name, TokenStream tokenStream, FieldType type) {
        super(name, tokenStream, type);
    }

    public TVectorTextField(String name, String value, FieldType type) {
        super(name, value, type);
    }
}
