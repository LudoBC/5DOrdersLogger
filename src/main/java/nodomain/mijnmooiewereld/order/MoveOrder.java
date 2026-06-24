package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public record MoveOrder(
        Location destination,
        String status,
        Unit unit,
        Location location
) implements Order {
    @Override
    public String actionString() {
        return "- " + destination().relativeToString(location);
    }
}
