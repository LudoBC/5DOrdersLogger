package nodomain.mijnmooiewereld.orders.logger;

import nodomain.mijnmooiewereld.orders.logger.order.Order;
import nodomain.mijnmooiewereld.utils.CheckedSupplier;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

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
        Path outputPath = null;
        PrintStream outputWriter;

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
        } else if (input.isBlank()) {
            inputStreamSupplier = () -> System.in;
        } else {
            throw new IllegalArgumentException("A path to a Json file must be input");
        }

        if (outputPath != null) {
            if (Files.exists(outputPath)) {
                String wantToDelete = IO.readln("A file already exists at "
                        + outputPath.toAbsolutePath()
                        + ". Would you like to remove it? [Y, n]");
                if (wantToDelete.isBlank() || List.of('y', 'Y').contains(wantToDelete.charAt(0))) {
                    Files.delete(outputPath);
                }
            }
            Files.createFile(outputPath);
            outputWriter = new PrintStream(Files.newOutputStream(outputPath));
        } else {
            outputWriter = System.out;
        }

        try (InputStream inputStream = inputStreamSupplier.get()) {
            List<Order> inputData = OrderDao.ORDER_DAO.getAllFromSource(inputStream);
            filterOrdersAndSortByPower(inputData, outputWriter).values()
                    .forEach(ownedOrders -> writeOrdersPerPower(outputWriter, ownedOrders));
        } finally {
            if (outputWriter != System.out) {
                outputWriter.close();
            }
        }
    }

    static Map<String, List<Order>> filterOrdersAndSortByPower(List<Order> orders, PrintStream outputWriter) {
        var currentBoards = orders.stream()
                .map(Order::board)
                .distinct()
                .collect(groupingBy(Location.Board::timeline,
                        maxBy(comparing(Location.Board::turn))));
        Location.Board furthersBoard = currentBoards.getOrDefault(1, Optional.empty())
                .orElse(new Location.Board(1900, Location.Board.Phase.WINTER, 1));
        outputWriter.print("# TURN " + (furthersBoard.turn() + 1) + " - " + furthersBoard.phase() + " " + furthersBoard.year());
        return orders.stream()
                .filter(o ->
                        currentBoards.get(o.timeline())
                                .filter(o.board()::equals)
                                .isPresent())
                .collect(groupingBy(Order::owner));
    }

    static void writeOrdersPerPower(PrintStream output, List<Order> ownedOrders) {
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
