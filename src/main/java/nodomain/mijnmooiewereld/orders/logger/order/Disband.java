package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public record Disband(
        String status,
        Unit unit,
        Location location
) implements Order {
    @Override
    public String actionString() {
        return "";
    }

    @Override
    public String printableString() {
        return "Disband " + unit.type() + " " + location;
    }

}
