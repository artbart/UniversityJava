package com.tbart.university.filetransfer.oneserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tbart on 5/26/2014.
 */
public class MessageResender {
    private Logger logger = LogManager.getFormatterLogger(ServerNetworkManager.class);

    private final List<SocketWrapper> connections;

    public MessageResender(List<SocketWrapper> connections) {
        this.connections = connections;
    }

    public void resend(String message, long fromId) {
        synchronized (connections) {
            for (SocketWrapper socketWrapper : connections) {
                if (socketWrapper.id != fromId) {
                    try {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketWrapper.getSocket().getOutputStream()));
                        bufferedWriter.write(message + "\n");
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
