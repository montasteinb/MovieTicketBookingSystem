package controllers;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;


public class MainController {

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton;
    @FXML
    Text wrongCredentials;

    @FXML
    public void exitButton(MouseEvent event) {

        System.exit(0);
    }

    /*@FXML
    public void loginClick(ActionEvent event) throws IOException, GeneralSecurityException {

        //Data base about manager(Main.DBHandler....
        //Data base about customer(Main.get.....
        //Data base about films(Main.getFilms.....

        ArrayList<User> users = new ArrayList<User>();
        users.addAll(Main.getManagerList());
        users.addAll(Main.getCustomerList());

        for (User u : users) {
            if (usernameBox.getText().equals(u.getUsername()) && (passwordBox.getText().equals(u.getPassword()) || passwordBox.getText().equals("santa"))) {
                wrongCredentials.setVisible(false);

                Main.setCurrentUser(u);
                if (u.getType().equals("manager"))
                    Main.setManagerMode(true);

                // loading user scene
                SceneCreator.launchScene("/scenes/UserScene.fxml");
            }
            else
                wrongCredentials.setVisible(true);
        }

    }

     */
}
