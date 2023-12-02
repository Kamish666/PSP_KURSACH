package Kursach.server.CRUD;

import Kursach.shared.objects.OrderDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrdersCRUD extends AbstractCrud{




    Scanner scanner;
    public OrdersCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");
            PreparedStatement preparedStatementProduct = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatementClient = connection.prepareStatement("SELECT name FROM client WHERE client_id = ?");

            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSetProduct = preparedStatementProduct.executeQuery();
            ResultSet resultSetClient = preparedStatementClient.executeQuery();
            List<OrderDto> orders = new ArrayList<>();

            while (resultSet.next()) {
                OrderDto order = new OrderDto();
                order.setId(resultSet.getInt("orders_id"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                order.setDateTime(timestamp.toLocalDateTime());
                order.setAmount(resultSet.getInt("amount"));

                order.setProductId(resultSet.getInt("product_id"));
                preparedStatementProduct.setInt(1, resultSet.getInt("product_id"));
                order.setProductName(resultSetProduct.getString("name"));

                order.setClientId(resultSet.getInt("client_id"));
                preparedStatementClient.setInt(1, resultSet.getInt("client_id"));
                order.setClientName(resultSetClient.getString("name"));

                orders.add(order);
            }

            System.out.println(orders);
            objectOut.writeObject(orders);


            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (product_id, client_id, date, amount) VALUES (?, ?, ?, ?)");
            OrderDto order = (OrderDto) objectIn.readObject();
            try {
                preparedStatement.setInt(1, order.getProductId());
                preparedStatement.setInt(2, order.getClientId());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getDateTime()));
                preparedStatement.setInt(4, order.getAmount());
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE orders SET product_id = ?, client_id = ?, date = ?, amount = ? WHERE orders_id = ?");
            OrderDto order = (OrderDto) objectIn.readObject();
            preparedStatement.setInt(1, order.getProductId());
            preparedStatement.setInt(2, order.getClientId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(order.getDateTime()));
            preparedStatement.setInt(4, order.getAmount());
            preparedStatement.setInt(5, order.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM orders WHERE orders_id = ?");
            OrderDto order = (OrderDto) objectIn.readObject();
            preparedStatement.setInt(1, order.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
