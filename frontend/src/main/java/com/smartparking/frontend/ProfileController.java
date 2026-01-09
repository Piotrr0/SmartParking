package com.smartparking.frontend;

import com.smartparking.frontend.dto.UserDTO;
import com.smartparking.frontend.service.UserService;
import com.smartparking.frontend.service.WalletService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class ProfileController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField topUpField;

    private final WalletService walletService = new WalletService();
    private final UserService userService = new UserService();
    private Long currentUserId;

    public void initialize() {
        if (UserSession.getInstance() != null) {
            this.currentUserId = UserSession.getInstance().getUserId();
            loadUserData();
            loadWalletData();
        }
    }

    private void loadWalletData() {
        new Thread(() -> {
            double balance = walletService.getBalance(currentUserId);
            Platform.runLater(() -> balanceLabel.setText(String.format("$%.2f", balance)));
        }).start();
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
    public void handleTopUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("wallet-topup-view.fxml"));
            Parent root = loader.load();

            WalletTopUpController controller = loader.getController();
            controller.setOnSuccessCallback(this::loadWalletData);

            Stage stage = new Stage();
            stage.setTitle("Add Funds");
            stage.setScene(new Scene(root));
            stage.getScene().getStylesheets().addAll(
                    BootstrapFX.bootstrapFXStylesheet(),
                    getClass().getResource("styles.css").toExternalForm()
            );

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(balanceLabel.getScene().getWindow());

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open payment window.");
        }
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