package code.java.view;

import code.java.controller.HomeController;
import code.java.controller.SceneController;
import code.java.network.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class HereToSlay extends Application {
    private static final String HOME_FXML_PATH = "/code/fxml/HomeMenu.fxml";

    public static void main(String[] args) {launch(args);}
    @Override
    public void start(Stage stage) throws Exception {

        SceneController.addScene(HOME_FXML_PATH, SceneController.HOME, new HomeController());

        SceneController.initialise(stage);
    }
}