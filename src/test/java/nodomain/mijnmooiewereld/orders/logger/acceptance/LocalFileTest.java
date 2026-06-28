package nodomain.mijnmooiewereld.orders.logger.acceptance;

import nodomain.mijnmooiewereld.orders.logger.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalFileTest {
    static final Path
            exampleInputMD = Path.of("exampleInput.md"),
            exampleOutputMd = Path.of("exampleOutput.md");

    @Test
    void testLogWriterFromLocalFile() throws IOException {
        Files.deleteIfExists(exampleInputMD);
        Main.main("exampleInput.json");
        assertTrue(FileComparator.haveSameContent(exampleInputMD, exampleOutputMd));
    }

    @AfterAll
    static void deleteCreatedFile() throws IOException {
        Files.deleteIfExists(exampleInputMD);
    }
}
