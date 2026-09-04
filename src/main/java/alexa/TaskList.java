package alexa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns Alexa's ordered collection of tasks and provides operations on that collection.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks in their existing order.
     *
     * @param tasks the tasks with which to initialise this list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at a zero-based position.
     *
     * @param index the zero-based task position
     * @return the requested task
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at a zero-based position.
     *
     * @param index the zero-based task position
     * @return the removed task
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view of the tasks for persistence.
     *
     * @return the tasks in their current order
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }
}