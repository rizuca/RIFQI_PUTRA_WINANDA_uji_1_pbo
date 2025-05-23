interface Electric {
    void chargeBattery();
}

// Abstract class Vehicle sebagai parent class
// Abstract class tidak dapat diinstansiasi langsung dan bisa memiliki method abstract
abstract class Vehicle {
    // Protected agar bisa diakses oleh subclass
    protected String brand;
    protected String model;
    protected int year;
    
    // Constructor untuk inisialisasi properties dasar
    public Vehicle(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    
    // Method abstract yang harus diimplementasikan oleh subclass
    // Menerapkan konsep abstraction
    public abstract void startEngine();
    
    // Method konkret yang bisa digunakan langsung oleh subclass
    public void displayInfo() {
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
    }
    
    // Getter methods
    public String getBrand() {
        return brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public int getYear() {
        return year;
    }
}

// Class Car yang mewarisi Vehicle dan mengimplementasikan interface Electric
// Menerapkan konsep inheritance dan interface implementation
class Car extends Vehicle implements Electric {
    private int numberOfDoors;
    private boolean isElectric;
    private int batteryLevel;
    
    // Constructor dengan parameter tambahan untuk Car
    public Car(String brand, String model, int year, int numberOfDoors, boolean isElectric) {
        // Memanggil constructor parent class
        super(brand, model, year);
        this.numberOfDoors = numberOfDoors;
        this.isElectric = isElectric;
        this.batteryLevel = isElectric ? 50 : 0; // Default battery level untuk mobil listrik
    }
    
    // Override method abstract dari parent class
    // Implementasi spesifik untuk Car
    @Override
    public void startEngine() {
        if (isElectric) {
            if (batteryLevel > 10) {
                System.out.println("🔋 " + brand + " " + model + " electric engine started silently!");
                System.out.println("Battery level: " + batteryLevel + "%");
                batteryLevel -= 5; // Konsumsi baterai saat start
            } else {
                System.out.println("❌ Cannot start engine. Battery level too low: " + batteryLevel + "%");
                System.out.println("Please charge the battery first!");
            }
        } else {
            System.out.println("🚗 " + brand + " " + model + " gasoline engine started with a roar!");
        }
    }
    
    // Implementasi method dari interface Electric
    @Override
    public void chargeBattery() {
        if (isElectric) {
            System.out.println("🔌 Charging " + brand + " " + model + " battery...");
            batteryLevel = 100;
            System.out.println("✅ Battery fully charged! Current level: " + batteryLevel + "%");
        } else {
            System.out.println("❌ This car doesn't have an electric battery to charge.");
        }
    }
    
    // Method tambahan khusus untuk Car
    public void openTrunk() {
        System.out.println("🚗 " + brand + " " + model + " trunk opened!");
    }
    
    // Override displayInfo untuk menambahkan informasi spesifik Car
    @Override
    public void displayInfo() {
        super.displayInfo(); // Memanggil method parent
        System.out.println("Number of doors: " + numberOfDoors);
        System.out.println("Electric: " + (isElectric ? "Yes" : "No"));
        if (isElectric) {
            System.out.println("Battery level: " + batteryLevel + "%");
        }
    }
    
    // Getter methods
    public int getNumberOfDoors() {
        return numberOfDoors;
    }
    
    public boolean isElectric() {
        return isElectric;
    }
    
    public int getBatteryLevel() {
        return batteryLevel;
    }
}

// Class Motorcycle yang mewarisi Vehicle
// Menerapkan konsep inheritance
class Motorcycle extends Vehicle {
    private int engineCapacity; // dalam CC
    private boolean hasSidecar;
    
    // Constructor untuk Motorcycle
    public Motorcycle(String brand, String model, int year, int engineCapacity, boolean hasSidecar) {
        super(brand, model, year);
        this.engineCapacity = engineCapacity;
        this.hasSidecar = hasSidecar;
    }
    
    // Override method abstract dari parent class
    // Implementasi spesifik untuk Motorcycle
    @Override
    public void startEngine() {
        System.out.println("🏍️ " + brand + " " + model + " motorcycle engine started!");
        System.out.println("Engine roaring with " + engineCapacity + "CC power!");
        
        if (hasSidecar) {
            System.out.println("Ready to ride with sidecar attached!");
        }
    }
    
    // Method tambahan khusus untuk Motorcycle
    public void doWheelie() {
        if (!hasSidecar) {
            System.out.println("🏍️ " + brand + " " + model + " performing a wheelie!");
        } else {
            System.out.println("❌ Cannot perform wheelie with sidecar attached!");
        }
    }
    
    // Override displayInfo untuk menambahkan informasi spesifik Motorcycle
    @Override
    public void displayInfo() {
        super.displayInfo(); // Memanggil method parent
        System.out.println("Engine capacity: " + engineCapacity + "CC");
        System.out.println("Has sidecar: " + (hasSidecar ? "Yes" : "No"));
    }
    
    // Getter methods
    public int getEngineCapacity() {
        return engineCapacity;
    }
    
    public boolean hasSidecar() {
        return hasSidecar;
    }
}

// Main class untuk mendemonstrasikan penggunaan
public class MainApp {
    public static void main(String[] args) {
        System.out.println("=== VEHICLE MANAGEMENT SYSTEM ===\n");
        
        // Membuat array Vehicle untuk mendemonstrasikan polymorphism
        // Polymorphism: satu interface, banyak implementasi
        Vehicle[] vehicles = {
            new Car("Tesla", "Model 3", 2023, 4, true),           // Mobil listrik
            new Car("Toyota", "Camry", 2022, 4, false),          // Mobil bensin
            new Motorcycle("Harley-Davidson", "Street 750", 2023, 750, false),
            new Motorcycle("Honda", "Gold Wing", 2022, 1833, true)
        };
        
        // Demonstrasi polymorphism
        System.out.println("🔄 DEMONSTRASI POLYMORPHISM:");
        System.out.println("Memanggil method startEngine() untuk semua vehicle:\n");
        
        for (int i = 0; i < vehicles.length; i++) {
            System.out.println("--- Vehicle " + (i + 1) + " ---");
            vehicles[i].displayInfo();
            System.out.println();
            
            // Polymorphism: method yang sama dipanggil, tapi implementasi berbeda
            vehicles[i].startEngine();
            System.out.println();
        }
        
        // Demonstrasi interface implementation
        System.out.println("\n🔋 DEMONSTRASI INTERFACE ELECTRIC:");
        
        // Menggunakan instanceof untuk type checking
        for (Vehicle vehicle : vehicles) {
            if (vehicle instanceof Electric) {
                System.out.println("\n--- Electric Vehicle Found ---");
                vehicle.displayInfo();
                
                // Casting untuk mengakses method dari interface
                Electric electricVehicle = (Electric) vehicle;
                electricVehicle.chargeBattery();
                
                // Start engine setelah charge
                System.out.println("\nStarting engine after charging:");
                vehicle.startEngine();
            }
        }
        
        // Demonstrasi method khusus setiap class
        System.out.println("\n🎯 DEMONSTRASI METHOD KHUSUS:");
        
        // Akses method khusus Car
        Car tesla = (Car) vehicles[0];
        System.out.println("\n--- Tesla specific methods ---");
        tesla.openTrunk();
        
        // Akses method khusus Motorcycle
        Motorcycle harley = (Motorcycle) vehicles[2];
        System.out.println("\n--- Harley specific methods ---");
        harley.doWheelie();
        
        Motorcycle honda = (Motorcycle) vehicles[3];
        System.out.println("\n--- Honda with sidecar ---");
        honda.doWheelie(); // Akan gagal karena ada sidecar
        
        // Demonstrasi penggunaan Electric interface secara langsung
        System.out.println("\n⚡ DEMONSTRASI PENGGUNAAN INTERFACE LANGSUNG:");
        
        // Membuat reference Electric untuk Car
        Electric electricCar = new Car("BMW", "i4", 2023, 4, true);
        
        System.out.println("Charging electric car:");
        electricCar.chargeBattery();
        
        // Untuk akses method Vehicle, perlu casting
        if (electricCar instanceof Vehicle) {
            Vehicle bmw = (Vehicle) electricCar;
            System.out.println("\nStarting BMW after charge:");
            bmw.startEngine();
        }
        
        System.out.println("\n🎉 Program selesai dieksekusi!");
        System.out.println("\nKonsep OOP yang telah didemonstrasikan:");
        System.out.println("✅ Abstraction (abstract class Vehicle)");
        System.out.println("✅ Inheritance (Car & Motorcycle extends Vehicle)");
        System.out.println("✅ Interface Implementation (Car implements Electric)");
        System.out.println("✅ Polymorphism (method startEngine() berbeda implementasi)");
        System.out.println("✅ Encapsulation (private fields dengan getter methods)");
    }
}