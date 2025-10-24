package Juego;

import Cartas.Carta;
import Cartas.Palo;
import Listas.ListaSimple;

public class Foundation {
    private ListaSimple<Carta> pila;
    private Palo palo;

    public Foundation(Palo palo) {
        this.palo = palo;
        pila = new ListaSimple<>();
    }

    public void agregarCarta(Carta carta) {
        pila.insertarFin(carta);
    }

    public Carta sacarCarta() {
        return pila.eliminarFin();
    }

    public Carta verUltimaCarta() {
        return pila.getFinal();
    }

    public boolean estaVacia() {
        return pila.estaVacia();
    }

    public Palo getPalo() {
        return palo;
    }
    public int getSize(){
        return pila.getSize();
    }

    public boolean puedeAgregar(Carta carta) {
        if (!carta.getFigura().equals(palo.getFigura())) return false;

        if (estaVacia()) {
            return carta.getValor() == 1;
        } else {
            Carta ultima = verUltimaCarta();
            return carta.getValor() == ultima.getValor() + 1;
        }
    }

    public ListaSimple<Carta> getPila() {
        return pila;
    }

    public int tamaño() {
        return pila.getSize();
    }
}
