package Server.CRUD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Scanner;

public class CountryCRUD {
    private Connection connection;

    private List<Country> list = new ArrayList<>();

    private List<String> list1 = new ArrayList<>();

    //в итоговаом вариате удалить
    //
    private Scanner scanner = new Scanner(System.in);
    //
    //


    public CountryCRUD(Connection connection, int choise) {
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country");

            ResultSet resultSet = preparedStatement.executeQuery();
/*            Country object = new Country();*/
            while (resultSet.next()) {
/*                object.setId(resultSet.getInt("country_id"));
                object.setCountry(resultSet.getString("country_name"));
                list.add(object);*/
                //способ 2
                list1.add(resultSet.getString("country_name"));
                System.out.println(resultSet.getString("country_name"));
            }

            //способ 1 с list и object
            //отправка данных (list) на сервер


            //способ 2 с list1
            //отправка данных (list1) на сервер

            preparedStatement = connection.prepareStatement("SELECT * FROM country Where country_name = ?");
            while (true) {
                //прием строки с с сервера
                String name_country = scanner.nextLine();
                //
                if ("окно закрыто".equals(name_country)) {
                    break;
                } else{
                    preparedStatement.setString(1, name_country);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int country_id = resultSet.getInt("country_id");
                    //
                    //отправка country_id
                    //
                    System.out.println(country_id);
                    System.out.println(name_country);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*    private void select() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list1.add(resultSet.getString("country_name"));
                System.out.println(resultSet.getString("country_name"));
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM country Where country_name = ?");
            while (true) {
                String name_country = scanner.nextLine();
                if ("окно закрыто".equals(name_country)) {
                    break;
                } else {
                    preparedStatement.setString(1, name_country);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    int country_id = resultSet.getInt("country_id");
                    System.out.println(country_id);
                    System.out.println(name_country);
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/



    // Метод для добавления данных
    private void insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country Where country_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO country (country_name) VALUES (?)");
            ResultSet resultSet = null;
            while(true) {
                //
                //получаю название страны
                String name_country = scanner.nextLine();
                if("окно закрыто".equals(name_country)){ break; }
                preparedStatement.setString(1, name_country);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()) {//если это выполняется, значит страны с таким названием еще нет
                    //отправляем 1
                    preparedStatement1.setString(1, name_country);
                    preparedStatement1.execute();
                    System.out.println("country is now");
                }else{
                //страна с таким названием уже существует
                //отправляем 0
                System.out.println("country is");}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для редактирования данных
    private void update() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country");

            ResultSet resultSet = preparedStatement.executeQuery();
            /*            Country object = new Country();*/
            while (resultSet.next()) {
/*                object.setId(resultSet.getInt("country_id"));
                object.setCountry(resultSet.getString("country_name"));
                list.add(object);*/
                //способ 2
                list1.add(resultSet.getString("country_name"));
                System.out.println(resultSet.getString("country_name"));
            }

            //способ 1 с list и object
            //отправка данных (list) на сервер


            //способ 2 с list1
            //отправка данных (list1) на сервер
            String name_country;
            String new_name_country;
            int country_id = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM country Where country_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE country SET country_name = ? WHERE country_id = ?");
            while (true) {
                //прием строки с сервера
                name_country = scanner.nextLine();
                //
                if ("окно закрыто".equals(name_country)) {
                    break;
                }else if ("редактировать".equals(name_country) && country_id != 0) {
                    try {
                        // получаю новое имя
                        new_name_country = scanner.nextLine();
                        //выполняю обновление
                        preparedStatement1.setString(1, new_name_country);
                        preparedStatement1.setInt(2, country_id);
                        preparedStatement1.executeUpdate();
                        break;
                    }catch (SQLException e){
                        System.out.println("country is");
                    }
                } else {
                    preparedStatement.setString(1, name_country);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    country_id = resultSet.getInt("country_id");
                    //
                    //отправка country_id
                    //
                    System.out.println(country_id);
                    System.out.println(name_country);

                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для удаления данных
    private void delete() {
/*        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM country WHERE country_id = ?");
            preparedStatement.setInt(1, countryId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Данные удалены успешно.");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM country");

            ResultSet resultSet = preparedStatement.executeQuery();
            /*            Country object = new Country();*/
            while (resultSet.next()) {
/*                object.setId(resultSet.getInt("country_id"));
                object.setCountry(resultSet.getString("country_name"));
                list.add(object);*/
                //способ 2
                list1.add(resultSet.getString("country_name"));
                System.out.println(resultSet.getString("country_name"));
            }

            //способ 1 с list и object
            //отправка данных (list) на сервер


            //способ 2 с list1
            //отправка данных (list1) на сервер
            String name_country;
            int country_id = 0;
            preparedStatement = connection.prepareStatement("SELECT * FROM country Where country_name = ?");
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM country WHERE country_id = ?");
            while (true) {
                //прием строки с сервера
                name_country = scanner.nextLine();
                //
                if ("окно закрыто".equals(name_country)) {
                    break;
                } else if ("удалить".equals(name_country) && country_id != 0) {
                    try {
                        //выполняю удаление
                        preparedStatement1.setInt(1, country_id);
                        preparedStatement1.executeUpdate();
                        break;
                    } catch (SQLException e) {
                        System.out.println("country is use");
                    }
                } else {
                    preparedStatement.setString(1, name_country);
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    country_id = resultSet.getInt("country_id");
                    //
                    //отправка country_id
                    //
                    System.out.println(country_id);
                    System.out.println(name_country);

                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class Country {
        private int id;
        private String country;

        Country(int id, String country){
            this.id=id;
            this.country=country;
        }
        Country(){}

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
