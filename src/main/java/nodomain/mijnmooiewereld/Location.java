package nodomain.mijnmooiewereld;

public record Location(
        int timeline,
        int year,
        String phase,
        String region
) {
    public String toString() {
        return region;
    }

    public String boardId() {
        return "T"+timeline+" "+phase.charAt(0)+year+" "+region;
    }

    public String relativeBoardId(Location to) {
        if (isSameBoard(to)) {
            return toString();
        }
        return boardId();
    }

    public boolean isSameBoard(Location as) {
        return this.timeline == as.timeline && this.year == as.year && this.phase.equals(as.phase);
    }
}

