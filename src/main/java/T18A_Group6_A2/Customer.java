package T18A_Group6_A2;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer {

    private final String username;
    private final String password;
    private boolean savedPay;
    private Card card;


    public Customer(String username, String password, boolean savedPay, Card card){
        this.username = username;
        this.password = password;
        this.savedPay = savedPay;
        this.card = card;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSavedPay() {
        return savedPay;
    }

    public Card getCard() {
        return card;
    }

    public void setSavedPay() {
        this.savedPay = true;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<String> getLastFiveSnacks(){
        List<String> snacks = new ArrayList<>();
        try{
            File file = new File("CustHist.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    if(y[0].equals(this.username)){
                        snacks.add(y[1]); //currently all snacks
                    }
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        if(snacks.size() == 0){
            return null;
        }

        //filter last 5 only
        int limit = 5;
        if(snacks.size() <= limit){
            return snacks;
        }else{
            List<String> filterSnacks = new ArrayList<>();
            for(int i = snacks.size() - 1; i >= snacks.size() - limit; i--){
                filterSnacks.add(snacks.get(i));
            }
            return filterSnacks;
        }

    }

}
