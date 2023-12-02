package Kursach.client;

import Kursach.shared.objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Polzovatel {

    public User getCurrentUser() {
        return current_user;
    }

    public void setCurrentUser(User current_user) {
        this.current_user = current_user;
    }

    User current_user;
    private static Polzovatel instance;

    public static void setInstance(Polzovatel client) {
        instance = client;
    }
    public static Polzovatel getInstance() {
        return instance;
    }

    private int port;
    private String host;
    private Socket socket;

    private ObjectInputStream objectIn;

    private ObjectOutputStream objectOut;

    public Polzovatel(String host, int port) {
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

    public Object receive(Object object) {
        send(object);
        return receive();
    }
    public Object receive() {
        try {
            System.out.println("receiving");
            Object object = objectIn.readObject();
            System.out.println("Received: " + object);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
