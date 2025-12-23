module com.smartparking.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.smartparking.frontend to javafx.fxml;
    opens com.smartparking.frontend.dto to com.fasterxml.jackson.databind;
    exports com.smartparking.frontend;
}