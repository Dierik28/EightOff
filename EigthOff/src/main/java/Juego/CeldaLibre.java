package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

public class CeldaLibre {
    private ListaSimple<Carta>[] celdas;
    private final int CAPACIDAD = 8;

    public CeldaLibre() {
        celdas = new ListaSimple[CAPACIDAD];
        for (int i = 0; i < CAPACIDAD; i++) {
            celdas[i] = new ListaSimple<>();
        }
    }

    public boolean agregarCarta(Carta carta) {
        for (ListaSimple<Carta> celda : celdas) {
            if (celda.estaVacia()) {
                celda.insertarFin(carta);
                return true;
            }
        }
        return false;
    }

    public Carta sacarCarta(int index) {
        if (index < 0 || index >= CAPACIDAD) return null;
        return celdas[index].eliminarFin();
    }


    public Carta verCarta(int index) {
        if (index < 0 || index >= CAPACIDAD) return null;
        if (celdas[index].estaVacia()) return null;
        return celdas[index].getFinal();
    }


    public boolean hayCeldaLibre() {
        for (ListaSimple<Carta> celda : celdas) {
            if (celda.estaVacia()) return true;
        }
        return false;
    }

    public int capacidad() {
        return CAPACIDAD;
    }
}
