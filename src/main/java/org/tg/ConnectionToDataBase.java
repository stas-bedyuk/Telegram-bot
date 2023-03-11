package org.tg;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionToDataBase {
    String userName = "admin";
    String password = "1234";
    String url = "jdbc:sqlserver://192.168.56.1:49389;database=EnglishWordsDatabase;encrypt=false;trustServerCertificate=true;" ;
Connection Connect() throws IOException, ClassNotFoundException, SQLException {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


            Connection conn = DriverManager.getConnection(url,userName,password);

                System.out.println("Connection success...");
                return conn;

    }

}
