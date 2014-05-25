package com.tbart.university.filetransfer;


/**
 * Created by tbart on 5/25/2014.
 */
public class ClientRunner {
    public static void main(String[] args) {
        ClientNetworkManager networkManager = new ClientNetworkManager(9999);
        networkManager.sentText("privet");
        networkManager.sentFile("d:\\Yandex.Disk\\Archive\\main.cpp");
    }
}
