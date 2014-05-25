package com.tbart.university.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tbart on 5/25/2014.
 */
public interface MessageWorker {
    public void sentText(OutputStream os, String text, String userName) throws IOException;
    public void sentFile(OutputStream os, String filePath, String userName) throws IOException;
    public boolean readMessage(InputStream is) throws IOException;
}
