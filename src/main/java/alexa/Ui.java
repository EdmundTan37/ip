package alexa;

import java.util.List;
import java.util.Scanner;

/**
 * Handles Alexa's console input and all messages shown to the user.
 */
public class Ui {
    /** A visual divider used to frame chatbot messages. */
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner input;

    /** Creates a console user interface that reads commands from standard input. */
    public Ui() {
        input = new Scanner(System.in);
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return {@code true} when a command can be read
     */
    public boolean hasNextCommand() {
        return input.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the entered command
     */
    public String readCommand() {
        return input.nextLine();
    }

    /** Shows Alexa's greeting. */
    public void showGreeting() {
        System.out.println(DIVIDER);
        System.out.println("                 A L E X A");
        System.out.println("Hello! I'm Alexa.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Shows all tasks in the list. */
    public void showTaskList(TaskList tasks) {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            System.out.println((index + 1) + "." + tasks.get(index));
        }
        System.out.println(DIVIDER);
    }

    /** Shows every task whose description matches a find keyword. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println(DIVIDER);
        System.out.println("Here are the matching tasks in your list:");
        for (int index = 0; index < matchingTasks.size(); index++) {
            System.out.println((index + 1) + "." + matchingTasks.get(index));
        }
        System.out.println(DIVIDER);
    }

    /** Shows confirmation after a task is added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Shows confirmation after a task's completion status changes. */
    public void showTaskStatus(Task task, boolean isDone) {
        System.out.println(DIVIDER);
        System.out.println(isDone
                ? "Nice! I've marked this task as done:"
                : "Ok, I've marked this task as not done yet:");
        System.out.println("  " + task);
        System.out.println(DIVIDER);
    }

    /** Shows confirmation after a task is deleted. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(DIVIDER);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        System.out.println(DIVIDER);
    }

    /** Shows an error in Alexa's standard message frame. */
    public void showError(String message) {
        System.out.println(DIVIDER);
        System.out.println("OOPS!!! " + message);
        System.out.println(DIVIDER);
    }

    /** Shows a recoverable error when saved tasks cannot be loaded. */
    public void showLoadingError() {
        System.out.println(DIVIDER);
        System.out.println("OOPS!!! I could not load your saved tasks. Starting with an empty list.");
        System.out.println(DIVIDER);
    }

    /** Shows Alexa's farewell. */
    public void showFarewell() {
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
