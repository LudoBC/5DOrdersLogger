package nodomain.mijnmooiewereld.orders.logger;

import nodomain.mijnmooiewereld.orders.logger.order.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderWriterTest {

    private String normalize(String s) {
        return s.replace("\r\n", "\n") // normalize Windows line endings
                .replace("\r", "\n")   // normalize old Mac line endings
                .lines()
                .map(String::stripTrailing) // remove trailing spaces
                .collect(Collectors.joining("\n"))
                .trim(); // remove leading/trailing blank lines
    }

    @Test
    void testWriteOrders() throws IOException {
        Path jsonPath = Path.of("src/test/resources/order.json");

        List<Order> ordersList = OrderDao.ORDER_DAO.getAllFromSource(Files.newInputStream(jsonPath));
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var printStream = new PrintStream(byteArrayOutputStream);

        Main.filterOrdersAndSortByPower(ordersList).values()
                .forEach(ownedOrders -> Main.writeOrdersPerPower(printStream, ownedOrders));

        String output = normalize(byteArrayOutputStream.toString());

        List<String> expectedBlocks = Stream.of(
                """
                ## RUSSIA
                ### T1S'02:
                - A Ukr - Rum
                - F Sev S A Ukr - Rum
                
                ### T2F'01:
                - *F Sev - Bla*
                - *A Ukr S T2S'02 A Ukr - T2S'02 Rum*
                """,
                """
                ## ENGLAND
                ### T1S'02:
                - F Nth C T2F'01 A Yor - T1F'01 Bel
                
                ### T2F'01:
                - A Yor - T1F'01 Bel
                """
        ).map(this::normalize).toList();

        expectedBlocks.forEach(block ->
                assertTrue(output.contains(block),
                        () -> "Expected block not found:\n" + block + "\nActual output:\n" + output)
        );
    }
}

