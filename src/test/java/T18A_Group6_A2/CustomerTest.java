package T18A_Group6_A2;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CustomerTest {

    @Test
    public void testCustomersetPay(){
        Card a = new Card("aditya", 16543);
        Customer c = new Customer("demo", "1234", false, null);
        assertFalse(c.isSavedPay());
        assertNull(c.getCard());
        c.setCard(a);
        c.setSavedPay();
        assertNotNull(c.getCard());
        assertTrue(c.isSavedPay());
    }


    @Test
    public void testCustomerHistory(){
        Customer c = new Customer("democustomer", "1234", false, null);
        boolean ex = false;
        List<String> s = null;
        try{
            s = c.getLastFiveSnacks();
        }catch (Exception e){
            ex = true;
        }

        assertNotNull(s);
        assertFalse(ex);
    }


    @Test
    public void testgetCardBalance(){
        Card a = new Card("aditya", 16543);
        assertEquals(100, a.getBalance(), 0);
    }


}
