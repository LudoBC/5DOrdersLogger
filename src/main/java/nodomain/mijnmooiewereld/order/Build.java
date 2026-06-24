package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public record Build(String status,
                    Unit unit,
                    Location location
) implements Order {
    @Override
    public String actionString() {
        return "";
    }

    @Override
    public String printableString() {
        return "Build " + unit.type() + " " + location;
    }
}
