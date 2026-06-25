package nodomain.mijnmooiewereld.orders.logger;

import nodomain.mijnmooiewereld.orders.logger.order.Order;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class Main {
    private Main() {}

    private static final String INPUT_LOCATION_OF_JSON_FILE = """
            Please input the location on the local file system of the .json file containing the results to be turned into the orders log.
            This file can be obtained from the 5D diplomacy adjudicator.
            """;

    static void main(String[] args) throws IOException {
        Path input = Path.of(Arrays.stream(args).findFirst().orElseGet(() -> IO.readln(INPUT_LOCATION_OF_JSON_FILE)));
        if (! input.toString().endsWith(".json")) throw new IllegalArgumentException("A path to a Json file must be input");
        Path output = input.resolve("..")
                .resolve(input.getFileName().toString().replace(".json", ".md"))
                .normalize();
        Files.createFile(output);
        try (var toOutput = new PrintWriter(Files.newBufferedWriter(output))) {
            filterOrdersAndSortByPower(OrderDao.ORDER_DAO.getAllFromSource(input)).values()
                    .forEach(ownedOrders -> writeOrdersPerPower(toOutput, ownedOrders));
        }
    }

    static Map<String, List<Order>> filterOrdersAndSortByPower(List<Order> orders) {
        var currentBoards = orders.stream()
                .map(Order::board)
                .distinct()
                .collect(groupingBy(Location.Board::timeline,
                        maxBy(comparing(Location.Board::turn))));
        return orders.stream()
                .filter(o ->
                        currentBoards.get(o.timeline())
                                .filter(o.board()::equals)
                                .isPresent())
                .collect(groupingBy(Order::owner));
    }

    static void writeOrdersPerPower(PrintWriter output, List<Order> ownedOrders) {
        output.println("### " + ownedOrders.getFirst().unit().owner());
        ownedOrders.stream()
                .collect(groupingBy(Order::timeline))
                .values().forEach(orders -> {
                    output.println(orders.getFirst().board() + ":\\");
                    output.println(orders.stream()
                            .map(Order::printableString)
                            .collect(joining("\\"+System.lineSeparator())));
                    output.println();
                });
        output.println();
    }
}
