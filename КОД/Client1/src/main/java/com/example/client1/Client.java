package com.example.client1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static Client instance;
    private int port;
    private String host;
    private Socket socket;

    private ObjectInputStream objectIn;

    private Scanner scanner;

    private PrintWriter writer;
    private ObjectOutputStream objectOut;

    Client(String host, int port){
        this.port = port;
        this.host = host;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        writer = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
        objectOut = new ObjectOutputStream(socket.getOutputStream());
        objectIn = new ObjectInputStream(socket.getInputStream());
        System.out.println("Подключился к " + host + ':' + port);
    }

    public void send(Object object) throws IOException {
        objectOut.writeObject(object);
    }

    public void sendInt(int num) {
        writer.write(num);
    }

    public int receiveInt() {
        return scanner.nextInt();
    }

    public Object receive() throws IOException, ClassNotFoundException {
        Object object = objectIn.readObject();
        System.out.println(object);
        return object;
    }
}
