package com.smartparking.frontend;

import com.smartparking.frontend.service.WalletService;
import com.smartparking.frontend.service.BookingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingViewController {

    @FXML private Label spotLabelText;
    @FXML private Label hourlyRateLabel;
    @FXML private Label totalPriceLabel;

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> timeSpinner;
    @FXML private TextField minuteField;
    @FXML private TextField durationField;
    @FXML private TextField cardHolderField;
    @FXML private TextField cardNumberField;
    @FXML private TextField cvvField;
    @FXML private TextField expiryField;


    @FXML private RadioButton rbCard;
    @FXML private RadioButton rbWallet;
    @FXML private VBox cardFormBox;
    @FXML private VBox walletInfoBox;
    @FXML private Label walletBalanceLabel;

    private ToggleGroup paymentGroup;
    private final WalletService walletService = new WalletService();
    private final BookingService bookingService = new BookingService();
    private Long spotId;
    private Long areaId;
    private double pricePerHour;

    public void initData(Long spotId, String spotLabel, double price, Long areaId, LocalDateTime startDateTime, int duration) {
        this.spotId = spotId;
        this.pricePerHour = price;
        this.areaId = areaId;

        spotLabelText.setText(spotLabel);
        hourlyRateLabel.setText("$" + price + " / hr");

        datePicker.setValue(startDateTime.toLocalDate());
        setupTimeSpinner();
        timeSpinner.setValue(startDateTime.getHour());

        minuteField.setText(String.format("%02d", startDateTime.getMinute()));
        addMinuteValidation();

        durationField.setText(String.valueOf(duration));

        if (UserSession.getInstance() != null) {
            cardHolderField.setText(UserSession.getInstance().getUsername());
        }

        durationField.textProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        calculateTotal();
        setupPaymentToggle();
    }

    private void addMinuteValidation() {
        minuteField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                minuteField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (minuteField.getText().length() > 2) {
                minuteField.setText(minuteField.getText().substring(0, 2));
            }
        });
    }

    private void setupPaymentToggle() {
        paymentGroup = new ToggleGroup();
        rbCard.setToggleGroup(paymentGroup);
        rbWallet.setToggleGroup(paymentGroup);

        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isWallet = rbWallet.isSelected();
            cardFormBox.setVisible(!isWallet);
            walletInfoBox.setVisible(isWallet);

            if (isWallet) {
                Long userId = UserSession.getInstance().getUserId();
                new Thread(() -> {
                    double balance = walletService.getBalance(userId);
                    Platform.runLater(() -> walletBalanceLabel.setText(String.format("Available: $%.2f", balance)));
                }).start();
            }
        });
    }

    private void setupTimeSpinner() {
        if(timeSpinner.getItems().isEmpty()){
            for (int i = 0; i < 24; i++) {
                timeSpinner.getItems().add(i);
            }
        }
    }

    private void calculateTotal() {
        try {
            int duration = Integer.parseInt(durationField.getText());
            if (duration < 1) duration = 1;
            double total = duration * pricePerHour;
            totalPriceLabel.setText(String.format("$%.2f", total));
        } catch (NumberFormatException e) {
            totalPriceLabel.setText("$-");
        }
    }

    @FXML
    public void handleSubmit() {
        try {
            if (datePicker.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Missing Info", "Please select a date.");
                return;
            }

            LocalDate date = datePicker.getValue();
            int hour = timeSpinner.getValue();

            int minute = Integer.parseInt(minuteField.getText());
            if (minute < 0 || minute > 59) {
                showAlert(Alert.AlertType.WARNING, "Invalid Time", "Minutes must be between 0 and 59.");
                return;
            }

            LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));
            int duration = Integer.parseInt(durationField.getText());

            String paymentMethod = rbWallet.isSelected() ? "WALLET" : "CARD";
            String cardNum = "";
            String cardHolder = "";
            String cvv = "";
            String expiry = "";

            if ("CARD".equals(paymentMethod)) {
                if (cardHolderField.getText().isEmpty() || cardNumberField.getText().isEmpty() ||
                        cvvField.getText().isEmpty() || expiryField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Missing Info", "Please fill in all card details.");
                    return;
                }
                cardNum = cardNumberField.getText();
                cardHolder = cardHolderField.getText();
                cvv = cvvField.getText();
                expiry = expiryField.getText();
            }

            Long userId = (UserSession.getInstance() != null) ? UserSession.getInstance().getUserId() : 1L;

            String response = bookingService.createBooking(userId, spotId, startDateTime, duration, cardNum, cardHolder, paymentMethod, cvv, expiry);

            if (!response.startsWith("Error")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", response);
                handleCancel();
            } else {
                showAlert(Alert.AlertType.ERROR, "Booking Failed", response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid data format.");
        }
    }

    @FXML
    public void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("spots-view.fxml"));
            Parent root = loader.load();

            ParkingSpotsController controller = loader.getController();
            controller.initData(areaId);

            Stage stage = (Stage) spotLabelText.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet(), getClass().getResource("styles.css").toExternalForm());
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