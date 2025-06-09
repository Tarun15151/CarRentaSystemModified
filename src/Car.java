public class Car {
    private int id;
    private String brand;
    private String model;
    private boolean available;

    public Car(int id, String brand, String model, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return available;
    }
}