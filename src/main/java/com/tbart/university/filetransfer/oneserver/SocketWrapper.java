package com.tbart.university.filetransfer.oneserver;

import java.net.Socket;

/**
 * Created by tbart on 5/26/2014.
 */
public class SocketWrapper {
    public final long id;
    public final String clientIp;
    private Socket socket;
    private boolean isAlive;

    public SocketWrapper(Socket socket, long id) {
        this.id = id;
        this.socket = socket;
        clientIp = socket.getInetAddress().toString();
        isAlive = true;
    }

    public synchronized Socket getSocket() {
        return socket;
    }

    public synchronized boolean isAlive() {
        return isAlive;
    }

    public synchronized void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
}
