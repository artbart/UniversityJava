package com.tbart.university.filetransfer;

/**
 * Created by tbart on 5/25/2014.
 */
public class ServerRunner {
    public static void main(String[] args) {
        MessageWorkerDef messageWorkerDef = new MessageWorkerDef("D:/tmp", System.out);
        ServerNetworkManager serverNM = new ServerNetworkManager(9999, messageWorkerDef);
        new Thread(serverNM).start();
    }
}
