package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public record SupportOrder(
        Location destination,
        String status,
        Unit unit,
        Location location,
        Location supportLocation
) implements Order {
    @Override
    public String actionString() {
        if (supportLocation.equals(destination)) {
            return "S " + destination.relativeToString(location);
        } else if (location.board().isSameBoard(supportLocation.board())) {
            return "S " + supportLocation + " - " + destination.relativeToString(location);
        } else {
            return "S " + supportLocation.toLongString() + " - " + destination.toLongString();
        }
    }
}
