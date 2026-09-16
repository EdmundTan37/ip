package alexa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests GUI-ready responses provided by {@link Alexa}. */
class AlexaTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_todoCommand_returnsAdditionConfirmation() {
        Alexa alexa = new Alexa(temporaryDirectory.resolve("data").resolve("alexa.txt"));

        assertEquals("Got it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.",
                alexa.getResponse("todo read book"));
    }

    @Test
    void getResponse_invalidCommand_returnsFormattedError() {
        Alexa alexa = new Alexa(temporaryDirectory.resolve("data").resolve("alexa.txt"));

        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", alexa.getResponse("dance"));
    }
}