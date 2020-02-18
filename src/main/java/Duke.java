import javafx.fxml.FXML;
import java.io.IOException;
import java.util.Scanner;

/**
 *  Duke Class.
 *  The main class to run the whole code.
 *
 * @author Amos Cheong
 */
public class Duke {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private String FilePath;

    public Duke() {}

    public Duke(String filePath) {
        // Initialize the taskList from Storage class
        FilePath = filePath;
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load(), ui, storage);
        } catch (IOException e) {
            dukeAnswer(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    @FXML
    public String getResponse(String input) {
        return "Duke heard: " + input;
    }

    public TaskList getTaskList() {
        return this.tasks;
    }

    public Storage getStorage() {
        return this.storage;
    }
    /**
     *  Handles message that is parsed in for Duke to answer.
     * @param message The message for Duke to reply to user
     */
    public void dukeAnswer(String message) {
        System.out.println("_______________________________________________________" +
                "\n" + message + "\n" +
                "_______________________________________________________");
    }
    /**
     * Find out the type of command and execute it.
     *
     * @param input The line input by the user.
     * @param tasks TaskList object to acces to list of tasks.
     * @param storage Storage object.
     * @return String message that Duke has to tell the user
     */
    public static String executeCommmand(String input, TaskList tasks, Ui ui, Storage storage) {
        // Split arguments to get the first index
        String[] arguments = input.split(" ");

        return DukeCommand.valueOf(arguments[0].toUpperCase()).execute(input, tasks, ui, storage);
    }

    /**
     * A method to get the file path from the Duke object.
     *
     * @return String The path of the file or the file location.
     */
    public String getFilePath() {
        return this.FilePath;
    }

    /**
     * Runs the whole code.
     */
    public void run() {
        // Greet the user
        this.dukeAnswer(ui.welcomeMessage());

        Scanner sc = new Scanner(System.in);

        // Assumption : To add things into the list, the user has to
        // type a [command][description]. For example, to add
        // "read book" into the list, the user has to type
        // "todo read book" instead of just typing "read book"
        // as typing "read book" will cause the code to throw an exception
        // Exits when the user types 'bye'
        String line = sc.nextLine();
        while (!line.equals("bye")) {
            // Check for any errors in the command
            // by passing command to Parser object
            // If no errors, method detectForError()
            // will returns an empty String
            String ErrorMessage = new Parser(line, ui).detectError(tasks);

            if (ErrorMessage.equals("")) {
                // Run this line of code when there is no error with the command
                // But when specific tasks (Event/Deadline) is added, the date
                // and time will be validated here. Therefore, it will still
                // print out error code when the date or time format input
                // is incorrect
                 this.dukeAnswer(executeCommmand(line, tasks, ui, storage));
            } else {
                // Invalid command
                this.dukeAnswer(ErrorMessage);
            }

            // Wait for next input command
            line = sc.nextLine();
        }
            try {
                storage.store(tasks, ui);
            } catch (IOException ioex) {
                dukeAnswer(ioex.getMessage());
            } finally {
                // Say 'bye' to the user
                dukeAnswer(ui.exitMessage());
            }

    }

    /**
     * The main method of the class Duke.
     * @param args Unused.
     * @return Nothing.
     **/
    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
}