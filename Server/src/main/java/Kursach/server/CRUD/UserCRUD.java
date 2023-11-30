package Kursach.server.CRUD;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCRUD extends AbstractCrud {
    private int id;

    private List<String> list = new ArrayList<>();


    public UserCRUD(Socket clientSocket, int choice, int id) throws IOException {
        super(clientSocket, choice);
        this.id = id;

    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user_id <> ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE name = ?");
            while (true) {
                String userName = scanner.nextLine();

                if ("окно закрыто".equals(userName)) {
                    break;
                } else {
                    preparedStatement.setString(1, userName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int userId = resultSet.getInt("user_id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    int role = resultSet.getInt("role");
                    System.out.println(userId);
                    System.out.println(userName);
                    System.out.println(login);
                    System.out.println(password);
                    System.out.println(role);
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
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO user (name, login, password, role) VALUES (?, ?, ?, ?)");
            String name = new String();
            String login = new String();
            String password = new String();
            int role = 0;
            while (true) {
                while (true) {
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            name = scanner.nextLine();
                            login = scanner.nextLine();
                            password = scanner.nextLine();
                            role = scanner.nextInt();
                            scanner.nextLine();  // Очистка буфера после nextInt
                            preparedStatement1.setString(1, name);
                            preparedStatement1.setString(2, login);
                            preparedStatement1.setString(3, password);
                            preparedStatement1.setInt(4, role);
                            preparedStatement1.execute();
                            System.out.println("user is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("user is");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user_id <> ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String userName;
            String newUserName, newLogin, newPassword;
            int userId = 0, newRole;
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE user SET name = ?, login = ?, password = ?, role = ? WHERE user_id = ?");
            while (true) {
                userName = scanner.nextLine();

                if ("окно закрыто".equals(userName)) {
                    break;
                } else if ("редактировать".equals(userName) && userId != 0) {
                    try {
                        newUserName = scanner.nextLine();
                        newLogin = scanner.nextLine();
                        newPassword = scanner.nextLine();
                        newRole = scanner.nextInt();
                        scanner.nextLine();  // Очистка буфера после nextInt
                        preparedStatement1.setString(1, newUserName);
                        preparedStatement1.setString(2, newLogin);
                        preparedStatement1.setString(3, newPassword);
                        preparedStatement1.setInt(4, newRole);
                        preparedStatement1.setInt(5, userId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("user is");
                    }
                } else {
                    preparedStatement.setString(1, userName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    userId = resultSet.getInt("user_id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    int role = resultSet.getInt("role");
                    System.out.println(userId);
                    System.out.println(userName);
                    System.out.println(login);
                    System.out.println(password);
                    System.out.println(role);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user_id <> ?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String userName;
            int userId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM user WHERE user_id = ?");
            while (true) {
                userName = scanner.nextLine();

                if ("окно закрыто".equals(userName)) {
                    break;
                } else if ("удалить".equals(userName) && userId != 0) {
                    try {
                        preparedStatement1.setInt(1, userId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("user is in use");
                    }
                } else {
                    preparedStatement.setString(1, userName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    userId = resultSet.getInt("user_id");
                    String login = resultSet.getString("login");
                    String password = resultSet.getString("password");
                    int role = resultSet.getInt("role");
                    System.out.println(userId);
                    System.out.println(userName);
                    System.out.println(login);
                    System.out.println(password);
                    System.out.println(role);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
