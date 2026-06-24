package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

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
            return "S " + destination.relativeBoardId(location);
        } else if (location.isSameBoard(supportLocation)) {
            return "S " + supportLocation + " - " + destination.relativeBoardId(location);
        } else {
            return "S " + supportLocation.boardId() + " - " + destination.boardId();
        }
    }
}
