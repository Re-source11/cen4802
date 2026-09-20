import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RepMaxTest {


    @Test
    void epleyKnownValue() {
        assertEquals(262.5, SimpleHttpServer.epley(225, 5), 0.01);
    }

    @Test
    void epleyKnownValue2(){
    assertEquals(110.0,SimpleHttpServer.epley(100, 3), 0.01);
    }

    @Test
    void brzyckiKnownValue(){assertEquals(100.0, SimpleHttpServer.brzycki(100, 1), 0.01);}

}