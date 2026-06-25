package nodomain.mijnmooiewereld.orders.logger;

import java.util.List;

import static java.util.Arrays.stream;

public sealed interface Province {
    //TODO add functionality for provinces with multiple coasts

    static boolean allowsArmies(String abbreviation) {
        return !SEA_PROVINCES.contains(abbreviation.substring(0, 3).toUpperCase());
    }

    static boolean allowsFleets(String abbreviation) {
        return !INLAND_PROVINCES.contains(abbreviation.substring(0, 3).toUpperCase());
    }

    static Unit associatedUnit(String... abbreviations) {
        if (stream(abbreviations).allMatch(Province::allowsArmies)
                && !stream(abbreviations).allMatch(Province::allowsFleets)) {
            return UNOWNED_ARMY;
        }
        if (stream(abbreviations).allMatch(Province::allowsFleets)
                && !stream(abbreviations).allMatch(Province::allowsArmies)) {
            return UNOWNED_FLEET;
        }
        return UNOWNED_UNIT;
    }

    Unit UNOWNED_UNIT = new Unit("unowned", "Unit", false);
    Unit UNOWNED_ARMY = new Unit("unowned", "Army", false);
    Unit UNOWNED_FLEET = new Unit("unowned", "Fleet", false);


    List<String> COASTAL_PROVINCES = stream(Coastal.values()).map(Enum::name).toList();
    enum Coastal implements Province {
        // supply centers
        POR, SPA, BRE, BEL, HOL, KIE, BER, DEN, SWE, NWY, STP, SEV, CON, BUL, RUM,
        GRE, TRI, NAP, LON, TUN, MAR, VEN, ROM, SMY, ANK, EDI, LVP, PIC, PRU,
        //non supply centers
        FIN, LVN, ALB, APU, CLY, NAF, GAS, PIE, TUS, ARM, SYR, YOR, WAL
    }

    List<String> INLAND_PROVINCES = stream(InLand.values()).map(Enum::name).toList();
    enum InLand implements Province {
        // supply centers
        PAR, MUN, MOS, WAR, SER, BUD, VIE,
        //non supply centers
        BUR, RUH, SIL, UKR, GAL, BOH, TYR
    }

    List<String> SEA_PROVINCES = stream(Sea.values()).map(Enum::name).toList();
    enum Sea implements Province {
        BAR, NWG, NTH, SKA, HEL, BAL, BOT, ENG, IRI, NAO, MAO, WES, LYO, TYS, ION, ADR, AEG, EAS, BLA
    }
}
