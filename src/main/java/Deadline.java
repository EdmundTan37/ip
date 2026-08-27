package src.main.java;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** A task that must be completed by a specified date. */
public class Deadline extends Task {
    /** The input format accepted by Alexa commands and used in saved data. */
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    /** The friendlier date format displayed to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate by;

    /**
     * Creates a deadline task.
     *
     * @param description the task description
     * @param by the deadline date
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toStorageString() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + by.format(INPUT_FORMAT);
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }
}