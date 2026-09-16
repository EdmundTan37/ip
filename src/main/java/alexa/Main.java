package alexa;

import java.io.IOException;
import java.nio.file.Path;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** Provides Alexa's JavaFX application entry point. */
public class Main extends Application {
    /** The task application used to process GUI commands. */
    private final Alexa alexa = new Alexa(Path.of("data", "alexa.txt"));

    /**
     * Loads Alexa's main window and displays it.
     *
     * @param stage The primary JavaFX window.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            MainWindow mainWindow = loader.getController();
            mainWindow.setAlexa(alexa);

            stage.setTitle("Alexa");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load the Alexa user interface.", exception);
        }
    }
}