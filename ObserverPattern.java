import java.util.*;

public class ObserverPattern {
    public static void main(String[] args) {
        Stock google = new Stock("GOOG", 1200.0);

        google.addInvestor(new MobileAppInvestor("Alice"));
        google.addInvestor(new MobileAppInvestor("Bob"));

        google.setPrice(1250.5);
        google.setPrice(1300.0);
    }
}

// Observer
interface Investor {
    void update(String stock, double price);
}

// Concrete Observer
class MobileAppInvestor implements Investor {
    private String name;

    public MobileAppInvestor(String name) {
        this.name = name;
    }

    public void update(String stock, double price) {
        System.out.println(name + " notified: " + stock + " price updated to $" + price);
    }
}

// Subject
class Stock {
    private String name;
    private double price;
    private List<Investor> investors = new ArrayList<>();

    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void addInvestor(Investor inv) {
        investors.add(inv);
    }

    public void setPrice(double price) {
        this.price = price;
        notifyAllInvestors();
    }

    private void notifyAllInvestors() {
        for (Investor inv : investors) {
            inv.update(name, price);
        }
    }
}
