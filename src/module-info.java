module Here.to.slay {
    requires javafx.controls;
    requires javafx.fxml;

    exports code.java.network;
    opens code.java.network to javafx.fxml;

    exports code.java.view;
    opens code.java.view to javafx.fxml;

    exports code.java.controller;
    opens code.java.controller to javafx.fxml;
}