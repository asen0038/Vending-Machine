package T18A_Group6_A2;

import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void testDatabaseNotNull(){
        Database a = new Database();
        assertNotNull(a);
    }

    @Test
    public void testDatabaseLoad(){
        Database a = new Database();
        boolean excaught = false;
        try{
            a.load();
        }catch (Exception e){
            excaught = true;
        }
        assertFalse(excaught);
    }

    @Test
    public void testDatabaseUpdate(){
        Database a = new Database();
        a.load();
        boolean excaught = false;
        try{
            a.update();
        }catch (Exception e){
            excaught = true;
        }
        assertFalse(excaught);
    }

    @Test
    public void testGetItems(){
        Database a = new Database();
        assertNotNull(a.getItems());
    }

    @Test
    public void testGetCustomers(){
        Database a = new Database();
        assertNotNull(a.getCustomers());
    }

    @Test
    public void testGetSellers(){
        Database a = new Database();
        assertNotNull(a.getSellers());
    }

    @Test
    public void testGetCashiers(){
        Database a = new Database();
        assertNotNull(a.getCashiers());
    }

    @Test
    public void testGetOwners(){
        Database a = new Database();
        assertNotNull(a.getOwners());
    }

    @Test
    public void testGetCashes(){
        Database a = new Database();
        assertNotNull(a.getCashes());
    }

    @Test
    public void testGetCards(){
        Database a = new Database();
        assertNotNull(a.getCards());
    }

}
