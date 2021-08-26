import java.util.ArrayList;
import java.util.HashSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Manager;
import model.User;
import service.Movie;

public class Main extends Application {

    static Parent root;
    static Stage primaryStage;
    static Main m = null;
    static User currentUser;
    static Boolean managerMode = false;
    static String selectedMovieTitle = "", selectedDate = "", selectedTime = "";
    static ArrayList<String> selectedSeats;


    // arrayLists to be populated with the information from the text files
    static HashSet<Manager> managers = new HashSet<Manager>();
    static HashSet<User> users = new HashSet<User>();
    static HashSet<Movie> movies = new HashSet<Movie>();
    //static HashSet<BookingHistoryItem> bookings = new HashSet<BookingHistoryItem>();


    public static void main(String[] args) throws Exception {

        m = new Main();
    }

    static HashSet<Manager> getManagerList() {

        return managers;
    }

    static HashSet<User> getUserList() {

        return users;
    }

    static HashSet<Movie> getMovieList() {

        return movies;
    }

    /*static HashSet<BookingHistoryItem> getBookingList() {

        return bookings;
    }

     */

    static void resetManagerList() {

        managers.clear();
    }

    static void resetUserList() {

        users.clear();
    }

    static void resetMovieList() {

        movies.clear();
    }

    /*static void resetBookingList() {

        bookings.clear();
    }

     */

    static Main getMainApplication() {

        return m;
    }

    //reads Data base about users.......


    static User getCurrentUser() {

        return currentUser;
    }

    static void setCurrentUser(User currentUser) {

        Main.currentUser = currentUser;
    }

    static boolean isManager() {

        return managerMode;
    }

    static void setManagerMode(boolean managerMode) {

        Main.managerMode = managerMode;
    }

    static User getUserByUsername(String username) {
        for (User u : users)
            if (u.getUsername().equals(username))
                return u;

        return null;
    }

    /*static Movie getMovieByTitle(String title) {

        for (Movie movie : Main.getMovieList()) {
            if (movie.getTitle().equals(title))
                return movie;
        }

        return null;
    }
     */

    static void setSelectedMovieTitle(String selectedMovieTitle) {

        Main.selectedMovieTitle = selectedMovieTitle;
    }

    static String getSelectedMovieTitle() {

        return selectedMovieTitle;
    }

    static void setSelectedDate(String selectedDate) {

        Main.selectedDate = selectedDate;
    }

    static String getSelectedDate() {

        return selectedDate;
    }

    static void setSelectedTime(String selectedTime) {

        Main.selectedTime = selectedTime;
    }

    static String getSelectedTime() {

        return selectedTime;
    }

    static void setSelectedSeats(ArrayList<String> selectedSeats) {

        Main.selectedSeats = selectedSeats;
    }

    static ArrayList<String> getSelectedSeats() {

        return selectedSeats;
    }

    static Parent getRoot() {

        return root;
    }

    static void setRoot(Parent root) {

        Main.root = root;
    }

    static Stage getStage() {

        return primaryStage;
    }

    static void setStage(Stage stage) {

        Main.primaryStage = stage;
    }

    /*public static String getPath() {
        return null;
    }

     */


    @Override
    public void start(Stage primaryStage) {

        try {
            // setting up the login scene
            root = FXMLLoader.load(getClass().getResource("/scenes/LoginScene.fxml"));
            Main.primaryStage = primaryStage;
            primaryStage.setTitle("Movie Ticket Booking System");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 700, 400);
            //scene.getStylesheets().add(getClass().getResource("/scenes/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


