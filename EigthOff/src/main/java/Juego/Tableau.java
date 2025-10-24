package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

import java.util.Objects;

public class Tableau {
    private final ListaSimple<Carta> cartas = new ListaSimple<>();
    private final boolean soloRey = false;

    public int size() {
        return cartas.getSize();
    }

    public boolean estaVacia() {
        return cartas.estaVacia();
    }

    public Carta peek() {
        return cartas.getFinal();
    }

    public Carta pop() {
        return cartas.eliminarFin();
    }

    public void clear() {
        while (!estaVacia()) {
            pop();
        }
    }

    public void pushInicial(Carta carta) {
        cartas.insertarFin(carta);
    }

    private boolean esMovimientoValido(Carta carta) {
        if (carta == null) return false;

        Carta top = peek();
        if (top == null) {
            return !soloRey || carta.getValor() == 13;
        }

        return Objects.equals(top.getFigura(), carta.getFigura()) && top.getValor() - 1 == carta.getValor();
    }

    public boolean push(Carta carta) {
        if (!esMovimientoValido(carta)) return false;
        cartas.insertarFin(carta);
        return true;
    }

    public boolean escaleraValida(ListaSimple<Carta> run) {
        int n = run.getSize();
        if (n == 0) return false;

        for (int i = 1; i < n; i++) {
            Carta abajo = run.getPosicion(i - 1);
            Carta arriba = run.getPosicion(i);
            if (abajo.getFigura() != arriba.getFigura()) return false;
            if (abajo.getValor() - 1 != arriba.getValor()) return false;
        }
        return true;
    }

    public boolean puedoColocarEscalera(ListaSimple<Carta> run) {
        if (!escaleraValida(run)) return false;
        return esMovimientoValido(run.getPosicion(0));
    }

    public boolean pushEscalera(ListaSimple<Carta> run) {
        if (!puedoColocarEscalera(run)) return false;
        for (int i = 0; i < run.getSize(); i++) {
            cartas.insertarFin(run.getPosicion(i));
        }
        return true;
    }

    public ListaSimple<Carta> getCartas() {
        return cartas;
    }

    public ListaSimple<Carta> topN(int n) {
        ListaSimple<Carta> result = new ListaSimple<>();
        int size = size();
        if (n <= 0 || n > size) return result;

        for (int i = size - n; i < size; i++) {
            result.insertarFin(cartas.getPosicion(i));
        }
        return result;
    }

    public ListaSimple<Carta> popN(int n) {
        ListaSimple<Carta> result = topN(n);
        for (int i = 0; i < n; i++) pop();
        return result;
    }

    public void pushAllInicial(ListaSimple<Carta> run) {
        for (int i = 0; i < run.getSize(); i++) {
            cartas.insertarFin(run.getPosicion(i));
        }
    }
}
