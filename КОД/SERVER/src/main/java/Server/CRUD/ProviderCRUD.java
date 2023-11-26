package Server.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProviderCRUD {
    private Connection connection;

    private List<Provider> list = new ArrayList<>();

    private List<String> list1 = new ArrayList<>();

    private Scanner scanner = new Scanner(System.in);

    public ProviderCRUD(Connection connection, int choise) {
        this.connection = connection;

        switch (choise) {
            case 1: select(); break;
            case 2: insert(); break;
            case 3: update(); break;
            case 4: delete(); break;
        }
    }

    private void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");
            while (true) {
                String providerName = scanner.nextLine();

                if ("окно закрыто".equals(providerName)) {
                    break;
                } else {
                    preparedStatement.setString(1, providerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int providerId = resultSet.getInt("provider_id");
                    String email = resultSet.getString("email");
                    System.out.println(providerId);
                    System.out.println(providerName);
                    System.out.println(email);
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
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO provider (name, email) VALUES (?, ?)");
            String name = new String();
            String email = new String();
            while(true) {
                while (true) {
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            name = scanner.nextLine();
                            email = scanner.nextLine();
                            preparedStatement1.setString(1, name);
                            preparedStatement1.setString(2, email);
                            preparedStatement1.execute();
                            System.out.println("provider is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("provider is");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String providerName;
            String newProviderName, newProviderEmail;
            int providerId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE provider SET name = ?, email = ? WHERE provider_id = ?");
            while (true) {
                providerName = scanner.nextLine();

                if ("окно закрыто".equals(providerName)) {
                    break;
                } else if ("редактировать".equals(providerName) && providerId != 0) {
                    try {
                        newProviderName = scanner.nextLine();
                        newProviderEmail = scanner.nextLine();
                        preparedStatement1.setString(1, newProviderName);
                        preparedStatement1.setString(2, newProviderEmail);
                        preparedStatement1.setInt(3, providerId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("provider is");
                    }
                } else {
                    preparedStatement.setString(1, providerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    providerId = resultSet.getInt("provider_id");
                    String email = resultSet.getString("email");
                    System.out.println(providerId);
                    System.out.println(providerName);
                    System.out.println(email);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            String providerName;
            int providerId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM provider WHERE provider_id = ?");
            while (true) {
                providerName = scanner.nextLine();

                if ("окно закрыто".equals(providerName)) {
                    break;
                } else if ("удалить".equals(providerName) && providerId != 0) {
                    try {
                        preparedStatement1.setInt(1, providerId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("provider is in use");
                    }
                } else {
                    preparedStatement.setString(1, providerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    providerId = resultSet.getInt("provider_id");
                    String email = resultSet.getString("email");
                    System.out.println(providerId);
                    System.out.println(providerName);
                    System.out.println(email);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class Provider {
        private int id;
        private String name;
        private String email;

        Provider(int id, String name, String email){
            this.id = id;
            this.name = name;
            this.email = email;
        }

        Provider(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}

