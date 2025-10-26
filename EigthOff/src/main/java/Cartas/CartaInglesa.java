package Cartas;

public class CartaInglesa extends Carta {

    /**
     * Crea una carta inglesa con valor, palo y color
     */
    public CartaInglesa(int valor, Palo figura, String color) {
        super(valor, figura, color);
    }

    /**
     * Compara esta carta con otra para ver cuál es mayor
     * - Si tienen mismo valor y color: son iguales
     * - Si mismo valor pero distinto palo: compara por peso del palo
     * - Si distinto valor: compara por valor numérico
     */
    @Override
    public int compareTo(Carta o) {
        // Ambas cartas son iguales
        if (getValor() == o.getValor() ) {
            if (getColor().equals(o.getColor())) {
                return 0;
            } else {
                // Tienen mismo valor, distinto palo
                return palo.getPeso() - o.palo.getPeso();
            }
        }
        // Ambas cartas tienen el distinto valor
        return getValor() - o.getValor();
    }
}