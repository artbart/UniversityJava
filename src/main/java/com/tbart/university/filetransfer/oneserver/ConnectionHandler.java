package com.tbart.university.filetransfer.oneserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Created by tbart on 5/26/2014.
 */
public class ConnectionHandler implements Runnable{
    private Logger logger = LogManager.getFormatterLogger(ConnectionHandler.class);

    private final OutputStream os;
    private SocketWrapper socketWrapper;
    private MessageResender messageResender;

    public ConnectionHandler(SocketWrapper socketWrapper, OutputStream os, MessageResender messageResender) {
        this.messageResender = messageResender;
        this.socketWrapper = socketWrapper;
        this.os = os;
    }


    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socketWrapper.getSocket().getInputStream()))){
            logger.info("start listening");
            while (true) {
                String pr = br.readLine();
                if (pr == null) {
                    break;
                }
                logger.info("::" + pr);
                if (pr.charAt(0) == '0'){
                    socketWrapper.setAlive(true);
                } else {
                    String msg = pr.substring(1);
                    synchronized (os){
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
                        bufferedWriter.write(msg + "\n");
                        bufferedWriter.flush();
                    }
                    messageResender.resend(msg, socketWrapper.id);
                }
            }
        } catch (IOException e) {
            logger.info("I was killed");
        }
    }
}