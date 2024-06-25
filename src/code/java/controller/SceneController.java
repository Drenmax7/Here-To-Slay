package code.java.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class SceneController {
    // Constants

    public static final String WINDOW_TITLE = "Here To Slay";
    public static final String HOME = "Home";
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 800;


    /**
     * The list of the sub scenes that the application uses
     */
    private static final HashMap<String, SubScene> listScene = new HashMap<>();

    /**
     * The element on which scenes are placed
     */
    private static final BorderPane root = new BorderPane();

    private static Stage stage;

    public static void addScene(String path, String name, Object controller) throws IOException {
        // read xml file to get view
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(path));

        // Set controller
        fxmlLoader.setController(controller);

        VBox vBox = fxmlLoader.load();

        // Create the new scene and add it to the list
        SubScene scene = new SubScene(vBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        listScene.put(name,scene);
    }

    public static void initialise(Stage stage){
        SceneController.stage = stage;

        // initial scene
        root.setCenter(listScene.get(HOME));

        stage.setTitle(WINDOW_TITLE);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
