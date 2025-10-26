package Cartas;

public enum Palo {
    TREBOL(1,"♣","negro","Treboles"),
    DIAMANTE(2,"♦","rojo","Diamantes"),
    CORAZON(3,"❤","rojo","Corazones"),
    PICA(4,"♠","negro","Picas");

    private final int peso;
    private final String figura;
    private final String color;
    private final String paloString;

    /**
     * Define un palo de cartas con su peso, símbolo, color y nombre
     */
    Palo(int peso, String figura, String color, String paloString) {
        this.peso = peso;
        this.figura = figura;
        this.color = color;
        this.paloString = paloString;
    }

    /**
     * Obtiene el peso para comparar palos
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Obtiene el símbolo del palo
     */
    public String getFigura() {
        return figura;
    }

    /**
     * Obtiene el color del palo
     */
    public String getColor() {
        return color;
    }
}