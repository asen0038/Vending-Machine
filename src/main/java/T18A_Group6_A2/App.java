package T18A_Group6_A2;

/*
This class sets up the database and starts the program
 */

public class App {

    public void setup(){
        Database db = new Database();
        db.load();
        VendingMachine v = new VendingMachine(db);
        v.start();
    }

    public static void main(String[] args) {
        App a = new App();
        a.setup();
    }
}
