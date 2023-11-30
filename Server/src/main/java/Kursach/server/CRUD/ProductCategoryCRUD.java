package Kursach.server.CRUD;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryCRUD extends AbstractCrud{
    protected final List<String> list = new ArrayList<>();




    public ProductCategoryCRUD(Socket clientSocket, int choice) throws IOException {
        super(clientSocket, choice);

    }

    @Override
    protected void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product_category");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("category"));
                System.out.println(resultSet.getString("category"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM product_category WHERE category = ?");
            while (true) {
                String categoryName = scanner.nextLine();

                if ("окно закрыто".equals(categoryName)) {
                    break;
                } else {
                    preparedStatement.setString(1, categoryName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int categoryId = resultSet.getInt("product_category_id");
                    String definition = resultSet.getString("definition");
                    System.out.println(categoryId);
                    System.out.println(categoryName);
                    System.out.println(definition);
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
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO product_category (category, definition) VALUES (?, ?)");
            String category = new String();
            String definition = new String();
            while(true) {
                while (true) {
                    category = scanner.nextLine();
                    if ("окно закрыто".equals(category)) {
                        return;
                    } else if ("добавить".equals(category)) {
                        try {
                            category = scanner.nextLine();
                            definition = scanner.nextLine();
                            preparedStatement1.setString(1, category);
                            preparedStatement1.setString(2, definition);
                            preparedStatement1.execute();
                            System.out.println("product_category is now");
                            break;
                        } catch (SQLException e) {
                            System.out.println("product_category is");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product_category");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("category"));
                System.out.println(resultSet.getString("category"));
            }

            String categoryName;
            String newCategoryName, newDefinition;
            int categoryId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM product_category WHERE category = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE product_category SET category = ?, definition = ? WHERE product_category_id = ?");
            while (true) {
                categoryName = scanner.nextLine();

                if ("окно закрыто".equals(categoryName)) {
                    break;
                } else if ("редактировать".equals(categoryName) && categoryId != 0) {
                    try {
                        newCategoryName = scanner.nextLine();
                        newDefinition = scanner.nextLine();
                        preparedStatement1.setString(1, newCategoryName);
                        preparedStatement1.setString(2, newDefinition);
                        preparedStatement1.setInt(3, categoryId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("product_category is");
                    }
                } else {
                    preparedStatement.setString(1, categoryName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    categoryId = resultSet.getInt("product_category_id");
                    String definition = resultSet.getString("definition");
                    System.out.println(categoryId);
                    System.out.println(categoryName);
                    System.out.println(definition);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM product_category");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getString("category"));
                System.out.println(resultSet.getString("category"));
            }

            String categoryName;
            int categoryId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM product_category WHERE category = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM product_category WHERE product_category_id = ?");
            while (true) {
                categoryName = scanner.nextLine();

                if ("окно закрыто".equals(categoryName)) {
                    break;
                } else if ("удалить".equals(categoryName) && categoryId != 0) {
                    try {
                        preparedStatement1.setInt(1, categoryId);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("product_category is in use");
                    }
                } else {
                    preparedStatement.setString(1, categoryName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    categoryId = resultSet.getInt("product_category_id");
                    String definition = resultSet.getString("definition");
                    System.out.println(categoryId);
                    System.out.println(categoryName);
                    System.out.println(definition);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class ProductCategory {
        private int id;
        private String category;
        private String definition;

        ProductCategory(int id, String category, String definition){
            this.id = id;
            this.category = category;
            this.definition = definition;
        }

        ProductCategory(){}

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
