package src.main.java;
import java.util.Scanner;
import java.util.ArrayList;
import src.main.java.Task;

/**
 * A chatbot that greets the user before ending the program.
 */
public class Alexa {
    /** A visual divider used to frame the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";
    private static ArrayList<Task> tasks = new ArrayList<>();
    static int task_counter = 1;

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
        if (text.equals("list")) {
            listText();
            return ;
        }
        if (text.equals("bye")) {
            byeText();
            return ;
        }

        String[] marked_task = text.split(" ", 2);

        if (marked_task[0].equals("mark")) {
            markTask(true, Integer.parseInt(marked_task[1]));
        } else if (marked_task[0].equals("unmark")) {
            markTask(false, Integer.parseInt(marked_task[1]));
        } else {
            Task new_task = new Task(task_counter++, false,text);
            tasks.add(new_task);
            System.out.println(DIVIDER);
            new_task.toString();
            System.out.println(DIVIDER);
        }
    }

    static void byeText() {
        System.out.println(DIVIDER);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

     static void listText() {
        System.out.println(DIVIDER);
        System.out.println("Here are the tasks in your list:");
        print_tasks();
        System.out.println(DIVIDER);
    }

     static void markTask(boolean mark, int position) {
        Task task = tasks.get(position - 1);
        if (mark) {
            task.changeCompleted();
            System.out.println(DIVIDER);
            System.out.println("Nice! I've marked this task as done:");
            task.toString();
            System.out.println(DIVIDER);
        } else {
            task.changeUncompleted();
            System.out.println(DIVIDER);
            System.out.println("Ok, I've marked this task as not done yet:");
            task.toString();
            System.out.println(DIVIDER);
        }
    }

     static void print_tasks() {
        for  (Task task : tasks) {
            task.toString();
        }
    }

}
