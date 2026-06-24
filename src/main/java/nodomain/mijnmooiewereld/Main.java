package nodomain.mijnmooiewereld;

import nodomain.mijnmooiewereld.order.Order;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Main {
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
            writeOrders(OrderDao.ORDER_DAO.getAllFromSource(input), toOutput);
        }
    }

    static void writeOrders(Stream<Order> orders, PrintWriter output) {
        orders.collect(groupingBy(Order::owner)).forEach((key, value) -> {
            output.println("# " + key);
            value.stream().collect(groupingBy(Order::timeline)).values().forEach(ownedOrders -> {
                int currentTurn = ownedOrders.stream().mapToInt(Order::turn).max().orElseThrow();
                output.println("T" + ownedOrders.getFirst().timeline() + " " + Order.turnString(currentTurn) + ":");
                ownedOrders.stream()
                        .filter(order -> order.turn() == currentTurn)
                        .map(Order::printableString)
                        .forEach(output::println);
            });
            output.println();
        });
    }
}
