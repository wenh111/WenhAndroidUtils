package com.wenh.baselibrary.network;


import com.wenh.baselibrary.R;
import com.wenh.baselibrary.callback.Callback;

import java.io.IOException;
import java.net.Socket;

public class TestConnectionTask implements Runnable {

    private String serverAddress;

    private int tcpPort;

    private int httpPort;

    private Socket socket;

    private Callback<Integer> callback;

    public TestConnectionTask(String serverAddress, String httpPort, String tcpPort, Callback<Integer> callback) {
        this.serverAddress = serverAddress;
        this.httpPort = Integer.valueOf(httpPort);
        this.tcpPort = Integer.valueOf(tcpPort);
        this.callback = callback;
    }

    @Override
    public void run() {

        try {
            socket = new Socket(serverAddress, httpPort);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onCallback(R.string.connect_error);
            return;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket = null;
        }


        try {
            socket = new Socket(serverAddress, tcpPort);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onCallback(R.string.tcp_connect_error);
            return;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket = null;
        }

        callback.onCallback(R.string.connect_normal);
    }
}
