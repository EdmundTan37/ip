package src.main.java;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Coordinates task commands, storage, and the console user interface.
 */
public class Alexa {
    /** The task collection and its operations. */
    private static final TaskList TASKS = new TaskList();
    /** The component responsible for console interaction. */
    private static final Ui UI = new Ui();

    /**
     * Starts Alexa, displays its greeting, and exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        UI.showGreeting();

        while (UI.hasNextCommand()) {
            String command = UI.readCommand();
            if (command.equals("bye")) {
                UI.showFarewell();
                return;
            }
            try {
                handleCommand(command);
            } catch (AlexaException exception) {
                UI.showError(exception.getMessage());
            }
        }
    }

    /** Handles a single command entered by the user. */
    private static void handleCommand(String command) throws AlexaException {
        if (command.equals("list")) {
            UI.showTaskList(TASKS);
        } else if (isCommand(command, "todo")) {
            addTask(new Todo(requireDescription(argumentAfter(command, "todo"), "todo")));
        } else if (isCommand(command, "deadline")) {
            addDeadline(argumentAfter(command, "deadline"));
        } else if (isCommand(command, "event")) {
            addEvent(argumentAfter(command, "event"));
        } else if (isCommand(command, "mark")) {
            updateTaskStatus(argumentAfter(command, "mark"), true);
        } else if (isCommand(command, "unmark")) {
            updateTaskStatus(argumentAfter(command, "unmark"), false);
        } else if (isCommand(command, "delete")) {
            deleteTask(argumentAfter(command, "delete"));
        } else {
            throw new AlexaException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /** Adds a deadline from text in the form {@code description /by yyyy-MM-dd}. */
    private static void addDeadline(String details) throws AlexaException {
        String[] parts = details.split(" /by ", 2);
        if (parts.length != 2) {
            throw new AlexaException("A deadline needs a description and date: deadline DESCRIPTION /by yyyy-MM-dd.");
        }
        addTask(new Deadline(requireDescription(parts[0], "deadline"), parseDate(parts[1], "deadline date")));
    }

    /** Adds an event from text in the form {@code description /from yyyy-MM-dd /to yyyy-MM-dd}. */
    private static void addEvent(String details) throws AlexaException {
        String[] fromParts = details.split(" /from ", 2);
        if (fromParts.length != 2) {
            throw new AlexaException("An event needs a description, start date, and end date: "
                    + "event DESCRIPTION /from yyyy-MM-dd /to yyyy-MM-dd.");
        }
        String[] toParts = fromParts[1].split(" /to ", 2);
        if (toParts.length != 2) {
            throw new AlexaException("An event needs an end date after /to.");
        }
        addTask(new Event(requireDescription(fromParts[0], "event"), parseDate(toParts[0], "event start date"),
                parseDate(toParts[1], "event end date")));
    }

    /** Stores a task, saves the updated list, and confirms the addition to the user. */
    private static void addTask(Task task) throws AlexaException {
        TASKS.add(task);
        saveTasks();
        UI.showTaskAdded(task, TASKS.size());
    }

    /** Updates the completion status of one task. */
    private static void updateTaskStatus(String numberText, boolean isDone) throws AlexaException {
        String command = isDone ? "mark" : "unmark";
        int taskNumber = getTaskNumber(numberText, command);
        Task task = TASKS.get(taskNumber - 1);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmark();
        }
        saveTasks();
        UI.showTaskStatus(task, isDone);
    }

    /** Removes one task from the list, saves it, and confirms the deletion. */
    private static void deleteTask(String numberText) throws AlexaException {
        int taskNumber = getTaskNumber(numberText, "delete");
        Task deletedTask = TASKS.remove(taskNumber - 1);
        saveTasks();
        UI.showTaskDeleted(deletedTask, TASKS.size());
    }

    /** Parses a date in the required {@code yyyy-MM-dd} command format. */
    private static LocalDate parseDate(String dateText, String dateName) throws AlexaException {
        try {
            return LocalDate.parse(requireValue(dateText, dateName));
        } catch (DateTimeParseException exception) {
            throw new AlexaException("The " + dateName + " must use yyyy-MM-dd, for example 2019-10-15.");
        }
    }

    /** Saves the current task list and turns write errors into a user-facing message. */
    private static void saveTasks() throws AlexaException {
        try {
            Storage.save(TASKS.asList());
        } catch (IOException exception) {
            throw new AlexaException("I could not save your tasks: " + exception.getMessage());
        }
    }

    /** Returns whether the input contains a command followed by whitespace or nothing. */
    private static boolean isCommand(String command, String commandWord) {
        return command.equals(commandWord) || command.startsWith(commandWord + " ");
    }

    /** Returns the text entered after a command word. */
    private static String argumentAfter(String command, String commandWord) {
        return command.substring(commandWord.length()).trim();
    }

    /** Requires a non-empty task description. */
    private static String requireDescription(String description, String taskType) throws AlexaException {
        if (description.trim().isEmpty()) {
            throw new AlexaException("The description of a " + taskType + " cannot be empty.");
        }
        return description.trim();
    }

    /** Requires a non-empty value for a task detail such as a deadline or time. */
    private static String requireValue(String value, String valueName) throws AlexaException {
        if (value.trim().isEmpty()) {
            throw new AlexaException("The " + valueName + " cannot be empty.");
        }
        return value.trim();
    }

    /** Parses and validates a one-based task number for a command. */
    private static int getTaskNumber(String numberText, String command) throws AlexaException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText.trim());
        } catch (NumberFormatException exception) {
            throw new AlexaException("Please provide a task number, for example: " + command + " 1.");
        }
        if (taskNumber < 1 || taskNumber > TASKS.size()) {
            throw new AlexaException("There is no task " + taskNumber + ". Use list to see the task numbers.");
        }
        return taskNumber;
    }
}