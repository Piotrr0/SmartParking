package com.smartparking.frontend;

import com.smartparking.frontend.dto.ParkingAreaRequest;
import com.smartparking.frontend.service.ParkingService;
import com.smartparking.frontend.dto.ParkingSpotRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ParkingSpotsController {

    @FXML private Text areaNameText;
    @FXML private Label areaCityLabel;
    @FXML private FlowPane spotsContainer;

    @FXML private DatePicker dateFilter;
    @FXML private ComboBox<Integer> timeFilter;
    @FXML private TextField durationFilter;

    private final ParkingService parkingService = new ParkingService();
    private Long currentAreaId;

    public void initData(Long areaId) {
        this.currentAreaId = areaId;

        dateFilter.setValue(LocalDate.now());
        for (int i = 0; i < 24; i++) timeFilter.getItems().add(i);

        int nextHour = LocalTime.now().plusHours(1).getHour();
        timeFilter.setValue(nextHour);

        handleCheckAvailability();
    }

    @FXML
    public void handleCheckAvailability() {
        if (dateFilter.getValue() == null || timeFilter.getValue() == null || durationFilter.getText().isEmpty()) {
            return;
        }

        LocalDateTime selectedStart = LocalDateTime.of(dateFilter.getValue(), LocalTime.of(timeFilter.getValue(), 0));
        int duration = 2;
        try {
            duration = Integer.parseInt(durationFilter.getText());
        } catch (NumberFormatException e) {
            durationFilter.setText("2");
        }

        ParkingAreaRequest area = parkingService.getParkingAreaAvailability(currentAreaId, selectedStart.toString(), duration);
        if (area != null) {
            updateUI(area);
        }
    }

    private void updateUI(ParkingAreaRequest area) {
        areaNameText.setText(area.getName());
        areaCityLabel.setText(area.getCity());
        spotsContainer.getChildren().clear();

        if (area.getSpots() != null) {
            for (ParkingSpotRequest spot : area.getSpots()) {
                spotsContainer.getChildren().add(createSpotCard(spot));
            }
        }
    }

    private VBox createSpotCard(ParkingSpotRequest spot) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
        card.setPrefWidth(120);

        Text label = new Text(spot.getLabel());
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        String typeText = (spot.getType() != null) ? spot.getType() : "Standard";
        Label type = new Label(typeText);
        type.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");

        String priceText = (spot.getPricePerHour() != null) ? "$" + spot.getPricePerHour() + "/hr" : "Free";
        Label price = new Label(priceText);
        price.setStyle("-fx-text-fill: #212529; -fx-font-weight: bold;");

        Button statusBtn = new Button();
        statusBtn.setMaxWidth(Double.MAX_VALUE);
        statusBtn.getStyleClass().add("btn");

        if (spot.isOccupied()) {
            statusBtn.setText("Unavailable");
            statusBtn.getStyleClass().add("btn-danger");
            statusBtn.setDisable(true);
        } else {
            statusBtn.setText("Book");
            statusBtn.getStyleClass().add("btn-success");
            statusBtn.setDisable(false);
            statusBtn.setOnAction(e -> openBookingWindow(spot));
        }
        card.getChildren().addAll(label, type, price, statusBtn);
        return card;
    }

    private void openBookingWindow(ParkingSpotRequest spot) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("booking-view.fxml"));
            Parent root = loader.load();

            BookingViewController controller = loader.getController();
            LocalDateTime start = LocalDateTime.of(dateFilter.getValue(), LocalTime.of(timeFilter.getValue(), 0));
            int duration = Integer.parseInt(durationFilter.getText());
            controller.initData(spot.getId(), spot.getLabel(), spot.getPricePerHour(), currentAreaId, start, duration);

            Stage stage = (Stage) spotsContainer.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(
                    BootstrapFX.bootstrapFXStylesheet(),
                    getClass().getResource("styles.css").toExternalForm()
            );
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) areaNameText.getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet(), getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}