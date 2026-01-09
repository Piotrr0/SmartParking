package com.smartparking.frontend;

import org.kordamp.bootstrapfx.BootstrapFX;
import com.smartparking.frontend.dto.ParkingAreaRequest;
import com.smartparking.frontend.service.ParkingService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private FlowPane parkingGrid;

    private final ParkingService parkingService = new ParkingService();

    public void initialize() {
        loadParkingData(); }

    public void setUsername(String username) {
        welcomeLabel.setText("Welcome back, " + username);
    }

    @FXML
    public void loadParkingData() {
        parkingGrid.getChildren().clear();
        List<ParkingAreaRequest> areas = parkingService.getAllParkingAreas();

        for (ParkingAreaRequest area : areas) {
            VBox card = createParkingCard(area);
            parkingGrid.getChildren().add(card);
        }
    }

    private VBox createParkingCard(ParkingAreaRequest area) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPrefWidth(200);
        card.setMinWidth(200);

        Text name = new Text(area.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #212529;");

        Label city = new Label(area.getCity());
        city.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");

        Button viewBtn = new Button("View Spots");
        viewBtn.getStyleClass().addAll("btn", "btn-primary");
        viewBtn.setMaxWidth(Double.MAX_VALUE);

        viewBtn.setOnAction(e -> openSpotsView(area.getId()));
        card.getChildren().addAll(name, city, viewBtn);
        return card;
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

    private void openSpotsView(Long areaId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("spots-view.fxml"));
            Parent root = loader.load();

            ParkingSpotsController controller = loader.getController();
            controller.initData(areaId);

            Stage stage = (Stage) parkingGrid.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(
                    BootstrapFX.bootstrapFXStylesheet(),
                    ParkingApplication.class.getResource("styles.css").toExternalForm()
            );

            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void handleMyBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my-bookings-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) parkingGrid.getScene().getWindow();
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

}