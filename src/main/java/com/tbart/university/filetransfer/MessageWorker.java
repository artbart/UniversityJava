package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by tbart on 5/25/2014.
 */
public class MessageWorker {
    public static final byte MESSAGE_TEXT = 0;
    public static final byte MESSAGE_FILE = 1;

    private Logger logger = LogManager.getFormatterLogger(MessageWorker.class);

    private String filesDirectory;
    private BufferedWriter resultOutput;

    public MessageWorker(String filesDirectory, OutputStream outputStream) {
        this.filesDirectory = filesDirectory;
        this.resultOutput = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    public void readMessage(InputStream is) throws IOException  {
        DataInputStream dis = new DataInputStream(is);

        byte mVersion = dis.readByte();
        logger.info("[version : %d]", mVersion);
        if (mVersion != 1) {
            throw new IOException("unsupported version");
        }

        byte mType = dis.readByte();
        logger.info("[type : %d]", mType);

        switch (mType){
            case MESSAGE_TEXT:
                readText(dis);
                break;
            case MESSAGE_FILE:
                readFile(dis);
                break;
            default:
                throw new IOException("unsupported message type");
        }
    }

    private void readText(DataInputStream dis) throws IOException {
        String message = dis.readUTF();
        logger.info("[message : %s]", message);
        writeToOut(String.format("text was received: %s", message));
    }

    private void readFile(DataInputStream dis) throws IOException {
        String fileName = dis.readUTF();
        int fileSize = (int) dis.readLong();
        byte[] fileData = new byte[fileSize];
        int realSize = dis.read(fileData);
        logger.info("file reading [size : %d], [real size : %d]", fileSize, realSize);
        OutputStream output = new FileOutputStream(filesDirectory + "/" + fileName);
        output.write(fileData);
        writeToOut(String.format("file was received [filePath : %s], [fileSize : %d]", (filesDirectory + "/" + fileName), realSize));
    }

    private void writeToOut(String text) throws IOException {
        resultOutput.write(text);
        resultOutput.write("\n");
        resultOutput.flush();
    }

}
