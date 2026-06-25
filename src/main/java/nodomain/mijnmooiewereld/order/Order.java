package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public interface Order {
    String status();
    Unit unit();
    Location location();

    default int timeline() {
        return location().board().timeline();
    }

    default int year() {
        return location().board().year();
    }

    default String owner() {
        return unit().owner();
    }

    String actionString();

    default String printableString() {
        String base = unit().type().charAt(0) + " " + location() + " " + actionString();
        if ("Failure".equals(status())) {
            return "*"+base+"*";
        } else {
           return base;
        }
    }
}
