package nodomain.mijnmooiewereld.orders.logger.acceptance;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import nodomain.mijnmooiewereld.orders.logger.Main;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebFileTest {
    private HttpServer server;
    private final int port = 8081;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new SimplePageHandler());
        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("Test server started at http://localhost:" + port);
    }

    @AfterEach
    void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Test server stopped.");
        }
    }

    @Test
    void testLogWriterFromWebpage() throws IOException {
        Path orderLogMD = Path.of("orderlog.md"), exampleOutputMd = Path.of("exampleOutput.md");
        Files.deleteIfExists(orderLogMD);
        Main.main("http://localhost:" + port);
        assertTrue(FileComparator.haveSameContent(orderLogMD, exampleOutputMd));
        Files.delete(orderLogMD);
    }

    static class SimplePageHandler implements HttpHandler {
        String json;
        {
            try {
                json = Files.readString(Path.of("exampleInput.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
