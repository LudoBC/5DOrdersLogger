package nodomain.mijnmooiewereld.orders.logger;

import nodomain.mijnmooiewereld.orders.logger.order.Order;
import nodomain.mijnmooiewereld.utils.CheckedSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class Main {
    private Main() {}

    static final String INPUT_LOCATION_OF_JSON_FILE = """
            Please input the location of the .json file containing the results to be turned into the orders log.
            The location must either a path on the local file system, or a web page.
            The correct file can be obtained from the 5D diplomacy adjudicator.
            """;

    public static void main(String... args) throws IOException {
        String input = Arrays.stream(args).findFirst().orElseGet(() -> IO.readln(INPUT_LOCATION_OF_JSON_FILE));
        CheckedSupplier<InputStream, IOException> inputStreamSupplier;
        Path outputPath;

        if (input.startsWith("http")) {
            URL url = new URL(input);
            inputStreamSupplier = url::openStream;
            outputPath = Path.of("orderlog.md");
        } else if (input.endsWith("json")) {
            Path inputPath = Path.of(input);
            inputStreamSupplier = () -> Files.newInputStream(inputPath);
            outputPath = inputPath.resolve("..")
                    .resolve(inputPath.getFileName().toString().replace(".json", ".md"))
                    .normalize();
        } else {
            throw new IllegalArgumentException("A path to a Json file must be input");
        }

        if (Files.exists(outputPath)) {
            String wantToDelete = IO.readln("A file already exists at "
                    + outputPath.toAbsolutePath()
                    + ". Would you like to remove it? [Y, n]");
            if (wantToDelete.isBlank() || List.of('y', 'Y').contains(wantToDelete.charAt(0))) {
                Files.delete(outputPath);
            }
        }
        Files.createFile(outputPath);

        try (
                PrintWriter outputWriter = new PrintWriter(Files.newBufferedWriter(outputPath));
                InputStream inputStream = inputStreamSupplier.get()
        ) {
            List<Order> inputData = OrderDao.ORDER_DAO.getAllFromSource(inputStream);
            outputWriter.print("# Order log:");
            filterOrdersAndSortByPower(inputData).values()
                    .forEach(ownedOrders -> writeOrdersPerPower(outputWriter, ownedOrders));
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
        output.print(System.lineSeparator() + "## " + ownedOrders.getFirst().unit().owner().toUpperCase());
        ownedOrders.stream()
                .collect(groupingBy(Order::timeline))
                .values().stream()
                .peek(orders -> output.println(System.lineSeparator() + "### " + orders.getFirst().board() + ":"))
                .flatMap(List::stream)
                .map(Order::printableString)
                .map("- "::concat)
                .forEachOrdered(output::println);
    }
}
