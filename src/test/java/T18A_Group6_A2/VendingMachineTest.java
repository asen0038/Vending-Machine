package T18A_Group6_A2;

import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class VendingMachineTest {

    static Database d;
    @BeforeClass
    public static void setup(){
        d = new Database();
        d.load();
    }

    @Test
    public void testNotNull(){
        VendingMachine v = new VendingMachine(d);
        assertNotNull(v);
    }

    @Test
    public void testCheckCustomerUserName(){
        VendingMachine v = new VendingMachine(d);
        boolean t = v.checkCustomerUserName("democustomer");
        boolean f = v.checkCustomerUserName("notexsits");
        assertTrue(t);
        assertFalse(f);
    }

    @Test
    public void testCheckCustomerPassword(){
        VendingMachine v = new VendingMachine(d);
        boolean t = v.checkCustomerPassword("democustomer", "1234");
        boolean f1 = v.checkCustomerPassword("democustomer","99999");
        boolean f2 = v.checkCustomerPassword("notexists", "1234");
        boolean f3 = v.checkCustomerPassword("notexits", "99999");
        assertTrue(t);
        assertFalse(f1);
        assertFalse(f2);
        assertFalse(f3);
    }

    @Test
    public void testCustomerDefaultDisplayValid(){
        VendingMachine v = new VendingMachine(d);
        boolean ex = false;
        try{
            v.customerDefaultDisplay("democustomer");
        }catch (Exception e){
            ex = true;
        }
        assertFalse(ex);
    }

    @Test
    public void testCustomerDefaultDisplayInValid(){
        VendingMachine v = new VendingMachine(d);
        boolean ex = false;
        try{
            v.customerDefaultDisplay("notexists");
        }catch (Exception e){
            ex = true;
        }
        assertFalse(ex);
    }

    @Test
    public void testNewCustomerDefaultDisplay(){
        VendingMachine v = new VendingMachine(d);
        boolean ex = false;
        try{
            v.customerDefaultDisplay("aditya");
        }catch (Exception e){
            ex = true;
        }
        assertFalse(ex);
    }

    @Test
    public void testCheckCustomerNameAlreadyExists(){
        VendingMachine v = new VendingMachine(d);
        boolean t = v.checkCustomerNameAlreadyExists("democustomer");
        boolean f = v.checkCustomerNameAlreadyExists("notexists");
        assertTrue(t);
        assertFalse(f);
    }
    
    @Test
    public void testExistsSnack() {
        VendingMachine v = new VendingMachine(d);
        List<String> temp = new ArrayList<>();
        temp.add("Drinks");
        assertTrue(v.exists(temp,"Drinks"));
        temp.add("Cakes");
        assertTrue(v.exists(temp,"Cakes"));
    }
    
    @Test
    public void testCalculateChange() {
        VendingMachine v = new VendingMachine(d);
        Transaction transaction = new Transaction();
        transaction.setPrice(0.5);
        assertTrue(v.calculateChange(0.5, transaction));
    }

    @Test
    public void testCardPay() {
        VendingMachine v = new VendingMachine(d);
        assertFalse(v.cardPay("Charles", 40691, 110));
        assertTrue(v.cardPay("Maxine", 34402, 90));
    }

    @Test
    public void testCheckCustCardPay() {
        VendingMachine v = new VendingMachine(d);
        assertFalse(v.checkCustCardPay("democustomer", 1000));
    }

    @Test
    public void testCheckCashierNameAlreadyExists() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkCashierNameAlreadyExists("democashier"));
        assertFalse(v.checkCashierNameAlreadyExists("cashierA"));
    }

    @Test
    public void testCheckCashierPassword() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkCashierPassword("democashier", "1234"));
        assertFalse(v.checkCashierPassword("democashier", "123456"));
        assertFalse(v.checkCashierPassword("cashierA", "123456"));
    }

    @Test
    public void testCheckCashierUserName() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkCashierUserName("democashier"));
        assertFalse(v.checkCashierUserName("cashierA"));
    }

    @Test
    public void testCheckSellerUserName() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkSellerUserName("demoseller"));
        assertFalse(v.checkSellerUserName("sellerA"));
    }

    @Test
    public void testCheckSellerPassword() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkSellerPassword("demoseller", "1234"));
        assertFalse(v.checkSellerPassword("sellerA", "1234"));
        assertFalse(v.checkSellerPassword("demoseller", "123"));
    }

    @Test
    public void testCheckSellerNameAlreadyExists() {
        VendingMachine v = new VendingMachine(d);
        assertTrue(v.checkSellerNameAlreadyExists("demoseller"));
        assertFalse(v.checkSellerNameAlreadyExists("sellerA"));
    }

}
