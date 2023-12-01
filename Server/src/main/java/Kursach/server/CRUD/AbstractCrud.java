package Kursach.server.CRUD;

import Kursach.server.Main;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.SQLOutput;
import java.util.Scanner;

public abstract class AbstractCrud {
    protected final Connection connection;

    protected final ObjectInputStream objectIn;
    protected final ObjectOutputStream objectOut;


    protected abstract void select();

    protected abstract void update();

    protected abstract void insert();

    protected abstract void delete();

    public void execute(int choice) {
        System.out.println("executing method" + choice);
        switch (choice) {
            case 1 -> select();
            case 2 -> insert();
            case 3 -> update();
            case 4 -> delete();
            default -> {
            }
        }
    }

    AbstractCrud(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        connection = Main.getConnection();
        this.objectIn = objectIn;
        this.objectOut = objectOut;

    }
}
