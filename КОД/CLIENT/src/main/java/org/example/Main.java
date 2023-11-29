package org.example;

import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("tcp_ip_5.Server connecting...");
            Socket clientSocket = new Socket("127.0.0.1", 2525);
            System.out.println("Connection established...");

            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            ObjectOutputStream coos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream cois = new ObjectInputStream(clientSocket.getInputStream());







            coos.close();
            cois.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
