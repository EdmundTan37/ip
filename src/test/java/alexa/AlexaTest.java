package alexa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    @Test
    void getResponse_duplicateTodo_rejectsAdditionAndPreservesSavedTasks() throws IOException {
        Path dataFile = temporaryDirectory.resolve("data").resolve("alexa.txt");
        Alexa alexa = new Alexa(dataFile);

        alexa.getResponse("todo read book");
        alexa.getResponse("mark 1");

        assertEquals("OOPS!!! This task already exists in task 1.", alexa.getResponse("todo read book"));
        assertEquals("Here are the tasks in your list:\n1.[T][X] read book", alexa.getResponse("list"));
        assertEquals(List.of("T | 1 | read book"), Files.readAllLines(dataFile));
    }

    @Test
    void getResponse_commandWithLeadingTrailingAndTabWhitespace_addsTask() {
        Alexa alexa = new Alexa(temporaryDirectory.resolve("data").resolve("alexa.txt"));

        assertEquals("Got it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.",
                alexa.getResponse("  todo\tread book  "));
    }
}
