package com.smartparking.frontend;

import com.smartparking.frontend.dto.UserProfileResponse;
import com.smartparking.frontend.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private PasswordField newPasswordField;
    @FXML private Circle avatarCircle;
    @FXML private Text defaultIcon;

    private final UserService userService = new UserService();
    private Long currentUserId;

    @FXML
    public void initialize() {
        if (UserSession.getInstance() != null) {
            currentUserId = UserSession.getInstance().getUserId();
            loadProfile();
        }
    }

    private void loadProfile() {
        UserProfileResponse user = userService.getUserProfile(currentUserId);
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            emailLabel.setText(user.getEmail());

            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                try {
                    byte[] imageBytes = Base64.getDecoder().decode(user.getAvatar());
                    Image img = new Image(new ByteArrayInputStream(imageBytes));
                    avatarCircle.setFill(new ImagePattern(img));
                    defaultIcon.setVisible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                defaultIcon.setVisible(true);
            }
        }
    }

    @FXML
    public void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(usernameLabel.getScene().getWindow());
        if (selectedFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                String base64Encoded = Base64.getEncoder().encodeToString(fileContent);

                if (userService.updateAvatar(currentUserId, base64Encoded)) {
                    Image img = new Image(new ByteArrayInputStream(fileContent));
                    avatarCircle.setFill(new ImagePattern(img));
                    defaultIcon.setVisible(false);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile photo updated!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to upload photo.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleChangePassword() {
        String newPass = newPasswordField.getText();
        if (newPass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Password cannot be empty.");
            return;
        }

        if (userService.changePassword(currentUserId, newPass)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
            newPasswordField.clear();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to change password.");
        }
    }

    @FXML
    public void handleDeleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you absolutely sure?");
        alert.setContentText("This action cannot be undone. All your bookings will be deleted.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (userService.deleteAccount(currentUserId)) {
                UserSession.cleanUserSession();
                loadScene("login-view.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete account.");
            }
        }
    }

    @FXML
    public void handleBack() {
        loadScene("dashboard-view.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            if (fxmlFile.equals("dashboard-view.fxml")) {
                DashboardController controller = loader.getController();
                if (UserSession.getInstance() != null) {
                    controller.setUsername(UserSession.getInstance().getUsername());
                }
            }

            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            if (!fxmlFile.equals("login-view.fxml")) {
                scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet(), getClass().getResource("styles.css").toExternalForm());
            } else {
                scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            }

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}