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
    public ManufacturerCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer");
            PreparedStatement preparedStatementCountry = connection.prepareStatement("SELECT country_name FROM country WHERE country_id = ?");

            ResultSet resultSet = preparedStatement.executeQuery();

            List<ManufacturerDto> manufacturers = new ArrayList<>();

            while (resultSet.next()) {
                ManufacturerDto manufacturer = new ManufacturerDto();
                manufacturer.setId(resultSet.getInt("manufacturer_id"));
                manufacturer.setName(resultSet.getString("manufacturer_name"));
                manufacturer.setCountryId(resultSet.getInt("country_id"));
                ResultSet resultSetCountry = preparedStatementCountry.executeQuery();
                preparedStatementCountry.setInt(1, resultSet.getInt("country_id"));
                manufacturer.setCountryName(resultSetCountry.getString("country_name"));
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
                preparedStatement.setInt(2, manufacturer.getCountryId());
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
            preparedStatement.setInt(2, manufacturer.getCountryId());
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
}
