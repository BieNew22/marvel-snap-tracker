module com.example.marvelsnaptracker {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires io.github.cdimascio.dotenv.java;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires java.desktop;
    requires java.sql;

    opens com.example.marvelsnaptracker to javafx.fxml;
    exports com.example.marvelsnaptracker;
    exports com.example.marvelsnaptracker.controller;
    exports com.example.marvelsnaptracker.deck to com.fasterxml.jackson.databind;
    opens com.example.marvelsnaptracker.controller to javafx.fxml;
    exports com.example.marvelsnaptracker.card to com.fasterxml.jackson.databind;
    exports com.example.marvelsnaptracker.controller.normal;
    opens com.example.marvelsnaptracker.controller.normal to javafx.fxml;
}