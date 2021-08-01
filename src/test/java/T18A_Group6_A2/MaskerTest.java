package T18A_Group6_A2;

import org.junit.Test;
import static org.junit.Assert.*;

public class MaskerTest {
    @Test
    public void testStopMasker() {
        Masker masker = new Masker("testPrompt");
        assertFalse(masker.returnStop());
        masker.stopMasking();
        assertFalse(masker.returnStop());
    }
}
