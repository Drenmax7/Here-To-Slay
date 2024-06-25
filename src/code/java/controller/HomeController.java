package code.java.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;

public class HomeController {
    @FXML
    ImageView testcarte;
    @FXML
    public void initialize() throws Exception {
        /*
        File file = new File("src/code/java/controller/DW$Big Buckley.png");
        Image image = new Image(file.toURI().toString());
        testcarte.setImage(image);
        testcarte.onMouseEnteredProperty().set((MouseEvent t) -> {
            System.out.println("clique");
        });*/
    }
}
