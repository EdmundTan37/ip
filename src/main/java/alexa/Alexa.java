package alexa;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Coordinates task commands, storage, and the console or graphical user interface.
 */
public class Alexa {
    /** The task collection and its operations. */
    private final TaskList tasks;
    /** The component responsible for console interaction and response formatting. */
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

    /** Runs Alexa's command loop in the console. */
    public void run() {
        ui.showGreeting();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                ui.showFarewell();
                return;
            }
            ui.showResponse(getResponse(command));
        }
    }

    /**
     * Returns Alexa's greeting for a graphical user interface.
     *
     * @return The greeting message.
     */
    public String getGreeting() {
        return ui.getGreetingMessage();
    }

    /**
     * Processes a command and returns the response without writing to standard output.
     *
     * @param command The command entered by the user.
     * @return The response that should be shown to the user.
     */
    public String getResponse(String command) {
        if (command.equals("bye")) {
            return ui.getFarewellMessage();
        }
        try {
            return handleCommand(command);
        } catch (AlexaException exception) {
            return ui.getErrorMessage(exception.getMessage());
        }
    }

    /** Starts Alexa's console interface using the standard relative data-file location. */
    public static void main(String[] args) {
        new Alexa(Path.of("data", "alexa.txt")).run();
    }

    /** Handles a single command and returns its user-facing response. */
    private String handleCommand(String command) throws AlexaException {
        if (command.equals("list")) {
            return ui.getTaskListMessage(tasks);
        } else if (parser.isCommand(command, "find")) {
            String keyword = parser.parseFindKeyword(parser.getArgument(command, "find"));
            return ui.getMatchingTasksMessage(tasks.findTasks(keyword));
        } else if (parser.isCommand(command, "todo")) {
            Task task = addTask(parser.parseTodo(parser.getArgument(command, "todo")));
            return ui.getTaskAddedMessage(task, tasks.size());
        } else if (parser.isCommand(command, "deadline")) {
            Task task = addTask(parser.parseDeadline(parser.getArgument(command, "deadline")));
            return ui.getTaskAddedMessage(task, tasks.size());
        } else if (parser.isCommand(command, "event")) {
            Task task = addTask(parser.parseEvent(parser.getArgument(command, "event")));
            return ui.getTaskAddedMessage(task, tasks.size());
        } else if (parser.isCommand(command, "mark")) {
            Task task = updateTaskStatus(parser.getArgument(command, "mark"), true);
            return ui.getTaskStatusMessage(task, true);
        } else if (parser.isCommand(command, "unmark")) {
            Task task = updateTaskStatus(parser.getArgument(command, "unmark"), false);
            return ui.getTaskStatusMessage(task, false);
        } else if (parser.isCommand(command, "delete")) {
            Task deletedTask = deleteTask(parser.getArgument(command, "delete"));
            return ui.getTaskDeletedMessage(deletedTask, tasks.size());
        } else {
            throw new AlexaException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /** Stores a task, saves the updated list, and returns the task. */
    private Task addTask(Task task) throws AlexaException {
        tasks.add(task);
        saveTasks();
        return task;
    }

    /** Updates the completion status of one task and returns it. */
    private Task updateTaskStatus(String numberText, boolean isDone) throws AlexaException {
        String command = isDone ? "mark" : "unmark";
        int taskNumber = parser.parseTaskNumber(numberText, command, tasks.size());
        Task task = tasks.get(taskNumber - 1);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmark();
        }
        saveTasks();
        return task;
    }

    /** Removes one task from the list, saves it, and returns the removed task. */
    private Task deleteTask(String numberText) throws AlexaException {
        int taskNumber = parser.parseTaskNumber(numberText, "delete", tasks.size());
        Task deletedTask = tasks.remove(taskNumber - 1);
        saveTasks();
        return deletedTask;
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