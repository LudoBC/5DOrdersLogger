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
        public boolean isSameBoard(Board as) {
            return this.timeline == as.timeline && this.year == as.year && this.phase.equals(as.phase);
        }

        public String toString() {
            return "T"+timeline+" "+phase.charAt(0)+year;
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

