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
        System.out.println("Client insert");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO client (name, email) VALUES (?, ?)");
            Client client = (Client) objectIn.readObject();
            System.out.println("received " + client);
            try {
                preparedStatement.setString(1, client.getName());
                preparedStatement.setString(2, client.getEmail());
                preparedStatement.execute();
                System.out.println("inserted " + client);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void update() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE client SET name = ?, email = ? WHERE client_id = ?");
            Client client = (Client) objectIn.readObject();
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setInt(3, client.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM client WHERE client_id = ?");
            Client client = (Client) objectIn.readObject();
            preparedStatement.setInt(1, client.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
