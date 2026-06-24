package nodomain.mijnmooiewereld.order;

import nodomain.mijnmooiewereld.Location;
import nodomain.mijnmooiewereld.Unit;

public interface Order {
    String status();
    Unit unit();
    Location location();

    default int timeline() {
        return location().timeline();
    }

    default int year() {
        return location().year();
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

    int STARTING_YEAR = 1901;

    default int turn() {
        if ("Fall".equals(location().phase())) {
            return 2 * (year() - STARTING_YEAR) + 1;
        }
        return 2 * (year() - STARTING_YEAR);
    }

    static String turnString(int turn) {
        if (turn % 2 == 0) {
            return "S"+(turn/2 + STARTING_YEAR);
        }
        return "F"+(turn/2 + STARTING_YEAR);
    }
}
