package nodomain.mijnmooiewereld;

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

        public String toString() {
            return "T"+timeline+" "+phase.charAt(0)+year;
        }

        public int turn() {
            if ("Fall".equals(phase)) {
                return 2 * (year() - STARTING_YEAR) + 1;
            }
            return 2 * (year() - STARTING_YEAR);
        }
    }

    public String toString() {
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

