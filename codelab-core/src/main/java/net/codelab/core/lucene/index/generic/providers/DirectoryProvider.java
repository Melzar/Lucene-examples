package net.codelab.core.lucene.index.generic.providers;

import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface DirectoryProvider {

    public Directory openOrCreateIndexDirectory(String directoryPath) throws IOException;

}
