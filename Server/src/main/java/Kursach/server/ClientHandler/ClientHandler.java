package Kursach.server.ClientHandler;

import Kursach.server.CRUD.*;
import Kursach.server.Main;
import Kursach.shared.objects.User;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final ProductCategoryCRUD productCategoryCrud;
    private final ClientCRUD clientCrud;
    private final UserCRUD userCrud;
    private final ManufacturerCRUD manufacturerCrud;
    private final ProductCRUD productCrud;
    private final OrdersCRUD ordersCrud;
    private  CountryCRUD countryCrud;
    private  ProviderCRUD providerCrud;

    private PersonalProfile personalProfile;

    private Scanner scanner;
    private PrintWriter writer;
    private Socket clientSocket;
    private Connection connection;
    ObjectOutputStream objectOut;
    ObjectInputStream objectIn;
    private int id;
    private int role;


    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.connection = Main.getConnection();
        objectIn = new ObjectInputStream(clientSocket.getInputStream());
        objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
        System.out.println("Обменялись стримами");

        countryCrud = new CountryCRUD(objectIn, objectOut);
        providerCrud = new ProviderCRUD(objectIn, objectOut);
        productCategoryCrud = new ProductCategoryCRUD(objectIn, objectOut);
        clientCrud = new ClientCRUD(objectIn, objectOut);
        userCrud = new UserCRUD(objectIn, objectOut);
        manufacturerCrud = new ManufacturerCRUD(objectIn, objectOut);
        productCrud = new ProductCRUD(objectIn, objectOut);
        ordersCrud = new OrdersCRUD(objectIn, objectOut);
        personalProfile = new PersonalProfile(objectIn, objectOut);

    }

    public boolean register(String name, String login, String password) throws SQLException, IOException {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (name, login, password, role) VALUES (?,?,?,2)");
            preparedStatement.setString(1, name);//name
            preparedStatement.setString(2, login);//login
            preparedStatement.setString(3, password);//password
            int res = preparedStatement.executeUpdate();
            System.out.println(res);
            if (res > 0) {
                return login(login, password);
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean login(String login, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
            preparedStatement.setString(1, login);//login
            preparedStatement.setString(2, password);//password
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("user_id");
                userCrud.setId(id);
                personalProfile.setId(id);
                role = resultSet.getInt("role");
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void authorize() throws IOException, SQLException {
        try {
            int userCount = getUserCount();
            objectOut.writeObject(userCount);
            objectOut.flush();
            String name, login, password;
            User user = new User();
            if (userCount == 0) {
                while(true) {
                    System.out.println("waiting to register");
                    name = (String) objectIn.readObject();
                    login = (String) objectIn.readObject();
                    password = (String) objectIn.readObject();
                    System.out.println("calling register");
                    boolean registered = register(name, login, password);
                    if(registered) break;
                    else objectOut.writeObject(null);
                }

            } else {
                while (true) {
                    System.out.println("waiting to login");
                    login = (String) objectIn.readObject();
                    password = (String) objectIn.readObject();
                    System.out.println("calling login");
                    boolean loggedIn = login(login, password);
                    if(loggedIn) break;
                    else objectOut.writeObject(null);
                }

            }
            System.out.println("authorized");
            user.setRole(role);
            user.setId(id);
            objectOut.writeObject(user);
            objectOut.flush();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void performOperation(Integer choice) throws IOException {
        //операции с друмя числами это CRUD
        //первое число обозначает номер элемента, с которым будет выполняться CRUD
        //второе число обозначает какая именно операция:1-select, 2-insert, 3-update, 4-delete
        // прием данных
        int crudType = choice / 10;
        int crudMethod = choice % 10;
        System.out.println("Method: " + crudType + ", " + crudMethod);
        AbstractCrud crud = switch (crudType) {
            case 1 -> countryCrud;
            case 2 -> providerCrud;
            case 3 -> productCategoryCrud;
            case 4 -> clientCrud;
            case 5 -> userCrud;
            case 6 -> manufacturerCrud;
            case 7 -> productCrud;
            case 8 -> ordersCrud;
            case 9 -> personalProfile;
            default -> {
                throw new RuntimeException("Как же круто что число выбирает операции, а не енам, но мне впадлу енам делать уже, поэтому страдай исключением");
            }
        };
        crud.execute(crudMethod);
    }

    @Override
    public void run() {
        try {
            authorize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            while (true) {
                Integer choice = (Integer) objectIn.readObject();
                System.out.println("Recevied " + choice);
                performOperation(choice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private int getUserCount() throws SQLException {
        int userCount = 0;

        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user");
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                userCount = resultSet.getInt(1);
            }
        }
        System.out.println(userCount);
        if (userCount == 0) {
        } else userCount = 1;
        return userCount;
    }
}


//package Server.ClientHandler;
//
//import java.io.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import Server.CRUD.*;
//import java.util.Scanner;
//
//public class ClientHandler {
//    private Scanner scanner = new Scanner(System.in);
//    private Connection connection;
//    private int id;
//    private int role;
//
//
//    public ClientHandler(Connection connection) {
//        this.connection = connection;
//        //перенести потом в метод run
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE login = ? AND password = ?");
//            ResultSet resultSet = null;
//            int userCount = getUserCount();
//            if(userCount == 0){
//                preparedStatement = connection.prepareStatement("INSERT INTO user (name, login, password, role) VALUES (?,?,?,2)");
//                //прием данных и их отправка в таблицу
//                preparedStatement.setString(1, scanner.nextLine());//name
//                preparedStatement.setString(2, scanner.nextLine());//login
//                preparedStatement.setString(3, scanner.nextLine());//password
//                preparedStatement.execute();
//                //preparedStatement.setInt(4, 2);
//            }
//            else{
//                while(true) {
//                    //прием данных и их отправка на проверку
//                    preparedStatement.setString(1, scanner.nextLine());//login
//                    preparedStatement.setString(2, scanner.nextLine());//password
//                    resultSet = preparedStatement.executeQuery();
//                    if (resultSet.next()) {
//                        id = resultSet.getInt("user_id");
//                        role = resultSet.getInt("role");
//                        break;
//                    }
//
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        while (true){
//            //операции с друмя числами это CRUD
//            //первое число обозначает номер элемента, с которым будет выполняться CRUD
//            //второе число обозначает какая именно операция:1-select, 2-insert, 3-update, 4-delete
//            // прием данных
//            switch (scanner.nextInt()){
//                //Country
//                case 11:{CountryCRUD a = new CountryCRUD(connection,1); break;}
//                case 12:{CountryCRUD a = new CountryCRUD(connection,2); break;}
//                case 13:{CountryCRUD a = new CountryCRUD(connection,3); break;}
//                case 14:{CountryCRUD a = new CountryCRUD(connection,4); break;}
//                //Provider
//                case 21:{ProviderCRUD a = new ProviderCRUD(connection,1); break;}
//                case 22:{ProviderCRUD a = new ProviderCRUD(connection,2); break;}
//                case 23:{ProviderCRUD a = new ProviderCRUD(connection,3); break;}
//                case 24:{ProviderCRUD a = new ProviderCRUD(connection,4); break;}
//                //ProductCategory
//                case 31:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,1); break;}
//                case 32:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,2); break;}
//                case 33:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,3); break;}
//                case 34:{ProductCategoryCRUD a = new ProductCategoryCRUD(connection,4); break;}
//                //Client
//                case 41:{ClientCRUD a = new ClientCRUD(connection,1); break;}
//                case 42:{ClientCRUD a = new ClientCRUD(connection,2); break;}
//                case 43:{ClientCRUD a = new ClientCRUD(connection,3); break;}
//                case 44:{ClientCRUD a = new ClientCRUD(connection,4); break;}
//                //User
//                case 51:{UserCRUD a = new UserCRUD(connection,1, id); break;}
//                case 52:{UserCRUD a = new UserCRUD(connection,2, id); break;}
//                case 53:{UserCRUD a = new UserCRUD(connection,3, id); break;}
//                case 54:{UserCRUD a = new UserCRUD(connection,4, id); break;}
//                //Manufacturer
//                case 61:{ManufacturerCRUD a = new ManufacturerCRUD(connection,1); break;}
//                case 62:{ManufacturerCRUD a = new ManufacturerCRUD(connection,2); break;}
//                case 63:{ManufacturerCRUD a = new ManufacturerCRUD(connection,3); break;}
//                case 64:{ManufacturerCRUD a = new ManufacturerCRUD(connection,4); break;}
//                //Product
//                case 71:{ProductCRUD a = new ProductCRUD(connection,1); break;}
//                case 72:{ProductCRUD a = new ProductCRUD(connection,2); break;}
//                case 73:{ProductCRUD a = new ProductCRUD(connection,3); break;}
//                case 74:{ProductCRUD a = new ProductCRUD(connection,4); break;}
//                //Orders
//                case 81:{OrdersCRUD a = new OrdersCRUD(connection,1); break;}
//                case 82:{OrdersCRUD a = new OrdersCRUD(connection,2); break;}
//                case 83:{OrdersCRUD a = new OrdersCRUD(connection,3); break;}
//                case 84:{OrdersCRUD a = new OrdersCRUD(connection,4); break;}
//                default:System.out.println("Error");
//            }
//        }
//    }
//
//    private int getUserCount() throws SQLException {
//        int userCount = 0;
//
//        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user");
//             ResultSet resultSet = statement.executeQuery()) {
//
//            if (resultSet.next()) {
//                userCount = resultSet.getInt(1);
//            }
//        }
//        System.out.println(userCount);
//        if (userCount == 0) {
//        } else userCount = 1;
//        return userCount;
//    }
//}
