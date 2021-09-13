package movieticketsystem.controllers;

import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import movieticketsystem.Main;
import movieticketsystem.models.Movie;
import movieticketsystem.repository.DBHandler;

public class BookingWindowSceneController extends UserSceneController implements Initializable {

    @FXML
    private ComboBox<String> dateComboBox;
    @FXML
    private Label titleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusMessage;

    static Movie selectedMovie;

    private Connection connection = DBHandler.getConnection();

    private ObservableList<String> availableDates;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            ResultSet availableDatesResultSet = getAvailableDates();

            if (!availableDatesResultSet.next())
                dateComboBox.setPromptText("No dates available");

            availableDates = resultSetToDates(availableDatesResultSet);

            dateComboBox.getItems().addAll(availableDates);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    private void closeWindow(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    public void confirmBooking(ActionEvent event) {

        if (dateComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
            statusMessage.setText("No date selected!");
            return;
        }
        int selectedIndex = dateComboBox.getSelectionModel().getSelectedIndex();
        String selectedDate = availableDates.get(selectedIndex);
        System.out.println(selectedDate);
        System.out.println("INSERT INTO bookings values ( '" + Main.user.getUsername() + "', '" + selectedDate + "', '" + selectedMovie.getId() + "') ;");

        try {
            connection = DBHandler.getConnection();
            String query = "INSERT INTO bookings(date, username, movie_id, roomNo) " +
                    "VALUES('" + selectedDate + "' , '" + Main.user.getUsername() + "', " + selectedMovie.getId() + ", roomNo = " + selectedMovie.getId() + ");";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.executeUpdate();
            updateBookings();
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage.setText("Error loading database!");
        }
        closeWindow(event);
    }

    private void updateBookings() {

        int selectedIndex = dateComboBox.getSelectionModel().getSelectedIndex();
        String selectedDate = availableDates.get(selectedIndex);

        try {
            connection = DBHandler.getConnection();
            String query = "UPDATE screenings SET attendance = attendance+1 WHERE movie_Id = " + selectedMovie.getId() + " AND date = '" + selectedDate + "';";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.executeUpdate();

            statusMessage.setText("Success!");
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage.setText("Error loading database!");
        }
    }

    private ResultSet getAvailableDates() throws SQLException {

        int movieId = selectedMovie.getId();
        connection = DBHandler.getConnection();
        String query = "SELECT date FROM screenings LEFT JOIN rooms ON rooms.number = screenings.roomNo " +
                "WHERE movie_id = " + movieId + ";";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery(query);

        return resultSet;
    }

    private ObservableList<String> resultSetToDates(ResultSet rs) {

        ObservableList<String> dates = FXCollections.observableArrayList();
        String currentDate;

        try {
            while (rs.next()) {

                currentDate = rs.getString("date");
                dates.add(currentDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        dates.toString();
        return dates;
    }
}