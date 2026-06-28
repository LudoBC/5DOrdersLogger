package nodomain.mijnmooiewereld.orders.logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProvinceTest {
    @Test
    void provinceTest() {
        assertEquals(Province.UNOWNED_ARMY, Province.associatedUnit("Mun"));
        assertEquals(Province.UNOWNED_FLEET, Province.associatedUnit("MAO"));
        assertEquals(Province.UNOWNED_UNIT, Province.associatedUnit("Bel"));
    }
}
