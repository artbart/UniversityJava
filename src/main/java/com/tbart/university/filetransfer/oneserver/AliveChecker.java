package com.tbart.university.filetransfer.oneserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tbart on 5/26/2014.
 */
public class AliveChecker implements Runnable {
    private Logger logger = LogManager.getFormatterLogger(ServerNetworkManager.class);

    private final List<SocketWrapper> connections;

    public AliveChecker(List<SocketWrapper> connections) {
        this.connections = connections;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (connections) {
                    Iterator<SocketWrapper> iterator = connections.iterator();
                    while (iterator.hasNext()) {
                        SocketWrapper socketWrapper = iterator.next();
                        if (socketWrapper.isAlive()) {
                            socketWrapper.setAlive(false);
                        } else {
                            logger.info("%s is dead. Socket was killed. ", socketWrapper.clientIp);
                            System.out.println("kill him");
                            try {
                                socketWrapper.getSocket().close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            iterator.remove();
                        }
                    }
                }
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            logger.info("I was killed");
        }
    }
}
