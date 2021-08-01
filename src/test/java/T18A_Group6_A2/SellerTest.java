package T18A_Group6_A2;

import org.junit.Test;
import static org.junit.Assert.*;


public class SellerTest {

    private final String username = "demoseller";
    private final String password = "1234";

    @Test
    public void testTusername() {
        Seller seller = new Seller(username, password);
        assertEquals(seller.getUsername(), username);
    }

    @Test
    public void testTpassword() {
        Seller seller = new Seller(username, password);
        assertEquals(seller.getPassword(), password);
    }
}
