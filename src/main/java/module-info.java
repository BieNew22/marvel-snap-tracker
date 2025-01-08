module com.example.marvelsnaptracker {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires io.github.cdimascio.dotenv.java;
    requires com.fasterxml.jackson.databind;
    requires static lombok;
    requires java.desktop;
    requires java.sql;

    opens com.example.marvelsnaptracker to javafx.fxml;
    exports com.example.marvelsnaptracker;
    exports com.example.marvelsnaptracker.contoller;
    opens com.example.marvelsnaptracker.contoller to javafx.fxml;
}