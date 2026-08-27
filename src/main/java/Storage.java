package src.main.java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Saves Alexa's task list in a relative, platform-independent data file. */
public class Storage {
    /** The location of the data file relative to the project directory. */
    private static final Path DATA_FILE = Path.of("data", "alexa.txt");

    /**
     * Writes every task to the data file, creating its parent directory when needed.
     *
     * @param tasks the tasks to save
     * @throws IOException if the data file cannot be written
     */
    public static void save(List<Task> tasks) throws IOException {
        Path parentDirectory = DATA_FILE.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }
        List<String> taskLines = tasks.stream().map(Task::toStorageString).toList();
        Files.write(DATA_FILE, taskLines, StandardCharsets.UTF_8);
    }
}