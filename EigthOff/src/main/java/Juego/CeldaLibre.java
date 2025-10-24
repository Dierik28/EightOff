package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

public class CeldaLibre {
    private ListaSimple<Carta> cartas;

    public CeldaLibre() {
        cartas = new ListaSimple<>();
    }

    public boolean agregarCarta(Carta carta) {
        if (cartas.estaVacia()) {
            cartas.insertarFin(carta);
            return true;
        }
        return false; // Solo puede tener una carta
    }

    public Carta peek() {
        return cartas.getFinal();
    }

    public Carta sacarCarta(int index) {
        if (!cartas.estaVacia()) {
            return cartas.eliminarFin();
        }
        return null;
    }

    public Carta verCarta(int index) {
        if (!cartas.estaVacia()) {
            return cartas.getFinal();
        }
        return null;
    }

    public boolean estaVacia() {
        return cartas.estaVacia();
    }

    public void clear() {
        cartas = new ListaSimple<>();
    }
}