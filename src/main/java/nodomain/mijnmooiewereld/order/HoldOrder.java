package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public record HoldOrder(
        String status,
        Unit unit,
        Location location
) implements Order {
    @Override
    public String actionString() {
        return "Holds";
    }
}
