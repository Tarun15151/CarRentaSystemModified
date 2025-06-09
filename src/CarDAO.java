import java.sql.*;
import java.util.*;

public class CarDAO {
    private Connection conn;

    public CarDAO() {
        conn = DBConnection.getConnection();
    }

    public List<Car> getAllAvailableCars() {
        List<Car> cars = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM cars WHERE available = TRUE");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cars.add(new Car(rs.getInt("id"), rs.getString("brand"), rs.getString("model"), rs.getBoolean("available")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public boolean rentCar(int carId, String customerName) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO rentals (car_id, customer_name, rent_date) VALUES (?, ?, CURDATE())");
            stmt.setInt(1, carId);
            stmt.setString(2, customerName);
            stmt.executeUpdate();

            PreparedStatement updateCar = conn.prepareStatement("UPDATE cars SET available = FALSE WHERE id = ?");
            updateCar.setInt(1, carId);
            updateCar.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnCar(int carId) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE cars SET available = TRUE WHERE id = ?");
            stmt.setInt(1, carId);
            stmt.executeUpdate();

            PreparedStatement updateRental = conn.prepareStatement("UPDATE rentals SET return_date = CURDATE() WHERE car_id = ? AND return_date IS NULL");
            updateRental.setInt(1, carId);
            updateRental.executeUpdate();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}