import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/car_rental", "root", "Anjana15@");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}