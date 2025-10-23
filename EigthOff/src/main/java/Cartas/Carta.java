package Cartas;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Carta implements Comparable<Carta> {

    private int valor;
    protected Palo palo;
    private String ruta;
    private boolean faceUp;
    private String color;

    public Carta(int valor, Palo palo, String color) {
        this.valor = valor;
        this.palo = palo;
        this.color = color;
        this.ruta = generarRuta();
        this.faceUp = false;
    }

    private String generarRuta() {
        String nombreFigura = palo.getPaloString();

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
        return palo.getColor();
    }

    public String getFigura() {
        return palo.getFigura();
    }

    public void makeFaceDown(){
        faceUp = false;
    }

    public void makeFaceUp(){
        faceUp = true;
    }

    public boolean isFaceUp(){
        return faceUp;
    }

    @Override
    public int compareTo(Carta otraCarta) {
        if (this.valor != otraCarta.valor) {
            return Integer.compare(this.valor, otraCarta.valor);
        } else {
            return Integer.compare(this.palo.getPeso(), otraCarta.palo.getPeso());
        }
    }

    @Override
    public String toString() {
        return valor + " " + palo.getFigura() + " " + palo.getColor();
    }
}
