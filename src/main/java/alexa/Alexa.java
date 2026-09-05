package alexa;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Coordinates task commands, storage, and the console user interface.
 */
public class Alexa {
    /** The task collection and its operations. */
    private final TaskList tasks;
    /** The component responsible for console interaction. */
    private final Ui ui;
    /** The component responsible for interpreting command text. */
    private final Parser parser;
    /** The component responsible for persistent task data. */
    private final Storage storage;

    /**
     * Creates Alexa with task data stored at the supplied path.
     *
     * @param dataFile The relative location of Alexa's task data file.
     */
    public Alexa(Path dataFile) {
        ui = new Ui();
        parser = new Parser();
        storage = new Storage(dataFile);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (IOException exception) {
            ui.showLoadingError();
            loadedTasks = new TaskList();
        }
        tasks = loadedTasks;
    }

    /** Runs Alexa's command loop. */
    public void run() {
        ui.showGreeting();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                ui.showFarewell();
                return;
            }
            try {
                handleCommand(command);
            } catch (AlexaException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }

    /** Starts Alexa using the standard relative data-file location. */
    public static void main(String[] args) {
        new Alexa(Path.of("data", "alexa.txt")).run();
    }

    /** Handles a single command entered by the user. */
    private void handleCommand(String command) throws AlexaException {
        if (command.equals("list")) {
            ui.showTaskList(tasks);
        } else if (parser.isCommand(command, "find")) {
            ui.showMatchingTasks(tasks.findTasks(parser.parseFindKeyword(parser.getArgument(command, "find"))));
        } else if (parser.isCommand(command, "todo")) {
            addTask(parser.parseTodo(parser.getArgument(command, "todo")));
        } else if (parser.isCommand(command, "deadline")) {
            addTask(parser.parseDeadline(parser.getArgument(command, "deadline")));
        } else if (parser.isCommand(command, "event")) {
            addTask(parser.parseEvent(parser.getArgument(command, "event")));
        } else if (parser.isCommand(command, "mark")) {
            updateTaskStatus(parser.getArgument(command, "mark"), true);
        } else if (parser.isCommand(command, "unmark")) {
            updateTaskStatus(parser.getArgument(command, "unmark"), false);
        } else if (parser.isCommand(command, "delete")) {
            deleteTask(parser.getArgument(command, "delete"));
        } else {
            throw new AlexaException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /** Stores a task, saves the updated list, and confirms the addition to the user. */
    private void addTask(Task task) throws AlexaException {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded(task, tasks.size());
    }

    /** Updates the completion status of one task. */
    private void updateTaskStatus(String numberText, boolean isDone) throws AlexaException {
        String command = isDone ? "mark" : "unmark";
        int taskNumber = parser.parseTaskNumber(numberText, command, tasks.size());
        Task task = tasks.get(taskNumber - 1);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmark();
        }
        saveTasks();
        ui.showTaskStatus(task, isDone);
    }

    /** Removes one task from the list, saves it, and confirms the deletion. */
    private void deleteTask(String numberText) throws AlexaException {
        int taskNumber = parser.parseTaskNumber(numberText, "delete", tasks.size());
        Task deletedTask = tasks.remove(taskNumber - 1);
        saveTasks();
        ui.showTaskDeleted(deletedTask, tasks.size());
    }

    /** Saves the current task list and turns write errors into a user-facing message. */
    private void saveTasks() throws AlexaException {
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            throw new AlexaException("I could not save your tasks: " + exception.getMessage());
        }
    }
}
