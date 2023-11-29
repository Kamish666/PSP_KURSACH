package Server;

import Server.СonnectDB.*;
import Server.ClientHandler.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.*;
import java.net.*;


public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        System.out.println("Server starting...");
        ServerSocket serverSocket = null;
        try {
            ReaderDBdata data = new ReaderDBdata();
            connection = DriverManager.getConnection(data.getUrl(), data.getName(), data.getPassword());
            System.out.println("Server connected to database");
            try {
                serverSocket = new ServerSocket(2525);

                while (true) {
                    Socket clientAccepted = serverSocket.accept();
                    System.out.println("Connection established...");
                    String socketInfo = "Клиент " + clientAccepted.getInetAddress() + ":" + clientAccepted.getPort() + " подключен.";
                    System.out.println(socketInfo);

                    Thread clientThread = new Thread(new ClientHandler(clientAccepted, connection));
                    clientThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

















































/*package Server;

import Server.СonnectDB.*;
import Server.ClientHandler.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.*;
import java.net.*;


public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        System.out.println("Server starting...");
        ServerSocket serverSocket = null;
        try {
            ReaderDBdata data = new ReaderDBdata();
            connection = DriverManager.getConnection(data.getUrl(), data.getName(), data.getPassword());
            System.out.println("Server connected to database");
            ClientHandler use = new ClientHandler(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}*/

