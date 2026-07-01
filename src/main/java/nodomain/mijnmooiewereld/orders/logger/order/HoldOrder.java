package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public record HoldOrder(
        String status,
        Unit unit,
        Location location
) implements Order.MainPhase {
    @Override
    public String actionString() {
        return "Holds";
    }
}
