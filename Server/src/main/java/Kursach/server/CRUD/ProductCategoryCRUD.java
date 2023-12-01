package Kursach.server.CRUD;

import Kursach.shared.objects.ProductCategory;

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

public class ProductCategoryCRUD extends AbstractCrud{
    protected final List<String> list = new ArrayList<>();




    Scanner scanner;
    public ProductCategoryCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product_category");

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ProductCategory> productCategorys = new ArrayList<>();
            while (resultSet.next()) {
                ProductCategory productCategory = new ProductCategory(
                        resultSet.getInt("product_category"),
                        resultSet.getString("category"),
                        resultSet.getString("definition")
                );
                productCategorys.add(productCategory);
            }
            System.out.println(productCategorys);
            objectOut.writeObject(productCategorys);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product_category (category, definition) VALUES (?, ?)");
            ProductCategory productCategory = (ProductCategory) objectIn.readObject();
            try {
                preparedStatement.setString(1, productCategory.getCategory());
                preparedStatement.setString(1, productCategory.getDefinition());
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product_category SET category = ?, definition = ? WHERE product_category_id = ?");
            ProductCategory productCategory = (ProductCategory) objectIn.readObject();
            preparedStatement.setString(1, productCategory.getCategory());
            preparedStatement.setString(2, productCategory.getDefinition());
            preparedStatement.setInt(3, productCategory.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product_category WHERE product_category_id = ?");
            ProductCategory productCategory = (ProductCategory) objectIn.readObject();
            preparedStatement.setInt(1, productCategory.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
