package nodomain.mijnmooiewereld.orders.logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTest {
    @Test
    void turnTest() {
        Location.Board fall1902 = new Location.Board(1902, Location.Board.Phase.FALL, 0);
        assertEquals(4, fall1902.turn());
    }
}
