package Kursach.server.CRUD;

import Kursach.shared.objects.Country;

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

public class CountryCRUD extends AbstractCrud {
    public CountryCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Country> countries = new ArrayList<>();
            while (resultSet.next()) {
                Country country = new Country(
                        resultSet.getInt("country_id"),
                        resultSet.getString("country_name")
                );
                countries.add(country);
            }
            System.out.println(countries);
            objectOut.writeObject(countries);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO country (country_name) VALUES (?)");
            Country country = (Country) objectIn.readObject();
            try {
                preparedStatement.setString(1, country.getCountry());
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE country SET country_name = ? WHERE country_id = ?");
            Country country = (Country) objectIn.readObject();
            preparedStatement.setString(1, country.getCountry());
            preparedStatement.setInt(2, country.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM country WHERE country_id = ?");
            Country country = (Country) objectIn.readObject();
            preparedStatement.setInt(1, country.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}