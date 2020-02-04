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
            ui.showErrorMessage(e.getMessage());
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

    /**
     * Find out the type of command and execute it.
     *
     * @param input The line input by the user.
     * @param tasks TaskList object to acces to list of tasks.
     * @param storage Storage object.
     */
    public static void execcommand(String input, TaskList tasks, Ui ui, Storage storage) {
        // Split arguments to get the first index
        String[] arguments = input.split(" ");

        DukeCommand.valueOf(arguments[0].toUpperCase()).execute(input, tasks, ui, storage);
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
        ui.greetUser();

        Scanner sc = new Scanner(System.in);

        // Exits when the user types 'bye'
        String line = sc.nextLine();
        while (!line.equals("bye")) {
            // Check for any errors in the command
            // by passing command to Parser object
            // If no errors, method checkForError()
            // will returns true else return false
            if (new Parser(line, ui).checkForError(tasks))
                // Assumption : To add things into the list, the user has to
                // type a [command][description]. For example, to add
                // "read book" into the list, the user has to type
                // "todo read book" instead of just typing "read book"
                // as typing "read book" will cause the code to throw an exception
                execcommand(line, tasks, ui, storage);
            // Wait for next input command
            line = sc.nextLine();
        }

        // Say 'bye' to the user
        ui.exit();
    }

    /**
     * The main method of the class Duke.
     * @param args Unused.
     * @return Nothing.
     **/
    public static void main(String[] args) {
        new Duke("C:\\Users\\SOHNB101\\Documents\\myduke\\duke\\data\\duke.txt").run();
    }
}