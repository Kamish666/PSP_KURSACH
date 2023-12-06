package Kursach.server.CRUD;

import Kursach.shared.objects.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class OrdersCRUD extends AbstractCrud {


    Scanner scanner;

    public OrdersCRUD(ObjectInputStream objectIn, ObjectOutputStream objectOut) throws IOException {
        super(objectIn, objectOut);
    }

    @Override
    protected void select() {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT
                    o.orders_id, o.date, o.amount,
                    p.product_id, p.name AS product_name, p.price, p.category_id, p.manufacturer_id, p.provider_id,
                    c.client_id, c.name AS client_name, c.email
                    FROM orders o
                    JOIN product p ON o.product_id = p.product_id
                    JOIN client c ON o.client_id = c.client_id
                    """);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<OrderDto> orders = new ArrayList<>();

            while (resultSet.next()) {
                OrderDto order = new OrderDto();
                order.setId(resultSet.getInt("orders_id"));
                Timestamp timestamp = resultSet.getTimestamp("date");
                order.setDateTime(timestamp.toLocalDateTime());
                order.setAmount(resultSet.getInt("amount"));

                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("category_id"),
                        resultSet.getInt("manufacturer_id"),
                        resultSet.getInt("provider_id")
                );
                order.setProduct(product);

                Client client = new Client(
                        resultSet.getInt("client_id"),
                        resultSet.getString("client_name"),
                        resultSet.getString("email")
                );
                order.setClient(client);

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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = order.getDateTime().format(formatter);
            try {
                preparedStatement.setInt(1, order.getProductId());
                preparedStatement.setInt(2, order.getClientId());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(formattedDateTime));
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
