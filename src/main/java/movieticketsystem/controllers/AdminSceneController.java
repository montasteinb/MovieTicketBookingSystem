package movieticketsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import movieticketsystem.models.Movie;
import movieticketsystem.models.User;
import movieticketsystem.repository.DBHandler;
import movieticketsystem.service.Screening;

import static movieticketsystem.Main.user;

public class AdminSceneController extends LoginSceneController implements Initializable {

    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie,String> colMoviesName;
    @FXML
    private TableColumn<Movie, Integer> colMoviesId;
    @FXML
    private TableColumn<Movie,String> colMoviesGenre;
    @FXML
    private ComboBox<String> movieGenreComboBox;
    @FXML
    private Label statusMessage;
    @FXML
    private TextField movieSearchField;
    @FXML
    private TextField newMovieNameField;
    @FXML
    private ComboBox<String> newMovieGenreComboBox;
    @FXML
    private TableView<Screening> screeningTable;
    @FXML
    private TableColumn<Screening,String> colScreeningMovie;
    @FXML
    private TableColumn<Screening,String> colScreeningDate;
    @FXML
    private TableColumn<Screening,Integer> colScreeningRoom;
    @FXML
    private TableColumn<Screening,Integer> colScreeningAttendance;
    @FXML
    private ComboBox<String> newScreeningMoviesComboBox;
    @FXML
    private ComboBox<Integer> newScreeningRoomComboBox;
    @FXML
    private TextField newScreeningDateField;
    @FXML
    private Button promoteUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User,String> colUserUsername;
    @FXML
    private TableColumn<User,String> colUserName;
    @FXML
    private TableColumn<User,String> colUserLastName;
    @FXML
    private TableColumn<User,Boolean> colUserIsAdmin;

    private static Connection connection = DBHandler.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colMoviesName.setCellValueFactory(new PropertyValueFactory<Movie,String>("name"));
        colMoviesGenre.setCellValueFactory(new PropertyValueFactory<Movie,String>("genre"));
        colMoviesId.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));

        colScreeningMovie.setCellValueFactory(new PropertyValueFactory<Screening,String>("movieName"));
        colScreeningDate.setCellValueFactory(new PropertyValueFactory<Screening,String>("date"));
        colScreeningRoom.setCellValueFactory(new PropertyValueFactory<Screening,Integer>("roomNumber"));
        colScreeningAttendance.setCellValueFactory(new PropertyValueFactory<Screening,Integer>("attendance"));

        colUserUsername.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        colUserName.setCellValueFactory(new PropertyValueFactory<User,String>("firstName"));
        colUserLastName.setCellValueFactory(new PropertyValueFactory<User,String>("lastName"));
        colUserIsAdmin.setCellValueFactory(new PropertyValueFactory<User,Boolean>("admin"));

        movieGenreComboBox.getItems().addAll("All","Action","Comedy","Drama","Sci-Fi","Documentary");
        newMovieGenreComboBox.getItems().addAll("Action","Comedy","Drama","Sci-Fi","Documentary");

        addAllMoviesToTable();
        addAllScreeningsToTable();
        addMoviesToComboBox();
        addRoomsToComboBox();
        addAllUsersToTable();

    }


    public void addAllMoviesToTable() {

        try {
            String query = "SELECT * FROM movies";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery();
            movieTable.setItems(resultSetToMovieList(result));



        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Error with database!");
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

    public void addMovie(Movie movie) throws SQLException {

        int movieId = getNewMovieId();
        String movieName = newMovieNameField.getText();
        String movieGenre = newMovieGenreComboBox.getSelectionModel().getSelectedItem();

        if (movieName.isEmpty() || movieGenre.isEmpty()) {
            statusMessage.setText("Fields should not be empty!");
            return;
        }try{
            connection = DBHandler.getConnection();


            String query = "INSERT INTO movies (id, name, genre) VALUES ( '" + movieId + "', '" + movieName + "', '" + movieGenre + "' );";
            PreparedStatement statement = connection.prepareStatement(query);

                statement.executeUpdate();
                addAllMoviesToTable();

            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                statusMessage.setText("The movie has been added!");
            }
        }

    @FXML
    private void handleAddMovie(ActionEvent actionEvent){

        try {
            Movie movie = new Movie(newMovieNameField.getText(), newMovieGenreComboBox.getSelectionModel().getSelectedItem());
            addMovie(movie);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteMovie() {

        int movieId = movieTable.getSelectionModel().getSelectedItem().getId();

        connection = DBHandler.getConnection();
        try{
            String query = "DELETE FROM movies WHERE id = " + movieId + ";";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.executeUpdate();

            statusMessage.setText("The movie was deleted");
            addAllMoviesToTable();
        }
        catch(SQLException e){
            e.printStackTrace();
            statusMessage.setText("Error with database!");
        }
    }


    private int getNewMovieId() throws SQLException{
        connection = DBHandler.getConnection();
        String query = "SELECT MAX(id) AS id FROM movies;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery(query);
        int id=0;

        while(rs.next())
            id = rs.getInt("id");

        return ++ id;
    }



    private void addAllScreeningsToTable(){

        try {
            connection = DBHandler.getConnection();
            String query = "SELECT movies.name as movieName, screenings.date as date, screenings.roomNo as roomNo, screenings.attendance as attendance FROM screenings " +
                    "INNER JOIN movies ON movies.id = screenings.movie_id;";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);

            screeningTable.setItems(resultSetToScreeningList(result));

        } catch (SQLException e) {

            e.printStackTrace();
            statusMessage.setText("Error with database!");
        }
    }

    private ObservableList<Screening> resultSetToScreeningList(ResultSet rs) throws SQLException{

        ObservableList<Screening> screenings = FXCollections.observableArrayList();
        String movieName;
        String date;
        int roomNumber;
        int attendance;

        while(rs.next()){
            movieName = rs.getString(1);
            date = rs.getString(2);
            roomNumber = rs.getInt(3);
            attendance = rs.getInt(4);
            screenings.add(new Screening(movieName, date, roomNumber, attendance));
        }

        return screenings;
    }


    public void deleteScreening() {


        Screening selectedScreening = screeningTable.getSelectionModel().getSelectedItem();
        String date = screeningTable.getSelectionModel().getSelectedItem().getDate();
        int movieId = screeningTable.getSelectionModel().getSelectedItem().getMovieId();

        try {
            System.out.println("Delete from screenings where movie is " + selectedScreening.getMovieName() + " and date is " + selectedScreening.getDate());
            connection = DBHandler.getConnection();
            String query = "DELETE FROM screenings WHERE movie_id = " + movieId + " AND date = '" + date + "';";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.executeUpdate(query);
            statusMessage.setText("The view has been deleted");


        } catch (SQLException e) {
            e.printStackTrace();
            statusMessage.setText("Error with database!");
        }
    }


    public int findMovieId() throws SQLException {
        connection = DBHandler.getConnection();
        String movieName = newScreeningMoviesComboBox.getSelectionModel().getSelectedItem();
        String query = "SELECT id FROM movies WHERE name = '" + movieName + "'";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet movieResultSet = statement.executeQuery();
        movieResultSet.next();
        int movieId = movieResultSet.getInt("id");

        return movieId;
    }


    public void addScreening(){
        try {

            String movieName = newScreeningMoviesComboBox.getSelectionModel().getSelectedItem();
            String date = newScreeningDateField.getText();
            int roomNumber = newScreeningRoomComboBox.getSelectionModel().getSelectedItem();
            int movieId =  findMovieId();

            if(movieName.isEmpty() || date.isEmpty() || roomNumber < 0) {
                statusMessage.setText("Fields must not be empty!");
            }

            if (screeningExists(date, roomNumber)){
                statusMessage.setText("There is already a screening at this time and room!");
                return;
            }
            connection = DBHandler.getConnection();

            String query = "INSERT INTO screenings (date, movie_id, roomNo) VALUES('" + date + "', '" + movieId + "', '" + roomNumber + "');";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statusMessage.setText("Successful introduction!");

            addAllScreeningsToTable();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void addMoviesToComboBox(){

        try{
            connection = DBHandler.getConnection();
            String query = "SELECT * FROM movies";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);
            ObservableList<Movie> movies = resultSetToMovieList(result);


            for(Movie m: movies)
                newScreeningMoviesComboBox.getItems().add(m.getName());
        }

        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void addRoomsToComboBox() {

        try{
            connection = DBHandler.getConnection();
            String query = "SELECT number FROM rooms";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);

            ObservableList<Integer> rooms = FXCollections.observableArrayList();

            while(result.next())
                rooms.add(result.getInt("number"));

            newScreeningRoomComboBox.getItems().addAll(rooms);
        }

        catch(SQLException e){
            e.printStackTrace();
        }
    }


    private boolean screeningExists(String date, int room) throws SQLException{
        date = newScreeningDateField.getText();
        connection = DBHandler.getConnection();
        String query = "SELECT * FROM screenings WHERE date ='" + date + "' and roomNo= '" + room + "';";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet rs = statement.executeQuery(query);

        return rs.next();
    }

    private void addAllUsersToTable() {

        try{
            connection = DBHandler.getConnection();
            String query = "SELECT * FROM users";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet result = statement.executeQuery(query);
            usersTable.getItems().addAll(resultSetToUserList(result));
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private ObservableList<User> resultSetToUserList(ResultSet rs) {

        ObservableList<User> users = FXCollections.observableArrayList();

        String username = "";
        String firstName = "";
        String lastName = "";
        boolean isAdmin = false;
        String password = "";

        try{
            while(rs.next()){

                username = rs.getString("username");
                firstName = rs.getString("firstName");
                lastName = rs.getString("lastName");
                isAdmin = rs.getBoolean("isAdmin");
                password = rs.getString("password");
                users.add(new User(username,firstName,lastName,isAdmin, password));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    public void promoteUser() {

        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if( selectedUser==null ){
            statusMessage.setText("No user selected");
            return;
        }
        if(selectedUser.getAdmin()){
            statusMessage.setText("User is already an admin");
            return;
        }
        try {
            connection = DBHandler.getConnection();
            String query = "UPDATE users SET isAdmin = true WHERE username ='" + selectedUser.getUsername() + "' ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate(query);

            statusMessage.setText("Success!");
            refreshUsersTable();
        }
        catch(SQLException e){
            statusMessage.setText("Database problem!");
            e.printStackTrace();
        }
    }

    public void deleteUser() {

        User selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if( selectedUser==null ){
            statusMessage.setText("No user selected");
            return;
        }

        try {
            connection = DBHandler.getConnection();
            String query = "DELETE FROM users WHERE username = '" + selectedUser.getUsername() + "';";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statusMessage.setText("Success!");
            refreshUsersTable();
        }
        catch(SQLException e){
            statusMessage.setText("Database problem!");
            e.printStackTrace();
        }
    }

    private void refreshUsersTable() {

        usersTable.getItems().setAll();
        addAllUsersToTable();
    }
    public void searchMovies() {

        String genreFilter = movieGenreComboBox.getSelectionModel().getSelectedItem();
        String nameFilter = movieSearchField.getText();

        try {
            String query = "SELECT * FROM movies WHERE ";
            PreparedStatement statement = connection.prepareStatement(query);

            if ((genreFilter.isEmpty() || genreFilter.equals("All")) && nameFilter.isEmpty()){
                addAllMoviesToTable();
                return;
            }
            else if ( genreFilter.isEmpty() || genreFilter.equals("All") )
                query += "name LIKE '%"+nameFilter+"%';";
            else if ( nameFilter.isEmpty())
                query += " genre = '"+genreFilter+"';";
            else {
                query += "genre='" + genreFilter + "' AND name LIKE '%" + nameFilter + "%';";
            }

            ResultSet result = statement.executeQuery(query);
            movieTable.setItems(resultSetToMovieList(result));
        }
        catch (SQLException e){
            e.printStackTrace();
            statusMessage.setText("Error with database!");
        }
    }

    public void signOut(ActionEvent event) {

        try{
            Scene loginScene = new Scene (FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/movieticketsystem/loginScene.fxml"))));
            user = null;
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
