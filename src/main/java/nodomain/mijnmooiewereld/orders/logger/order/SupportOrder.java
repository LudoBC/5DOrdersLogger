package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public record SupportOrder(
        Location destination,
        String status,
        Unit unit,
        Location location,
        Location supportLocation,
        Unit supportedUnit
) implements Order {
    @Override
    public String actionString() {
        String base = "S " + supportedUnit.type().charAt(0) + " ";
        if (supportLocation.equals(destination)) {
            return base + " " + supportedUnit.type().charAt(0) + " " + destination.relativeToString(location) + " H";
        } else if (location.board().isSameBoard(supportLocation.board())) {
            return base + supportedUnit.type().charAt(0) + " " + supportLocation + " - " + destination.relativeToString(location);
        } else {
            return base + supportedUnit.type().charAt(0) + " " + supportLocation.toLongString() + " - " + destination.toLongString();
        }
    }
}
