package Kursach.server.CRUD;

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

public class ProductCRUD extends AbstractCrud {

    private List<String> list = new ArrayList<>();
    private List<String> list_category = new ArrayList<>();
    private List<String> list_manufacturer = new ArrayList<>();
    private List<String> list_provider = new ArrayList<>();


    Scanner scanner;
    public ProductCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT category FROM product_category WHERE product_category_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT manufacturer_name FROM manufacturer WHERE manufacturer_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT name FROM provider WHERE provider_id = ?");
            while (true) {
                String productName = scanner.nextLine();

                if ("окно закрыто".equals(productName)) {
                    break;
                } else {
                    preparedStatement.setString(1, productName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int productId = resultSet.getInt("product_id");
                    String categoryName;
                    String manufacturerName;
                    String providerName;
                    int categoryId = resultSet.getInt("category_id");
                    int manufacturerId = resultSet.getInt("manufacturer_id");
                    int providerId = resultSet.getInt("provider_id");

                    preparedStatement1.setInt(1, categoryId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    categoryName = resultSet.getString("category");

                    preparedStatement2.setInt(1, manufacturerId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    manufacturerName = resultSet.getString("manufacturer_name");

                    preparedStatement3.setInt(1, providerId);
                    resultSet = preparedStatement3.executeQuery();
                    resultSet.next();
                    providerName = resultSet.getString("name");

                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(categoryName);
                    System.out.println(manufacturerName);
                    System.out.println(providerName);
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
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO product (name, price, category_id, manufacturer_id, provider_id) VALUES (?, ?, ?, ?, ?)");
            String name = new String();
            double price;
            String category_name = new String();
            String manufacturer_name = new String();
            String provider_name = new String();
            ListNameCategory();
            ListNameManufacturer();
            ListNameProvider();
            int categoryId, manufacturerId, providerId;
            while (true) {
                while (true) {
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            name = scanner.nextLine();
                            price = Double.parseDouble(scanner.nextLine());
                            category_name = scanner.nextLine();
                            manufacturer_name = scanner.nextLine();
                            provider_name = scanner.nextLine();

                            categoryId = getIdCategory(category_name);
                            manufacturerId = getIdManufacturer(manufacturer_name);
                            providerId = getIdProvider(provider_name);

                            preparedStatement1.setString(1, name);
                            preparedStatement1.setDouble(2, price);
                            preparedStatement1.setInt(3, categoryId);
                            preparedStatement1.setInt(4, manufacturerId);
                            preparedStatement1.setInt(5, providerId);
                            preparedStatement1.execute();
                            System.out.println("product is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("product is");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }
            ListNameCategory();
            ListNameManufacturer();
            ListNameProvider();

            //отправка list_category, list_manufacturer, list_provider

            String productName;
            String newProductName;
            int productId = 0;
            double price;
            int categoryId, manufacturerId, providerId;
            String category_name = new String();
            String manufacturer_name = new String();
            String provider_name = new String();
            preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE product SET name = ?, price = ?, category_id = ?, manufacturer_id = ?, provider_id = ? WHERE product_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT category FROM product_category WHERE product_category_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT manufacturer_name FROM manufacturer WHERE manufacturer_id = ?");
            PreparedStatement preparedStatement4 = connection.prepareStatement("SELECT name FROM provider WHERE provider_id = ?");
            while (true) {
                productName = scanner.nextLine();

                if ("окно закрыто".equals(productName)) {
                    break;
                } else if ("редактировать".equals(productName) && productId != 0) {
                    try {
                        newProductName = scanner.nextLine();
                        price = Double.parseDouble(scanner.nextLine());
                        category_name = scanner.nextLine();
                        manufacturer_name = scanner.nextLine();
                        provider_name = scanner.nextLine();

                        categoryId = getIdCategory(category_name);
                        manufacturerId = getIdManufacturer(manufacturer_name);
                        providerId = getIdProvider(provider_name);

                        preparedStatement1.setString(1, newProductName);
                        preparedStatement1.setDouble(2, price);
                        preparedStatement1.setInt(3, categoryId);
                        preparedStatement1.setInt(4, manufacturerId);
                        preparedStatement1.setInt(5, providerId);
                        preparedStatement1.setInt(6, productId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("product is");
                    }
                } else {
                    preparedStatement.setString(1, productName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    productId = resultSet.getInt("product_id");
                    price = resultSet.getDouble("price");
                    categoryId = resultSet.getInt("category_id");
                    manufacturerId = resultSet.getInt("manufacturer_id");
                    providerId = resultSet.getInt("provider_id");

                    preparedStatement2.setInt(1, categoryId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    category_name = resultSet.getString("category");

                    preparedStatement3.setInt(1, manufacturerId);
                    resultSet = preparedStatement3.executeQuery();
                    resultSet.next();
                    manufacturer_name = resultSet.getString("manufacturer_name");

                    preparedStatement4.setInt(1, providerId);
                    resultSet = preparedStatement4.executeQuery();
                    resultSet.next();
                    provider_name = resultSet.getString("name");

                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(price);
                    System.out.println(category_name);
                    System.out.println(manufacturer_name);
                    System.out.println(provider_name);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String productName;
            int productId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM product WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT category FROM product_category WHERE product_category_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT manufacturer_name FROM manufacturer WHERE manufacturer_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT name FROM provider WHERE provider_id = ?");
            PreparedStatement preparedStatement4 = connection.prepareStatement("DELETE FROM product WHERE product_id = ?");
            while (true) {
                productName = scanner.nextLine();

                if ("окно закрыто".equals(productName)) {
                    break;
                } else if ("удалить".equals(productName) && productId != 0) {
                    try {
                        preparedStatement4.setInt(1, productId);
                        preparedStatement4.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("product is in use");
                    }
                } else {
                    preparedStatement.setString(1, productName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    productId = resultSet.getInt("product_id");
                    String categoryName;
                    String manufacturerName;
                    String providerName;
                    int categoryId = resultSet.getInt("category_id");
                    int manufacturerId = resultSet.getInt("manufacturer_id");
                    int providerId = resultSet.getInt("provider_id");

                    preparedStatement1.setInt(1, categoryId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    categoryName = resultSet.getString("category");

                    preparedStatement2.setInt(1, manufacturerId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    manufacturerName = resultSet.getString("manufacturer_name");

                    preparedStatement3.setInt(1, providerId);
                    resultSet = preparedStatement3.executeQuery();
                    resultSet.next();
                    providerName = resultSet.getString("name");

                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(categoryName);
                    System.out.println(manufacturerName);
                    System.out.println(providerName);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getIdCategory(String category_name) throws SQLException {
        int id_category = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT product_category_id FROM product_category WHERE category = ?")) {
            statement.setString(1, category_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_category = resultSet.getInt("product_category_id");
            }
        }
        return id_category;
    }

    private int getIdManufacturer(String manufacturer_name) throws SQLException {
        int id_manufacturer = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT manufacturer_id FROM manufacturer WHERE manufacturer_name = ?")) {
            statement.setString(1, manufacturer_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_manufacturer = resultSet.getInt("manufacturer_id");
            }
        }
        return id_manufacturer;
    }

    private int getIdProvider(String provider_name) throws SQLException {
        int id_provider = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT provider_id FROM provider WHERE name = ?")) {
            statement.setString(1, provider_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_provider = resultSet.getInt("provider_id");
            }
        }
        return id_provider;
    }

    private void ListNameCategory() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT category FROM product_category");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String categoryName = resultSet.getString("category");
                list_category.add(categoryName);
                System.out.println(categoryName);
            }
        }
    }

    private void ListNameManufacturer() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT manufacturer_name FROM manufacturer");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String manufacturerName = resultSet.getString("manufacturer_name");
                list_manufacturer.add(manufacturerName);
                System.out.println(manufacturerName);
            }
        }
    }

    private void ListNameProvider() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM provider");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String providerName = resultSet.getString("name");
                list_provider.add(providerName);
                System.out.println(providerName);
            }
        }
    }
}
