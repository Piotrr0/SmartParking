package com.smartparking.frontend;

import com.smartparking.frontend.dto.BookingResponse;
import com.smartparking.frontend.service.BookingService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyBookingsController {

    @FXML private TableView<BookingResponse> bookingsTable;
    @FXML private TableColumn<BookingResponse, Void> colId;
    @FXML private TableColumn<BookingResponse, String> colParking;
    @FXML private TableColumn<BookingResponse, String> colCity;
    @FXML private TableColumn<BookingResponse, String> colSpot;
    @FXML private TableColumn<BookingResponse, String> colStart;
    @FXML private TableColumn<BookingResponse, String> colEnd;
    @FXML private TableColumn<BookingResponse, Number> colPrice;
    @FXML private TableColumn<BookingResponse, String> colStatus;

    private final BookingService bookingService = new BookingService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        colId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        bookingsTable.setRowFactory(tv -> new TableRow<BookingResponse>() {
            @Override
            protected void updateItem(BookingResponse item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    try {
                        LocalDateTime start = LocalDateTime.parse(item.getStartTime(), formatter);
                        LocalDateTime end = LocalDateTime.parse(item.getEndTime(), formatter);
                        LocalDateTime now = LocalDateTime.now();

                        if (end.isBefore(now)) {
                            setStyle("-fx-background-color: #ffcccc;");
                        } else if (start.isBefore(now) && end.isAfter(now)) {
                            setStyle("-fx-background-color: #ccffcc;");
                        } else {
                            setStyle("");
                        }
                    } catch (Exception e) {
                        setStyle("");
                    }
                }
            }
        });

        colParking.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getParkingName()));

        colCity.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCity()));

        colSpot.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSpotLabel()));

        colStart.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStartTime()));

        colEnd.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEndTime()));

        colPrice.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getTotalPrice()));

        colStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        loadBookings();
    }

    private void loadBookings() {
        if (UserSession.getInstance() != null) {
            Long userId = UserSession.getInstance().getUserId();
            List<BookingResponse> bookings = bookingService.getUserBookings(userId);
            ObservableList<BookingResponse> data = FXCollections.observableArrayList(bookings);
            bookingsTable.setItems(data);
        }
    }

    @FXML
    public void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            if (UserSession.getInstance() != null) {
                controller.setUsername(UserSession.getInstance().getUsername());
            }

            Stage stage = (Stage) bookingsTable.getScene().getWindow();
            Scene scene = new Scene(root);

            scene.getStylesheets().addAll(
                    BootstrapFX.bootstrapFXStylesheet(),
                    getClass().getResource("styles.css").toExternalForm()
            );

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}