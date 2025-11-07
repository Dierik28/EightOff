package Juego;

import Cartas.Carta;
import Listas.ListaSimple;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;

public class EightOffController {
    // Botones de la interfaz gráfica
    @FXML private Button btnPista;
    @FXML private Button btnUndo;
    @FXML private Button btnSalir;
    @FXML private Button btnHistorial;
    @FXML private Button btnAplicarHistorial;
    @FXML private Button btnUndoHistorial;
    @FXML private Button btnRedoHistorial;

    // Elementos de la interfaz gráfica
    @FXML private StackPane freeCell1, freeCell2, freeCell3, freeCell4;
    @FXML private StackPane freeCell5, freeCell6, freeCell7, freeCell8;
    @FXML private StackPane fundacionPicas, fundacionCorazones, fundacionDiamantes, fundacionTreboles;
    @FXML private VBox columna1, columna2, columna3, columna4;
    @FXML private VBox columna5, columna6, columna7, columna8;
    @FXML private HBox freeCellsHBox, tableausHBox;
    @FXML private VBox foundationsVBox;
    @FXML private Label lblEstado;
    @FXML private Label lblPosicionHistorial;
    @FXML private VBox panelHistorial;

    private EightOff juegoEightOff;
    private ArrayList<VBox> panelesColumnas;
    private ArrayList<StackPane> panelesFundaciones;
    private ArrayList<StackPane> panelesFreeCells;
    private Timeline hintTimeline;
    private StackPane panelPistaActual;
    private EstadoJuego pistaActual;
    private boolean juegoTerminado = false;

    /**
     * Inicializa el controlador: crea el juego, configura listas y eventos
     */
    @FXML
    private void initialize() {
        juegoEightOff = new EightOff();

        panelesColumnas = new ArrayList<>();
        panelesColumnas.add(columna1); panelesColumnas.add(columna2);
        panelesColumnas.add(columna3); panelesColumnas.add(columna4);
        panelesColumnas.add(columna5); panelesColumnas.add(columna6);
        panelesColumnas.add(columna7); panelesColumnas.add(columna8);

        panelesFundaciones = new ArrayList<>();
        panelesFundaciones.add(fundacionTreboles); panelesFundaciones.add(fundacionDiamantes);
        panelesFundaciones.add(fundacionCorazones); panelesFundaciones.add(fundacionPicas);

        panelesFreeCells = new ArrayList<>();
        panelesFreeCells.add(freeCell1); panelesFreeCells.add(freeCell2);
        panelesFreeCells.add(freeCell3); panelesFreeCells.add(freeCell4);
        panelesFreeCells.add(freeCell5); panelesFreeCells.add(freeCell6);
        panelesFreeCells.add(freeCell7); panelesFreeCells.add(freeCell8);

        btnUndo.setOnAction(e -> manejarUndo());
        btnPista.setOnAction(e -> manejarPista());
        btnSalir.setOnAction(e -> System.exit(0));
        btnHistorial.setOnAction(e -> activarModoHistorial());
        btnAplicarHistorial.setOnAction(e -> aplicarHistorial());
        btnUndoHistorial.setOnAction(e -> undoHistorial());
        btnRedoHistorial.setOnAction(e -> redoHistorial());

        configurarEventos();
        juegoEightOff.repartirCartasIniciales();
        actualizarInterfaz();
        panelHistorial.setVisible(false);
    }

    /**
     * Activa el modo historial
     */
    private void activarModoHistorial() {
        if (juegoTerminado) return;

        if (!juegoEightOff.sePuedeActivarHistorial()) {
            System.out.println("Historial Vacío");
            return;
        }

        try {
            juegoEightOff.activarModoHistorial();
            panelHistorial.setVisible(true);
            actualizarControlesHistorial();
            actualizarEstadoControles();
            resaltarMovimientoActual();
            lblEstado.setText("Modo historial");
            lblEstado.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");

            System.out.println("Historial activado. Total movimientos: " +
                    juegoEightOff.getHistorial().getTotalMovimientos());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error, No se pudo activar el modo historial: " + e.getMessage());
        }
    }

    /**
     * Aplica el estado del historial al juego
     */
    private void aplicarHistorial() {
        try {
            juegoEightOff.aplicarHistorial();
            panelHistorial.setVisible(false);
            actualizarInterfaz();
            actualizarEstadoControles();
            lblEstado.setText("Historial aplicado");
            lblEstado.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error, No se pudo aplicar el historial: " + e.getMessage());
        }
    }


    /**
     * Retrocede en el historial
     */
    private void undoHistorial() {
        if (juegoEightOff.undoDesdeHistorial()) {
            actualizarInterfaz();
            actualizarControlesHistorial();
            resaltarMovimientoActual();
        }
    }

    /**
     * Avanza en el historial
     */
    private void redoHistorial() {
        if (juegoEightOff.redoDesdeHistorial()) {
            actualizarInterfaz();
            actualizarControlesHistorial();
            resaltarMovimientoActual();
        }
    }

    /**
     * Actualiza los controles del historial
     */
    private void actualizarControlesHistorial() {
        HistorialJuego historial = juegoEightOff.getHistorial();
        int posActual = historial.getPosicionActual() + 1;
        int total = historial.getTotalMovimientos();

        lblPosicionHistorial.setText(posActual + " / " + total);

        btnUndoHistorial.setDisable(historial.getPosicionActual() < 0);
        btnRedoHistorial.setDisable(historial.getPosicionActual() >= total - 1);
    }

    /**
     * Resalta las cartas involucradas en el movimiento actual del historial
     */
    private void resaltarMovimientoActual() {
        if (!juegoEightOff.getHistorial().isModoHistorial()) return;

        EstadoJuego movimiento = juegoEightOff.getHistorial().getMovimientoActual();
        if (movimiento == null) return;

        limpiarResaltados();

        switch (movimiento.getTipo()) {
            case TT -> {

                resaltarCartaMovidaTableau(movimiento.getFromIdx(), movimiento.getCarta());
                resaltarColumnaTableau(movimiento.getToIdx());
            }
            case TC -> {

                resaltarCartaMovidaTableau(movimiento.getFromIdx(), movimiento.getCarta());
                resaltarFreeCell(movimiento.getToIdx());
            }
            case TF -> {

                resaltarCartaMovidaTableau(movimiento.getFromIdx(), movimiento.getCarta());
                resaltarFundacion(movimiento.getToIdx());
            }
            case CT -> {

                resaltarFreeCell(movimiento.getFromIdx());
                resaltarColumnaTableau(movimiento.getToIdx());
            }
            case CF -> {
                resaltarFreeCell(movimiento.getFromIdx());
                resaltarFundacion(movimiento.getToIdx());
            }
            case FT -> {
                resaltarFundacion(movimiento.getFromIdx());
                resaltarColumnaTableau(movimiento.getToIdx());
            }
            case FC -> {
                resaltarFundacion(movimiento.getFromIdx());
                resaltarFreeCell(movimiento.getToIdx());
            }
        }
    }

    /**
     * Resalta la carta específica que se movió en una columna del tableau
     */
    private void resaltarCartaMovidaTableau(int indiceTableau, Carta cartaMovida) {
        if (indiceTableau >= 0 && indiceTableau < panelesColumnas.size()) {
            VBox columna = panelesColumnas.get(indiceTableau);

            for (javafx.scene.Node node : columna.getChildren()) {
                if (node instanceof Label) {
                    Label cartaLabel = (Label) node;
                    String textoCarta = cartaLabel.getText();
                    if (representaCarta(textoCarta, cartaMovida)) {
                        cartaLabel.setStyle(cartaLabel.getStyle() + " -fx-effect: dropshadow(gaussian, #FFD700, 20, 0.5, 0, 0); -fx-border-color: #FFD700; -fx-border-width: 2;");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Resalta una columna del tableau completa
     */
    private void resaltarColumnaTableau(int indiceTableau) {
        if (indiceTableau >= 0 && indiceTableau < panelesColumnas.size()) {
            VBox columna = panelesColumnas.get(indiceTableau);
            columna.setStyle("-fx-background-color: rgba(255, 215, 0, 0.2); -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #FFD700; -fx-border-width: 2;");
        }
    }

    /**
     * Resalta una celda libre
     */
    private void resaltarFreeCell(int indiceFreeCell) {
        if (indiceFreeCell >= 0 && indiceFreeCell < panelesFreeCells.size()) {
            StackPane freeCell = panelesFreeCells.get(indiceFreeCell);
            freeCell.setStyle("-fx-border-color: #FFD700; -fx-border-width: 3; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: rgba(255, 215, 0, 0.3);");
        }
    }

    /**
     * Resalta una fundación
     */
    private void resaltarFundacion(int indiceFundacion) {
        if (indiceFundacion >= 0 && indiceFundacion < panelesFundaciones.size()) {
            StackPane fundacion = panelesFundaciones.get(indiceFundacion);
            fundacion.setStyle("-fx-border-color: #FFD700; -fx-border-width: 3; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: rgba(255, 215, 0, 0.3);");
        }
    }

    /**
     * Verifica si el texto de una etiqueta representa la carta especificada
     */
    private boolean representaCarta(String textoEtiqueta, Carta carta) {
        if (textoEtiqueta == null || carta == null) return false;

        String valor;
        switch (carta.getValor()) {
            case 1: valor = "A"; break;
            case 11: valor = "J"; break;
            case 12: valor = "Q"; break;
            case 13: valor = "K"; break;
            default: valor = String.valueOf(carta.getValor());
        }

        String textoEsperado = valor + carta.getFigura();
        return textoEtiqueta.equals(textoEsperado);
    }

    /**
     * Resalta una carta en el tableau
     */
    private void resaltarCartaTableau(int indiceTableau) {
        if (indiceTableau >= 0 && indiceTableau < panelesColumnas.size()) {
            VBox columna = panelesColumnas.get(indiceTableau);
            if (!columna.getChildren().isEmpty()) {
                Label ultimaCarta = (Label) columna.getChildren().get(columna.getChildren().size() - 1);
                ultimaCarta.setStyle(ultimaCarta.getStyle() + " -fx-effect: dropshadow(gaussian, #FFD700, 20, 0.5, 0, 0);");
            }
        }
    }

    /**
     * Limpia todos los resaltados
     */
    private void limpiarResaltados() {
        for (StackPane freeCell : panelesFreeCells) {
            freeCell.setStyle("-fx-border-color: rgba(255,255,255,0.8); -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: rgba(144, 238, 144, 0.3);");
        }
        for (StackPane fundacion : panelesFundaciones) {
            fundacion.setStyle("-fx-border-color: rgba(255,255,255,0.8); -fx-border-width: 2; -fx-border-radius: 12; -fx-background-radius: 12; -fx-background-color: rgba(144, 238, 144, 0.3);");
        }

        for (VBox columna : panelesColumnas) {
            columna.setStyle("-fx-background-color: rgba(144, 238, 144, 0.2); -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: rgba(255,255,255,0.5); -fx-border-width: 1;");

            for (javafx.scene.Node node : columna.getChildren()) {
                if (node instanceof Label) {
                    Label cartaLabel = (Label) node;
                    String estiloOriginal = cartaLabel.getStyle();
                    estiloOriginal = estiloOriginal.replaceAll("-fx-effect:[^;]*;", "");
                    estiloOriginal = estiloOriginal.replaceAll("-fx-border-color:[^;]*;", "-fx-border-color: black;");
                    estiloOriginal = estiloOriginal.replaceAll("-fx-border-width:[^;]*;", "-fx-border-width: 1;");
                    cartaLabel.setStyle(estiloOriginal);
                }
            }
        }
    }


    /**
     * Muestra una pista al usuario resaltando un movimiento posible
     */
    @FXML
    private void manejarPista() {
        if (juegoEightOff.getHistorial().isModoHistorial()) {
            System.out.println("La función de pista no está disponible en modo historial.");
            return;
        }

        limpiarPista();
        if (juegoTerminado) return;

        EstadoJuego nuevaPista = juegoEightOff.obtenerPista();
        if (nuevaPista != null) {
            pistaActual = nuevaPista;
            mostrarPista(pistaActual);
            hintTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> limpiarPista()));
            hintTimeline.play();
        } else {
            mostrarVentanaSinMovimientos();
        }
    }

    /**
     * Deshace el último movimiento realizado
     */
    @FXML
    private void manejarUndo() {
        if (juegoTerminado) return;

        if (juegoEightOff.getHistorial().isModoHistorial()) {
            undoHistorial();
        } else {
            if (juegoEightOff.undo()) {
                actualizarInterfaz();
                lblEstado.setText("Movimiento deshecho");
                lblEstado.setStyle("-fx-text-fill: #87CEEB; -fx-font-weight: bold;");
            }
        }
    }


    /**
     * Muestra un mensaje descriptivo de la pista encontrada
     */
    private void mostrarPista(EstadoJuego pista) {
        if (pista != null) {
            String mensaje = obtenerMensajePista(pista);
            lblEstado.setText("Pista: " + mensaje);
            lblEstado.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");
        }
    }

    /**
     * Convierte el tipo de movimiento en un mensaje legible
     */
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

    /**
     * Limpia cualquier efecto de pista activo
     */
    private void limpiarPista() {
        if (panelPistaActual != null) {
            panelPistaActual.setEffect(null);
            panelPistaActual = null;
        }
        if (hintTimeline != null) {
            hintTimeline.stop();
        }
    }

    /**
     * Configura los eventos de arrastrar y soltar para todos los componentes
     */
    private void configurarEventos() {
        for (int i = 0; i < panelesFreeCells.size(); i++) {
            configurarFreeCell(panelesFreeCells.get(i), i);
        }
        for (int i = 0; i < panelesColumnas.size(); i++) {
            configurarColumna(panelesColumnas.get(i), i);
        }
        for (int i = 0; i < panelesFundaciones.size(); i++) {
            configurarFundacion(panelesFundaciones.get(i), i);
        }
    }

    /**
     * Configura eventos para una celda libre
     */
    private void configurarFreeCell(StackPane freeCell, int indice) {
        freeCell.setOnDragDetected(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

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
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            if (e.getDragboard().hasString() && !e.getDragboard().getString().startsWith("freecell")) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        freeCell.setOnDragDropped(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.setDropCompleted(false);
                e.consume();
                return;
            }

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

    /**
     * Configura eventos para una columna del tableau
     */
    private void configurarColumna(VBox columna, int indice) {
        columna.setOnDragDetected(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }
            if (!juegoEightOff.getTableauObject(indice).estaVacia()) {
                Dragboard db = columna.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("columna-" + indice);
                db.setContent(content);
                e.consume();
            }
        });

        columna.setOnDragOver(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        columna.setOnDragEntered(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            if (e.getDragboard().hasString()) {
                columna.setEffect(new Glow(0.3));
            }
            e.consume();
        });

        columna.setOnDragExited(e -> {
            columna.setEffect(null);
            e.consume();
        });

        columna.setOnDragDropped(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.setDropCompleted(false);
                e.consume();
                return;
            }

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
                    limpiarPista();
                    pistaActual = null;
                }
            }
            columna.setEffect(null);
            e.setDropCompleted(exito);
            e.consume();
        });
    }

    /**
     * Configura eventos para una fundación
     */
    private void configurarFundacion(StackPane fundacion, int indice) {
        fundacion.setOnDragDetected(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            Carta carta = juegoEightOff.getTopFoundation(indice);
            if (carta != null) {
                Dragboard db = fundacion.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString("foundation-" + indice);
                db.setContent(content);
                e.consume();
            }
        });

        fundacion.setOnDragOver(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        fundacion.setOnDragEntered(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.consume();
                return;
            }

            if (e.getDragboard().hasString()) {
                fundacion.setEffect(new Glow(0.5));
            }
            e.consume();
        });

        fundacion.setOnDragExited(e -> {
            fundacion.setEffect(null);
            e.consume();
        });

        fundacion.setOnDragDropped(e -> {
            if (juegoEightOff.getHistorial().isModoHistorial()) {
                e.setDropCompleted(false);
                e.consume();
                return;
            }

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
    }

    /**
     * Deshabilita todos los controles de juego cuando está en modo historial
     */
    private void actualizarEstadoControles() {
        boolean modoHistorial = juegoEightOff.getHistorial().isModoHistorial();

        btnPista.setDisable(modoHistorial);
        btnUndo.setDisable(modoHistorial);
        if (modoHistorial) {
            lblEstado.setText("Modo historial - Solo visualización");
            lblEstado.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
        }
    }


    /**
     * Actualiza toda la interfaz gráfica
     */
    private void actualizarInterfaz() {
        if (juegoTerminado) return;

        actualizarColumnas();
        actualizarFreeCells();
        actualizarFundaciones();
        actualizarEstadoControles();

        if (juegoEightOff.getHistorial().isModoHistorial()) {
            resaltarMovimientoActual();
        } else {
            limpiarResaltados();
            verificarEstadoJuego();
        }
    }


    /**
     * Actualiza las columnas del tableau
     */
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

    /**
     * Actualiza las celdas libres
     */
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

    /**
     * Actualiza las fundaciones
     */
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

    /**
     * Crea una etiqueta visual para representar una carta
     */
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
        cartaLabel.setMinSize(110, 150);
        cartaLabel.setStyle(
                "-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                        "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1;" +
                        "-fx-background-radius: 4; -fx-border-radius: 8;" +
                        "-fx-alignment: top-left; -fx-padding: 5;"
        );

        return cartaLabel;
    }

    /**
     * Configura una etiqueta de carta para ser arrastrable
     */
    private void configurarArrastreCarta(Label cartaLabel, int indice, String tipo) {
        cartaLabel.setOnDragDetected(e -> {
            Dragboard db = cartaLabel.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(tipo + "-" + indice);
            db.setContent(content);
            e.consume();
        });
    }

    /**
     * Verifica el estado del juego (victoria o sin movimientos)
     */
    private void verificarEstadoJuego() {
        if (juegoTerminado) return;

        if (juegoEightOff.evaluarVictoria()) {
            mostrarVentanaVictoria();
        } else if (!juegoEightOff.hayMovimientosDisponibles()) {
            mostrarVentanaSinMovimientos();
        }
    }

    /**
     * Muestra pantalla de victoria cuando el juego es ganado
     */
    private void mostrarVentanaVictoria() {
        if (juegoTerminado) return;
        juegoTerminado = true;

        lblEstado.setText("¡Felicidades! ¡Has ganado!");
        lblEstado.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-font-size: 18px;");

        try {
            Stage ventanaVictoria = new Stage(StageStyle.UNDECORATED);
            ventanaVictoria.initModality(Modality.APPLICATION_MODAL);
            Image imagenVictoria = new Image(getClass().getResourceAsStream("/imagenes/victoria.png"));
            ImageView imageView = new ImageView(imagenVictoria);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(400);

            Label mensaje = new Label(" ¡HAS GANADO! ");
            mensaje.setStyle("-fx-text-fill: white; -fx-font-size: 64px; -fx-font-weight: bold;");

            Button cerrarBtn = new Button("Salir del juego");
            cerrarBtn.setStyle("-fx-font-size: 24px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            cerrarBtn.setOnAction(e -> {
                try {
                    ventanaVictoria.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/pantallaInicio.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) lblEstado.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            VBox layout = new VBox(40);
            layout.setStyle("-fx-background-color: black; -fx-alignment: center;");
            layout.getChildren().addAll(imageView, mensaje, cerrarBtn);

            Scene escena = new Scene(layout);
            ventanaVictoria.setScene(escena);
            ventanaVictoria.setFullScreen(true);
            ventanaVictoria.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Muestra pantalla cuando el jugador se queda sin movimientos
     */

    private void mostrarVentanaSinMovimientos() {
        if (juegoTerminado) return;
        juegoTerminado = true;

        lblEstado.setText("¡Sin movimientos! Juego terminado.");
        lblEstado.setStyle("-fx-text-fill: #FF6B6B; -fx-font-weight: bold;");

        Stage ventanaFin = new Stage(StageStyle.DECORATED);
        ventanaFin.initModality(Modality.WINDOW_MODAL);

        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: black; -fx-alignment: center; -fx-padding: 30;");

        Label mensaje = new Label("¡NO QUEDAN MOVIMIENTOS!");
        mensaje.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Button cerrarBtn = new Button("Regresar al menú");
        cerrarBtn.setStyle("-fx-font-size: 18px; -fx-background-color: #FF6B6B; -fx-text-fill: white;");
        cerrarBtn.setOnAction(e -> {
            try {
                ventanaFin.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/pantallaInicio.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) lblEstado.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(mensaje, cerrarBtn);

        Scene escena = new Scene(layout, 500, 250);
        ventanaFin.setScene(escena);
        ventanaFin.setResizable(false);
        ventanaFin.show();
    }
}