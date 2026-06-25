package nodomain.mijnmooiewereld.orders.logger.order;

import nodomain.mijnmooiewereld.orders.logger.Location;
import nodomain.mijnmooiewereld.orders.logger.Unit;

public interface Order {
    String status();
    Unit unit();
    Location location();

    default Location.Board board() {
        return location().board();
    }

    default int timeline() {
        return board().timeline();
    }

    default int year() {
        return board().year();
    }

    default String owner() {
        return unit().owner();
    }

    default String actionString() {
        throw new UnsupportedOperationException();
    }

    default String printableString() {
        String base = unit().type().charAt(0) + " " + location() + " " + actionString();
        if ("Failure".equals(status())) {
            return "*"+base+"*";
        } else {
           return base;
        }
    }
}
