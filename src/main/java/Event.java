package src.main.java;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** A task with a start date and an end date. */
public class Event extends Task {
    /** The input format accepted by Alexa commands and used in saved data. */
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    /** The friendlier date format displayed to the user. */
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);
    private final LocalDate from;
    private final LocalDate to;

    /**
     * Creates an event task.
     *
     * @param description the task description
     * @param from the event start date
     * @param to the event end date
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toStorageString() {
        return "E | " + (isDone() ? "1" : "0") + " | " + getDescription()
                + " | " + from.format(INPUT_FORMAT) + " | " + to.format(INPUT_FORMAT);
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + getDescription()
                + " (from: " + from.format(DISPLAY_FORMAT) + " to: " + to.format(DISPLAY_FORMAT) + ")";
    }
}