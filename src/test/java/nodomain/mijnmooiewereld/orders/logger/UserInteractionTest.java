package nodomain.mijnmooiewereld.orders.logger;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Due to the way that the IO class captures System.in, this class must be the only class
 * that runs tests anything related to System.in. It must also be set up before IO captures
 * System.in. All other tests may therefore not trigger any part of the application that uses IO input.
 */
public class UserInteractionTest {
    private final PrintStream originalOut = System.out;
    private static final InputStream originalIn = System.in;
    private ByteArrayOutputStream capturedOut;
    private static PipedOutputStream capturedIn;

    private final Path someMd = Path.of("someFile.md"), someJson = Path.of("someFile.json");

    @BeforeAll
    static void captureInStream() throws IOException {
        capturedIn = new PipedOutputStream();
        System.setIn(new PipedInputStream(capturedIn));
    }

    @AfterAll
    static void restoreInStream() throws IOException {
        capturedIn.close();
        System.setIn(originalIn);
    }


    @BeforeEach
    void captureOutStream() {
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
    }

    @AfterEach
    void restoreOutStream() throws IOException {
        System.setOut(originalOut);

        Files.deleteIfExists(someJson);
        Files.deleteIfExists(someMd);
    }

    @Test
    void givenNoCommandLineInputWhenTheUserGivesANonJsonFileThenAnErrorOccurs() throws IOException {
        writeInputLine("some/location.txt");

        IllegalArgumentException illegalInput = assertThrows(IllegalArgumentException.class, Main::main);
        assertEquals("A path to a Json file must be input", illegalInput.getMessage());

        String expectedOutput = Main.INPUT_LOCATION_OF_JSON_FILE;
        assertEquals(expectedOutput, capturedOut.toString());
    }

    @Test
    void givenNoCommandLineInputWhenTheUserGivesAJsonFileWhichDoesntExistThenAnErrorOccurs() throws IOException {
        Files.deleteIfExists(Path.of("some/location.json"));
        writeInputLine("some/location.json");

        assertThrows(IOException.class, Main::main);

        String expectedOutput = Main.INPUT_LOCATION_OF_JSON_FILE;
        assertEquals(expectedOutput, capturedOut.toString());
    }

    static final String MINIMAL_JSON = """
            {
                "iteration" : 0,
                "boards" : [],
                "orders" : []
            }
            """;

    @Test
    void givenMdFileAlreadyExistWhenTheUserGivesPermissionThenItIsDeletedBeforeTheProgramContinues() throws IOException {
        Files.createFile(someJson);
        Files.writeString(someJson, MINIMAL_JSON);
        Files.createFile(someMd);
        Files.writeString(someMd, "this file is non empty");
        assertFalse(Files.readString(someMd).isBlank());

        writeInputLine("Yes");

        Main.main("someFile.json");

        assertEquals("# Order log:", Files.readString(someMd));
    }

    private void writeInputLine(String text) throws IOException {
        capturedIn.write((text + System.lineSeparator()).getBytes());
        capturedIn.flush();
    }
}
