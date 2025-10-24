package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

public class Tableau {
    private final ListaSimple<Carta> cartas = new ListaSimple<>();

    public int size() {
        return cartas.getSize();
    }

    public boolean estaVacia() {
        return cartas.estaVacia();
    }

    public Carta peek() {
        return cartas.getFinal();
    }

    public Carta getCarta(int i) {
        if (i < 0 || i >= cartas.getSize()) return null;
        return cartas.getPosicion(i);
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
        carta.makeFaceUp();
    }

    private boolean esMovimientoValido(Carta carta) {
        if (carta == null) return false;

        Carta top = peek();
        if (top == null) {
            return carta.getValor() == 13; // 13 = King (K)
        }

        boolean mismoPalo = top.getFigura().equals(carta.getFigura());
        boolean ordenDescendente = top.getValor() == carta.getValor() + 1;

        return mismoPalo && ordenDescendente;
    }

    public boolean esEscaleraValidaDesde(int i) {
        int size = cartas.getSize();
        if (size == 0 || i < 0 || i >= size - 1) return true;

        for (int j = i; j < size - 1; j++) {
            Carta actual = cartas.getPosicion(j);
            Carta siguiente = cartas.getPosicion(j + 1);

            boolean paloIgual = actual.getFigura().equals(siguiente.getFigura());
            boolean valorDescendente = actual.getValor() == siguiente.getValor() + 1;

            if (!(paloIgual && valorDescendente)) {
                return false;
            }
        }
        return true;
    }

    public boolean puedoColocarCarta(Carta carta) {
        return esMovimientoValido(carta);
    }

    public boolean push(Carta carta) {
        if (!esMovimientoValido(carta)) return false;
        cartas.insertarFin(carta);
        carta.makeFaceUp();
        return true;
    }

    public boolean escaleraValida(ListaSimple<Carta> run) {
        int n = run.getSize();
        if (n == 0) return false;

        if (n == 1) {
            return true;
        }

        for (int i = 0; i < n - 1; i++) {
            Carta cartaActual = run.getPosicion(i);
            Carta cartaSiguiente = run.getPosicion(i + 1);
            if (!cartaActual.getFigura().equals(cartaSiguiente.getFigura())) {
                return false;
            }
            if (cartaActual.getValor() != cartaSiguiente.getValor() + 1) {
                return false;
            }
        }
        return true;
    }

    public boolean puedoColocarEscalera(ListaSimple<Carta> run) {
        if (!escaleraValida(run)) return false;
        Carta primeraCarta = run.getPosicion(0);
        return esMovimientoValido(primeraCarta);
    }

    public boolean pushEscalera(ListaSimple<Carta> run) {
        if (!puedoColocarEscalera(run)) return false;
        for (int i = 0; i < run.getSize(); i++) {
            Carta carta = run.getPosicion(i);
            cartas.insertarFin(carta);
            carta.makeFaceUp();
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
            Carta carta = run.getPosicion(i);
            cartas.insertarFin(carta);
            carta.makeFaceUp();
        }
    }

    public ListaSimple<Carta> getCartasVisibles() {
        ListaSimple<Carta> visibles = new ListaSimple<>();
        for (int i = 0; i < cartas.getSize(); i++) {
            Carta carta = cartas.getPosicion(i);
            if (carta.isFaceUp()) {
                visibles.insertarFin(carta);
            }
        }
        return visibles;
    }
}