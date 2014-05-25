package com.tbart.university.filetransfer.oneserver;

import com.sun.xml.internal.ws.Closeable;

import javax.xml.ws.WebServiceException;
import java.io.*;
import java.net.Socket;

/**
 * Created by tbart on 5/26/2014.
 */
public class ClientNetworkManager implements Runnable, Closeable {
    private final Socket socket;

    public ClientNetworkManager(int serverPort, String serverIp) throws IOException {
        socket = new Socket(serverIp, serverPort);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    while (true) {
                        String pr = br.readLine();
                        if (pr == null) {
                            break;
                        }
                        synchronized (System.out) {
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
                            bufferedWriter.write(pr + "\n");
                            bufferedWriter.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMessage(String message, String username) {
        synchronized (socket) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bufferedWriter.write("1" + username + ": " + message + "\n");
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        try {

            while (!Thread.interrupted()) {
                synchronized (socket) {
                    try {
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bufferedWriter.write("0\n");
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(5000);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws WebServiceException {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
