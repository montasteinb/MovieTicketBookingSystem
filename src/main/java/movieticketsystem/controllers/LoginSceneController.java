package movieticketsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.Parent;
import javafx.stage.Stage;
import movieticketsystem.Main;
import movieticketsystem.models.User;
import movieticketsystem.repository.DBHandler;


public class LoginSceneController implements Initializable {

    @FXML
    private Font x1;
    @FXML
    private TextField logInUsername;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private CheckBox loginAdmin;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label signupErrorMessage;
    @FXML
    private Label loginErrorMessage;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpFirstName;
    @FXML
    private TextField signUpLastName;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpPasswordRepeat;
    @FXML
    private Label signUpErrorMessage;
    Parent sceneParent;

    private Connection connection = DBHandler.getConnection();

    private ActionEvent event = new ActionEvent();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    @FXML
    private void login(ActionEvent event) throws SQLException {
        String usernameFieldValue = logInUsername.getText();
        String passwordFieldValue = loginPassword.getText();
        Boolean adminLoginFieldValue = loginAdmin.isSelected();
        this.event = event;

        if (usernameFieldValue.isEmpty() || passwordFieldValue.isEmpty()) {
            loginErrorMessage.setText("The username and password fields must not be empty!");
            return;
            }

            connection = DBHandler.getConnection();
            String query = "SELECT * FROM users WHERE username = '" + usernameFieldValue + "' " + "LIMIT 1;";
            PreparedStatement statement = connection.prepareStatement(query);


            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                loginErrorMessage.setText("The data is incorrect!");
                return;
            }
            User user = initializeUserFromResultSet(result);

            if (adminLoginFieldValue) {
                adminLogin(user);
            } else {
                regularUserLogin(user);
            }
        }


    private User initializeUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getBoolean("isAdmin"), rs.getString("password"));
    }

    private void loadScene(String sceneType) {
        String windowTitle = "";

               try {
            if (Objects.equals(sceneType, "admin")) {
                sceneParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/movieticketsystem/adminScene.fxml")));
                windowTitle = "MovieTickets - Admin";
            } else if(Objects.equals(sceneType, "user")) {
                sceneParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/movieticketsystem/userScene.fxml")));
                windowTitle = "MovieTickets";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(sceneParent);
        Node node = (Node) event.getSource();
        Stage window = (Stage) node.getScene().getWindow();
        window.setScene(scene);
        window.setResizable(true);
        window.setTitle(windowTitle);
        window.show();

    }

   @FXML
    private void signUp(ActionEvent event) throws SQLException {

       String signUpUsernameValue = signUpUsername.getText();
       String signUpPasswordValue = signUpPassword.getText();
       String signUpPasswordRepeatValue = signUpPasswordRepeat.getText();
       String signUpFirstNameValue = signUpFirstName.getText();
       String signUpLastNameValue = signUpLastName.getText();
       this.event = event;


       if (signUpUsernameValue.isEmpty() || signUpPasswordValue.isEmpty() || signUpPasswordRepeatValue.isEmpty() || signUpFirstNameValue.isEmpty() || signUpLastNameValue.isEmpty()) {
           signUpErrorMessage.setText("There are empty fields!");
           return;
       }

       if (!signUpPasswordValue.equals(signUpPasswordRepeatValue)) {
           signUpErrorMessage.setText("The passwords do not match!");
           return;
       }
       connection = DBHandler.getConnection();

       String query = "INSERT INTO users(firstName, lastName, username, password, isAdmin) VALUES ('" + signUpFirstNameValue + "', '" + signUpLastNameValue + "', '" + signUpUsernameValue + "', '" + signUpPasswordValue + "', '" + 0 +"');";
       PreparedStatement statement = connection.prepareStatement(query);

       statement.executeUpdate();

       User user = new User(signUpFirstNameValue, signUpLastNameValue, signUpUsernameValue, signUpPasswordValue, false);

       regularUserLogin(user);
    }

    private void adminLogin(User user) {

        if (!user.getAdmin()) {
            loginErrorMessage.setText("The user does not have admin rights");
            return;
        }
        Main.user = user;
        loadScene("admin");
    }

    private void regularUserLogin(User user) {
        Main.user = user;
        loadScene("user");
    }
}
