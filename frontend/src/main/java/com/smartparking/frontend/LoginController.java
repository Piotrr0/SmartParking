package com.smartparking.frontend;

import com.smartparking.frontend.service.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
                        System.out.println("LOGIN SUCCESS -> Load Dashboard");
                        // TODO: scene.setRoot(dashboardRoot);
                    }
                } else {
                    statusLabel.setText("Invalid credentials or server error.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setVisible(true);
                }
            });
        }).start();
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