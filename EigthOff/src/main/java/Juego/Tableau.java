package Juego;

import Cartas.Carta;
import Listas.ListaSimple;

public class Tableau {
    private final ListaSimple<Carta> cartas = new ListaSimple<>();

    /**
     * Devuelve la cantidad de cartas en esta columna
     */
    public int size() {
        return cartas.getSize();
    }

    /**
     * Verifica si la columna está vacía
     */
    public boolean estaVacia() {
        return cartas.estaVacia();
    }

    /**
     * Mira la carta superior sin sacarla
     */
    public Carta peek() {
        return cartas.getFinal();
    }

    /**
     * Obtiene una carta en una posición específica
     */
    public Carta getCarta(int i) {
        if (i < 0 || i >= cartas.getSize()) return null;
        return cartas.getPosicion(i);
    }

    /**
     * Saca y devuelve la carta superior
     */
    public Carta pop() {
        return cartas.eliminarFin();
    }

    /**
     * Limpia todas las cartas de la columna
     */
    public void clear() {
        while (!estaVacia()) {
            pop();
        }
    }

    /**
     * Agrega una carta al final
     */
    public void pushInicial(Carta carta) {
        cartas.insertarFin(carta);
        carta.makeFaceUp();
    }

    /**
     * Verifica si una carta puede ser colocada en esta columna
     * - Columna vacía: solo Rey
     * - Columna con cartas: mismo palo y valor descendente
     */
    private boolean esMovimientoValido(Carta carta) {
        if (carta == null) return false;

        Carta top = peek();
        if (top == null) {
            return carta.getValor() == 13;
        }

        boolean mismoPalo = top.getFigura().equals(carta.getFigura());
        boolean ordenDescendente = top.getValor() == carta.getValor() + 1;

        return mismoPalo && ordenDescendente;
    }

    /**
     * Verifica si hay una escalera válida desde cierta posición
     */
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

    /**
     * Verifica si se puede colocar una carta en esta columna
     */
    public boolean puedoColocarCarta(Carta carta) {
        return esMovimientoValido(carta);
    }

    /**
     * Agrega una carta si el movimiento es válido
     */
    public boolean push(Carta carta) {
        if (!esMovimientoValido(carta)) return false;
        cartas.insertarFin(carta);
        carta.makeFaceUp();
        return true;
    }

    /**
     * Verifica si una lista de cartas forma una escalera válida
     */
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

    /**
     * Obtiene todas las cartas de la columna
     */
    public ListaSimple<Carta> getCartas() {
        return cartas;
    }

    /**
     * Obtiene las N cartas superiores sin sacarlas
     */
    public ListaSimple<Carta> topN(int n) {
        ListaSimple<Carta> result = new ListaSimple<>();
        int size = size();
        if (n <= 0 || n > size) return result;

        for (int i = size - n; i < size; i++) {
            result.insertarFin(cartas.getPosicion(i));
        }
        return result;
    }

    /**
     * Saca las N cartas superiores
     */
    public ListaSimple<Carta> popN(int n) {
        ListaSimple<Carta> result = topN(n);
        for (int i = 0; i < n; i++) pop();
        return result;
    }

    /**
     * Agrega varias cartas al final
     */
    public void pushAllInicial(ListaSimple<Carta> run) {
        for (int i = 0; i < run.getSize(); i++) {
            Carta carta = run.getPosicion(i);
            cartas.insertarFin(carta);
            carta.makeFaceUp();
        }
    }

    /**
     * Obtiene la escalera válida que comienza desde cierta posición
     */
    public ListaSimple<Carta> obtenerEscaleraDesde(int i) {
        ListaSimple<Carta> run = new ListaSimple<>();
        int size = cartas.getSize();
        if (i < 0 || i >= size) return run;

        for (int j = i; j < size; j++) {
            run.insertarFin(cartas.getPosicion(j));
        }

        if (escaleraValida(run)) {
            return run;
        } else {
            ListaSimple<Carta> escalera = new ListaSimple<>();
            escalera.insertarFin(cartas.getPosicion(size - 1));
            for (int j = size - 2; j >= i; j--) {
                Carta actual = cartas.getPosicion(j);
                Carta siguiente = cartas.getPosicion(j + 1);
                if (actual.getFigura().equals(siguiente.getFigura()) &&
                        actual.getValor() == siguiente.getValor() + 1) {
                    escalera.insertarInicio(actual);
                } else {
                    break;
                }
            }
            return escalera;
        }
    }

    /**
     * Verifica si se puede colocar una escalera en esta columna
     */
    public boolean puedoColocarEscalera(ListaSimple<Carta> run) {
        if (run == null || run.getSize() == 0) return false;
        Carta primera = run.getPosicion(0);
        return esMovimientoValido(primera);
    }

    /**
     * Agrega una escalera completa si es válida
     */
    public boolean pushEscalera(ListaSimple<Carta> run) {
        if (!puedoColocarEscalera(run)) return false;
        for (int i = 0; i < run.getSize(); i++) {
            Carta carta = run.getPosicion(i);
            cartas.insertarFin(carta);
            carta.makeFaceUp();
        }
        return true;
    }
}