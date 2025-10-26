package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

public class CeldaLibre {
    private ListaSimple<Carta> cartas;

    /**
     * Crea una celda libre vacía para el juego
     */
    public CeldaLibre() {
        cartas = new ListaSimple<>();
    }

    /**
     * Agrega una carta solo si la celda está vacía
     * Devuelve true si pudo agregar, false si ya tenía carta
     */
    public boolean agregarCarta(Carta carta) {
        if (cartas.estaVacia()) {
            cartas.insertarFin(carta);
            return true;
        }
        return false;
    }

    /**
     * Mira la carta en la celda sin sacarla
     */
    public Carta peek() {
        return cartas.getFinal();
    }

    /**
     * Saca y devuelve la carta de la celda
     */
    public Carta sacarCarta(int index) {
        if (!cartas.estaVacia()) {
            return cartas.eliminarFin();
        }
        return null;
    }

    /**
     * Mira la carta en la celda sin sacarla
     */
    public Carta verCarta(int index) {
        if (!cartas.estaVacia()) {
            return cartas.getFinal();
        }
        return null;
    }

    /**
     * Verifica si la celda está vacía
     */
    public boolean estaVacia() {
        return cartas.estaVacia();
    }
}