package movieticketsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import movieticketsystem.models.User;

import java.io.IOException;

public class Main extends Application {

    public static User user;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("loginScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        //scene.getStylesheets().add(getClass().getResource("style.css").toString());
        //scene.setUserAgentStylesheet("style.css");
        //scene.getStylesheets().add(getClass().getResource("movieticketsystem/style.css").toExternalForm());

        stage.setTitle("Movie Ticket System - Login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}