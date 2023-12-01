package Kursach.server.CRUD;

import Kursach.shared.objects.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientCRUD extends AbstractCrud {

    protected final List<String> list = new ArrayList<>();
    Scanner scanner; //удалить

    int choice;

    public ClientCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client");

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Client> clients = new ArrayList<>();
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                clients.add(client);
            }
            objectOut.writeObject(clients);

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {

        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO client (name, email) VALUES (?, ?)");
            String name = new String();
            String email = new String();
            while (true) {
                while (true) {
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            name = scanner.nextLine();
                            email = scanner.nextLine();
                            preparedStatement1.setString(1, name);
                            preparedStatement1.setString(2, email);
                            preparedStatement1.execute();
                            System.out.println("client is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("client is");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void update() {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String clientName;
            String newClientName, newClientEmail;
            int clientId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE client SET name = ?, email = ? WHERE client_id = ?");
            while (true) {
                clientName = scanner.nextLine();

                if ("окно закрыто".equals(clientName)) {
                    break;
                } else if ("редактировать".equals(clientName) && clientId != 0) {
                    try {
                        newClientName = scanner.nextLine();
                        newClientEmail = scanner.nextLine();
                        preparedStatement1.setString(1, newClientName);
                        preparedStatement1.setString(2, newClientEmail);
                        preparedStatement1.setInt(3, clientId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("client is");
                    }
                } else {
                    preparedStatement.setString(1, clientName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    clientId = resultSet.getInt("client_id");
                    String email = resultSet.getString("email");
                    System.out.println(clientId);
                    System.out.println(clientName);
                    System.out.println(email);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String clientName;
            int clientId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM client WHERE client_id = ?");
            while (true) {
                clientName = scanner.nextLine();

                if ("окно закрыто".equals(clientName)) {
                    break;
                } else if ("удалить".equals(clientName) && clientId != 0) {
                    try {
                        preparedStatement1.setInt(1, clientId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("client is in use");
                    }
                } else {
                    preparedStatement.setString(1, clientName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    clientId = resultSet.getInt("client_id");
                    String email = resultSet.getString("email");
                    System.out.println(clientId);
                    System.out.println(clientName);
                    System.out.println(email);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
