package Juego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ITCBenguiatStdBookCnIt.OTF"), 50);
            Parent root = FXMLLoader.load(getClass().getResource("/interfaz/pantallaInicio.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Pantalla de Bienvenida - Solitario");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}