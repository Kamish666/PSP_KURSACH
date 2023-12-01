package Kursach.server.CRUD;

import Kursach.shared.objects.Provider;

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

public class ProviderCRUD extends AbstractCrud{

    private List<String> list = new ArrayList<>();


    Scanner scanner;
    public ProviderCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    //функция
    // @Overrideпросмотра
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Provider> providers = new ArrayList<>();
            while (resultSet.next()) {
                Provider provider = new Provider(
                        resultSet.getInt("provider_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                providers.add(provider);
            }
            System.out.println(providers);
            objectOut.writeObject(providers);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO provider (name, email) VALUES (?, ?)");
            Provider provider = (Provider) objectIn.readObject();
            try {
                preparedStatement.setString(1, provider.getName());
                preparedStatement.setString(2, provider.getEmail());
                preparedStatement.execute();
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE provider SET name = ?, email = ? WHERE provider_id = ?");
            Provider provider = (Provider) objectIn.readObject();
            preparedStatement.setString(1, provider.getName());
            preparedStatement.setString(2, provider.getEmail());
            preparedStatement.setInt(3, provider.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM provider WHERE provider_id = ?");
            Provider provider = (Provider) objectIn.readObject();
            preparedStatement.setInt(1, provider.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

