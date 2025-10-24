// Mazo.java - Modificado para Eight Off
package Cartas;

import Listas.ListaDobleCircular;

public class Mazo {
    private ListaDobleCircular<Carta> mazo;

    public Mazo() {
        mazo = new ListaDobleCircular<>();
        llenar();
        mezclar();
    }

    private void llenar() {
        for (int i = 1; i <= 13; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa carta = new CartaInglesa(i, palo, palo.getColor());
                carta.makeFaceUp(); // En Eight Off, todas las cartas empiezan boca arriba
                mazo.insertarFin(carta);
            }
        }
    }

    private void mezclar() {
        mazo.shuffle();
    }

    public ListaDobleCircular<Carta> getMazo() {
        return mazo;
    }

    public Carta sacarCarta() {
        return mazo.eliminarInicio();
    }

    public boolean estaVacio() {
        return mazo.estaVacia();
    }

    public int cartasRestantes() {
        return mazo.tamaño();
    }
}