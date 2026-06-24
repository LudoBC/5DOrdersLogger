package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public record ConvoyOrder(
        Location destination,
        String status,
        Unit unit,
        Location location,
        Location convoyLocation
) implements Order {
    @Override
    public String actionString() {
        if (location.isSameBoard(convoyLocation)) {
            return "S " + convoyLocation + " - " + destination.relativeBoardId(location);
        } else {
            return "S " + convoyLocation.boardId() + " - " + destination.boardId();
        }

    }
}
