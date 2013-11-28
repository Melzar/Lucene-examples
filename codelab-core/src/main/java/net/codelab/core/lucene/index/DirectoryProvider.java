package net.codelab.core.lucene.index;

/**
 * Created by Melzarek on 25/11/13.
 */
public interface DirectoryProvider {

    public void openOrCreateIndexDirectory();

    public void getIndexDirectory();
}
