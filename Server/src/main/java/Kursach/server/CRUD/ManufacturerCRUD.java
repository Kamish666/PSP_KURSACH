package Kursach.server.CRUD;

import Kursach.shared.objects.ManufacturerDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManufacturerCRUD extends AbstractCrud {
    protected final List<String> list = new ArrayList<>();


    private List<String> list_country = new ArrayList<>();


    Scanner scanner;

    public ManufacturerCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ManufacturerDto> manufacturers = new ArrayList<>();
            while (resultSet.next()) {
                ManufacturerDto manufacturer = new ManufacturerDto(
                        resultSet.getInt("manufacturer_id"),
                        resultSet.getString("manufacturer_name"),
                        getNameCountry(resultSet.getInt("country_id"))
                );
                manufacturers.add(manufacturer);
            }
            System.out.println(manufacturers);
            objectOut.writeObject(manufacturers);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO manufacturer (manufacturer_name, country_id) VALUES (?, ?)");
            ManufacturerDto manufacturer = (ManufacturerDto) objectIn.readObject();
            try {
                preparedStatement.setString(1, manufacturer.getName());
                preparedStatement.setInt(2, getIdCountry(manufacturer.getCountry()));
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE manufacturer SET manufacturer_name = ?, country_id = ? WHERE manufacturer_id = ?");
            ManufacturerDto manufacturer = (ManufacturerDto) objectIn.readObject();
            preparedStatement.setString(1, manufacturer.getName());
            preparedStatement.setInt(2, getIdCountry(manufacturer.getCountry()));
            preparedStatement.setInt(3, manufacturer.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM manufacturer WHERE manufacturer_id = ?");
            ManufacturerDto manufacturer = (ManufacturerDto) objectIn.readObject();
            preparedStatement.setInt(1, manufacturer.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getIdCountry(String country_name) throws SQLException {
        int id_country = -1; // Значение по умолчанию или другое подходящее

        try (PreparedStatement statement = connection.prepareStatement("SELECT country_id FROM country WHERE country_name = ?")) {
            statement.setString(1, country_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_country = resultSet.getInt("country_id");
            }
        }
        return id_country;
    }


    private String getNameCountry(int country_id) throws SQLException {
        String country_name = null;

        try (PreparedStatement statement = connection.prepareStatement("SELECT country_name FROM country WHERE country_id = ?")) {
            statement.setInt(1, country_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                country_name = resultSet.getString("country_id");
            }
        }
        return country_name;
    }

}
