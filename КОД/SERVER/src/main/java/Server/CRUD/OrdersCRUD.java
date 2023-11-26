package Server.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrdersCRUD {
    private Connection connection;

    private List<Order> list = new ArrayList<>();

    private List<String> list1 = new ArrayList<>();
    private List<String> list_product = new ArrayList<>();
    private List<String> list_customer = new ArrayList<>();

    private Scanner scanner = new Scanner(System.in);

    public OrdersCRUD(Connection connection, int choise) {
        this.connection = connection;

        switch (choise) {
            case 1:
                select();
                break;
            case 2:
                insert();
                break;
            case 3:
                update();
                break;
            case 4:
                delete();
                break;
        }
    }

    private void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("order_number"));
                System.out.println(resultSet.getString("order_number"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE order_number = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM customer WHERE customer_id = ?");
            while (true) {
                String orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else {
                    preparedStatement.setString(1, orderNumber);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int orderId = resultSet.getInt("order_id");
                    String productName;
                    String customerName;
                    int productId = resultSet.getInt("product_id");
                    int customerId = resultSet.getInt("customer_id");

                    preparedStatement1.setInt(1, productId);
                    resultSet = preparedStatement1.executeQuery();
                    resultSet.next();
                    productName = resultSet.getString("name");

                    preparedStatement2.setInt(1, customerId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    customerName = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(orderNumber);
                    System.out.println(productName);
                    System.out.println(customerName);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insert() {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO orders (order_number, product_id, customer_id) VALUES (?, ?, ?)");
            String orderNumber = new String();
            String product_name = new String();
            String customer_name = new String();
            ListNameProduct();
            ListNameCustomer();
            int productId, customerId;
            while (true) {
                while (true) {
                    orderNumber = scanner.nextLine();
                    if ("окно закрыто".equals(orderNumber)) {
                        return;
                    } else if ("добавить".equals(orderNumber)) {
                        try {
                            orderNumber = scanner.nextLine();
                            product_name = scanner.nextLine();
                            customer_name = scanner.nextLine();

                            productId = getIdProduct(product_name);
                            customerId = getIdCustomer(customer_name);

                            preparedStatement1.setString(1, orderNumber);
                            preparedStatement1.setInt(2, productId);
                            preparedStatement1.setInt(3, customerId);
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
    }

    private void update() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list1.add(resultSet.getString("order_number"));
                System.out.println(resultSet.getString("order_number"));
            }
            ListNameProduct();
            ListNameCustomer();

            //отправка list_product, list_customer

            String orderNumber;
            String newOrderNumber;
            int orderId = 0;
            int productId, customerId;
            String product_name = new String();
            String customer_name = new String();
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE order_number = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE orders SET order_number = ?, product_id = ?, customer_id = ? WHERE order_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT name FROM customer WHERE customer_id = ?");
            while (true) {
                orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else if ("редактировать".equals(orderNumber) && orderId != 0) {
                    try {
                        newOrderNumber = scanner.nextLine();
                        product_name = scanner.nextLine();
                        customer_name = scanner.nextLine();

                        productId = getIdProduct(product_name);
                        customerId = getIdCustomer(customer_name);

                        preparedStatement1.setString(1, newOrderNumber);
                        preparedStatement1.setInt(2, productId);
                        preparedStatement1.setInt(3, customerId);
                        preparedStatement1.setInt(4, orderId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("order is");
                    }
                } else {
                    preparedStatement.setString(1, orderNumber);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    orderId = resultSet.getInt("order_id");
                    productId = resultSet.getInt("product_id");
                    customerId = resultSet.getInt("customer_id");

                    preparedStatement2.setInt(1, productId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    product_name = resultSet.getString("name");

                    preparedStatement3.setInt(1, customerId);
                    resultSet = preparedStatement3.executeQuery();
                    resultSet.next();
                    customer_name = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(orderNumber);
                    System.out.println(product_name);
                    System.out.println(customer_name);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("order_number"));
                System.out.println(resultSet.getString("order_number"));
            }

            String orderNumber;
            int orderId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE order_number = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM orders WHERE order_id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT name FROM product WHERE product_id = ?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("SELECT name FROM customer WHERE customer_id = ?");
            while (true) {
                orderNumber = scanner.nextLine();

                if ("окно закрыто".equals(orderNumber)) {
                    break;
                } else if ("удалить".equals(orderNumber) && orderId != 0) {
                    try {
                        preparedStatement1.setInt(1, orderId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("order is in use");
                    }
                } else {
                    preparedStatement.setString(1, orderNumber);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    orderId = resultSet.getInt("order_id");
                    String product_name;
                    String customer_name;
                    int productId = resultSet.getInt("product_id");
                    int customerId = resultSet.getInt("customer_id");

                    preparedStatement2.setInt(1, productId);
                    resultSet = preparedStatement2.executeQuery();
                    resultSet.next();
                    product_name = resultSet.getString("name");

                    preparedStatement3.setInt(1, customerId);
                    resultSet = preparedStatement3.executeQuery();
                    resultSet.next();
                    customer_name = resultSet.getString("name");

                    System.out.println(orderId);
                    System.out.println(orderNumber);
                    System.out.println(product_name);
                    System.out.println(customer_name);
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

    private int getIdCustomer(String customer_name) throws SQLException {
        int id_customer = -1;

        try (PreparedStatement statement = connection.prepareStatement("SELECT customer_id FROM customer WHERE name = ?")) {
            statement.setString(1, customer_name);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                id_customer = resultSet.getInt("customer_id");
            }
        }
        return id_customer;
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

    private void ListNameCustomer() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT name FROM customer");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String customerName = resultSet.getString("name");
                list_customer.add(customerName);
                System.out.println(customerName);
            }
        }
    }

    class Order {
        private int id;
        private String orderNumber;
        private int productId;
        private int customerId;

        Order(int id, String orderNumber, int productId, int customerId) {
            this.id = id;
            this.orderNumber = orderNumber;
            this.productId = productId;
            this.customerId = customerId;
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

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
