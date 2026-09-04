package alexa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests command recognition and argument validation performed by {@link Parser}. */
class ParserTest {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void isCommand_exactCommand_returnsTrue() {
        assertTrue(parser.isCommand("list", "list"));
    }

    @Test
    void isCommand_commandWithArgument_returnsTrue() {
        assertTrue(parser.isCommand("todo read book", "todo"));
    }

    @Test
    void isCommand_commandPrefixOnly_returnsFalse() {
        assertFalse(parser.isCommand("todone", "todo"));
    }

    @Test
    void getArgument_surroundingWhitespace_returnsTrimmedArgument() {
        assertEquals("read book", parser.getArgument("todo   read book   ", "todo"));
    }

    @Test
    void parseTodo_validDescription_createsTodo() throws AlexaException {
        Todo todo = parser.parseTodo("read book");

        assertEquals("[T][ ] read book", todo.toString());
        assertEquals("T | 0 | read book", todo.toStorageString());
    }

    @Test
    void parseTodo_blankDescription_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class, () -> parser.parseTodo("   "));

        assertEquals("The description of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    void parseDeadline_validIsoDate_createsDeadlineWithFriendlyDisplay() throws AlexaException {
        Deadline deadline = parser.parseDeadline("return book /by 2019-10-15");

        assertEquals("[D][ ] return book (by: Oct 15 2019)", deadline.toString());
        assertEquals("D | 0 | return book | 2019-10-15", deadline.toStorageString());
    }

    @Test
    void parseDeadline_missingDateMarker_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class,
                () -> parser.parseDeadline("return book 2019-10-15"));

        assertEquals("A deadline needs a description and date: deadline DESCRIPTION /by yyyy-MM-dd.",
                exception.getMessage());
    }

    @Test
    void parseDeadline_invalidDate_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class,
                () -> parser.parseDeadline("return book /by tomorrow"));

        assertEquals("The deadline date must use yyyy-MM-dd, for example 2019-10-15.", exception.getMessage());
    }

    @Test
    void parseEvent_validIsoDates_createsEventWithFriendlyDisplay() throws AlexaException {
        Event event = parser.parseEvent("project meeting /from 2019-10-16 /to 2019-10-17");

        assertEquals("[E][ ] project meeting (from: Oct 16 2019 to: Oct 17 2019)", event.toString());
        assertEquals("E | 0 | project meeting | 2019-10-16 | 2019-10-17", event.toStorageString());
    }

    @Test
    void parseEvent_missingEndDateMarker_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class,
                () -> parser.parseEvent("project meeting /from 2019-10-16"));

        assertEquals("An event needs an end date after /to.", exception.getMessage());
    }

    @Test
    void parseTaskNumber_validTrimmedNumber_returnsNumber() throws AlexaException {
        assertEquals(2, parser.parseTaskNumber(" 2 ", "mark", 3));
    }

    @Test
    void parseTaskNumber_nonNumericText_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class,
                () -> parser.parseTaskNumber("first", "mark", 3));

        assertEquals("Please provide a task number, for example: mark 1.", exception.getMessage());
    }

    @Test
    void parseTaskNumber_outsideTaskList_throwsException() {
        AlexaException exception = assertThrows(AlexaException.class,
                () -> parser.parseTaskNumber("4", "delete", 3));

        assertEquals("There is no task 4. Use list to see the task numbers.", exception.getMessage());
    }
}