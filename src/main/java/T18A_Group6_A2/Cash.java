package T18A_Group6_A2;

public class Cash {

    private final double amount;
    private final String type;
    private int qty;

    public Cash(double amount, String type, int qty){
        this.amount = amount;
        this.type = type;
        this.qty = qty;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
