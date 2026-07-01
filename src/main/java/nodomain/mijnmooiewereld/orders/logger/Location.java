package nodomain.mijnmooiewereld.orders.logger;

import org.jetbrains.annotations.NotNull;

public record Location(
        Board board,
        String region
) {
    public record Board(
            int year,
            Phase phase,
            int timeline
    ) {
        public enum Phase {
            SPRING, FALL, WINTER
        }

        public static int STARTING_YEAR = 1901;

        public boolean isSameBoard(Board as) {
            return this.timeline == as.timeline && this.year == as.year && this.phase.equals(as.phase);
        }

        public @NotNull String toString() {
            return "T"+timeline+phase.name().charAt(0)+Integer.toString(year).replace("19", "'");
        }

        public int turn() {
            return 3 * (year() - STARTING_YEAR) +  switch (phase) {
                case SPRING -> 0;
                case FALL -> 1;
                case WINTER -> 2;
            };
        }
    }

    public @NotNull String toString() {
        return region;
    }

    public @NotNull String toString(char unitType) {
        return unitType + " " + region;
    }

    public String toLongString() {
        return board + " " + region;
    }

    public String toLongString(char unitType) {
        return board + " " + unitType + " " + region;
    }


    public String relativeToString(Location to) {
        if (board.isSameBoard(to.board)) {
            return toString();
        }
        return toLongString();
    }

    public String relativeToString(Location to, char unitType) {
        if (board.isSameBoard(to.board)) {
            return toString(unitType);
        }
        return toLongString(unitType);
    }

}

