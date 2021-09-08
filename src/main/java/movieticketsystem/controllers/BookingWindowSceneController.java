package movieticketsystem.controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.sql.Timestamp;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import movieticketsystem.Main;
import movieticketsystem.models.Movie;
import movieticketsystem.repository.DBHandler;

public class BookingWindowSceneController implements Initializable {

    @FXML
    private ComboBox<String> dateComboBox;
    @FXML
    private Label titleLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusMessage;

    private final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");

    static Movie selectedMovie;


    private ObservableList<Timestamp> availableDatesInTimestamps;

    private ObservableList<String> availableDatesInFormattedStrings;


    @Override
    public void initialize(URL url, ResourceBundle rb) {


        try {

            ResultSet availableDatesResultSet = getAvailableDates();

            if(!availableDatesResultSet.next())
                dateComboBox.setPromptText("No dates available");

            availableDatesInTimestamps = resultSetToTimestamps(availableDatesResultSet);

            availableDatesInFormattedStrings = timestampsToFormattedDates(availableDatesInTimestamps);

            dateComboBox.getItems().addAll(availableDatesInFormattedStrings);

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void cancel(ActionEvent event) {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    private void closeWindow(ActionEvent event){
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    public void confirmBooking() {

        if(dateComboBox.getSelectionModel().getSelectedItem().isEmpty()){
            statusMessage.setText("No date selected!");
            return;
        }

        int selectedIndex = dateComboBox.getSelectionModel().getSelectedIndex();

        String selectedTimestamp = availableDatesInTimestamps.get(selectedIndex).toString();
        System.out.println(selectedTimestamp);
        System.out.println("INSERT INTO bookings values ( '"+ Main.user.getUsername()+"', '"+selectedTimestamp+"', '"+selectedMovie.getId()+"') ;");


        try{

            DBHandler.statement.execute("INSERT INTO bookings values ( '"+ Main.user.getUsername()+"', '"+selectedTimestamp+"', '"+selectedMovie.getId()+"') ;");
            DBHandler.statement.execute("UPDATE screenings SET attendance=attendance+1 WHERE mid="+selectedMovie.getId()+" AND sdate='"+selectedTimestamp+"'");
            statusMessage.setText("Success!");

        }
        catch(SQLException e){
            e.printStackTrace();
            statusMessage.setText("Error loading database!");
        }
    }

    private ResultSet getAvailableDates() throws SQLException{

        int movieId = selectedMovie.getId();

        ResultSet rs = DBHandler.statement.executeQuery("SELECT sdate FROM screenings s JOIN rooms r ON (r.id = s.rnumber) WHERE mid='"+movieId+"' AND s.attendance<r.capacity AND s.sdate>CURRENT_TIMESTAMP");

        return rs;
    }

    private ObservableList<Timestamp> resultSetToTimestamps(ResultSet rs) {

        ObservableList<Timestamp> timestamps = FXCollections.observableArrayList();
        Timestamp currentTimestamp;

        try{
            while(rs.next()){

                currentTimestamp = rs.getTimestamp("sdate");
                timestamps.add(currentTimestamp);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return timestamps;
    }

    private ObservableList<String> timestampsToFormattedDates(ObservableList<Timestamp> timestamps) throws SQLException{

        ObservableList<String> formattedDates = FXCollections.observableArrayList();
        String formattedDate;


        for (Timestamp timestamp: timestamps){
            formattedDate = timestamp.toLocalDateTime().format(formatter);
            formattedDates.add(formattedDate);
        }

        return formattedDates;
    }
}
