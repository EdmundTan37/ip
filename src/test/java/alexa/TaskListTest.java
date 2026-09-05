package alexa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task search behavior performed by {@link TaskList}. */
class TaskListTest {
    @Test
    void findTasks_matchingKeyword_returnsMatchesInListOrder() {
        TaskList tasks = new TaskList(List.of(
                new Todo("read book"),
                new Todo("buy groceries"),
                new Todo("return BOOK")));

        List<Task> matches = tasks.findTasks("book");

        assertEquals(List.of("[T][ ] read book", "[T][ ] return BOOK"),
                matches.stream().map(Task::toString).toList());
    }

    @Test
    void findTasks_noMatchingKeyword_returnsEmptyList() {
        TaskList tasks = new TaskList(List.of(new Todo("read book")));

        assertTrue(tasks.findTasks("meeting").isEmpty());
    }
}