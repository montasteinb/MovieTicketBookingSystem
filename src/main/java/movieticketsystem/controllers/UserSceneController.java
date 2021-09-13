package movieticketsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
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
import movieticketsystem.models.Movie;
import movieticketsystem.repository.DBHandler;
import movieticketsystem.service.Booking;
import static movieticketsystem.Main.user;

public class UserSceneController extends LoginSceneController implements Initializable {

    @FXML
    private Label statusMessage;
    @FXML
    private Button usignOutButton;
    @FXML
    private Tab umoviesTab;
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
    private TableColumn<Movie, String> colMoviesName;
    @FXML
    private TableColumn<Movie, String> colMoviesGenre;
    @FXML
    private TableColumn<Movie, Integer> colMoviesId;

    @FXML
    private ComboBox<String> movieGenreComboBox;

    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, String> colBMovie;
    @FXML
    private TableColumn<Booking, String> colBDate;
    @FXML
    private TableColumn<Booking, Integer> colBRoom;

    private static Connection connection = DBHandler.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colMoviesName.setCellValueFactory(new PropertyValueFactory<Movie, String>("name"));
        colMoviesGenre.setCellValueFactory(new PropertyValueFactory<Movie, String>("genre"));
        colMoviesId.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));


        colBMovie.setCellValueFactory(new PropertyValueFactory<Booking, String>("movieName"));
        colBDate.setCellValueFactory(new PropertyValueFactory<Booking, String>("date"));
        colBRoom.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("movieId"));

        movieGenreComboBox.getItems().addAll("All", "Action", "Comedy", "Drama", "Sci-Fi", "Documentary");

        addAllMoviesToTable();

        addAllBookingsToTable();

    }

    public void addAllMoviesToTable() {

        try {
            connection = DBHandler.getConnection();
            String query = "SELECT * FROM movies";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);
            movieTable.setItems(resultSetToMovieList(result));

        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Database problem!");
        }
    }


    private ObservableList<Movie> resultSetToMovieList(ResultSet rs) throws SQLException {

        ObservableList<Movie> movies = FXCollections.observableArrayList();
        int movieId;
        String movieName;
        String movieGenre;

        while (rs.next()) {
            movieId = rs.getInt(1);
            movieName = rs.getString(2);
            movieGenre = rs.getString(3);
            movies.add(new Movie(movieId, movieName, movieGenre));
        }

        return movies;
    }

    private void addAllBookingsToTable() {

        String username = user.getUsername();
        try {
            connection = DBHandler.getConnection();
            String query = "SELECT DISTINCT movies.name as moviename, bookings.date as date, screenings.roomNo as roomNo, users.username " +
                    "FROM bookings " +
                    "JOIN screenings ON bookings.date = screenings.date " +
                    "AND bookings.movie_id = screenings.movie_id " +
                    "JOIN movies ON movies.id = screenings.movie_id " +
                    "JOIN users ON users.username = bookings.username WHERE users.username = '" + username + "';";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);
            bookingsTable.setItems(resultSetToBookingList(result));


        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Database problem!");

        }
    }


    private ObservableList<Booking> resultSetToBookingList(ResultSet rs) throws SQLException {

        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        String movieName;
        String formattedDate;
        int roomNumber;


        while (rs.next()) {
            movieName = rs.getString("movieName");
            formattedDate = rs.getString("date");
            roomNumber = rs.getInt("roomNo");
            bookings.add(new Booking(movieName, formattedDate, roomNumber));
        }

        return bookings;
    }

    public void bookTicket() {

        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();

        if (selectedMovie == null) {
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
            Scene bookingModalScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/movieticketsystem/bookingWindowScene.fxml"))));
            modalWindow.setScene(bookingModalScene);
            modalWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signOut(ActionEvent event) {

        try {
            Scene loginScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/movieticketsystem/loginScene.fxml"))));
            user = null;
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.setResizable(false);
            window.setTitle("CinemaTickets - Login");
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchMovies() {

        String genreFilter = movieGenreComboBox.getSelectionModel().getSelectedItem();
        String nameFilter = movieSearchField.getText();

        try {
            String query = "SELECT * FROM movies WHERE ";
            PreparedStatement statement = connection.prepareStatement(query);

            if ((genreFilter.isEmpty() || genreFilter.equals("All")) && nameFilter.isEmpty()) {
                addAllMoviesToTable();
                return;
            } else if (genreFilter.isEmpty() || genreFilter.equals("All"))
                query += "name LIKE '%" + nameFilter + "%';";
            else if (nameFilter.isEmpty())
                query += " genre = '" + genreFilter + "';";
            else {
                query += "genre='" + genreFilter + "' AND name LIKE '%" + nameFilter + "%';";
            }

            ResultSet result = statement.executeQuery(query);
            movieTable.setItems(resultSetToMovieList(result));
        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage.setText("Error with database!");
        }
    }
}
