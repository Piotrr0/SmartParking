package com.smartparking.frontend;

import com.smartparking.frontend.dto.UserDTO;
import com.smartparking.frontend.service.UserService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class ProfileController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private final UserService userService = new UserService();
    private Long currentUserId;

    public void initialize() {
        if (UserSession.getInstance() != null) {
            this.currentUserId = UserSession.getInstance().getUserId();
            loadUserData();
        }
    }

    private void loadUserData() {
        new Thread(() -> {
            UserDTO user = userService.getUserDetails(currentUserId);
            Platform.runLater(() -> {
                if (user != null) {
                    usernameField.setText(user.getUsername());
                    emailField.setText(user.getEmail());
                } else {
                    showAlert("Error", "Could not load user data.");
                }
            });
        }).start();
    }

    @FXML
    public void handleSave() {
        String newUsername = usernameField.getText();
        String newEmail = emailField.getText();
        String newPass = passwordField.getText();

        if (newUsername.isEmpty() || newEmail.isEmpty()) {
            statusLabel.setText("Username and Email cannot be empty.");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setVisible(true);
            return;
        }

        new Thread(() -> {
            String result = userService.updateUser(currentUserId, newUsername, newEmail, newPass);
            Platform.runLater(() -> {
                if ("Success".equals(result)) {
                    statusLabel.setText("Profile updated successfully!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setVisible(true);
                    passwordField.clear();

                    UserSession.setSession(currentUserId, newUsername);
                } else {
                    statusLabel.setText("Error: " + result);
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setVisible(true);
                }
            });
        }).start();
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();

            DashboardController dashboard = loader.getController();
            dashboard.setUsername(UserSession.getInstance().getUsername());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet(), getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}