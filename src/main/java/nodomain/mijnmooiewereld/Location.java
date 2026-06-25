package nodomain.mijnmooiewereld;

import org.jetbrains.annotations.NotNull;

public record Location(
        Board board,
        String region
) {
    public record Board(
            int year,
            String phase,
            int timeline
    ) {
        public static int STARTING_YEAR = 1901;

        public boolean isSameBoard(Board as) {
            return this.timeline == as.timeline && this.year == as.year && this.phase.equals(as.phase);
        }

        public @NotNull String toString() {
            return "T"+timeline+" "+phase.charAt(0)+year;
        }

        public int turn() {
            return 3 * (year() - STARTING_YEAR) +  switch (phase) {
                case "Spring" -> 0;
                case "Fall" -> 1;
                case "Winter" -> 2;
                default -> throw new IllegalStateException("Unexpected value: " + phase);
            };
        }
    }

    public @NotNull String toString() {
        return region;
    }

    public String toLongString() {
        return board + " " + region;
    }

    public String relativeToString(Location to) {
        if (board.isSameBoard(to.board)) {
            return toString();
        }
        return toLongString();
    }
}

