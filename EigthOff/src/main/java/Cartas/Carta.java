package Cartas;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Carta implements Comparable <Carta> {

    private int valor;
    private String color;
    private String figura;
    private String ruta;


    public Carta(int valor, String figura, String color) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
        this.ruta = generarRuta();
    }


    private String generarRuta() {
        String nombreFigura;
        switch (figura) {
            case "♥" -> nombreFigura = "Corazones";
            case "♦" -> nombreFigura = "Diamantes";
            case "♣" -> nombreFigura = "Treboles";
            case "♠" -> nombreFigura = "Picas";
            default -> nombreFigura = "";
        }

        String nombreValor = switch (valor) {
            case 1 -> "As";
            case 11 -> "Joto";
            case 12 -> "Queen";
            case 13 -> "King";
            default -> String.valueOf(valor);
        };

        return "/Cartas/" + nombreFigura + "/" + nombreValor + ".png";
    }

    public Image getImagen() {
        return new Image(getClass().getResourceAsStream(ruta));
    }

    public ImageView getImageView(double ancho, double alto) {
        ImageView vista = new ImageView(getImagen());
        vista.setFitWidth(ancho);
        vista.setFitHeight(alto);
        vista.setPreserveRatio(true);
        return vista;
    }

    public int getValor() {
        return valor;
    }

    public String getColor() {
        return color;
    }

    public String getFigura() {
        return figura;
    }

    public int compareTo(Carta otraCarta) {
        if (this.valor != otraCarta.valor) {
            return Integer.compare(this.valor, otraCarta.valor);
        } else {
            return this.figura.compareTo(otraCarta.figura);
        }
    }

    @Override
    public String toString() {
        return valor + " " + figura + " " + color;
    }
}
