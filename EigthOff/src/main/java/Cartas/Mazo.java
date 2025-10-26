package Cartas;

import Listas.ListaDobleCircular;

public class Mazo {
    private ListaDobleCircular<Carta> mazo;

    /**
     * Crea un mazo nuevo, lo llena con 52 cartas y las mezcla
     */
    public Mazo() {
        mazo = new ListaDobleCircular<>();
        llenar();
        mezclar();
    }

    /**
     * Crea las 52 cartas del mazo
     * Todas las cartas empiezan boca arriba para Eight Off
     */
    private void llenar() {
        for (int i = 1; i <= 13; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa carta = new CartaInglesa(i, palo, palo.getColor());
                carta.makeFaceUp();
                mazo.insertarFin(carta);
            }
        }
    }
    /**
     * Mezcla las cartas para orden aleatorio
     */
    private void mezclar() {
        mazo.shuffle();
    }

    /**
     * Devuelve el mazo completo de cartas
     */
    public ListaDobleCircular<Carta> getMazo() {
        return mazo;
    }
}