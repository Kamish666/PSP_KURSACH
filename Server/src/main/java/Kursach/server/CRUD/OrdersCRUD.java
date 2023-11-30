package Kursach.server.CRUD;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OrdersCRUD extends AbstractCrud{

    //какого хуя везде лист стрингов, а тут блять Integer
    private List<Integer> list = new ArrayList<>();
    private List<String> list_product = new ArrayList<>();
    private List<String> list_client = new ArrayList<>();

    private Scanner scanner = new Scanner(System.in);

    public OrdersCRUD(Socket clientSocket, int choice) throws IOException {
        super(clientSocket, choice);

    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getInt("orders_id"));
                System.out.println(resultSet.getInt("orders_id"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE orders_id = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM client WHERE client_id = ?");
            while (true) {
                String orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else {
                    preparedStatement.setInt(1, Integer.parseInt(orderNumber));
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int orderId = resultSet.getInt("orders_id");
                    String productName;
                    String clientName;
                    int productId = resultSet.getInt("product_id");
                    int clientId = resultSet.getInt("client_id");
                    int amount = resultSet.getInt("amount");
                    Date date = resultSet.getDate("date");

                    preparedStatement1.setInt(1, productId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    productName = resultSet.getString("name");

                    preparedStatement2.setInt(1, clientId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    clientName = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(productName);
                    System.out.println(clientName);
                    System.out.println(amount);
                    System.out.println(date);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*    private void insert() {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO orders (product_id, client_id, date, amount) VALUES (?, ?, ?, ?)");
            String orderNumber = new String();
            String product_name = new String();
            String client_name = new String();
            Date date = new Date();
            ListNameProduct();
            ListNameClient();
            int productId, clientId, amount;
            while (true) {
                while (true) {
                    orderNumber = scanner.nextLine();
                    if ("окно закрыто".equals(orderNumber)) {
                        return;
                    } else if ("добавить".equals(orderNumber)) {
                        try {
                            product_name = scanner.nextLine();
                            client_name = scanner.nextLine();
                            date = scanner.nextLine();
                            amount = scanner.nextInt();

                            productId = getIdProduct(product_name);
                            clientId = getIdClient(client_name);

                            preparedStatement1.setInt(1, productId);
                            preparedStatement1.setInt(2, clientId);
                            preparedStatement1.setDate(3, date);
                            preparedStatement1.setInt(4, amount);
                            preparedStatement1.execute();
                            System.out.println("order is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("order is");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void insert() {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO orders (product_id, client_id, date, amount) VALUES (?, ?, ?, ?)");
            String orderNumber;
            String product_name;
            String client_name;
            Date date = new Date();
            ListNameProduct();
            ListNameClient();
            int productId, clientId, amount;

            while (true) {
                orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else if ("добавить".equals(orderNumber)) {
                    try {
                        product_name = scanner.nextLine();
                        client_name = scanner.nextLine();
                        date = java.sql.Date.valueOf(scanner.nextLine()); // Преобразование строки в java.sql.Date
                        amount = scanner.nextInt();

                        productId = getIdProduct(product_name);
                        clientId = getIdClient(client_name);

                        preparedStatement1.setInt(1, productId);
                        preparedStatement1.setInt(2, clientId);
                        preparedStatement1.setDate(3, (java.sql.Date) date);
                        preparedStatement1.setInt(4, amount);
                        preparedStatement1.execute();
                        System.out.println("order is now");
                    } catch (SQLException e) {
                        System.out.println("Ошибка при добавлении заказа: " + e.getMessage());
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(resultSet.getInt("orders_id"));
                System.out.println(resultSet.getString("orders_id"));
            }
            ListNameProduct();
            ListNameClient();

            //отправка list_product, list_client

            String orderNumber;
            int orderId = 0;
            int productId, clientId, amount;
            String product_name = new String();
            String client_name = new String();
            Date date = new Date();
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE orders_id = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM client WHERE client_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("UPDATE orders SET product_id = ?, client_id = ?, date = ?, amount = ? WHERE orders_id = ?");
            while (true) {
                orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else if ("редактировать".equals(orderNumber) && orderId != 0) {
                    try {
                        product_name = scanner.nextLine();
                        client_name = scanner.nextLine();
                        date = java.sql.Date.valueOf(scanner.nextLine()); // Преобразование строки в java.sql.Date
                        amount = scanner.nextInt();

                        productId = getIdProduct(product_name);
                        clientId = getIdClient(client_name);

                        preparedStatement3.setInt(1, productId);
                        preparedStatement3.setInt(2, clientId);
                        preparedStatement3.setDate(3, (java.sql.Date) date);
                        preparedStatement3.setInt(4, amount);
                        preparedStatement3.setInt(5, orderId);
                        preparedStatement3.execute();
                        System.out.println("order is now");
                        break;
                    } catch (SQLException e) {
                        System.out.println("order is");
                    }
                } else {
                    preparedStatement.setInt(1, Integer.parseInt(orderNumber));
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    orderId = resultSet.getInt("orders_id");
                    productId = resultSet.getInt("product_id");
                    clientId = resultSet.getInt("client_id");
                    amount = resultSet.getInt("amount");
                    date = resultSet.getDate("date");

                    preparedStatement1.setInt(1, productId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    product_name = resultSet.getString("name");

                    preparedStatement2.setInt(1, clientId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    client_name = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(product_name);
                    System.out.println(client_name);
                    System.out.println(amount);
                    System.out.println(date);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getInt("orders_id"));
                System.out.println(resultSet.getString("orders_id"));
            }

            String orderNumber;
            int orderId = 0;
            int productId, clientId, amount;
            String product_name = new String();
            String client_name = new String();
            Date date = new Date();
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE orders_id = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM client WHERE client_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM orders WHERE orders_id = ?");
            while (true) {
                orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else if ("удалить".equals(orderNumber) && orderId != 0) {
                    try {
                        preparedStatement3.setInt(1, orderId);
                        preparedStatement3.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("order is in use");
                    }
                } else {
                    preparedStatement.setInt(1, Integer.parseInt(orderNumber));
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    orderId = resultSet.getInt("orders_id");
                    productId = resultSet.getInt("product_id");
                    clientId = resultSet.getInt("client_id");
                    amount = resultSet.getInt("amount");
                    date = resultSet.getDate("date");

                    preparedStatement1.setInt(1, productId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    product_name = resultSet.getString("name");

                    preparedStatement2.setInt(1, clientId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    client_name = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(product_name);
                    System.out.println(client_name);
                    System.out.println(amount);
                    System.out.println(date);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getIdProduct(String product_name) throws SQLException {
        int id_product = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT product_id FROM product WHERE name = ?")) {
            statement.setString(1, product_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_product = resultSet.getInt("product_id");
            }
        }
        return id_product;
    }

    private int getIdClient(String client_name) throws SQLException {
        int id_client = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT client_id FROM client WHERE name = ?")) {
            statement.setString(1, client_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_client = resultSet.getInt("client_id");
            }
        }
        return id_client;
    }

    private void ListNameProduct() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM product");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String productName = resultSet.getString("name");
                list_product.add(productName);
                System.out.println(productName);
            }
        }
    }

    private void ListNameClient() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM client");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String clientName = resultSet.getString("name");
                list_client.add(clientName);
                System.out.println(clientName);
            }
        }
    }

    class Order {
        private int id;
        private String orderNumber;
        private int productId;
        private int clientId;

        Order(int id, String orderNumber, int productId, int clientId) {
            this.id = id;
            this.orderNumber = orderNumber;
            this.productId = productId;
            this.clientId = clientId;
        }

        Order() {
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
