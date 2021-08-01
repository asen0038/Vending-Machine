package T18A_Group6_A2;

/*
This class runs and handles logic for user input and output
 */

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendingMachine {

    private Database db;

    public VendingMachine(Database db){
        this.db = db;
    }

    public void start(){

        System.out.println("WELCOME TO THE VENDING MACHINE");
        System.out.println("==============================");
        System.out.println();

        while(true){

            System.out.println("Enter the number for the action you would like to do: (ie: 1)");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Continue as anonymous");
            System.out.println("4. Exit");
            System.out.println();

            Scanner in = new Scanner(System.in);
            int cmd = in.nextInt();

            if(cmd == 1){ //Login and actions
                while(true){
                    System.out.println("Select your user type you want to login as from the following list: (ie: 1)");
                    System.out.println("1. Customer");
                    System.out.println("2. Seller");
                    System.out.println("3. Cashier");
                    System.out.println("4. Owner");
                    System.out.println("5. Exit");
                    System.out.println();
                    Scanner f = new Scanner(System.in);
                    int prompt = f.nextInt();
                    if(prompt == 1){ //Customer Login
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkCustomerUserName(username)){
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            if(this.checkCustomerPassword(username, password)){
                                System.out.println();
                                System.out.println("Welcome " + username);
                                System.out.println();
                                Transaction trans = new Transaction();
                                Date date = new Date();
                                trans.setDate(date);

                                while (true){
                                    this.customerDefaultDisplay(username);
                                    System.out.println();
                                    System.out.println("Please select the type of snack you want from the following list: (ie: 1)");
                                    this.displaySnackType();
                                    System.out.println("Press 'C' to go back anytime and cancel the transaction");
                                    Scanner num = new Scanner(System.in);
                                    String back = num.nextLine();
                                    if(back.equals("C") || back.equals("c")){
                                        this.writeCancelReason(username, "user cancelled", new Date());
                                        System.exit(0);
                                    }
                                    int type = Integer.parseInt(back);
                                    System.out.println();
                                    System.out.println("Here are the items available from this category with their price and qty:-");
                                    while(true){
                                        this.displaySnacksItems(type);
                                        System.out.println("Press 'B' to go back anytime to buy another snack type or cancel transaction");
                                        Scanner cancel = new Scanner(System.in);
                                        System.out.println();
                                        System.out.println("Enter the 4 digit snack code you want to purchase: (ie: 1000)");
                                        String can = cancel.nextLine();
                                        if(can.equals("B") || can.equals("b")){
                                            break;
                                        }
                                        long code = Long.parseLong(can);
                                        System.out.println();
                                        System.out.println("Enter the quantity you want for this snack: (ie: 3)");
                                        int qty = Integer.parseInt(cancel.nextLine());
                                        this.handleShoppingCart(code, qty, trans);
                                        System.out.println();
                                        System.out.println("Item added to your purchase list!");
                                        System.out.print("Continue Shopping? (Y|N): ");
                                        System.out.println();
                                        String ans = cancel.nextLine();
                                        if(ans.equals("N") || ans.equals("n")){
                                            while (true){
                                                System.out.println("Your total amount due is $" + trans.getPrice());
                                                System.out.println("How would you like to pay?");
                                                System.out.println("1. cash");
                                                System.out.println("2. credit card");
                                                System.out.println("3. saved payment type");
                                                System.out.print("Enter your selection: (ie: 1)");
                                                String pay = cancel.nextLine();
                                                if(pay.equals("B") || pay.equals("b")){
                                                    break;
                                                }else if(pay.equals("1")){//Assumed no errors input
                                                    List<Double> inserted = new ArrayList<>();
                                                    this.displayCash();
                                                    double cash = 0;
                                                    while (cash < trans.getPrice()){
                                                        double insert = Double.parseDouble(cancel.nextLine());
                                                        inserted.add(insert);
                                                        cash += insert;
                                                    }
                                                    if(!this.calculateChange(cash, trans)){
                                                        this.writeCancelReason(username, "no change available", new Date());
                                                        System.exit(0);
                                                    }
                                                    this.updateCashDB(inserted);
                                                    this.writeCustHist(username, trans);
                                                    trans.setPayMeth("cash");
                                                    trans.saveTransaction();
                                                    this.db.update();
                                                    System.exit(0);
                                                }else if(pay.equals("2")){
                                                    while(true){
                                                        System.out.println();
                                                        System.out.println("Please enter your card name: ");
                                                        String cardname = cancel.nextLine();
                                                        System.out.println();
                                                        System.out.println("Please enter your card number: ");
                                                        String cnum = cancel.nextLine();
                                                        System.out.println();
                                                        long cardnum = Long.parseLong(cnum);
                                                        if(cardPay(cardname, cardnum, trans.getPrice())){
                                                            trans.setChange(0);
                                                            trans.setPayMeth("card");
                                                            System.out.println("Would you like to save your card details for your next purchase? (Y|N)");
                                                            String a = cancel.nextLine();
                                                            System.out.println();
                                                            if(a.equals("Y") || a.equals("y")){
                                                                this.setCustomerCard(username, cardname, cardnum);
                                                                System.out.println("Your card details has been saved. Have a nice day!");
                                                                System.out.println();
                                                            }
                                                            this.writeCustHist(username, trans);
                                                            trans.saveTransaction();
                                                            this.db.update();
                                                            System.exit(0);
                                                        }else{
                                                            break;
                                                        }
                                                    }
                                                }else if(pay.equals("3")){

                                                    if(checkCustCardPay(username, trans.getPrice())){
                                                        trans.setChange(0);
                                                        trans.setPayMeth("card");
                                                        this.writeCustHist(username, trans);
                                                        trans.saveTransaction();
                                                        this.db.update();
                                                        System.exit(0);
                                                    }else{
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }

                            }else{
                                System.out.println();
                                System.out.println("Incorrect password. Please try again");
                                System.out.println();
                            }
                        }else{
                            System.out.println();
                            System.out.println("Username does not exists. Please try again");
                            System.out.println();
                        }

                    }else if(prompt == 2){ //Seller
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkSellerUserName(username)){
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            if(this.checkSellerPassword(username, password)){
                                System.out.println();
                                System.out.println("Welcome " + username);
                                System.out.println();

                                while (true){
                                    System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                    System.out.println("1. Get product report");
                                    System.out.println("2. Update item details");
                                    System.out.println("3. Exit");
                                    System.out.println();
                                    Scanner i = new Scanner(System.in);
                                    int p = Integer.parseInt(i.nextLine());
                                    if(p == 1){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Snacks.csv");
                                        System.out.println();
                                    }else if(p == 2){
                                        System.out.println("Which snack would you like to update? Enter item code:");
                                        this.displaySnackDetails();
                                        System.out.println();
                                        long x = Long.parseLong(i.nextLine());
                                        System.out.println("Which attribute would you like to update? Enter number:");
                                        System.out.println("1. Item price");
                                        System.out.println("2. Item quantity");
                                        int y = Integer.parseInt(i.nextLine());
                                        if(y == 1){
                                            System.out.println();
                                            System.out.println("Enter price for " + x + ":");
                                            double price = Double.parseDouble(i.nextLine());
                                            this.updateSnacksPrice(x, price);
                                        }else if( y == 2){
                                            System.out.println();
                                            System.out.println("Enter qty for " + x + ":");
                                            int qty = Integer.parseInt(i.nextLine());
                                            this.updateSnacksQty(x, qty);
                                        }
                                        this.db.update();
                                    }else if(p == 3){
                                        this.writeCancelReason(username, "user cancelled", new Date());
                                        break;
                                    }else{
                                        System.out.println("Invalid prompt, try again");
                                        System.out.println();
                                    }
                                }

                            }else{
                                System.out.println();
                                System.out.println("Incorrect password. Please try again");
                                System.out.println();
                            }
                        }else{
                            System.out.println();
                            System.out.println("Username does not exists. Please try again");
                            System.out.println();
                        }

                    }else if(prompt == 3){ //Cashier
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkCashierUserName(username)){
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            if(this.checkCashierPassword(username, password)){
                                System.out.println();
                                System.out.println("Welcome " + username);
                                System.out.println();

                                while (true){
                                    System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                    System.out.println("1. Get transaction report");
                                    System.out.println("2. Get cash details report");
                                    System.out.println("3. Update cash details");
                                    System.out.println("4. Exit");
                                    System.out.println();
                                    Scanner i = new Scanner(System.in);
                                    int p = Integer.parseInt(i.nextLine());
                                    if(p == 1){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Transaction.csv");
                                        System.out.println();
                                    }else if(p == 2){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Cash.csv");
                                        System.out.println();
                                    }else if(p == 3){
                                        System.out.println("Enter the number associated with the note/coin you want to update: ");
                                        this.displayCashierMoney();
                                        int cash = Integer.parseInt(i.nextLine());
                                        System.out.println("Enter the qty for the cash type you want to set: ");
                                        int qty = Integer.parseInt(i.nextLine());
                                        this.updateCashQty(cash, qty);
                                        this.db.update();
                                    }else if(p == 4){
                                        this.writeCancelReason(username, "user cancelled", new Date());
                                        break;
                                    }else{
                                        System.out.println("Invalid prompt, try again");
                                        System.out.println();
                                    }
                                }

                            }else{
                                System.out.println();
                                System.out.println("Incorrect password. Please try again");
                                System.out.println();
                            }
                        }else{
                            System.out.println();
                            System.out.println("Username does not exists. Please try again");
                            System.out.println();
                        }

                    }else if(prompt == 4){ //Owner
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkOwnerUserName(username)){
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            if(this.checkOwnerPassword(username, password)){
                                System.out.println();
                                System.out.println("Welcome " + username);
                                System.out.println();

                                while (true){
                                    System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                    System.out.println("1. Get product report");
                                    System.out.println("2. Update item details");
                                    System.out.println("3. Get transaction report");
                                    System.out.println("4. Get cash details report");
                                    System.out.println("5. Update cash details");
                                    System.out.println("6. Get all system users report");
                                    System.out.println("7. Get cancelled transaction report");
                                    System.out.println("8. Update system users");
                                    System.out.println("9. Exit");
                                    System.out.println();
                                    Scanner i = new Scanner(System.in);
                                    int p = Integer.parseInt(i.nextLine());
                                    if(p == 1){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Snacks.csv");
                                        System.out.println();

                                    }else if(p == 2){
                                        System.out.println("Which snack would you like to update? Enter item code:");
                                        this.displaySnackDetails();
                                        System.out.println();
                                        long x = Long.parseLong(i.nextLine());
                                        System.out.println("Which attribute would you like to update? Enter number:");
                                        System.out.println("1. Item price");
                                        System.out.println("2. Item quantity");
                                        int y = Integer.parseInt(i.nextLine());
                                        if(y == 1){
                                            System.out.println();
                                            System.out.println("Enter price for " + x + ":");
                                            double price = Double.parseDouble(i.nextLine());
                                            this.updateSnacksPrice(x, price);
                                        }else if( y == 2){
                                            System.out.println();
                                            System.out.println("Enter qty for " + x + ":");
                                            int qty = Integer.parseInt(i.nextLine());
                                            this.updateSnacksQty(x, qty);
                                        }
                                        this.db.update();

                                    }else if(p == 3){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Transaction.csv");
                                        System.out.println();

                                    }else if(p == 4){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Cash.csv");
                                        System.out.println();

                                    }else if(p == 5){
                                        System.out.println("Enter the number associated with the note/coin you want to update: ");
                                        this.displayCashierMoney();
                                        int cash = Integer.parseInt(i.nextLine());
                                        System.out.println("Enter the qty for the cash type you want to set: ");
                                        int qty = Integer.parseInt(i.nextLine());
                                        this.updateCashQty(cash, qty);
                                        this.db.update();

                                    }else if(p == 6){
                                        System.out.println("Your reports are ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Customer.csv to view customers");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Seller.csv to view sellers");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Cashier.csv to view cashiers");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Owner.csv to view owners");
                                        System.out.println();

                                    }else if(p == 7){
                                        System.out.println("Your report is ready");
                                        System.out.println("Please navigate to T18A_Group6_A2 folder and open Cancel.csv");
                                        System.out.println();

                                    }else if(p == 8){
                                        System.out.println("Which user would you like to update? Enter number:");
                                        System.out.println("1. Customer");
                                        System.out.println("2. Seller");
                                        System.out.println("3. Cashier");
                                        System.out.println("4. Owner");
                                        System.out.println();
                                        int x = Integer.parseInt(i.nextLine());
                                        System.out.println("What would you like to do? Enter number:");
                                        System.out.println("1. Add user");
                                        System.out.println("2. Remove User");
                                        int y = Integer.parseInt(i.nextLine());
                                        if(y == 1){
                                            System.out.println("Enter username of user you want to add:");
                                            String name = i.nextLine();
                                            this.addUser(x, name);
                                        }else if(y == 2){
                                            System.out.println("Enter username of user you want to remove:");
                                            String name = i.nextLine();
                                            this.removeUser(x, name);
                                        }
                                        this.db.update();
                                    }else if(p == 9){
                                        this.writeCancelReason(username, "user cancelled", new Date());
                                        break;
                                    }else{
                                        System.out.println("Invalid prompt, try again");
                                        System.out.println();
                                    }
                                }

                            }else{
                                System.out.println();
                                System.out.println("Incorrect password. Please try again");
                                System.out.println();
                            }
                        }else{
                            System.out.println();
                            System.out.println("Username does not exists. Please try again");
                            System.out.println();
                        }

                    }else if(prompt == 5){ //Exit
                        break;
                    }else{
                        System.out.println("Please provide valid number prompt");
                    }
                }

            }else if(cmd == 2){ //Create Account and actions

                while(true){
                    System.out.println("Select your user type you want to create account as from the following list: (ie: 1)");
                    System.out.println("1. Customer");
                    System.out.println("2. Seller");
                    System.out.println("3. Cashier");
                    System.out.println("4. Owner");
                    System.out.println("5. Exit");
                    System.out.println();
                    Scanner f = new Scanner(System.in);
                    int prompt = f.nextInt();

                    if(prompt == 1){ //Customer Create account
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkCustomerNameAlreadyExists(username)){
                            System.out.println("Username already exists. Please try again");
                        }else{
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            Customer customer = new Customer(username, password, false, null);
                            this.db.getCustomers().add(customer);
                            this.db.update();
                            System.out.println();
                            System.out.println("Account created. Welcome " + username);
                            System.out.println();

                            Transaction trans = new Transaction();
                            Date date = new Date();
                            trans.setDate(date);

                            while (true){
                                this.customerDefaultDisplay(username);
                                System.out.println();
                                System.out.println("Please select the type of snack you want from the following list: (ie: 1)");
                                this.displaySnackType();
                                System.out.println("Press 'C' to go back anytime and cancel the transaction");
                                Scanner num = new Scanner(System.in);
                                String back = num.nextLine();
                                if(back.equals("C") || back.equals("c")){
                                    this.writeCancelReason(username, "user cancelled", new Date());
                                    System.exit(0);
                                }
                                int type = Integer.parseInt(back);
                                System.out.println();
                                System.out.println("Here are the items available from this category with their price and qty:-");
                                while(true){
                                    this.displaySnacksItems(type);
                                    System.out.println("Press 'B' to go back anytime to buy another snack type or cancel transaction");
                                    Scanner cancel = new Scanner(System.in);
                                    System.out.println();
                                    System.out.println("Enter the 4 digit snack code you want to purchase: (ie: 1000)");
                                    String can = cancel.nextLine();
                                    if(can.equals("B") || can.equals("b")){
                                        break;
                                    }
                                    long code = Long.parseLong(can);
                                    System.out.println();
                                    System.out.println("Enter the quantity you want for this snack: (ie: 3)");
                                    int qty = Integer.parseInt(cancel.nextLine());
                                    this.handleShoppingCart(code, qty, trans);
                                    System.out.println();
                                    System.out.println("Item added to your purchase list!");
                                    System.out.print("Continue Shopping? (Y|N): ");
                                    System.out.println();
                                    String ans = cancel.nextLine();
                                    if(ans.equals("N") || ans.equals("n")){
                                        while (true){
                                            System.out.println("Your total amount due is $" + trans.getPrice());
                                            System.out.println("How would you like to pay?");
                                            System.out.println("1. cash");
                                            System.out.println("2. credit card");
                                            System.out.println("3. saved payment type");
                                            System.out.print("Enter your selection: (ie: 1)");
                                            String pay = cancel.nextLine();
                                            if(pay.equals("B") || pay.equals("b")){
                                                break;
                                            }else if(pay.equals("1")){//Assumed no errors input
                                                List<Double> inserted = new ArrayList<>();
                                                this.displayCash();
                                                double cash = 0;
                                                while (cash < trans.getPrice()){
                                                    double insert = Double.parseDouble(cancel.nextLine());
                                                    inserted.add(insert);
                                                    cash += insert;
                                                }
                                                if(!this.calculateChange(cash, trans)){
                                                    this.writeCancelReason(username, "no change available", new Date());
                                                    System.exit(0);
                                                }
                                                this.updateCashDB(inserted);
                                                this.writeCustHist(username, trans);
                                                trans.setPayMeth("cash");
                                                trans.saveTransaction();
                                                this.db.update();
                                                System.exit(0);
                                            }else if(pay.equals("2")){
                                                while(true){
                                                    System.out.println();
                                                    System.out.println("Please enter your card name: ");
                                                    String cardname = cancel.nextLine();
                                                    System.out.println();
                                                    System.out.println("Please enter your card number: ");
                                                    String cnum = cancel.nextLine();
                                                    System.out.println();
                                                    long cardnum = Long.parseLong(cnum);
                                                    if(cardPay(cardname, cardnum, trans.getPrice())){
                                                        trans.setChange(0);
                                                        trans.setPayMeth("card");
                                                        System.out.println("Would you like to save your card details for your next purchase? (Y|N)");
                                                        String a = cancel.nextLine();
                                                        System.out.println();
                                                        if(a.equals("Y") || a.equals("y")){
                                                            this.setCustomerCard(username, cardname, cardnum);
                                                            System.out.println("Your card details has been saved. Have a nice day!");
                                                            System.out.println();
                                                        }
                                                        this.writeCustHist(username, trans);
                                                        trans.saveTransaction();
                                                        this.db.update();
                                                        System.exit(0);
                                                    }else{
                                                        break;
                                                    }
                                                }
                                            }else if(pay.equals("3")){

                                                if(checkCustCardPay(username, trans.getPrice())){
                                                    trans.setChange(0);
                                                    trans.setPayMeth("card");
                                                    this.writeCustHist(username, trans);
                                                    trans.saveTransaction();
                                                    this.db.update();
                                                    System.exit(0);
                                                }else{
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }else if(prompt == 2){ //Seller
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkSellerNameAlreadyExists(username)){
                            System.out.println("Username already exists. Please try again");
                        }else{
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            Seller seller = new Seller(username, password);
                            this.db.getSellers().add(seller);
                            this.db.update();
                            System.out.println();
                            System.out.println("Account created. Welcome " + username);
                            System.out.println();

                            while (true){
                                System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                System.out.println("1. Get product report");
                                System.out.println("2. Update item details");
                                System.out.println("3. Exit");
                                System.out.println();
                                Scanner i = new Scanner(System.in);
                                int p = Integer.parseInt(i.nextLine());
                                if(p == 1){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Snacks.csv");
                                    System.out.println();
                                }else if(p == 2){
                                    System.out.println("Which snack would you like to update? Enter item code:");
                                    this.displaySnackDetails();
                                    System.out.println();
                                    long x = Long.parseLong(i.nextLine());
                                    System.out.println("Which attribute would you like to update? Enter number:");
                                    System.out.println("1. Item price");
                                    System.out.println("2. Item quantity");
                                    int y = Integer.parseInt(i.nextLine());
                                    if(y == 1){
                                        System.out.println();
                                        System.out.println("Enter price for " + x + ":");
                                        double price = Double.parseDouble(i.nextLine());
                                        this.updateSnacksPrice(x, price);
                                    }else if( y == 2){
                                        System.out.println();
                                        System.out.println("Enter qty for " + x + ":");
                                        int qty = Integer.parseInt(i.nextLine());
                                        this.updateSnacksQty(x, qty);
                                    }
                                    this.db.update();
                                }else if(p == 3){
                                    this.writeCancelReason(username, "user cancelled", new Date());
                                    break;
                                }else{
                                    System.out.println("Invalid prompt, try again");
                                    System.out.println();
                                }
                            }

                        }

                    }else if(prompt == 3){ //Cashier
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkCashierNameAlreadyExists(username)){
                            System.out.println("Username already exists. Please try again");
                        }else{
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            Cashier cashier = new Cashier(username, password);
                            this.db.getCashiers().add(cashier);
                            this.db.update();
                            System.out.println();
                            System.out.println("Account created. Welcome " + username);
                            System.out.println();

                            while (true) {
                                System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                System.out.println("1. Get transaction report");
                                System.out.println("2. Get cash details report");
                                System.out.println("3. Update cash details");
                                System.out.println("4. Exit");
                                System.out.println();
                                Scanner i = new Scanner(System.in);
                                int p = Integer.parseInt(i.nextLine());
                                if(p == 1){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Transaction.csv");
                                    System.out.println();
                                }else if(p == 2){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Cash.csv");
                                    System.out.println();
                                }else if(p == 3){
                                    System.out.println("Enter the number associated with the note/coin you want to update: ");
                                    this.displayCashierMoney();
                                    int cash = Integer.parseInt(i.nextLine());
                                    System.out.println("Enter the qty for the cash type you want to set: ");
                                    int qty = Integer.parseInt(i.nextLine());
                                    this.updateCashQty(cash, qty);
                                    this.db.update();
                                }else if(p == 4){
                                    this.writeCancelReason(username, "user cancelled", new Date());
                                    break;
                                }else{
                                    System.out.println("Invalid prompt, try again");
                                    System.out.println();
                                }
                            }

                        }

                    }else if(prompt == 4){ //Owner
                        System.out.println("Enter username: ");
                        Scanner c = new Scanner(System.in);
                        String username = c.nextLine();
                        if(this.checkOwnerNameAlreadyExists(username)){
                            System.out.println("Username already exists. Please try again");
                        }else{
                            System.out.println();
                            String password = this.readPassword("Enter password: ");
                            Owner o = new Owner(username, password);
                            this.db.getOwners().add(o);
                            this.db.update();
                            System.out.println();
                            System.out.println("Account created. Welcome " + username);
                            System.out.println();

                            while (true){
                                System.out.println("What would you like to do? Enter the number that suits your choice: ");
                                System.out.println("1. Get product report");
                                System.out.println("2. Update item details");
                                System.out.println("3. Get transaction report");
                                System.out.println("4. Get cash details report");
                                System.out.println("5. Update cash details");
                                System.out.println("6. Get all system users report");
                                System.out.println("7. Get cancelled transaction report");
                                System.out.println("8. Update system users");
                                System.out.println("9. Exit");
                                System.out.println();
                                Scanner i = new Scanner(System.in);
                                int p = Integer.parseInt(i.nextLine());
                                if(p == 1){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Snacks.csv");
                                    System.out.println();

                                }else if(p == 2){
                                    System.out.println("Which snack would you like to update? Enter item code:");
                                    this.displaySnackDetails();
                                    System.out.println();
                                    long x = Long.parseLong(i.nextLine());
                                    System.out.println("Which attribute would you like to update? Enter number:");
                                    System.out.println("1. Item price");
                                    System.out.println("2. Item quantity");
                                    int y = Integer.parseInt(i.nextLine());
                                    if(y == 1){
                                        System.out.println();
                                        System.out.println("Enter price for " + x + ":");
                                        double price = Double.parseDouble(i.nextLine());
                                        this.updateSnacksPrice(x, price);
                                    }else if( y == 2){
                                        System.out.println();
                                        System.out.println("Enter qty for " + x + ":");
                                        int qty = Integer.parseInt(i.nextLine());
                                        this.updateSnacksQty(x, qty);
                                    }
                                    this.db.update();

                                }else if(p == 3){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Transaction.csv");
                                    System.out.println();

                                }else if(p == 4){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Cash.csv");
                                    System.out.println();

                                }else if(p == 5){
                                    System.out.println("Enter the number associated with the note/coin you want to update: ");
                                    this.displayCashierMoney();
                                    int cash = Integer.parseInt(i.nextLine());
                                    System.out.println("Enter the qty for the cash type you want to set: ");
                                    int qty = Integer.parseInt(i.nextLine());
                                    this.updateCashQty(cash, qty);
                                    this.db.update();

                                }else if(p == 6){
                                    System.out.println("Your reports are ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Customer.csv to view customers");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Seller.csv to view sellers");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Cashier.csv to view cashiers");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Owner.csv to view owners");
                                    System.out.println();

                                }else if(p == 7){
                                    System.out.println("Your report is ready");
                                    System.out.println("Please navigate to T18A_Group6_A2 folder and open Cancel.csv");
                                    System.out.println();

                                }else if(p == 8){
                                    System.out.println("Which user would you like to update? Enter number:");
                                    System.out.println("1. Customer");
                                    System.out.println("2. Seller");
                                    System.out.println("3. Cashier");
                                    System.out.println("4. Owner");
                                    System.out.println();
                                    int x = Integer.parseInt(i.nextLine());
                                    System.out.println("What would you like to do? Enter number:");
                                    System.out.println("1. Add user");
                                    System.out.println("2. Remove User");
                                    int y = Integer.parseInt(i.nextLine());
                                    if(y == 1){
                                        System.out.println("Enter username of user you want to add:");
                                        String name = i.nextLine();
                                        this.addUser(x, name);
                                    }else if(y == 2){
                                        System.out.println("Enter username of user you want to remove:");
                                        String name = i.nextLine();
                                        this.removeUser(x, name);
                                    }
                                    this.db.update();
                                }else if(p == 9){
                                    this.writeCancelReason(username, "user cancelled", new Date());
                                    break;
                                }else{
                                    System.out.println("Invalid prompt, try again");
                                    System.out.println();
                                }
                            }
                        }

                    }else if(prompt == 5){ //Exit
                        break;
                    }else{
                        System.out.println("Please provide valid number prompt");
                    }
                }

            }else if(cmd == 3){ //Anonymous user
                String username = "anonymous";
                Transaction trans = new Transaction();
                Date date = new Date();
                trans.setDate(date);

                while (true){
                    System.out.println();
                    System.out.println("Please select the type of snack you want from the following list: (ie: 1)");
                    this.displaySnackType();
                    System.out.println("Press 'C' to go back anytime and cancel the transaction");
                    Scanner num = new Scanner(System.in);
                    String back = num.nextLine();
                    if(back.equals("C") || back.equals("c")){
                        this.writeCancelReason(username, "user cancelled", new Date());
                        System.exit(0);
                    }
                    int type = Integer.parseInt(back);
                    System.out.println();
                    System.out.println("Here are the items available from this category with their price and qty:-");
                    while(true){
                        this.displaySnacksItems(type);
                        System.out.println("Press 'B' to go back anytime to buy another snack type or cancel transaction");
                        Scanner cancel = new Scanner(System.in);
                        System.out.println();
                        System.out.println("Enter the 4 digit snack code you want to purchase: (ie: 1000)");
                        String can = cancel.nextLine();
                        if(can.equals("B") || can.equals("b")){
                            break;
                        }
                        long code = Long.parseLong(can);
                        System.out.println();
                        System.out.println("Enter the quantity you want for this snack: (ie: 3)");
                        int qty = Integer.parseInt(cancel.nextLine());
                        this.handleShoppingCart(code, qty, trans);
                        System.out.println();
                        System.out.println("Item added to your purchase list!");
                        System.out.print("Continue Shopping? (Y|N): ");
                        System.out.println();
                        String ans = cancel.nextLine();
                        if(ans.equals("N") || ans.equals("n")){
                            while (true){
                                System.out.println("Your total amount due is $" + trans.getPrice());
                                System.out.println("How would you like to pay?");
                                System.out.println("1. cash");
                                System.out.println("2. credit card");
                                System.out.print("Enter your selection: (ie: 1)");
                                String pay = cancel.nextLine();
                                if(pay.equals("B") || pay.equals("b")){
                                    break;
                                }else if(pay.equals("1")){//Assumed no errors input
                                    List<Double> inserted = new ArrayList<>();
                                    this.displayCash();
                                    double cash = 0;
                                    while (cash < trans.getPrice()){
                                        double insert = Double.parseDouble(cancel.nextLine());
                                        inserted.add(insert);
                                        cash += insert;
                                    }
                                    if(!this.calculateChange(cash, trans)){
                                        this.writeCancelReason(username, "no change available", new Date());
                                        System.exit(0);
                                    }
                                    this.updateCashDB(inserted);
                                    trans.setPayMeth("cash");
                                    trans.saveTransaction();
                                    this.db.update();
                                    System.exit(0);
                                }else if(pay.equals("2")){
                                    while(true){
                                        System.out.println();
                                        System.out.println("Please enter your card name: ");
                                        String cardname = cancel.nextLine();
                                        System.out.println();
                                        System.out.println("Please enter your card number: ");
                                        String cnum = cancel.nextLine();
                                        System.out.println();
                                        long cardnum = Long.parseLong(cnum);
                                        if(cardPay(cardname, cardnum, trans.getPrice())){
                                            trans.setChange(0);
                                            trans.setPayMeth("card");
                                            this.writeCustHist(username, trans);
                                            trans.saveTransaction();
                                            this.db.update();
                                            System.exit(0);
                                        }else{
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

            }else if(cmd == 4){ //Exit
                break;
            }else{ //Wrong prompt
                System.out.println("Please provide valid number prompt");
            }

        }

    }

    public boolean checkCustomerUserName(String username){
        for(Customer c : this.db.getCustomers()){
            if(username.equals(c.getUsername())){
                return true;
            }
        }
        return false;
    }

    //This will be the only single method that will not be tested
    private String readPassword(String p) {
        Masker m = new Masker(p);
        Thread mask = new Thread(m);
        mask.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m.stopMasking();
        return password;
    }

    public boolean checkCustomerPassword(String username, String password){
        for(Customer c : this.db.getCustomers()){
            if(username.equals(c.getUsername()) && password.equals(c.getPassword())){
                return true;
            }
        }
        return false;
    }

    public void customerDefaultDisplay(String username){
        for(Customer c : this.db.getCustomers()){
            if(username.equals(c.getUsername())){
                if(c.getLastFiveSnacks() == null){
                    return;
                }
                System.out.println("Here are your last 5 purchases:-");
                System.out.println();
                for(String snack : c.getLastFiveSnacks()){
                    System.out.println(snack);
                }
                break;
            }
        }
    }
    
    public boolean checkCustomerNameAlreadyExists(String username){

        for(Customer c : this.db.getCustomers()){
            if(c.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }


    public void displaySnackType(){
        int i = 1;
        List<String> temp = new ArrayList<>();
        for(Snack s : this.db.getItems()){
            if(!this.exists(temp, s.getType())){
                System.out.println(i + ". " + s.getType());
                i++;
                temp.add(s.getType());
            }
        }
    }

    public boolean exists(List<String> temp, String snack){
        for(String s : temp){
            if(s.equals(snack)){
                return true;
            }
        }
        return false;
    }

    public void displaySnacksItems(int type){
        if(type == 1){
            for(Snack s : this.db.getItems()){
                if(s.getType().equals("Drinks") && s.getQty() > 0){
                    System.out.println("Code: " +  s.getCode() + " name: " + s.getName() + " price: " + s.getPrice() + " qty: " + s.getQty());
                }
            }
        }else if(type == 2){
            for(Snack s : this.db.getItems()){
                if(s.getType().equals("Chocolates") && s.getQty() > 0){
                    System.out.println("Code: " +  s.getCode() + " name: " + s.getName() + " price: " + s.getPrice() + " qty: " + s.getQty());
                }
            }
        }else if(type == 3){
            for(Snack s : this.db.getItems()){
                if(s.getType().equals("Chips") && s.getQty() > 0){
                    System.out.println("Code: " +  s.getCode() + " name: " + s.getName() + " price: " + s.getPrice() + " qty: " + s.getQty());
                }
            }
        }else if(type == 4){
            for(Snack s : this.db.getItems()){
                if(s.getType().equals("Candies") && s.getQty() > 0){
                    System.out.println("Code: " +  s.getCode() + " name: " + s.getName() + " price: " + s.getPrice() + " qty: " + s.getQty());
                }
            }
        }else{
            System.out.println("Invalid type please try again");
        }
    }

    public void handleShoppingCart(long code, int qty, Transaction trans){
        if(qty <= 0){
            System.out.println("Invalid quantity");
            return;
        }

        boolean found = false;
        for(Snack s : this.db.getItems()){
            if(s.getCode() == code && s.getQty() >= qty){
                trans.getItemSold().add(s);
                trans.setPrice(s.getPrice() * qty);
                s.setQtySold(qty);
                found = true;
                break;
            }
        }

        if(!found){
            System.out.println("Invalid item code or quantity requested");
            return;
        }
    }

    public boolean cardPay(String cardname, long cardnum, double amount){

        for(Card c : this.db.getCards()){
            if(cardname.equals(c.getCardname()) && cardnum == c.getCardnum()){//valid card
                if(amount > c.getBalance()){
                    System.out.println("Not enough balance, please try again later");
                    return false;
                }else{
                    c.pay(amount);
                }
                System.out.println("Thank you for shopping in the vending machine");
                return true;
            }
        }

        System.out.println("Invalid card details, please try again later");
        return false;
    }

    public void setCustomerCard(String username, String cardname, long cardnum){
        for(Customer c : this.db.getCustomers()){
            if(c.getUsername().equals(username)){
                c.setSavedPay();
                Card card = new Card(cardname, cardnum);
                c.setCard(card);
                break;
            }
        }
    }

    public boolean checkCustCardPay(String username, double amount){
        for(Customer c : this.db.getCustomers()){
            if(c.getUsername().equals(username) && c.isSavedPay() && amount < c.getCard().getBalance()){
                c.getCard().pay(amount);
                System.out.println("Payment Successful");
                return true;
            }
        }
        System.out.println("Payment Failed");
        return false;
    }

    public void displayCash(){
        System.out.println();
        System.out.println("The machine takes the following types of cash types:");
        for(Cash c : this.db.getCashes()){
            System.out.println("$" + c.getAmount() + " " + c.getType());
        }
        System.out.println();
        System.out.println("Please enter the amount you would like to pay from the above list (ie: 10)");
        System.out.println("Insert one by one and press enter when one of them is inserted.");
    }

    public boolean calculateChange(double amount, Transaction trans){

        double change = amount - trans.getPrice();
        if(change == 0){
            System.out.println("Payment Succesful");
            System.out.println("Exact amount paid. No change required");
            System.out.println();
            trans.setChange(change);
            return true;
        }

        boolean changed = false;
        List<Cash> display = new ArrayList<>();
        for(Cash c : this.db.getCashes()){
            for(int i = 0; i < c.getQty(); i++){
                if(c.getAmount() <= change){
                    display.add(c);
                    change -= c.getAmount();
                    if(change == 0){
                        changed = true;
                        break;
                    }
                }
            }
            if(changed){
                break;
            }
        }

        //remove qty of cash
        for(Cash b : display){
            for(Cash c : this.db.getCashes()){
                if(b.getAmount() == c.getAmount()){
                    c.setQty(-1);
                }
            }
        }

        if(changed){
            System.out.println("Here is your change");
            for(Cash c : display){
                System.out.println("$" + c.getAmount() + " " + c.getType());
            }
            System.out.println();
            System.out.println("Thank You!");
            return true;
        }else{
            System.out.println("No change available in the system. Try again later");
            return false;
        }
    }

    public void updateCashDB(List<Double> inserted){
        for(Double b : inserted){
            for(Cash c : this.db.getCashes()){
                if(b == c.getAmount()){
                    c.setQty(1);
                }
            }
        }
    }

    public void writeCancelReason(String username, String reason, Date date){
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("Cancel.csv"), true));
            writer.println("");
            writer.print(username + "," + reason + "," + dateFormat.format(date));
            writer.close();

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeCustHist(String username, Transaction trans){
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("CustHist.csv"), true));
            for(Snack item : trans.getItemSold()){
                writer.println("");
                writer.print(username + "," + item.getName());
            }
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkSellerUserName(String username){
        for(Seller c : this.db.getSellers()){
            if(username.equals(c.getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean checkSellerPassword(String username, String password){
        for(Seller c : this.db.getSellers()){
            if(username.equals(c.getUsername()) && password.equals(c.getPassword())){
                return true;
            }
        }
        return false;
    }

    public boolean checkSellerNameAlreadyExists(String username){

        for(Seller c : this.db.getSellers()){
            if(c.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }

    public void displaySnackDetails(){
        for(Snack s : this.db.getItems()){
            System.out.println(s.getCode() + "    " + s.getName());
        }
    }

    public void updateSnacksPrice(long code, double price){

        if(price < 0){
            System.out.println("Invalid item price, please try again later");
            return;
        }

        for(Snack s : this.db.getItems()){
            if(s.getCode() == code){
                s.setPrice(price);
                System.out.println();
                System.out.println("Your changes have been successfully made!");
                System.out.println();
                return;
            }
        }

        System.out.println("Invalid item code, please try again later"); //shouldnt happen if code is correct
    }

    public void updateSnacksQty(long code, int qty){

        if(qty < 0 || qty > 15){
            System.out.println("Invalid item qty, please try again later");
            return;
        }

        for(Snack s : this.db.getItems()){
            if(s.getCode() == code){
                s.setQty(qty);
                System.out.println();
                System.out.println("Your changes have been successfully made!");
                System.out.println();
                return;
            }
        }

        System.out.println("Invalid item code, please try again later"); //shouldnt happen if code is correct
    }

    public boolean checkCashierUserName(String username){
        for(Cashier c : this.db.getCashiers()){
            if(username.equals(c.getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean checkCashierPassword(String username, String password){
        for(Cashier c : this.db.getCashiers()){
            if(username.equals(c.getUsername()) && password.equals(c.getPassword())){
                return true;
            }
        }
        return false;
    }

    public boolean checkCashierNameAlreadyExists(String username){

        for(Cashier c : this.db.getCashiers()){
            if(c.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }

    public void displayCashierMoney(){
        int x = 1;
        for(Cash c : this.db.getCashes()){
            System.out.println(x + "." + " " + c.getAmount());
            x++;
        }
    }

    public void updateCashQty(int cash, int qty){
        if(cash < 1 || cash > 5 || qty < 0){
            System.out.println("Invalid cash type or qty. Try again later.");
            return;
        }

        if(cash == 1){
            for(Cash c : this.db.getCashes()){
                if(c.getAmount() == 20){
                    c.setQty(qty);
                    break;
                }
            }
        }else if(cash == 2){
            for(Cash c : this.db.getCashes()){
                if(c.getAmount() == 10){
                    c.setQty(qty);
                    break;
                }
            }

        }else if(cash == 3){
            for(Cash c : this.db.getCashes()){
                if(c.getAmount() == 5){
                    c.setQty(qty);
                    break;
                }
            }

        }else  if(cash == 4){
            for(Cash c : this.db.getCashes()){
                if(c.getAmount() == 2){
                    c.setQty(qty);
                    break;
                }
            }

        }else {
            for(Cash c : this.db.getCashes()){
                if(c.getAmount() == 1){
                    c.setQty(qty);
                    break;
                }
            }
        }

        System.out.println();
        System.out.println("Your changes have been successfully made!");
        System.out.println();
    }

    //***********Write tests for functions below****************

    public boolean checkOwnerUserName(String username){
        for(Owner c : this.db.getOwners()){
            if(username.equals(c.getUsername())){
                return true;
            }
        }
        return false;
    }

    public boolean checkOwnerPassword(String username, String password){
        for(Owner c : this.db.getOwners()){
            if(username.equals(c.getUsername()) && password.equals(c.getPassword())){
                return true;
            }
        }
        return false;
    }

    public boolean checkOwnerNameAlreadyExists(String username){

        for(Owner c : this.db.getOwners()){
            if(c.getUsername().equals(username)){
                return true;
            }
        }

        return false;
    }

    public void addUser(int x, String name){

        if(x == 1){//customer
            Customer c = new Customer(name, "1234", false, null);
            this.db.getCustomers().add(c);

        }else if(x == 2){//seller
            Seller s = new Seller(name, "1234");
            this.db.getSellers().add(s);

        }else if(x == 3){//cashier
            Cashier s = new Cashier(name, "1234");
            this.db.getCashiers().add(s);

        }else if(x == 4){//owner
            Owner s = new Owner(name, "1234");
            this.db.getOwners().add(s);

        }

        System.out.println("System user added!");
        System.out.println();
    }

    public void removeUser(int x, String name){

        boolean found = false;

        if(x == 1){//customer
            for(Customer c : this.db.getCustomers()){
                if(c.getUsername().equals(name)){
                    found = true;
                    this.db.getCustomers().remove(c);
                    break;
                }
            }

        }else if(x == 2){//seller
            for(Seller c : this.db.getSellers()){
                if(c.getUsername().equals(name)){
                    found = true;
                    this.db.getSellers().remove(c);
                    break;
                }
            }

        }else if(x == 3){//cashier
            for(Cashier c : this.db.getCashiers()){
                if(c.getUsername().equals(name)){
                    found = true;
                    this.db.getCashiers().remove(c);
                    break;
                }
            }

        }else if(x == 4){//owner
            for(Owner c : this.db.getOwners()){
                if(c.getUsername().equals(name)){
                    found = true;
                    this.db.getOwners().remove(c);
                    break;
                }
            }

        }

        if(found){
            System.out.println("System user removed!");
            System.out.println();
        }else{
            System.out.println("User not found, try again later.");
            System.out.println();
        }
    }

}
