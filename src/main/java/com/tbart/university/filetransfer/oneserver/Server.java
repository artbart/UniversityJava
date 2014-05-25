package com.tbart.university.filetransfer.oneserver;

/**
 * Created by tbart on 5/26/2014.
 */
public class Server {
    public static void main(String[] args) {
        ServerNetworkManager serverNetworkManager = new ServerNetworkManager(9999);
        new Thread(serverNetworkManager).start();
    }
}
