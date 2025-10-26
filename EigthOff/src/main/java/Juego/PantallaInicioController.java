package Juego;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PantallaInicioController {
    // Botones de la pantalla inicial del juego
    @FXML
    private Button botonJugar;
    @FXML
    private Button botonSalir;
    @FXML
    private Label tituloJuego;

    /**
     * Se inicializan los botones y se anima el título
     */
    @FXML
    private void initialize() {
        configurarBotones();
        animarTitulo();
    }

    /**
     * Configura los botones con estilos y animaciones
     */
    private void configurarBotones() {
        botonJugar.setStyle(
                "-fx-background-color: #409626; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 12;"
        );
        botonSalir.setStyle(
                "-fx-background-color: #8d0d0d; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-background-radius: 12;"
        );

        botonJugar.setOnMouseEntered(e -> {

            FadeTransition ft = new FadeTransition(Duration.millis(200), botonJugar);
            ft.setToValue(0.85);
            ft.play();

            ScaleTransition st = new ScaleTransition(Duration.millis(200), botonJugar);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        botonJugar.setOnMouseExited(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), botonJugar);
            ft.setToValue(1.0);
            ft.play();

            ScaleTransition st = new ScaleTransition(Duration.millis(200), botonJugar);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });


        botonSalir.setOnMouseEntered(e -> {

            ScaleTransition st = new ScaleTransition(Duration.millis(200), botonSalir);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        botonSalir.setOnMouseExited(e -> {

            ScaleTransition st = new ScaleTransition(Duration.millis(200), botonSalir);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    /**
     * Crea una animación de parpadeo suave para el título del juego
     */
    private void animarTitulo() {
        FadeTransition ft = new FadeTransition(Duration.seconds(2), tituloJuego);
        ft.setFromValue(0.2);
        ft.setToValue(1.0);
        ft.setCycleCount(FadeTransition.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
    }

    /**
     * Cambia a la pantalla del juego cuando se presiona "Jugar"
     */
    @FXML
    private void jugar() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interfaz/tablero-eightoff.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) botonJugar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Jugar");
            stage.setResizable(false);
            stage.setFullScreen(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la aplicación cuando se presiona "Salir"
     */
    @FXML
    private void salir() {
        Stage stage = (Stage) botonSalir.getScene().getWindow();
        stage.close();
    }
}