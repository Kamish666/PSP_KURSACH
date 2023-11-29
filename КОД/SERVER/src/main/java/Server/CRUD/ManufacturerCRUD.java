package Server.CRUD;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManufacturerCRUD extends AbstractCrud{
    protected final List<String> list = new ArrayList<>();


    private List<String> list_country = new ArrayList<>();


    public ManufacturerCRUD(Socket clientSocket, int choice) throws IOException {
        super(clientSocket, choice);

    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("manufacturer_name"));
                System.out.println(resultSet.getString("manufacturer_name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer WHERE manufacturer_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT country_name FROM country WHERE country_id = ?");
            while (true) {
                String manufacturerName = scanner.nextLine();

                if ("окно закрыто".equals(manufacturerName)) {
                    break;
                } else {
                    preparedStatement.setString(1, manufacturerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int manufacturerId = resultSet.getInt("manufacturer_id");
                    int countryId = resultSet.getInt("country_id");
                    preparedStatement1.setInt(1, countryId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    String country = resultSet.getString("country_name");
                    System.out.println(manufacturerId);
                    System.out.println(manufacturerName);
                    System.out.println(country);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO manufacturer (manufacturer_name, country_id) VALUES (?, ?)");
            String name = new String();
            String country_name = new String();
            ListNameCountry();
            //отпрвка ListNameCountry клиенту
            int countryId;
            while(true) {
                while (true) {
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            name = scanner.nextLine();
                            country_name = scanner.nextLine();
                            countryId =  getIdCountry(country_name);
                            preparedStatement1.setString(1, name);
                            preparedStatement1.setInt(2, countryId);
                            preparedStatement1.execute();
                            System.out.println("manufacturer is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("manufacturer is");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("manufacturer_name"));
                System.out.println(resultSet.getString("manufacturer_name"));
            }
            ListNameCountry();

            //отправка list_country

            String manufacturerName;
            String newManufacturerName;
            int manufacturerId = 0;
            int countryId;
            String country_name = new String();
            preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer WHERE manufacturer_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE manufacturer SET manufacturer_name = ?, country_id = ? WHERE manufacturer_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT country_name FROM country WHERE country_id = ?");
            while (true) {
                manufacturerName = scanner.nextLine();

                if ("окно закрыто".equals(manufacturerName)) {
                    break;
                } else if ("редактировать".equals(manufacturerName) && manufacturerId != 0) {
                    try {
                        newManufacturerName = scanner.nextLine();
                        country_name = scanner.nextLine();
                        countryId =  getIdCountry(country_name);
                        preparedStatement1.setString(1, newManufacturerName);
                        preparedStatement1.setInt(2, countryId);
                        preparedStatement1.setInt(3, manufacturerId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("manufacturer is");
                    }
                } else {
                    preparedStatement.setString(1, manufacturerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    manufacturerId = resultSet.getInt("manufacturer_id");
                    countryId = resultSet.getInt("country_id");
                    preparedStatement2.setInt(1, countryId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    String country = resultSet.getString("country_name");
                    System.out.println(manufacturerId);
                    System.out.println(manufacturerName);
                    System.out.println(country);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT country_name FROM country WHERE country_id = ?");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("manufacturer_name"));
                System.out.println(resultSet.getString("manufacturer_name"));
            }

            String manufacturerName;
            int manufacturerId = 0;
            int countryId;
            preparedStatement = connection.prepareStatement("SELECT * FROM manufacturer WHERE manufacturer_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM manufacturer WHERE manufacturer_id = ?");
            while (true) {
                manufacturerName = scanner.nextLine();

                if ("окно закрыто".equals(manufacturerName)) {
                    break;
                } else if ("удалить".equals(manufacturerName) && manufacturerId != 0) {
                    try {
                        preparedStatement1.setInt(1, manufacturerId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("manufacturer is in use");
                    }
                } else {
                    preparedStatement.setString(1, manufacturerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    manufacturerId = resultSet.getInt("manufacturer_id");
                    countryId = resultSet.getInt("country_id");
                    preparedStatement2.setInt(1, countryId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    String country = resultSet.getString("country_name");
                    System.out.println(manufacturerId);
                    System.out.println(manufacturerName);
                    System.out.println(country);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
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


    private void ListNameCountry() throws SQLException{
        try (PreparedStatement statement = connection.prepareStatement("SELECT country_name FROM country");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String countryName = resultSet.getString("country_name");
                list_country.add(countryName);
                System.out.println(countryName);
            }
        }
    }

        class Manufacturer {
            private int id;
            private String name;
            private int countryId;

            Manufacturer(int id, String name, int countryId) {
                this.id = id;
                this.name = name;
                this.countryId = countryId;
            }

            Manufacturer() {
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCountryId() {
                return countryId;
            }

            public void setCountryId(int countryId) {
                this.countryId = countryId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
