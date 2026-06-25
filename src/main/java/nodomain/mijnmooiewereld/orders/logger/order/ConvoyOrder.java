package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public record ConvoyOrder(
        Location destination,
        String status,
        Unit unit,
        Location location,
        Location convoyLocation
) implements Order {
    @Override
    public String actionString() {
        if (location.board().isSameBoard(convoyLocation.board())) {
            return "C A " + convoyLocation + " - " + destination.relativeToString(location);
        } else {
            return "C A " + convoyLocation.toLongString() + " - " + destination.toLongString();
        }
    }
}
