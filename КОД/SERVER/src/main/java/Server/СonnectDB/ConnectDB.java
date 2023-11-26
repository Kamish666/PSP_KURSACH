/*
package Server.СonnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private Connection connection;

    public ConnectDB() {
        try {
            ReaderDBdata data = new ReaderDBdata();
            connection = DriverManager.getConnection(data.getUrl(), data.getName(), data.getPassword());
            //connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
*/
