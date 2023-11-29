package com.example.client1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private int port;
    private String host;
    private Socket socket;

    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;

    Client(String host, int port){
        this.port = port;
        this.host = host;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        objectIn = new ObjectInputStream(socket.getInputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Подключился к " + host + ':' + port);
    }

    public void send() {

    }
}
