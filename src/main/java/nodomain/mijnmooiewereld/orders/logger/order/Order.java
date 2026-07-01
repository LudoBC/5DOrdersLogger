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

    default String owner() {
        return unit().owner();
    }

    String printableString();

    interface MainPhase extends Order {
        String actionString();

        @Override
        default String printableString() {
            String base = unit().type().charAt(0) + " " + location() + " " + actionString();
            if ("Failure".equals(status())) {
                return "*"+base+"*";
            } else {
                return base;
            }
        }
    }
}
