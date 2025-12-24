package com.smartparking.frontend;

import com.smartparking.frontend.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class LoginController {

    @FXML private VBox loginCard, registerCard;
    @FXML private TextField loginUser, regUser, regEmail;
    @FXML private PasswordField loginPass, regPass;
    @FXML private Label statusLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void doLogin() {
        processAuth("/login", loginUser.getText(), loginPass.getText(), null);
    }

    @FXML
    public void doRegister() {
        processAuth("/register", regUser.getText(), regPass.getText(), regEmail.getText());
    }

    private void processAuth(String endpoint, String user, String pass, String email) {
        statusLabel.setVisible(false);

        new Thread(() -> {
            boolean success = authService.authenticate(endpoint, user, pass, email);
            Platform.runLater(() -> {
                if (success) {
                    if (endpoint.equals("/register")) {
                        statusLabel.setText("Registration successful! Please login.");
                        statusLabel.setStyle("-fx-text-fill: green;");
                        statusLabel.setVisible(true);
                        switchToLogin();
                    } else {
                        openDashboard(user);
                    }
                } else {
                    statusLabel.setText("Invalid credentials or server error.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setVisible(true);
                }
            });
        }).start();
    }

    private void openDashboard(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.setUsername(username);

            Stage stage = (Stage) loginUser.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);

            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            stage.setScene(scene);
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error loading dashboard");
            statusLabel.setVisible(true);
        }
    }

    @FXML
    public void switchToRegister() {
        loginCard.setVisible(false);
        registerCard.setVisible(true);
        statusLabel.setVisible(false);
    }

    @FXML
    public void switchToLogin() {
        registerCard.setVisible(false);
        loginCard.setVisible(true);
        statusLabel.setVisible(false);
    }
}