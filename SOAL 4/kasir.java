// Interface untuk produk yang dikenakan pajak
interface Taxable {
    double calculateTax();
}

// Abstract class Product sebagai base class
abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;
    
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Abstract method yang harus diimplementasi oleh subclass
    public abstract double calculateSubtotal();
    
    // Getter methods
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    
    // Method untuk menampilkan info produk
    public String getProductInfo() {
        return String.format("%s - Rp%.2f x %d", name, price, quantity);
    }
}

// Subclass Food yang extends Product dan implements Taxable
class Food extends Product implements Taxable {
    private static final double TAX_RATE = 0.10; // 10% pajak untuk makanan
    
    public Food(String name, double price, int quantity) {
        super(name, price, quantity);
    }
    
    @Override
    public double calculateSubtotal() {
        return price * quantity;
    }
    
    @Override
    public double calculateTax() {
        return calculateSubtotal() * TAX_RATE;
    }
    
    public double getTotalWithTax() {
        return calculateSubtotal() + calculateTax();
    }
}

// Subclass Beverage yang extends Product dan implements Taxable
class Beverage extends Product implements Taxable {
    private static final double TAX_RATE = 0.05; // 5% pajak untuk minuman
    private boolean isAlcoholic;
    
    public Beverage(String name, double price, int quantity, boolean isAlcoholic) {
        super(name, price, quantity);
        this.isAlcoholic = isAlcoholic;
    }
    
    @Override
    public double calculateSubtotal() {
        return price * quantity;
    }
    
    @Override
    public double calculateTax() {
        double baseTax = calculateSubtotal() * TAX_RATE;
        // Tambahan pajak 15% untuk minuman beralkohol
        if (isAlcoholic) {
            baseTax += calculateSubtotal() * 0.15;
        }
        return baseTax;
    }
    
    public double getTotalWithTax() {
        return calculateSubtotal() + calculateTax();
    }
    
    public boolean isAlcoholic() { return isAlcoholic; }
}

// Class untuk item kasir
class CashierItem {
    private Product product;
    
    public CashierItem(Product product) {
        this.product = product;
    }
    
    public Product getProduct() { return product; }
    
    public double getItemTotal() {
        if (product instanceof Taxable) {
            Taxable taxableProduct = (Taxable) product;
            return product.calculateSubtotal() + taxableProduct.calculateTax();
        }
        return product.calculateSubtotal();
    }
}

// Class utama sistem kasir
class CashierSystem {
    private java.util.List<CashierItem> items;
    
    public CashierSystem() {
        items = new java.util.ArrayList<>();
    }
    
    // Menambah produk ke keranjang belanja
    public void addProduct(Product product) {
        items.add(new CashierItem(product));
        System.out.println("Ditambahkan: " + product.getProductInfo());
    }
    
    // Menghitung total belanja menggunakan polymorphism
    public double calculateTotal() {
        double total = 0.0;
        for (CashierItem item : items) {
            total += item.getItemTotal();
        }
        return total;
    }
    
    // Menghitung total pajak
    public double calculateTotalTax() {
        double totalTax = 0.0;
        for (CashierItem item : items) {
            Product product = item.getProduct();
            if (product instanceof Taxable) {
                Taxable taxableProduct = (Taxable) product;
                totalTax += taxableProduct.calculateTax();
            }
        }
        return totalTax;
    }
    
    // Menghitung subtotal (sebelum pajak)
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (CashierItem item : items) {
            subtotal += item.getProduct().calculateSubtotal();
        }
        return subtotal;
    }
    
    // Menampilkan struk belanja
    public void printReceipt() {
        System.out.println("\n=== STRUK BELANJA ===");
        System.out.println("---------------------");
        
        for (CashierItem item : items) {
            Product product = item.getProduct();
            System.out.printf("%-20s Rp%8.2f\n", 
                product.getProductInfo(), 
                product.calculateSubtotal());
            
            if (product instanceof Taxable) {
                Taxable taxableProduct = (Taxable) product;
                System.out.printf("  Pajak:              Rp%8.2f\n", 
                    taxableProduct.calculateTax());
            }
        }
        
        System.out.println("---------------------");
        System.out.printf("Subtotal:            Rp%8.2f\n", calculateSubtotal());
        System.out.printf("Total Pajak:         Rp%8.2f\n", calculateTotalTax());
        System.out.printf("TOTAL BAYAR:         Rp%8.2f\n", calculateTotal());
        System.out.println("=====================");
    }
    
    // Method untuk mendapatkan jumlah item
    public int getItemCount() {
        return items.size();
    }
    
    // Method untuk clear keranjang
    public void clearCart() {
        items.clear();
        System.out.println("Keranjang belanja dikosongkan.");
    }
}

// Main class untuk demonstrasi
public class kasir {
    public static void main(String[] args) {
        // Membuat instance sistem kasir
        CashierSystem cashier = new CashierSystem();
        
        System.out.println("=== SISTEM KASIR SEDERHANA ===\n");
        
        // Menambahkan berbagai produk
        System.out.println("Menambahkan produk ke keranjang:");
        
        // Produk makanan
        Food nasiGoreng = new Food("Nasi Goreng", 25000, 2);
        Food ayamBakar = new Food("Ayam Bakar", 35000, 1);
        
        // Produk minuman
        Beverage tehManis = new Beverage("Teh Manis", 5000, 3, false);
        Beverage beer = new Beverage("Beer", 45000, 2, true);
        Beverage jus = new Beverage("Jus Jeruk", 15000, 1, false);
        
        // Menambahkan ke sistem kasir
        cashier.addProduct(nasiGoreng);
        cashier.addProduct(ayamBakar);
        cashier.addProduct(tehManis);
        cashier.addProduct(beer);
        cashier.addProduct(jus);
        
        // Menampilkan struk
        cashier.printReceipt();
        
        // Demonstrasi polymorphism
        System.out.println("\n=== DEMONSTRASI POLYMORPHISM ===");
        System.out.println("Total item: " + cashier.getItemCount());
        
        // Polymorphism: semua produk diperlakukan sebagai Product
        // tapi behavior calculateSubtotal() berbeda untuk setiap subclass
        System.out.println("\nDetail perhitungan per produk:");
        System.out.println("1. Nasi Goreng: Rp" + nasiGoreng.calculateSubtotal() + 
                           " + Pajak: Rp" + nasiGoreng.calculateTax() + 
                           " = Rp" + nasiGoreng.getTotalWithTax());
        
        System.out.println("2. Ayam Bakar: Rp" + ayamBakar.calculateSubtotal() + 
                           " + Pajak: Rp" + ayamBakar.calculateTax() + 
                           " = Rp" + ayamBakar.getTotalWithTax());
        
        System.out.println("3. Teh Manis: Rp" + tehManis.calculateSubtotal() + 
                           " + Pajak: Rp" + tehManis.calculateTax() + 
                           " = Rp" + tehManis.getTotalWithTax());
        
        System.out.println("4. Beer (Alkohol): Rp" + beer.calculateSubtotal() + 
                           " + Pajak: Rp" + beer.calculateTax() + 
                           " = Rp" + beer.getTotalWithTax());
        
        System.out.println("5. Jus Jeruk: Rp" + jus.calculateSubtotal() + 
                           " + Pajak: Rp" + jus.calculateTax() + 
                           " = Rp" + jus.getTotalWithTax());
    }
}