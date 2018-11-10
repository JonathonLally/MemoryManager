package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{         //Loads GUI, starting with MainView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        //MainViewController mainCtrl = loader.getController();       //contains reference to MainViewController instance
        primaryStage.setTitle("Memory Manger");
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/resources/ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
