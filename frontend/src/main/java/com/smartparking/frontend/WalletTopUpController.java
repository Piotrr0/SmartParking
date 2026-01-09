package com.smartparking.frontend;

import com.smartparking.frontend.service.WalletService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WalletTopUpController {

    @FXML private TextField amountField;
    @FXML private TextField cardHolderField;
    @FXML private TextField cardNumberField;
    @FXML private TextField cvvField;
    @FXML private Label errorLabel;

    private final WalletService walletService = new WalletService();
    private Runnable onSuccessCallback;

    public void setOnSuccessCallback(Runnable callback) {
        this.onSuccessCallback = callback;
    }

    public void initialize() {
        if (UserSession.getInstance() != null) {
            cardHolderField.setText(UserSession.getInstance().getUsername());
        }
    }

    @FXML
    public void handlePay() {
        errorLabel.setVisible(false);
        try {
            double amount = Double.parseDouble(amountField.getText());
            String cardNum = cardNumberField.getText();
            String holder = cardHolderField.getText();
            String cvv = cvvField.getText();

            if (cardNum.isEmpty() || holder.isEmpty() || cvv.isEmpty()) {
                showError("Please fill in all card details.");
                return;
            }

            Long userId = UserSession.getInstance().getUserId();

            new Thread(() -> {
                String result = walletService.topUp(userId, amount, cardNum, holder, cvv);
                Platform.runLater(() -> {
                    if ("Success".equals(result)) {
                        if (onSuccessCallback != null) onSuccessCallback.run();
                        handleCancel();
                    } else {
                        showError(result);
                    }
                });
            }).start();

        } catch (NumberFormatException e) {
            showError("Invalid amount format.");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) amountField.getScene().getWindow();
        stage.close();
    }
}