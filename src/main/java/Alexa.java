package src.main.java;
import java.util.Scanner;

/**
 * A chatbot that greets the user before ending the program.
 */
public class Alexa {
    /** A visual divider used to frame the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";
    /** Maximum number of tasks Alexa stores during one run. */
    private static final int MAX_TASKS = 100;
    /** The task list. Each item can be any subtype of {@link Task}. */
    private static final Task[] TASKS = new Task[MAX_TASKS];
    /** Number of tasks currently stored in {@link #TASKS}. */
    private static int taskCount;

    /**
     * Starts Alexa, displays its greeting, and exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println(DIVIDER);
        System.out.println("                 A L E X A");
        System.out.println("Hello! I'm Alexa.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);

        while (input.hasNextLine()) {
            String command = input.nextLine();
            if (command.equals("bye")) {
                printFarewell();
                return;
            }
            try {
                handleCommand(command);
            } catch (AlexaException exception) {
                printError(exception.getMessage());
            }
        }
    }

    /** Handles a single command entered by the user. */
    private static void handleCommand(String command) throws AlexaException {
        if (command.equals("list")) {
            printTaskList();
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
        } else {
            throw new AlexaException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /** Adds a deadline from text in the form {@code description /by deadline}. */
    private static void addDeadline(String details) throws AlexaException {
        String[] parts = details.split(" /by ", 2);
        if (parts.length != 2) {
            throw new AlexaException("A deadline needs a description and a due date: deadline DESCRIPTION /by DATE.");
        }
        addTask(new Deadline(requireDescription(parts[0], "deadline"), requireValue(parts[1], "due date")));
    }

    /** Adds an event from text in the form {@code description /from start /to end}. */
    private static void addEvent(String details) throws AlexaException {
        String[] fromParts = details.split(" /from ", 2);
        if (fromParts.length != 2) {
            throw new AlexaException("An event needs a description, start time, and end time: "
                    + "event DESCRIPTION /from START /to END.");
        }
        String[] toParts = fromParts[1].split(" /to ", 2);
        if (toParts.length != 2) {
            throw new AlexaException("An event needs an end time after /to.");
        }
        addTask(new Event(requireDescription(fromParts[0], "event"), requireValue(toParts[0], "start time"),
                requireValue(toParts[1], "end time")));
    }

    /** Stores a task and confirms the addition to the user. */
    private static void addTask(Task task) throws AlexaException {
        if (taskCount == MAX_TASKS) {
            throw new AlexaException("Your task list is full. Delete a task before adding another one.");
        }
        TASKS[taskCount++] = task;
        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Prints every task currently stored. */
    private static void printTaskList() {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < taskCount; index++) {
            System.out.println((index + 1) + "." + TASKS[index]);
        }
        System.out.println(DIVIDER);
    }

    /** Updates the completion status of one task. */
    private static void updateTaskStatus(String numberText, boolean isDone) throws AlexaException {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText.trim());
        } catch (NumberFormatException exception) {
            throw new AlexaException("Please provide a task number, for example: mark 1.");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new AlexaException("There is no task " + taskNumber + ". Use list to see the task numbers.");
        }
        Task task = TASKS[taskNumber - 1];
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmark();
        }
        System.out.println(DIVIDER);
        System.out.println(isDone ? "Nice! I've marked this task as done:"
                                  : "Ok, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(DIVIDER);
    }
    /** Prints Alexa's farewell. */
    private static void printFarewell() {
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
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

    /** Prints an error in Alexa's standard message frame. */
    private static void printError(String message) {
        System.out.println(DIVIDER);
        System.out.println("OOPS!!! " + message);
        System.out.println(DIVIDER);
    }
}
