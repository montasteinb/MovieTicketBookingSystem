package movieticketsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import movieticketsystem.Main;
import movieticketsystem.models.Movie;
import movieticketsystem.repository.DBHandler;
import movieticketsystem.service.Booking;

public class UserSceneController implements Initializable {

    private final DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    private Label statusMessage;
    @FXML
    private Button usignOutButton;
    @FXML
    private Tab umoviesTab;;
    @FXML
    private TextField movieSearchField;
    @FXML
    private Button movieSearchButton;
    @FXML
    private Tab ureservationsTab;
    @FXML
    private TableColumn<?, ?> ureservationsTable;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie,String> colMoviesName;
    @FXML
    private TableColumn<Movie,String> colMoviesGenre;
    @FXML
    private ComboBox<String> movieGenreComboBox;

    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking,String> colBMovie;
    @FXML
    private TableColumn<Booking,String> colBDate;
    @FXML
    private TableColumn<Booking,Integer> colBRoom;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colMoviesName.setCellValueFactory(new PropertyValueFactory<Movie,String>("name"));
        colMoviesGenre.setCellValueFactory(new PropertyValueFactory<Movie,String>("genre"));

        colBMovie.setCellValueFactory(new PropertyValueFactory<Booking,String>("movieName"));
        colBDate.setCellValueFactory(new PropertyValueFactory<Booking,String>("date"));
        colBRoom.setCellValueFactory(new PropertyValueFactory<Booking,Integer>("roomNumber"));

        movieGenreComboBox.getItems().addAll("All","Action","Comedy","Drama","Sci-Fi","Documentary");

        addAllMoviesToTable();

        addAllBookingsToTable();

    }

    public void addAllMoviesToTable() {

        try {

            ResultSet queryResultSet = DBHandler.statement.executeQuery("SELECT * FROM movies");
            movieTable.setItems(resultSetToMovieList(queryResultSet));

        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Database problem!");

        }
    }


    private ObservableList<Movie> resultSetToMovieList(ResultSet rs) throws SQLException{

        ObservableList<Movie> movies = FXCollections.observableArrayList();
        int movieId;
        String movieName;
        String movieGenre;

        while(rs.next()){
            movieId = rs.getInt(1);
            movieName = rs.getString(2);
            movieGenre = rs.getString(3);
            movies.add(new Movie(movieId,movieName, movieGenre));
        }

        return movies;
    }

    public void searchMovies() {

        String genreFilter = movieGenreComboBox.getSelectionModel().getSelectedItem();
        String nameFilter = movieSearchField.getText();

        String sqlQuery = "SELECT * FROM movies WHERE ";

        try {

            if ( ( genreFilter.isEmpty() || genreFilter.equals("All") ) && nameFilter.isEmpty() ){
                addAllMoviesToTable();
                return;
            }
            else if ( genreFilter.isEmpty() || genreFilter.equals("All") )
                sqlQuery += "name LIKE '%"+nameFilter+"%' ;";
            else if ( nameFilter.isEmpty())
                sqlQuery += "genre='"+genreFilter+"' ;";
            else
                sqlQuery += "genre='"+genreFilter+"' AND name LIKE '%"+nameFilter+"%' ;";


            ResultSet queryResult = DBHandler.statement.executeQuery(sqlQuery);

            movieTable.setItems(resultSetToMovieList(queryResult));

        }
        catch (SQLException e){
            e.printStackTrace();
            statusMessage.setText("Database problem!");
        }
    }

    private void addAllBookingsToTable() {

        try {

            ResultSet queryResultSet = DBHandler.statement.executeQuery("select distinct m.name as moviename, b.sdate as date, s.rnumber as rnumber " +
                    "from bookings b join screenings s on b.sdate=s.sdate and b.mid = s.mid " +
                    "join movies m on s.mid = m.id " +
                    "where username='"+ Main.user.getUsername()+"' ;");
            bookingsTable.setItems(resultSetToBookingList(queryResultSet));

        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Database problem!");

        }
    }

    private ObservableList<Booking> resultSetToBookingList(ResultSet rs) throws SQLException{

        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        String movieName;
        String formattedDate;
        int roomNumber;


        while(rs.next()){
            movieName = rs.getString("moviename");
            formattedDate = rs.getTimestamp("date").toLocalDateTime().format(formatter);
            roomNumber = rs.getInt("rnumber");
            bookings.add(new Booking(movieName, formattedDate, roomNumber));
        }

        return bookings;
    }

    public void bookTicket() {

        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if ( selectedMovie==null ){
            statusMessage.setText("There is no movie selected");
            return;
        }

        BookingWindowSceneController.selectedMovie = selectedMovie;

        Stage modalWindow = new Stage();
        modalWindow.initModality(Modality.APPLICATION_MODAL);
        modalWindow.setTitle("Ticket reservation");
        modalWindow.setHeight(300);
        modalWindow.setWidth(600);
        modalWindow.setResizable(false);

        try {
            Scene bookingModalScene = new Scene(FXMLLoader.load(getClass().getResource("bookingWindowScene.fxml")));
            modalWindow.setScene(bookingModalScene);
            modalWindow.showAndWait();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void signOut(ActionEvent event) {

        try{
            Scene loginScene = new Scene (FXMLLoader.load(getClass().getResource("loginScene.fxml")));
            Main.user = null;
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.setResizable(false);
            window.setTitle("CinemaTickets - Login");
            window.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}



