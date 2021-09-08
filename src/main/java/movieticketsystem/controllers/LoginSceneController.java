package movieticketsystem.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private Label signupErrorMessage;
    @FXML
    private Label loginErrorMessage;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpfirstName;
    @FXML
    private TextField signUplastName;
    @FXML
    private PasswordField signUpPassword;
    @FXML
    private PasswordField signUpPasswordRepeat;
    @FXML
    private Label signUpErrorMessage;

    private ActionEvent event = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    @FXML
    private void login(ActionEvent event) {
        String usernameFieldValue = logInUsername.getText();
        String passwordFieldValue = loginPassword.getText();
        Boolean adminLoginFieldValue = loginAdmin.isSelected();
        this.event = event;

        if (usernameFieldValue.isEmpty() || passwordFieldValue.isEmpty()) {
            loginErrorMessage.setText("The username and password fields must not be empty!");
            return;
        }

        try {
            ResultSet userQueryResult = DBHandler.statement.executeQuery("SELECT * FROM users WHERE username='" + usernameFieldValue + "' and password='" + passwordFieldValue + "'");

            if (!userQueryResult.next()) {
                loginErrorMessage.setText("The data is incorrect!");
                return;
            }
            User user = initializeUserFromResultSet(userQueryResult);

            if (adminLoginFieldValue)
                adminLogin(user);
            else
                regularUserLogin(user);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User initializeUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getString("username"), rs.getString("firstName"), rs.getString("lastName"), rs.getBoolean("isAdmin"));
    }

    private void loadScene(String sceneType) {
        Parent sceneParent = null;
        String windowTitle = "";


        try {
            if (sceneType.equals("admin")) {
                sceneParent = FXMLLoader.load(getClass().getResource("adminScene.fxml"));
                windowTitle = "MovieTickets - Admin";
            } else {
                sceneParent = FXMLLoader.load(getClass().getResource("userScene.fxml"));
                windowTitle = "MovieTickets";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(sceneParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.setResizable(true);
        window.setTitle(windowTitle);
        window.show();

    }

    @FXML
    private void signUp(ActionEvent event) {
        String signUpUsernameValue = signUpUsername.getText();
        String signUpPasswordValue = signUpPassword.getText();
        String signUpPasswordRepeatValue = signUpPasswordRepeat.getText();
        String signUpFnameValue = signUpfirstName.getText();
        String signUpLnameValue = signUplastName.getText();
        this.event = event;


        if (signUpUsernameValue.isEmpty() || signUpPasswordValue.isEmpty() || signUpPasswordRepeatValue.isEmpty() || signUpFnameValue.isEmpty() || signUpLnameValue.isEmpty()) {
            signUpErrorMessage.setText("There are empty fields!");
            return;
        }

        if (!signUpPasswordValue.equals(signUpPasswordRepeatValue)) {
            signUpErrorMessage.setText("The passwords do not match!");
            return;
        }


        try {
            ResultSet userQueryResult = DBHandler.statement.executeQuery("SELECT * FROM users WHERE username='" + signUpUsernameValue + "'");

            if (userQueryResult.next()) {
                signUpErrorMessage.setText("There is already a user with this username!");
                return;
            }

            DBHandler.statement.executeUpdate("INSERT INTO users VALUES ('" + signUpUsernameValue + "', '" + signUpPasswordValue + "', '" + signUpFnameValue + "', '" + signUpLnameValue + "', false) ;");

            User user = new User(signUpUsernameValue, signUpFnameValue, signUpLnameValue, false);

            regularUserLogin(user);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
