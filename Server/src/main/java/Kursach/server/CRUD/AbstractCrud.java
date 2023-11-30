package Kursach.server.CRUD;

import Kursach.server.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.Scanner;

public abstract class AbstractCrud {
    protected final Connection connection;

    protected final int choice;
    protected final Scanner scanner;
    protected final PrintWriter writer;
    protected final ObjectInputStream objectIn;
    protected final ObjectOutputStream objectOut;


    protected abstract void select();

    protected abstract void update();

    protected abstract void insert();

    protected abstract void delete();

    public void execute() {
        switch (choice) {
            case 1 -> select();
            case 2 -> insert();
            case 3 -> update();
            case 4 -> delete();
            default -> {
            }
        }
    }

    AbstractCrud(Socket clientSocket, int choice) throws IOException {
        this.choice = choice;
        connection = Main.getConnection();
        scanner = new Scanner(clientSocket.getInputStream());
        writer = new PrintWriter(clientSocket.getOutputStream());
        objectIn = new ObjectInputStream(clientSocket.getInputStream());
        objectOut = new ObjectOutputStream(clientSocket.getOutputStream());

    }
}
