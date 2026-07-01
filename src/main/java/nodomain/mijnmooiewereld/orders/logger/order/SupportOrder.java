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
) implements Order.MainPhase {
    @Override
    public String actionString() {
        char supportedUnitType = supportedUnit.type().charAt(0);
        if (supportLocation.equals(destination)) {
            return "S " + destination.relativeToString(location, supportedUnitType) + " H";
        } else if (location.board().isSameBoard(supportLocation.board())) {
            return "S " + supportLocation.toString(supportedUnitType) + " - " + destination.relativeToString(location);
        } else {
            return "S " + supportLocation.toLongString(supportedUnitType) + " - " + destination.toLongString();
        }
    }
}
