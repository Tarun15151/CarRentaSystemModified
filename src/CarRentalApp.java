import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CarRentalApp extends JFrame {
    private CarDAO carDAO;
    private JTextArea outputArea;

    public CarRentalApp() {
        carDAO = new CarDAO();

        setTitle("Car Rental System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton btnViewCars = new JButton("View Available Cars");
        JButton btnRentCar = new JButton("Rent a Car");
        JButton btnReturnCar = new JButton("Return a Car");
        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);

        btnViewCars.addActionListener(e -> showAvailableCars());
        btnRentCar.addActionListener(e -> rentCar());
        btnReturnCar.addActionListener(e -> returnCar());

        panel.add(btnViewCars);
        panel.add(btnRentCar);
        panel.add(btnReturnCar);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);
    }

    private void showAvailableCars() {
        List<Car> cars = carDAO.getAllAvailableCars();
        outputArea.setText("");
        for (Car c : cars) {
            outputArea.append("ID: " + c.getId() + ", " + c.getBrand() + " " + c.getModel() + "\n");
        }
    }

    private void rentCar() {
        String idStr = JOptionPane.showInputDialog("Enter Car ID to Rent:");
        String name = JOptionPane.showInputDialog("Enter Customer Name:");
        int id = Integer.parseInt(idStr);
        if (carDAO.rentCar(id, name)) {
            JOptionPane.showMessageDialog(this, "Car rented successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to rent car.");
        }
    }

    private void returnCar() {
        String idStr = JOptionPane.showInputDialog("Enter Car ID to Return:");
        int id = Integer.parseInt(idStr);
        if (carDAO.returnCar(id)) {
            JOptionPane.showMessageDialog(this, "Car returned successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to return car.");
        }
    }

    public static void main(String[] args) {
        new CarRentalApp();
    }
}