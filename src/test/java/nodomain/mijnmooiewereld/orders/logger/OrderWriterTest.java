package nodomain.mijnmooiewereld.orders.logger;

import nodomain.mijnmooiewereld.orders.logger.order.Order;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    void testWriteOrders() {
        Path jsonPath = Path.of("src/test/resources/order.json");

        List<Order> ordersList = OrderDao.ORDER_DAO.getAllFromSource(jsonPath);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Main.writeOrders(ordersList, pw);

        String output = normalize(sw.toString());

        List<String> expectedBlocks = Stream.of(
                """
                ### Russia
                T1 F1901:\\
                F Sev S Ukr - Rum
                
                T2 F1901:\\
                *F Sev - Bla*
                """,
                """
                ### England
                T1 F1901:\\
                F Nth C T2 F1901 Lon - T1 F1901 Bel
                
                T2 F1901:\\
                A Lon - T1 F1901 Bel
                """
        ).map(this::normalize).toList();

        expectedBlocks.forEach(block ->
                assertTrue(output.contains(block),
                        () -> "Expected block not found:\n" + block + "\nActual output:\n" + output)
        );
    }
}

