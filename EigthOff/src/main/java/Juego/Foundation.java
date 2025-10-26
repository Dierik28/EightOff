package Juego;

import Cartas.Carta;
import Cartas.Palo;
import Listas.ListaSimple;

public class Foundation {
    private ListaSimple<Carta> pila;
    private Palo palo;

    /**
     * Crea una fundación para un palo específico
     */
    public Foundation(Palo palo) {
        this.palo = palo;
        pila = new ListaSimple<>();
    }

    /**
     * Agrega una carta a la fundación y la pone boca arriba
     */
    public void agregarCarta(Carta carta) {
        pila.insertarFin(carta);
        carta.makeFaceUp();
    }

    /**
     * Saca y devuelve la última carta de la fundación
     */
    public Carta sacarCarta() {
        return pila.eliminarFin();
    }

    /**
     * Mira la última carta sin sacarla
     */
    public Carta verUltimaCarta() {
        if (pila.estaVacia()) {
            return null;
        }
        return pila.getFinal();
    }

    /**
     * Verifica si la fundación está vacía
     */
    public boolean estaVacia() {
        return pila.estaVacia();
    }

    /**
     * Obtiene el palo de esta fundación
     */
    public Palo getPalo() {
        return palo;
    }

    /**
     * Verifica si una carta puede ser agregada a esta fundación
     * - Debe ser del mismo palo
     * - Si está vacía: solo puede ser un As (valor 1)
     * - Si no está vacía: debe ser el siguiente valor en secuencia
     */
    public boolean puedeAgregar(Carta carta) {

        if (!carta.getFigura().equals(palo.getFigura())) return false;

        if (estaVacia()) {

            return carta.getValor() == 1;
        } else {
            Carta ultima = verUltimaCarta();

            return carta.getValor() == ultima.getValor() + 1;
        }
    }

    /**
     * Verifica si la fundación está completa
     */
    public boolean estaCompleta() {
        return pila.getSize() == 13;
    }

    /**
     * Limpia todas las cartas de la fundación
     */
    public void clear() {
        while(pila.eliminarFin() != null) {}
    }
}