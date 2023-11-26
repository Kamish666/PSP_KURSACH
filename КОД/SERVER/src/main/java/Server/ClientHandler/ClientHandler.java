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
import java.util.Scanner;

public class ClientHandler {
    private Scanner scanner = new Scanner(System.in);
    private Connection connection;
    private int id;
    private int role;


    public ClientHandler(Connection connection) {
        this.connection = connection;
        //перенести потом в метод run
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
            ResultSet resultSet = null;
            int userCount = getUserCount();
            if(userCount == 0){
                preparedStatement = connection.prepareStatement("INSERT INTO user (name, login, password, role) VALUES (?,?,?,2)");
                //прием данных и их отправка в таблицу
                preparedStatement.setString(1, scanner.nextLine());//name
                preparedStatement.setString(2, scanner.nextLine());//login
                preparedStatement.setString(3, scanner.nextLine());//password
                preparedStatement.execute();
                //preparedStatement.setInt(4, 2);
            }
            else{
                while(true) {
                    //прием данных и их отправка на проверку
                    preparedStatement.setString(1, scanner.nextLine());//login
                    preparedStatement.setString(2, scanner.nextLine());//password
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        id = resultSet.getInt("user_id");
                        role = resultSet.getInt("role");
                        break;
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true){
            //операции с друмя числами это CRUD
            //первое число обозначает номер элемента, с которым будет выполняться CRUD
            //второе число обозначает какая именно операция:1-select, 2-insert, 3-update, 4-delete
            // прием данных
            switch (scanner.nextInt()){
                //Country
                case 11:{CountryCRUD a = new CountryCRUD(connection,1); break;}
                case 12:{CountryCRUD a = new CountryCRUD(connection,2); break;}
                case 13:{CountryCRUD a = new CountryCRUD(connection,3); break;}
                case 14:{CountryCRUD a = new CountryCRUD(connection,4); break;}
                //Provider
                case 21:{ProviderCRUD a = new ProviderCRUD(connection,1); break;}
                case 22:{ProviderCRUD a = new ProviderCRUD(connection,2); break;}
                case 23:{ProviderCRUD a = new ProviderCRUD(connection,3); break;}
                case 24:{ProviderCRUD a = new ProviderCRUD(connection,4); break;}
                //ProductCategory
                case 31:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,1); break;}
                case 32:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,2); break;}
                case 33:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,3); break;}
                case 34:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,4); break;}
                //Client
                case 41:{ClientCRUD a = new ClientCRUD(connection,1); break;}
                case 42:{ClientCRUD a = new ClientCRUD(connection,2); break;}
                case 43:{ClientCRUD a = new ClientCRUD(connection,3); break;}
                case 44:{ClientCRUD a = new ClientCRUD(connection,4); break;}
                //User
                case 51:{UserCRUD a = new UserCRUD(connection,1, id); break;}
                case 52:{UserCRUD a = new UserCRUD(connection,2, id); break;}
                case 53:{UserCRUD a = new UserCRUD(connection,3, id); break;}
                case 54:{UserCRUD a = new UserCRUD(connection,4, id); break;}
                //Manufacturer
                case 61:{ManufacturerCRUD a = new ManufacturerCRUD(connection,1); break;}
                case 62:{ManufacturerCRUD a = new ManufacturerCRUD(connection,2); break;}
                case 63:{ManufacturerCRUD a = new ManufacturerCRUD(connection,3); break;}
                case 64:{ManufacturerCRUD a = new ManufacturerCRUD(connection,4); break;}
                //Product
                case 71:{ProductCRUD a = new ProductCRUD(connection,1); break;}
                case 72:{ProductCRUD a = new ProductCRUD(connection,2); break;}
                case 73:{ProductCRUD a = new ProductCRUD(connection,3); break;}
                case 74:{ProductCRUD a = new ProductCRUD(connection,4); break;}
                //Orders
                case 81:{OrdersCRUD a = new OrdersCRUD(connection,1); break;}
                case 82:{OrdersCRUD a = new OrdersCRUD(connection,2); break;}
                case 83:{OrdersCRUD a = new OrdersCRUD(connection,3); break;}
                case 84:{OrdersCRUD a = new OrdersCRUD(connection,4); break;}
                default:System.out.println("Error");
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
        System.out.println(userCount);
        if (userCount == 0) {
        } else userCount = 1;
        return userCount;
    }
}
