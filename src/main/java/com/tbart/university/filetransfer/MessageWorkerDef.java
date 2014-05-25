package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by tbart on 5/25/2014.
 */
public class MessageWorkerDef implements MessageWorker {
    public static final byte MESSAGE_TEXT = 0;
    public static final byte MESSAGE_FILE = 1;

    private Logger logger = LogManager.getFormatterLogger(MessageWorkerDef.class);

    private String filesDirectory;
    private final BufferedWriter resultOutput;

    public MessageWorkerDef(String filesDirectory, OutputStream outputStream) {
        this.filesDirectory = filesDirectory;
        this.resultOutput = new BufferedWriter(new OutputStreamWriter(outputStream));
    }

    /**
     * White text message to os in the following format:
     * byte - version of the message
     * UTF - name of sender
     * byte - type of message (text)
     * UTF - text
     *
     * @param os       - where to write a message
     * @param text     - text of a message
     * @param userName - - name of user, who sent a message
     * @throws IOException
     */
    @Override
    public void sentText(OutputStream os, String text, String userName) throws IOException {
        logger.info("sent text [text : %s]", text);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeByte(1);
        dos.writeUTF(userName);
        dos.writeByte(MessageWorkerDef.MESSAGE_TEXT);
        dos.writeUTF(text);
        dos.flush();
        os.flush();
    }


    /**
     * White file bytes to os in the following format:
     * byte - version of the message
     * UTF - name of sender
     * byte - type of message (file)
     * long - file size
     * bytes[] - file bytes (file data)
     *
     * @param os       - where to write a message
     * @param filePath - path to file to write
     * @param userName - name of user, who sent a message
     * @throws IOException
     */
    @Override
    public void sentFile(OutputStream os, String filePath, String userName) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        byte[] fileData = new byte[(int) fileSize];
        int realSize;
        try (FileInputStream fis = new FileInputStream(file)) {
            realSize = fis.read(fileData);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        logger.info("real file size : %d", realSize);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeByte(1);
        dos.writeUTF(userName);
        dos.writeByte(MessageWorkerDef.MESSAGE_FILE);
        dos.writeUTF(file.getName());
        dos.writeLong(fileSize);
        dos.write(fileData);
        dos.flush();
        os.flush();
    }


    /**
     * Start parsing of message and than, determine whether file it or message
     * and pass it to readFile or readMessage
     *
     * @param is -
     * @return - true if message was written successfully
     * @throws IOException
     */
    @Override
    public boolean readMessage(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);

        byte mVersion = dis.readByte();
        logger.info("[version : %d]", mVersion);
        if (mVersion != 1) {
            throw new IOException("unsupported version");
        }
        String userName = dis.readUTF();

        byte mType = dis.readByte();
        logger.info("[type : %d]", mType);

        switch (mType) {
            case MESSAGE_TEXT:
                readText(userName, dis);
                break;
            case MESSAGE_FILE:
                readFile(userName, dis);
                break;
            default:
                throw new IOException("unsupported message type");
        }

        return true;
    }

    /**
     * Read text from dis and write into resultOutput
     *
     * @param userName - name of user, who sent a message
     * @param dis      - DataInputStream from which text should be read
     * @throws IOException
     */
    private void readText(String userName, DataInputStream dis) throws IOException {
        String message = dis.readUTF();
        String toOut = String.format("%s: %s", userName, message);
        logger.info(toOut);
        writeToOut(toOut);
    }


    /**
     * Read file data from dis and write into the filesDirectory.
     * Also print some information into resultOutput
     *
     * @param userName - name of user, who sent file
     * @param dis      - is used
     * @throws IOException
     */
    private void readFile(String userName, DataInputStream dis) throws IOException {
        String fileName = dis.readUTF();
        int fileSize = (int) dis.readLong();
        byte[] fileData = new byte[fileSize];
        dis.readFully(fileData);
        String toOut = String.format("%s: [file] [filePath : %s]", userName, (filesDirectory + "/" + fileName));
        logger.info(toOut);
        OutputStream output = new FileOutputStream(filesDirectory + "/" + fileName);
        output.write(fileData);
        writeToOut(toOut);
    }

    private void writeToOut(String text) throws IOException {
        synchronized (resultOutput) {
            resultOutput.write(text);
            resultOutput.write("\n");
            resultOutput.flush();
        }
    }

}
