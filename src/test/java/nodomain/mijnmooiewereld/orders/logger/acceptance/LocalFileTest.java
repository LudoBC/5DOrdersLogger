package nodomain.mijnmooiewereld.orders.logger.acceptance;

import nodomain.mijnmooiewereld.orders.logger.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocalFileTest {
    private static final InputStream originalIn = System.in;
    private static final PrintStream originalOut = System.out;

    private ByteArrayOutputStream capturedOut;

    static final Path
            exampleInputJson = Path.of("exampleInput.json"),
            exampleInputMD = Path.of("exampleInput.md"),
            exampleOutputMd = Path.of("exampleOutput.md");

    @Test
    void testLogWriterFromLocalFile() throws IOException {
        Files.deleteIfExists(exampleInputMD);
        Main.main("exampleInput.json");
        assertTrue(FileComparator.haveSameContent(exampleInputMD, exampleOutputMd));
    }

    @Test
    void testLogWriterFromSystemIn() throws IOException {
        Files.deleteIfExists(exampleInputMD);
        try (InputStream fileInput = Files.newInputStream(exampleInputJson)) {
            System.setIn(fileInput);
            Main.main(" ");
        }
        assertEquals(capturedOut.toString(), Files.readString(exampleOutputMd));
    }


    @BeforeEach
    void captureOutStream() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
    }

    @AfterEach
    void restoreOutStream() {
        System.setOut(originalOut);
    }

    @AfterAll
    static void deleteCreatedFile() throws IOException {
        Files.deleteIfExists(exampleInputMD);
        System.setIn(originalIn);
    }
}
