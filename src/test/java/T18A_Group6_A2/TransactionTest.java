package T18A_Group6_A2;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void testTid() {
        Transaction transaction = new Transaction();
        transaction.setTid(2);
        assertEquals(transaction.getTid(), 2);
    }

    @Test
    public void testDate() {
        Transaction transaction = new Transaction();
        Date ToSet = new Date(2020, 11, 1);
        transaction.setDate(ToSet);
        assertEquals(transaction.getDate(), ToSet);
    }

    @Test
    public void testSnack() {
        List<Snack> itemSold = new ArrayList<Snack>();
        Snack coke = new Snack("Drinks",1002,"Coca Cola",7.0,7,0);
        Snack mm = new  Snack("Chocolates",2001,"M&M",5.0,7,0);
        itemSold.add(coke);
        itemSold.add(mm);
        Transaction transaction = new Transaction();
        assertTrue(transaction.getItemSold().isEmpty());
        transaction.setItemSold(itemSold);
        assertFalse(transaction.getItemSold().isEmpty());
        
        //test setting snack info
        coke.setType("Soft Drinks");
        coke.setCode(1111);
        coke.setName("Pepsi");
        coke.setPrice(8.0);
        coke.setQty(10);
        coke.setQtySold(5);
        assertEquals("Soft Drinks", coke.getType());
        assertEquals(1111, coke.getCode());
        assertEquals("Pepsi", coke.getName());
        assertEquals(8.0, coke.getPrice(), 0.1);
        assertEquals(5, coke.getQty());
        assertEquals(5, coke.getQtySold());
    }

    @Test
    public void testPrice() {
        Transaction transaction = new Transaction();
        assertEquals(0.0, transaction.getPrice(), 0.01);
        transaction.setPrice(2.1);
        assertEquals(2.1, transaction.getPrice(), 0.01);
    }

    @Test
    public void testChange() {
        Transaction transaction = new Transaction();
        transaction.setChange(0.5);
        assertEquals(0.5, transaction.getChange(), 0.01);
    }

    @Test
    public void testPayMeth() {
        Transaction transaction = new Transaction();
        assertNull(transaction.getPayMeth());
        transaction.setPayMeth("Cash");
        assertEquals("Cash", transaction.getPayMeth());
    }
    
    @Test
    public void testCardAndCashInfo() {
        Card card = new Card("ANZ", 0000000000000000);
        Cash cash = new Cash(40.0,"20", 2);
        cash.setQty(3);
        assertEquals(3,cash.getQty());
        card.pay(40.0);
        assertEquals("ANZ", card.getCardname());
        assertEquals(0000000000000000, card.getCardnum());
        assertEquals(60.0, card.getBalance(),0.1);
    }
    
    @Test
    public void testCalcTid() {
        Transaction t = new Transaction();
        boolean excaught = false;
        try{
            t.calcTid();
        }catch (Exception e){
            excaught = true;
        }
    }

    @Test
    public void testSaveTransaction() {
        Transaction t = new Transaction();
        boolean excaught = false;
        try{
            t.saveTransaction();
        }catch (Exception e){
            excaught = true;
        }
    }
}
