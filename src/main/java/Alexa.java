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
            handleCommand(command);
        }
    }

    /** Handles a single command entered by the user. */
    private static void handleCommand(String command) {
        if (command.equals("list")) {
            printTaskList();
        } else if (command.startsWith("todo ")) {
            addTask(new Todo(command.substring("todo ".length())));
        } else if (command.startsWith("deadline ")) {
            addDeadline(command.substring("deadline ".length()));
        } else if (command.startsWith("event ")) {
            addEvent(command.substring("event ".length()));
        } else if (command.startsWith("mark ")) {
            updateTaskStatus(command.substring("mark ".length()), true);
        } else if (command.startsWith("unmark ")) {
            updateTaskStatus(command.substring("unmark ".length()), false);
        }
    }

    /** Adds a deadline from text in the form {@code description /by deadline}. */
    private static void addDeadline(String details) {
        String[] parts = details.split(" /by ", 2);
        if (parts.length == 2) {
            addTask(new Deadline(parts[0], parts[1]));
        }
    }

    /** Adds an event from text in the form {@code description /from start /to end}. */
    private static void addEvent(String details) {
        String[] fromParts = details.split(" /from ", 2);
        if (fromParts.length != 2) {
            return;
        }
        String[] toParts = fromParts[1].split(" /to ", 2);
        if (toParts.length == 2) {
            addTask(new Event(fromParts[0], toParts[0], toParts[1]));
        }
    }

    /** Stores a task and confirms the addition to the user. */
    private static void addTask(Task task) {
        if (taskCount == MAX_TASKS) {
            return;
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
    private static void updateTaskStatus(String numberText, boolean isDone) {
        int taskNumber = Integer.parseInt(numberText);
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

}
