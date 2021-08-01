package T18A_Group6_A2;

/*
this is transaction for items sold
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Transaction {

    private int tid;
    private Date date;
    private List<Snack> itemSold;
    private double price; //total price
    private double change;
    private String payMeth;

    public Transaction() {
        this.itemSold = new ArrayList<>();
        this.price = 0.0;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Snack> getItemSold() {
        return itemSold;
    }

    public void setItemSold(List<Snack> itemSold) {
        this.itemSold = itemSold;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price += price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getPayMeth() {
        return payMeth;
    }

    public void setPayMeth(String payMeth) {
        this.payMeth = payMeth;
    }

    //**********Write test cases for these functions******************
    public void calcTid(){
        try{
            File file = new File("Transaction.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    this.tid = Integer.parseInt(y[0]); //always the last one
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        this.tid += 1; //id is always unique
    }

    public void saveTransaction(){
        this.calcTid();
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("Transaction.csv"), true));
            for(Snack item : this.getItemSold()){
                writer.println("");
                writer.print(this.tid + "," + dateFormat.format(date) + "," + item.getName() + "," + this.price + "," + this.change + "," + this.payMeth);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
