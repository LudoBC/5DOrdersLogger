package nodomain.mijnmooiewereld.orders.logger.acceptance;

import nodomain.mijnmooiewereld.orders.logger.Main;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalFileTest {
    @Test
    void testLogWriterFromLocalFile() throws IOException {
        Path exampleInputMD = Path.of("exampleInput.md"), exampleOutputMd = Path.of("exampleOutput.md");
        Files.deleteIfExists(exampleInputMD);
        Main.main("exampleInput.json");
        assertTrue(FileComparator.haveSameContent(exampleInputMD, exampleOutputMd));
        Files.delete(exampleInputMD);
        assertFalse(Files.exists(exampleInputMD));
    }
}
