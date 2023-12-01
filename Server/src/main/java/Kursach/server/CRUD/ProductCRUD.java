package Kursach.server.CRUD;

import Kursach.shared.objects.ProductDto1;

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

    public ProductCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");
            PreparedStatement preparedStatementManufacturer = connection.prepareStatement("SELECT * FROM manufacturer WHERE manufacturer_id = ?");
            PreparedStatement preparedStatementCategory = connection.prepareStatement("SELECT * FROM product_catugory WHERE product_category_id = ?");
            PreparedStatement preparedStatementProvider = connection.prepareStatement("SELECT * FROM provider WHERE provider_id = ?");

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSetManufacturer = preparedStatementManufacturer.executeQuery();
            ResultSet resultSetCategory = preparedStatementCategory.executeQuery();
            ResultSet resultSetProvider = preparedStatementProvider.executeQuery();
            List<ProductDto1> products = new ArrayList<>();

            while (resultSet.next()) {
                ProductDto1 product = new ProductDto1();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getInt("price"));

                product.setManufacturerId(resultSet.getInt("manufacturer_id"));
                preparedStatementManufacturer.setInt(1, resultSet.getInt("manufacturer_id"));
                product.setManufacturerName(resultSetManufacturer.getString("manufacturer_name"));
                product.setManufacturerCountryId(resultSetManufacturer.getInt("country_id"));

                product.setCategoryId(resultSet.getInt("product_category_id"));
                preparedStatementCategory.setInt(1, resultSet.getInt("product_category_id"));
                product.setCategoryName(resultSetCategory.getString("category"));
                product.setCategoryDifinition(resultSetCategory.getString("definition"));

                product.setProviderId(resultSet.getInt("provider_id"));
                preparedStatementProvider.setInt(1, resultSet.getInt("provider_id"));
                product.setProviderName(resultSetProvider.getString("name"));
                product.setProviderEmail(resultSetProvider.getString("email"));

                products.add(product);
            }

            System.out.println(products);
            objectOut.writeObject(products);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product (name, price, category_id, manufacturer_id, provider_id) VALUES (?, ?, ?, ?, ?)");
            ProductDto1 product = (ProductDto1) objectIn.readObject();
            try {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setInt(3, product.getCategoryId());
                preparedStatement.setInt(4, product.getManufacturerId());
                preparedStatement.setInt(5, product.getProviderId());
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product SET name = ?, price = ?, category_id = ?, manufacturer_id = ?, provider_id = ? WHERE product_id = ?");
            ProductDto1 product = (ProductDto1) objectIn.readObject();
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getCategoryId());
            preparedStatement.setInt(4, product.getManufacturerId());
            preparedStatement.setInt(5, product.getProviderId());
            preparedStatement.setInt(6, product.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product WHERE product_id = ?");
            ProductDto1 product = (ProductDto1) objectIn.readObject();
            preparedStatement.setInt(1, product.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
