package T18A_Group6_A2;

public class Snack {

    private String type;
    private long code;
    private String name;
    private double price;
    private int qty;
    private int qtySold;

    public Snack(String type, long code, String name, double price, int qty, int qtySold){
        this.type = type;
        this.code = code;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.qtySold = qtySold;
    }

    public String getType() {
        return type;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public int getQtySold() {
        return qtySold;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setQtySold(int qtySold) {
        this.qty -= qtySold;
        this.qtySold += qtySold;
    }
}
