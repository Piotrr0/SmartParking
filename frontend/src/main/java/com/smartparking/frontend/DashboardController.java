package com.smartparking.frontend;

import org.kordamp.bootstrapfx.BootstrapFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane parkingGrid;

    public void initialize() {
    }

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome back, " + username);
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(
                    BootstrapFX.bootstrapFXStylesheet(),
                    ParkingApplication.class.getResource("styles.css").toExternalForm()
            );
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}