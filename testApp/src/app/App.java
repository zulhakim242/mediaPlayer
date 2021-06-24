package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Stage primaryStage; //

    @Override
    public void start(Stage primaryStage) {
        try {
            App.primaryStage = primaryStage; //
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Mainframe.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setTitle("MediaPlayer");
            scene.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    if(primaryStage.isFullScreen()){
                        primaryStage.setFullScreen(false);
                    }
                    else {
                        primaryStage.setFullScreen(true);
                    }
                }
            });
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}