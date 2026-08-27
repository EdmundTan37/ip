package src.main.java;

import java.io.IOException;

/**
 * Coordinates task commands, storage, and the console user interface.
 */
public class Alexa {
    /** The task collection and its operations. */
    private static final TaskList TASKS = new TaskList();
    /** The component responsible for console interaction. */
    private static final Ui UI = new Ui();
    /** The component responsible for interpreting command text. */
    private static final Parser PARSER = new Parser();

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
        } else if (PARSER.isCommand(command, "todo")) {
            addTask(PARSER.parseTodo(PARSER.getArgument(command, "todo")));
        } else if (PARSER.isCommand(command, "deadline")) {
            addTask(PARSER.parseDeadline(PARSER.getArgument(command, "deadline")));
        } else if (PARSER.isCommand(command, "event")) {
            addTask(PARSER.parseEvent(PARSER.getArgument(command, "event")));
        } else if (PARSER.isCommand(command, "mark")) {
            updateTaskStatus(PARSER.getArgument(command, "mark"), true);
        } else if (PARSER.isCommand(command, "unmark")) {
            updateTaskStatus(PARSER.getArgument(command, "unmark"), false);
        } else if (PARSER.isCommand(command, "delete")) {
            deleteTask(PARSER.getArgument(command, "delete"));
        } else {
            throw new AlexaException("I'm sorry, but I don't know what that means :-(");
        }
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
        int taskNumber = PARSER.parseTaskNumber(numberText, command, TASKS.size());
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
        int taskNumber = PARSER.parseTaskNumber(numberText, "delete", TASKS.size());
        Task deletedTask = TASKS.remove(taskNumber - 1);
        saveTasks();
        UI.showTaskDeleted(deletedTask, TASKS.size());
    }

    /** Saves the current task list and turns write errors into a user-facing message. */
    private static void saveTasks() throws AlexaException {
        try {
            Storage.save(TASKS.asList());
        } catch (IOException exception) {
            throw new AlexaException("I could not save your tasks: " + exception.getMessage());
        }
    }
}