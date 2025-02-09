import javafx.application.Application; // Import for JavaFX application structure.
import javafx.stage.Stage; // Import for handling the primary stage in a JavaFX app.
import javafx.scene.image.Image; // Import for using image files in JavaFX.
import java.util.List; // Import for using lists in Java.
import java.util.ArrayList; // Import for using resizable-array implementation of the List interface.
import java.io.File; // Import for handling file operations.

/**
 * Main class that initializes a JavaFX application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application and sets up the initial scene.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        Background background = new Background(primaryStage);
    }

    /**
     * The main entry point for all JavaFX applications.
     *
     * @param args the command line arguments passed to the application.
     *             An application may get these parameters using the getParameters() method.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads all PNG images from a specified folder path into a list of Image objects.
     *
     * @param folderPath The directory path from which images are to be loaded.
     * @return List of Image objects containing all the PNG images from the directory.
     */
    public static List<Image> loadImagesFromFolder(String folderPath) {
        List<Image> images = new ArrayList<>();
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) { // Checks if the folder exists and is a directory
            File[] listOfFiles = folder.listFiles(); // Lists all files in the directory
            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                        images.add(new Image(file.toURI().toString())); // Creates an Image object from the file URI
                    }
                }
            } else {
                System.err.println("No files found in folder: " + folderPath);
            }
        } else {
            System.err.println("Folder not found or is not a directory: " + folderPath);
        }
        return images;
    }
}
