/*
package Server.ClientHandler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Connection connection;


    public ClientHandler(Socket clientSocket, Connection connection) {
        this.clientSocket = clientSocket;
        this.connection = connection;
    }

    @Override
    public void run() {
        try (ObjectInputStream sois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream soos = new ObjectOutputStream(clientSocket.getOutputStream())) {




            int userCount = getUserCount();
            soos.writeObject(userCount);




        } catch (IOException  | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private int getUserCount() throws SQLException {
        int userCount = 0;

        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                userCount = resultSet.getInt(1);
            }
        }

        return userCount;
    }
}*/







































package Server.ClientHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Server.CRUD.*;

public class ClientHandler {
    private Connection connection;


    public ClientHandler(Connection connection) {
        this.connection = connection;
        //перенести потом в метод run
        try {
            int userCount = getUserCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CountryCRUD a = new CountryCRUD(connection,4);
    }

    private int getUserCount() throws SQLException {
        int userCount = 0;

        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                userCount = resultSet.getInt(1);
            }
        }
        System.out.println(userCount);
        if (userCount == 0) {
        } else userCount = 1;
        return userCount;
    }
}