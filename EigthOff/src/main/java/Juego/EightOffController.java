package Juego;

import Cartas.Carta;
import Listas.ListaSimple;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;

public class EightOffController {

    @FXML private Button btnPista;
    @FXML private Button btnUndo;
    @FXML private Button btnSalir;

    @FXML private StackPane freeCell1, freeCell2, freeCell3, freeCell4;
    @FXML private StackPane freeCell5, freeCell6, freeCell7, freeCell8;

    @FXML private StackPane fundacionPicas;
    @FXML private StackPane fundacionCorazones;
    @FXML private StackPane fundacionDiamantes;
    @FXML private StackPane fundacionTreboles;


    @FXML private VBox columna1, columna2, columna3, columna4;
    @FXML private VBox columna5, columna6, columna7, columna8;

    @FXML private HBox freeCellsHBox;
    @FXML private HBox tableausHBox;
    @FXML private VBox foundationsVBox;

    @FXML private Label lblEstado;

    private EightOff juegoEightOff;
    private ArrayList<VBox> panelesColumnas;
    private ArrayList<StackPane> panelesFundaciones;
    private ArrayList<StackPane> panelesFreeCells;
    private Timeline hintTimeline;
    private StackPane panelPistaActual;
    private EstadoJuego pistaActual;

    @FXML
    private void initialize() {
        juegoEightOff = new EightOff();

        panelesColumnas = new ArrayList<>();
        panelesColumnas.add(columna1);
        panelesColumnas.add(columna2);
        panelesColumnas.add(columna3);
        panelesColumnas.add(columna4);
        panelesColumnas.add(columna5);
        panelesColumnas.add(columna6);
        panelesColumnas.add(columna7);
        panelesColumnas.add(columna8);

        panelesFundaciones = new ArrayList<>();
        panelesFundaciones.add(fundacionTreboles);
        panelesFundaciones.add(fundacionDiamantes);
        panelesFundaciones.add(fundacionCorazones);
        panelesFundaciones.add(fundacionPicas);

        panelesFreeCells = new ArrayList<>();
        panelesFreeCells.add(freeCell1);
        panelesFreeCells.add(freeCell2);
        panelesFreeCells.add(freeCell3);
        panelesFreeCells.add(freeCell4);
        panelesFreeCells.add(freeCell5);
        panelesFreeCells.add(freeCell6);
        panelesFreeCells.add(freeCell7);
        panelesFreeCells.add(freeCell8);


        btnUndo.setOnAction(e -> manejarUndo());
        btnPista.setOnAction(e -> manejarPista());
        btnSalir.setOnAction(e -> System.exit(0));

        configurarEventos();

        juegoEightOff.repartirCartasIniciales();
        actualizarInterfaz();
    }

    @FXML
    private void manejarPista() {

        limpiarPista();

        EstadoJuego nuevaPista = juegoEightOff.obtenerPista();

        if (nuevaPista != null) {
            pistaActual = nuevaPista;
            mostrarPista(pistaActual);

            hintTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> limpiarPista()));
            hintTimeline.play();
        } else {
            mostrarSinMovimientos();
        }
    }

    @FXML
    private void manejarUndo() {
        if (juegoEightOff.undo()) {
            actualizarInterfaz();
            lblEstado.setText("Movimiento deshecho");
            lblEstado.setStyle("-fx-text-fill: #87CEEB; -fx-font-weight: bold;");
        }
    }

    private void mostrarPista(EstadoJuego pista) {
        StackPane panelOrigen = obtenerPanelPorMovimiento(pista, true);
        if (panelOrigen != null) {
            panelOrigen.setEffect(new Glow(0.8));
            panelPistaActual = panelOrigen;

            String mensaje = obtenerMensajePista(pista);
            lblEstado.setText("Pista: " + mensaje);
            lblEstado.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");
        }
    }

    private String obtenerMensajePista(EstadoJuego pista) {
        switch (pista.getTipo()) {
            case TF: return "Mover de Columna " + (pista.getFromIdx() + 1) + " a Foundation";
            case CF: return "Mover de Celda libre " + (pista.getFromIdx() + 1) + " a Foundation";
            case CT: return "Mover de Celda libre " + (pista.getFromIdx() + 1) + " a Columna " + (pista.getToIdx() + 1);
            case TC: return "Mover de Columna " + (pista.getFromIdx() + 1) + " a Celda libre " + (pista.getToIdx() + 1);
            case TT: return "Mover de Columna " + (pista.getFromIdx() + 1) + " a Columna " + (pista.getToIdx() + 1);
            case FT: return "Mover de Foundation a Columna " + (pista.getToIdx() + 1);
            case FC: return "Mover de Foundation a Celda libre " + (pista.getToIdx() + 1);
            default: return "Movimiento disponible";
        }
    }

    private void limpiarPista() {
        if (panelPistaActual != null) {
            panelPistaActual.setEffect(null);
            panelPistaActual = null;
        }
        if (hintTimeline != null) {
            hintTimeline.stop();
        }

        if (lblEstado.getText().startsWith("Pista:")) {
            lblEstado.setStyle("-fx-text-fill: #90EE90; -fx-font-weight: normal;");
        }
    }

    private void mostrarSinMovimientos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sin movimientos");
        alert.setHeaderText("No hay movimientos disponibles");
        alert.setContentText("No hay más movimientos posibles en el juego actual.");
        alert.showAndWait();
    }

    private void configurarEventos() {

        for (int i = 0; i < panelesFreeCells.size(); i++) {
            StackPane celda = panelesFreeCells.get(i);
            configurarFreeCell(celda, i);
        }

        for (int i = 0; i < panelesColumnas.size(); i++) {
            configurarColumna(panelesColumnas.get(i), i);
        }

        for (int i = 0; i < panelesFundaciones.size(); i++) {
            configurarFundacion(panelesFundaciones.get(i), i);
        }
    }

    private void configurarFreeCell(StackPane freeCell, int indice) {
        freeCell.setOnDragDetected(e -> {
            Carta carta = juegoEightOff.getTopFreeCell(indice);
            if (carta != null) {
                Dragboard db = freeCell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("freecell-" + indice);
                db.setContent(content);
                e.consume();
            }
        });

        freeCell.setOnDragOver(e -> {
            if (e.getDragboard().hasString() &&
                    !e.getDragboard().getString().startsWith("freecell")) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        freeCell.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean exito = false;

            if (db.hasString()) {
                if (db.getString().startsWith("columna")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverTableauAFreeCell(origen, indice);
                } else if (db.getString().startsWith("foundation")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverFoundationAFreeCell(origen, indice);
                }

                if (exito) {
                    actualizarInterfaz();
                    verificarEstadoJuego();
                }
            }
            freeCell.setEffect(null);
            e.setDropCompleted(exito);
            e.consume();
        });
    }

    private void configurarColumna(VBox columna, int indice) {
        columna.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        columna.setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) columna.setEffect(new Glow(0.3));
            e.consume();
        });

        columna.setOnDragExited(e -> {
            columna.setEffect(null);
            e.consume();
        });

        columna.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean exito = false;

            if (db.hasString()) {
                if (db.getString().startsWith("freecell")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverFreeCellATableau(origen, indice);
                } else if (db.getString().startsWith("columna")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverCartaTableauATableau(origen, indice);
                } else if (db.getString().startsWith("foundation")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverFoundationATableau(origen, indice);
                }

                if (exito) {
                    actualizarInterfaz();
                    verificarEstadoJuego();
                    // Limpiar pista después de un movimiento exitoso
                    limpiarPista();
                    pistaActual = null;
                }
            }
            columna.setEffect(null);
            e.setDropCompleted(exito);
            e.consume();
        });
    }

    private void configurarFundacion(StackPane fundacion, int indice) {
        fundacion.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });

        fundacion.setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) fundacion.setEffect(new Glow(0.5));
            e.consume();
        });

        fundacion.setOnDragExited(e -> {
            fundacion.setEffect(null);
            e.consume();
        });

        fundacion.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean exito = false;

            if (db.hasString()) {
                if (db.getString().startsWith("freecell")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverFreeCellAFoundation(origen);
                } else if (db.getString().startsWith("columna")) {
                    int origen = Integer.parseInt(db.getString().split("-")[1]);
                    exito = juegoEightOff.moverTableauAFoundation(origen);
                }

                if (exito) {
                    actualizarInterfaz();
                    verificarEstadoJuego();
                    limpiarPista();
                    pistaActual = null;
                }
            }
            fundacion.setEffect(null);
            e.setDropCompleted(exito);
            e.consume();
        });

        fundacion.setOnDragDetected(e -> {
            Carta carta = juegoEightOff.getTopFoundation(indice);
            if (carta != null) {
                Dragboard db = fundacion.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("foundation-" + indice);
                db.setContent(content);
                e.consume();
            }
        });
    }


    private void actualizarInterfaz() {
        actualizarColumnas();
        actualizarFreeCells();
        actualizarFundaciones();
        verificarEstadoJuego();
    }

    private void actualizarColumnas() {
        for (int i = 0; i < panelesColumnas.size(); i++) {
            VBox columnaVBox = panelesColumnas.get(i);
            columnaVBox.getChildren().clear();

            Tableau tableau = juegoEightOff.getTableauObject(i);
            ListaSimple<Carta> cartas = tableau.getCartas();

            for (int j = 0; j < cartas.getSize(); j++) {
                Carta carta = cartas.getPosicion(j);
                Label cartaLabel = crearEtiquetaCarta(carta);

                if (j == cartas.getSize() - 1) {
                    configurarArrastreCarta(cartaLabel, i, "columna");
                }
                columnaVBox.getChildren().add(cartaLabel);
            }
        }
    }

    private void actualizarFreeCells() {
        for (int i = 0; i < panelesFreeCells.size(); i++) {
            StackPane freeCell = panelesFreeCells.get(i);
            freeCell.getChildren().clear();

            Carta carta = juegoEightOff.getTopFreeCell(i);
            if (carta != null) {
                Label cartaLabel = crearEtiquetaCarta(carta);
                configurarArrastreCarta(cartaLabel, i, "freecell");
                freeCell.getChildren().add(cartaLabel);
            }
        }
    }

    private void actualizarFundaciones() {
        for (int i = 0; i < panelesFundaciones.size(); i++) {
            StackPane fundacion = panelesFundaciones.get(i);
            fundacion.getChildren().clear();

            Carta carta = juegoEightOff.getTopFoundation(i);
            if (carta != null) {
                Label cartaLabel = crearEtiquetaCarta(carta);
                configurarArrastreCarta(cartaLabel, i, "foundation");
                fundacion.getChildren().add(cartaLabel);
            }
        }
    }
    private Label crearEtiquetaCarta(Carta carta) {
        String valor;
        switch (carta.getValor()) {
            case 1: valor = "A"; break;
            case 11: valor = "J"; break;
            case 12: valor = "Q"; break;
            case 13: valor = "K"; break;
            default: valor = String.valueOf(carta.getValor());
        }

        String palo = carta.getFigura();
        String color = carta.getColor().equals("rojo") ? "#DC143C" : "#000000";

        String textoCarta = valor + palo;

        Label cartaLabel = new Label(textoCarta);
        cartaLabel.setMinSize(70, 100);
        cartaLabel.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                        "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;" +
                        "-fx-background-radius: 4; -fx-border-radius: 8;" +
                        "-fx-alignment: top-left;" +
                        "-fx-padding: 5;"
        );

        return cartaLabel;
    }

    private void configurarArrastreCarta(Label cartaLabel, int indice, String tipo) {
        cartaLabel.setOnDragDetected(e -> {
            Dragboard db = cartaLabel.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tipo + "-" + indice);
            db.setContent(content);
            e.consume();
        });
    }

    private void verificarEstadoJuego() {
        if (juegoEightOff.evaluarVictoria()) {
            verificarVictoria();
        } else if (!juegoEightOff.hayMovimientosDisponibles()) {
            lblEstado.setText("¡Sin movimientos! Juego terminado.");
            lblEstado.setStyle("-fx-text-fill: #FF6B6B; -fx-font-weight: bold;");
        }
    }

    private void verificarVictoria() {
        lblEstado.setText("¡Felicidades! ¡Has ganado!");
        lblEstado.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 18px;");

        try {
            Image imagenVictoria = new Image(getClass().getResourceAsStream("/imagenes/victoria.png"));
            ImageView imageView = new ImageView(imagenVictoria);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            lblEstado.setGraphic(imageView);
        } catch (Exception e) {

        }
    }

    private StackPane obtenerPanelPorMovimiento(EstadoJuego pista, boolean esOrigen) {
        int indice = esOrigen ? pista.getFromIdx() : pista.getToIdx();

        switch (esOrigen ? pista.getFrom() : pista.getTo()) {
            case TABLEAU:
                return null;
            case FREE_CELL:
                return panelesFreeCells.get(indice);
            case FOUNDATION:
                return panelesFundaciones.get(indice);
            default:
                return null;
        }
    }
}