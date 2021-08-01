package T18A_Group6_A2;

public class Card {

    private final String cardname;
    private final long cardnum;
    private double balance = 100.00;

    public Card(String cardname, long cardnum){
        this.cardname = cardname;
        this.cardnum = cardnum;
    }

    public String getCardname() {
        return cardname;
    }

    public long getCardnum() {
        return cardnum;
    }

    public double getBalance() {
        return balance;
    }

    public void pay(double amount){
        this.balance -= amount;
    }

}
