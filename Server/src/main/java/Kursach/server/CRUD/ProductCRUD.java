package Kursach.server.CRUD;

import Kursach.shared.objects.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCRUD extends AbstractCrud {

    public ProductCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product");
            PreparedStatement preparedStatementManufacturer = connection.prepareStatement("SELECT * FROM manufacturer WHERE manufacturer_id = ?");
            PreparedStatement preparedStatementCategory = connection.prepareStatement("SELECT * FROM product_category WHERE product_category_id = ?");
            PreparedStatement preparedStatementProvider = connection.prepareStatement("SELECT * FROM provider WHERE provider_id = ?");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ProductDto> products = new ArrayList<>();

            while (resultSet.next()) {
                ProductDto product = new ProductDto();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getInt("price"));

                preparedStatementManufacturer.setInt(1, resultSet.getInt("manufacturer_id"));
                ResultSet resultSetManufacturer = preparedStatementManufacturer.executeQuery();
                resultSetManufacturer.next();
                Manufacturer manufacturer = new Manufacturer(
                        resultSetManufacturer.getInt("manufacturer_id"),
                        resultSetManufacturer.getString("manufacturer_name"),
                        resultSetManufacturer.getInt("country_id")
                );
                product.setManufacturer(manufacturer);
                resultSetManufacturer.close();

                preparedStatementCategory.setInt(1, resultSet.getInt("category_id"));
                ResultSet resultSetCategory = preparedStatementCategory.executeQuery();
                resultSetCategory.next();
                ProductCategory category = new ProductCategory(
                        resultSetCategory.getInt("product_category_id"),
                        resultSetCategory.getString("category"),
                        resultSetCategory.getString("definition")
                );
                product.setCategory(category);
                resultSetCategory.close();

                preparedStatementProvider.setInt(1, resultSet.getInt("provider_id"));
                ResultSet resultSetProvider = preparedStatementProvider.executeQuery();
                resultSetProvider.next();
                Provider provider = new Provider(
                        resultSetProvider.getInt("provider_id"),
                        resultSetProvider.getString("name"),
                        resultSetProvider.getString("email")
                );
                product.setProvider(provider);
                resultSetProvider.close();

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
            ProductDto product = (ProductDto) objectIn.readObject();
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
            ProductDto product = (ProductDto) objectIn.readObject();
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
            ProductDto product = (ProductDto) objectIn.readObject();
            preparedStatement.setInt(1, product.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
