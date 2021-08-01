package T18A_Group6_A2;

/*
This class loads all the data from the csv files
NOTE: No need to do anything else here
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Database {

    private List<Snack> items;
    private List<Customer> customers;
    private List<Seller> sellers;
    private List<Cashier> cashiers;
    private List<Owner> owners;
    private List<Cash> cashes;
    private List<Card> cards;

    public Database(){
        this.items = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.sellers = new ArrayList<>();
        this.cashiers = new ArrayList<>();
        this.owners = new ArrayList<>();
        this.cashes = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    //read from the database and store it internally for the duration of the program
    public void load(){
        this.setItems();
        this.setCustomers();
        this.setSellers();
        this.setCashiers();
        this.setOwners();
        this.setCashes();
        this.setCards();
    }

    public void setItems() {

        try{
            File file = new File("Snacks.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    String type = y[0];
                    long code = Long.parseLong(y[1]);
                    String name = y[2];
                    double price = Double.parseDouble(y[3]);
                    int qty = Integer.parseInt(y[4]);
                    int qtySold = Integer.parseInt(y[5]);
                    Snack s = new Snack(type,code,name,price,qty,qtySold);
                    this.items.add(s);
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }

    public void setCustomers() {
        try{
            File file = new File("Customer.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    String username = y[0];
                    String password = y[1];
                    if(y[2].equals("false")){
                        Customer c = new Customer(username, password, false, null);
                        this.customers.add(c);
                    }else if(y[2].equals("true")){
                        String cardname = y[3];
                        long cardnum = Long.parseLong(y[4]);
                        Card card = new Card(cardname, cardnum);
                        Customer c = new Customer(username, password, true, card);
                        this.customers.add(c);
                    }
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setSellers() {
        try{
            File file = new File("Seller.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    Seller s = new Seller(y[0], y[1]);
                    this.sellers.add(s);
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setCashiers() {
        try{
            File file = new File("Cashier.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    Cashier c = new Cashier(y[0], y[1]);
                    this.cashiers.add(c);
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setOwners() {
        try{
            File file = new File("Owner.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    Owner o = new Owner(y[0], y[1]);
                    this.owners.add(o);
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setCashes() {
        try{
            File file = new File("Cash.csv");
            Scanner f = new Scanner(new File(String.valueOf(file)));
            int tick = 0;
            while (f.hasNext()){
                if(tick == 0){
                    f.nextLine();
                }
                if(tick > 0){ //Do not check labels
                    String x = f.nextLine();
                    String[] y = x.split(",");
                    double amount = Double.parseDouble(y[0]);
                    String type = y[1];
                    int qty = Integer.parseInt(y[2]);
                    Cash c = new Cash(amount,type,qty);
                    this.cashes.add(c);
                }
                tick++;
            }
            f.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void setCards() {
        try{
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new FileReader("credit_cards.json"));

            for(Object obj : array){
                JSONObject jsonObj = (JSONObject) obj;
                String cardname = (String) jsonObj.get("name");
                String c = (String) jsonObj.get("number");
                long cardnum = Long.parseLong(c);
                this.cards.add(new Card(cardname,cardnum));
            }
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public List<Snack> getItems() {
        return items;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public List<Cash> getCashes() {
        return cashes;
    }

    public List<Card> getCards() {
        return cards;
    }

    //write to the csv database
    //Call this function once all task or transaction in the vending machine is done
    public void update(){
        this.updateItems();
        this.updateCustomers();
        this.updateSellers();
        this.updateCashiers();
        this.updateOwners();
        this.updateCashes();
    }

    public void updateItems() {
        try {
            File file = new File("Snacks.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("type,code,name,price,qty,qtySold");
            for(Snack s : this.items){
                String type = s.getType();
                String code = Long.toString(s.getCode());
                String name = s.getName();
                String price = Double.toString(s.getPrice());
                String qty = Integer.toString(s.getQty());
                String qtySold = Integer.toString(s.getQtySold());
                writer.println(type+","+code+","+name+","+price+","+qty+","+qtySold);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomers() {
        try {
            File file = new File("Customer.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("username,password,savedPayment,cardName,cardNum");
            for(Customer c : this.customers){
                String username = c.getUsername();
                String password = c.getPassword();
                String savedPay = Boolean.toString(c.isSavedPay());
                if(!c.isSavedPay()){
                    writer.println(username+","+password+","+savedPay+",null,null");
                }else{
                    String cardname = c.getCard().getCardname();
                    String cardnum = Long.toString(c.getCard().getCardnum());
                    writer.println(username+","+password+","+savedPay+","+cardname+","+cardnum);
                }
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateSellers() {
        try {
            File file = new File("Seller.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("username,password");
            for(Seller c : this.sellers){
                String username = c.getUsername();
                String password = c.getPassword();
                writer.println(username+","+password);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateCashiers() {
        try {
            File file = new File("Cashier.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("username,password");
            for(Cashier c : this.cashiers){
                String username = c.getUsername();
                String password = c.getPassword();
                writer.println(username+","+password);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateOwners() {
        try {
            File file = new File("Owner.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("username,password");
            for(Owner c : this.owners){
                String username = c.getUsername();
                String password = c.getPassword();
                writer.println(username+","+password);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateCashes() {
        try {
            File file = new File("Cash.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("amount,type,qty");
            for(Cash c : this.cashes){
                String amount = Double.toString(c.getAmount());
                String type = c.getType();
                String qty = Integer.toString(c.getQty());
                writer.println(amount+","+type+","+qty);
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
