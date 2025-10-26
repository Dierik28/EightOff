package Cartas;

public abstract class Carta implements Comparable<Carta> {

    /**
     * Atributos de la clase carta
     */

    private int valor;
    protected Palo palo;
    private boolean faceUp;
    private String color;

    /**
     * Construye una carta con valor, palo y color
     * La carta nace boca arriba
     */
    public Carta(int valor, Palo palo, String color) {
        this.valor = valor;
        this.palo = palo;
        this.color = color;
        this.faceUp = true;
    }

    /**
     * Obtiene el número de la carta (1-13)
     */
    public int getValor() {
        return valor;
    }

    /**
     * Obtiene el color desde el palo
     */
    public String getColor() {
        return palo.getColor();
    }

    /**
     * Obtiene la figura desde el palo
     */
    public String getFigura() {
        return palo.getFigura();
    }

    /**
     * Voltea la carta para que sea visible
     */
    public void makeFaceUp(){
        faceUp = true;
    }

    /**
     * Compara cartas: primero por valor, si empatan por peso del palo
     */
    @Override
    public int compareTo(Carta otraCarta) {
        if (this.valor != otraCarta.valor) {
            return Integer.compare(this.valor, otraCarta.valor);
        } else {
            return Integer.compare(this.palo.getPeso(), otraCarta.palo.getPeso());
        }
    }
    /**
     * Muestra la carta como texto: "valor figura color"
     */
    @Override
    public String toString() {
        return valor + " " + palo.getFigura() + " " + palo.getColor();
    }
}