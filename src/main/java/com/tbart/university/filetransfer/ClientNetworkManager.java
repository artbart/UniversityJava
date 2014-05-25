package com.tbart.university.filetransfer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * Created by tbart on 5/25/2014.
 */
public class ClientNetworkManager {
    private Logger logger = LogManager.getFormatterLogger(ClientNetworkManager.class);

    private int serverPort;

    public ClientNetworkManager(int serverPort) {
        this.serverPort = serverPort;
    }

    public void sentText(String text){
        logger.info("sent text [text : %s]", text);
        try (Socket socket = new Socket("localhost", serverPort); OutputStream os = socket.getOutputStream()){
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeByte(1);
            dos.writeByte(MessageWorker.MESSAGE_TEXT);
            dos.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sentFile(String filePath) {
        logger.info("sent file [filePath : %s]", filePath);
        File file = new File(filePath);
        byte[] fileData = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)){
            int realSize = fis.read(fileData);
            logger.info("real file size : %d", realSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Socket socket = new Socket("localhost", serverPort); OutputStream os = socket.getOutputStream()){
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeByte(1);
            dos.writeByte(MessageWorker.MESSAGE_FILE);
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());
            dos.write(fileData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
