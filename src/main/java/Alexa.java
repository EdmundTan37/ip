package src.main.java;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * A chatbot that greets the user before ending the program.
 */
public class Alexa {
    /** A visual divider used to frame the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";
    private static ArrayList<New_task> tasks = new ArrayList<>();
    private static int task_counter = 1;
    record New_task(int num, String t) {}
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

        while (true) {
            String choice = input.nextLine();
            input_statement(choice);
            if (choice.equals("bye")) {
                break;
            }
        }


    }
    static void input_statement(String text) {
        New_task new_task = new New_task(task_counter++, text);
        if (text.equals("bye")) {
            System.out.println(DIVIDER);
            System.out.println("Bye. Hope to see you again soon!");
            System.out.println(DIVIDER);
        } else if (text.equals("list")) {
            System.out.println(DIVIDER);
            print_tasks();
            System.out.println(DIVIDER);
        } else {
            tasks.add(new_task);
            System.out.println(DIVIDER);
            System.out.println(new_task.num + ". " +  new_task.t);
            System.out.println(DIVIDER);
        }
    }

    static void print_tasks () {
        for  (New_task task : tasks) {
            System.out.println(task.num + ". " + task.t);
        }
    }
}
