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
        if (location.board().isSameBoard(convoyLocation.board())) {
            return "C " + convoyLocation + " - " + destination.relativeToString(location);
        } else {
            return "C " + convoyLocation.toLongString() + " - " + destination.toLongString();
        }

    }
}
