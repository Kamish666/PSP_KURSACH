package Kursach.client;

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

    private ObjectOutputStream objectOut;

    public Client(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);

        objectOut = new ObjectOutputStream(socket.getOutputStream());
        objectIn = new ObjectInputStream(socket.getInputStream());
        System.out.println("Подключился к " + host + ':' + port);
    }

    public void send(Object object) {
        try {
            System.out.println("Sent: " + object);
            objectOut.writeObject(object);
            objectOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendInt(int num) throws IOException {
        send(num);
    }

    public int receiveInt() {
        return (Integer) receive();
    }

    public Object receive() {
        try {
            Object object = objectIn.readObject();
            System.out.println("Received: " + object);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
