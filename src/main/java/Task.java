package src.main.java;

/**
 * Represents a task that can be completed or left incomplete.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description the task description
     */
    protected Task(String description) {
        this.description = description;
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns the status icon for this task.
     *
     * @return {@code X} when completed, otherwise a space
     */
    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the common description of this task.
     *
     * @return the task description
     */
    protected String getDescription() {
        return description;
    }

    /**
     * Returns this task in the format shown to the user.
     *
     * @return formatted task details
     */
    @Override
    public abstract String toString();
}
