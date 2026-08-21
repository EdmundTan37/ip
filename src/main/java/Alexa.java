package src.main.java;
import java.util.Scanner;

/**
 * A chatbot that greets the user before ending the program.
 */
public class Alexa {
    /** A visual divider used to frame the chatbot's messages. */
    private static final String DIVIDER = "____________________________________________________________";

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
//        System.out.println("Bye. Hope to see you again soon!");
//        System.out.println(DIVIDER);

        while (true) {
//            System.out.println("What can I do for you?");
            String choice = input.nextLine();
            if (choice.equals("bye")) {
                input_statement(choice);
                break;
            } else {
                input_statement(choice);
            }

        }


    }
    static void input_statement(String text) {
        if (text.equals("bye")) {
            System.out.println("Bye. Hope to see you again soon!");
        } else {
            System.out.println(DIVIDER);
            System.out.println(text);
            System.out.println(DIVIDER);
        }
    }
}
