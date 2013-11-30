package net.codelab.core.lucene.index.generic.providers;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by Melzarek on 29/11/13.
 */
@Service
public class DefaultDirectoryProviderImpl implements DirectoryProvider {

    @Override
    public Directory openOrCreateIndexDirectory(String directoryPath) throws IOException {

        File file = new File(directoryPath);
        if(!file.exists())
        {
            file.mkdirs();
        }
        return new NIOFSDirectory(file);
    }
}
