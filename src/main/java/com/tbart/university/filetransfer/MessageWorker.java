package com.tbart.university.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by tbart on 5/25/2014.
 * <p/>
 * Interface for message worker
 * <p/>
 * Can do the following:
 * <ul>
 * <li> Write text message to OutputStream in appropriate format (with header, username and so on)</li>
 * <li> Write file data to OutputStream in appropriate format (with header, username and so on)</li>
 * <li> Receive message from InputStream, which was sent by other MessageWorker</li>
 * </ul>
 */
public interface MessageWorker {
    public void sentText(OutputStream os, String text, String userName) throws IOException;

    public void sentFile(OutputStream os, String filePath, String userName) throws IOException;

    public boolean readMessage(InputStream is) throws IOException;
}
