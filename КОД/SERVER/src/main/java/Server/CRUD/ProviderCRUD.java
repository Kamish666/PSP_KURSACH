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

    //функция просмотра
    private void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();
            //здесь читается таблица provider и записывается в list1, после чего list1 отперавляется клиенту
            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                //строку ниже надо удалить
                System.out.println(resultSet.getString("name"));
            }
            //здесь list1 надо отправить клиенту
            //у клиента должен быть JList (так в swing) куда заносятся все имена
            //когда человек кликает на имя, в полях появляются значения, которые клиент не може изменять
            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");

            while (true) {
                //команда принимается с сервера
                //если клиент закрыл окно то отправляется окно закрыто
                //если клиент выбрал какогото постаущика, то отсылается его имя
                String providerName = scanner.nextLine();

                if ("окно закрыто".equals(providerName)) {
                    break;
                } else {//метод для поиска информации о поставщике
                    //ищем поставщика в таблице
                    preparedStatement.setString(1, providerName);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    //подтягиваем данные из таблицы
                    int providerId = resultSet.getInt("provider_id");
                    String email = resultSet.getString("email");
                    //отправляем данные клиенту
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

    //функция добавления новых значений
    private void insert() {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO provider (name, email) VALUES (?, ?)");
            String name = new String();
            String email = new String();
            while(true) {
                //бесконечный цикл, из которого можно выйти, только если окно закрыто
                while (true) {
                    //принимаем каманду с клиента
                    name = scanner.nextLine();
                    if ("окно закрыто".equals(name)) {
                        return;
                    } else if ("добавить".equals(name)) {
                        try {
                            //принимает данные с клиента (все кроме id)
                            name = scanner.nextLine();
                            email = scanner.nextLine();
                            //заносим их в запрос
                            preparedStatement1.setString(1, name);
                            preparedStatement1.setString(2, email);
                            preparedStatement1.execute();
                            //если все прошло удачно, отправляем 1
                            System.out.println("provider is now");
                            break;
                        } catch (SQLException e) {
                            //если неудачно, отправляем 0
                            System.out.println("provider is");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //функция обновления
    private void update() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();
            //здесь читается таблица provider и записывается в list1, после чего list1 отперавляется клиенту
            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                //строку ниже надо удалить
                System.out.println(resultSet.getString("name"));
            }
            //здесь list1 надо отправить клиенту
            //у клиента должен быть JList (так в swing) куда заносятся все имена
            //когда человек кликает на имя, в полях появляются значения, которые клиент может изменять (все кроме id)
            String providerName;
            String newProviderName, newProviderEmail;
            int providerId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE provider SET name = ?, email = ? WHERE provider_id = ?");
            //цикл из которого можно выйти в 2 случаях. 1. если окно закрыто
            //если операция по добавлению успешно выполнена
            while (true) {
                providerName = scanner.nextLine();
                //принимаем данные с склиента.
                //при нажатии кнопка у клиента оправляется сначала действие кнопки, а только потом данные занесенные в полях
                if ("окно закрыто".equals(providerName)) {
                    break;
                    //метод ниже (редактировать) можно использовать, только когда нажата кнопка редактировать и
                    //пользователь до этого посмотрел какого либо провайдера и ему отобразилась о нем информация
                    //то есть else if может выполнится только после того как произошел else
                } else if ("редактировать".equals(providerName) && providerId != 0) {
                    try {
                        //принимаем навые данные с клиента
                        newProviderName = scanner.nextLine();
                        newProviderEmail = scanner.nextLine();
                        //заносим их в запрос
                        preparedStatement1.setString(1, newProviderName);
                        preparedStatement1.setString(2, newProviderEmail);
                        preparedStatement1.setInt(3, providerId);
                        //делаем запрос
                        preparedStatement1.executeUpdate();
                        //если все удачно, отправляем 1 и нас выбрасывает из этой функции в ClientHandler
                        break;
                    } catch (SQLException e) {
                        //если ошибка, отправляем 0 и остаемся в этой функции
                        System.out.println("provider is");
                    }
                } else {
                    //тоже самое, что и в select
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
            //тоже самое, что и в select
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM provider");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                list1.add(resultSet.getString("name"));
                System.out.println(resultSet.getString("name"));
            }

            //отправляем клиенту список поставщиков
            //все тоже самое, что и select, но здесь никакие поля не должны быть редактируемые клиентом
            String providerName;
            int providerId = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM provider WHERE name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM provider WHERE provider_id = ?");
            //цикл из которого можно выйти в 2 случаях
            //1 окно закрыто
            //2 операция успешно выполнен
            //код ниже чем-то похож на update
            while (true) {
                providerName = scanner.nextLine();
                //если нажата кнопка, передаются ее название
                if ("окно закрыто".equals(providerName)) {
                    break;
                } else if ("удалить".equals(providerName) && providerId != 0) {
                    try {
                        //заноси имя которое мы получили в else
                        preparedStatement1.setInt(1, providerId);
                        preparedStatement1.executeUpdate();
                        break;
                        //если операция успешно выполнена, отправляем 1 и нас выбрасываетс в ClientHandler
                    } catch (SQLException e) {
                        // если ошибка, отправляем 2, и продолжаем работать в этот окне
                        System.out.println("provider is in use");
                    }
                } else {
                    //тоже самое что и ы select и update
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

