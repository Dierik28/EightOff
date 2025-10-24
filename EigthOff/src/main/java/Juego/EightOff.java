package Juego;

import Cartas.Carta;
import Cartas.Mazo;
import Cartas.Palo;
import Listas.ListaSimple;

public class EightOff {
    public final Mazo mazo;
    private final Tableau[] tablero;
    private final CeldaLibre[] celdaLibres;
    public final Foundation[] foundations;
    private final ListaSimple<EstadoJuego> undo;

    public EightOff() {
        mazo = new Mazo();
        tablero = new Tableau[8];
        celdaLibres = new CeldaLibre[8];
        foundations = new Foundation[4];
        undo = new ListaSimple<>();
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        for (int i = 0; i < tablero.length; i++) tablero[i] = new Tableau();
        for (int i = 0; i < celdaLibres.length; i++) celdaLibres[i] = new CeldaLibre();
        Palo[] ordenPalos = Palo.values();
        for (int i = 0; i < 4; i++) foundations[i] = new Foundation(ordenPalos[i]);
    }

    public void limpiarComponentes() {
        for (Tableau t : tablero) t.clear();
        for (CeldaLibre fc : celdaLibres) while (fc.sacarCarta(0) != null) {}
        for (Foundation f : foundations) f.clear();
        while (undo.eliminarFin() != null) {}
    }

    public boolean moverTableauAFreeCell(int fromTableau, int toFreeCell) {
        if (!validarIndices(fromTableau, toFreeCell, 8, 8)) return false;
        Carta c = tablero[fromTableau].peek();
        if (c == null) return false;
        if (celdaLibres[toFreeCell].agregarCarta(c)) {
            tablero[fromTableau].pop();
            undo.insertarFin(EstadoJuego.tc(fromTableau, toFreeCell, c));
            return true;
        }
        return false;
    }

    public boolean moverFreeCellATableau(int fromFreeCell, int toTableau) {
        if (!validarIndices(fromFreeCell, toTableau, 8, 8)) return false;
        Carta c = celdaLibres[fromFreeCell].verCarta(0);
        if (c == null) return false;
        if (tablero[toTableau].puedoColocarCarta(c)) {
            celdaLibres[fromFreeCell].sacarCarta(0);
            tablero[toTableau].push(c);
            undo.insertarFin(EstadoJuego.ct(fromFreeCell, toTableau, c));
            return true;
        }
        return false;
    }

    public boolean moverTableauAFoundation(int fromTableau) {
        Carta c = tablero[fromTableau].peek();
        if (c == null) return false;
        int idx = getIdxFoundation(c.getFigura());
        if (foundations[idx].puedeAgregar(c)) {
            tablero[fromTableau].pop();
            foundations[idx].agregarCarta(c);
            undo.insertarFin(EstadoJuego.tf(fromTableau, idx, c));
            return true;
        }
        return false;
    }

    public boolean moverFreeCellAFoundation(int fromFreeCell) {
        Carta c = celdaLibres[fromFreeCell].verCarta(0);
        if (c == null) return false;
        int idx = getIdxFoundation(c.getFigura());
        if (foundations[idx].puedeAgregar(c)) {
            celdaLibres[fromFreeCell].sacarCarta(0);
            foundations[idx].agregarCarta(c);
            undo.insertarFin(EstadoJuego.cf(fromFreeCell, idx, c));
            return true;
        }
        return false;
    }

    public boolean moverFoundationATableau(int fromFoundation, int toTableau) {
        if (!validarIndices(fromFoundation, toTableau, 4, 8)) return false;
        Carta c = foundations[fromFoundation].verUltimaCarta();
        if (c == null) return false;
        if (tablero[toTableau].puedoColocarCarta(c)) {
            foundations[fromFoundation].sacarCarta();
            tablero[toTableau].push(c);
            undo.insertarFin(EstadoJuego.ft(fromFoundation, toTableau, c));
            return true;
        }
        return false;
    }

    public boolean moverFoundationAFreeCell(int fromFoundation, int toFreeCell) {
        if (!validarIndices(fromFoundation, toFreeCell, 4, 8)) return false;
        Carta c = foundations[fromFoundation].verUltimaCarta();
        if (c == null) return false;
        if (celdaLibres[toFreeCell].agregarCarta(c)) {
            foundations[fromFoundation].sacarCarta();
            undo.insertarFin(EstadoJuego.fc(fromFoundation, toFreeCell, c));
            return true;
        }
        return false;
    }

    public boolean moverCartaTableauATableau(int from, int to) {
        if (from == to) return false;
        if (!validarIndices(from, to, tablero.length, tablero.length)) return false;

        Carta carta = tablero[from].peek();
        if (carta == null) return false;

        if (tablero[to].puedoColocarCarta(carta)) {
            Carta cartaMovida = tablero[from].pop();
            tablero[to].push(cartaMovida);
            undo.insertarFin(EstadoJuego.tt(from, to, cartaMovida, 1));
            return true;
        }
        return false;
    }

    private boolean validarIndices(int a, int b, int maxA, int maxB) {
        return a >= 0 && a < maxA && b >= 0 && b < maxB;
    }

    private int getIdxFoundation(String figuraPalo) {
        for (int i = 0; i < foundations.length; i++)
            if (foundations[i].getPalo().getFigura().equals(figuraPalo)) return i;
        return -1;
    }

    public boolean evaluarVictoria() {
        for (Foundation f : foundations) if (f.getSize() != 13) return false;
        return true;
    }

    public boolean undo() {
        EstadoJuego ultimoMov = undo.eliminarFin();
        if (ultimoMov == null) return false;

        switch (ultimoMov.getTipo()) {
            case TT -> {
                int cantidad = Math.max(1, ultimoMov.getCantidad());
                ListaSimple<Carta> pack = tablero[ultimoMov.getToIdx()].popN(cantidad);
                tablero[ultimoMov.getFromIdx()].pushAllInicial(pack);
            }
            case TC -> {
                Carta c = celdaLibres[ultimoMov.getToIdx()].sacarCarta(0);
                tablero[ultimoMov.getFromIdx()].pushInicial(c);
            }
            case CT -> {
                Carta c = tablero[ultimoMov.getToIdx()].pop();
                celdaLibres[ultimoMov.getFromIdx()].agregarCarta(c);
            }
            case TF -> {
                Carta c = foundations[ultimoMov.getToIdx()].sacarCarta();
                tablero[ultimoMov.getFromIdx()].pushInicial(c);
            }
            case CF -> {
                Carta c = foundations[ultimoMov.getToIdx()].sacarCarta();
                celdaLibres[ultimoMov.getFromIdx()].agregarCarta(c);
            }
            case FT -> {
                Carta c = tablero[ultimoMov.getToIdx()].pop();
                foundations[ultimoMov.getFromIdx()].agregarCarta(c);
            }
            case FC -> {
                Carta c = celdaLibres[ultimoMov.getToIdx()].sacarCarta(0);
                foundations[ultimoMov.getFromIdx()].agregarCarta(c);
            }
        }
        return true;
    }

    public EstadoJuego obtenerPista() {

        for (int fc = 0; fc < celdaLibres.length; fc++) {
            Carta c = celdaLibres[fc].peek();
            if (c == null) continue;

            int f = getIdxFoundation(c.getFigura());
            if (f != -1 && foundations[f].puedeAgregar(c))
                return EstadoJuego.cf(fc, f, c);
        }

        for (int t = 0; t < tablero.length; t++) {
            Carta c = tablero[t].peek();
            if (c == null) continue;

            int f = getIdxFoundation(c.getFigura());
            if (f != -1 && foundations[f].puedeAgregar(c))
                return EstadoJuego.tf(t, f, c);
        }

        for (int fc = 0; fc < celdaLibres.length; fc++) {
            Carta c = celdaLibres[fc].peek();
            if (c == null) continue;

            for (int t = 0; t < tablero.length; t++) {
                if (tablero[t].puedoColocarCarta(c))
                    return EstadoJuego.ct(fc, t, c);
            }
        }

        int celdasLibres = getFreeCellsDisponibles();
        if (celdasLibres > 0) {
            for (int t = 0; t < tablero.length; t++) {
                Carta c = tablero[t].peek();
                if (c == null) continue;

                for (int fc = 0; fc < celdaLibres.length; fc++) {
                    if (celdaLibres[fc].estaVacia())
                        return EstadoJuego.tc(t, fc, c);
                }
            }
        }

        for (int t1 = 0; t1 < tablero.length; t1++) {
            if (tablero[t1].estaVacia()) continue;

            int size = tablero[t1].size();
            for (int i = 0; i < size; i++) {
                Carta c = tablero[t1].getCarta(i);
                if (!tablero[t1].esEscaleraValidaDesde(i)) continue;

                for (int t2 = 0; t2 < tablero.length; t2++) {
                    if (t1 == t2) continue;
                    if (tablero[t2].puedoColocarCarta(c))
                        return EstadoJuego.tt(t1, t2, c, size - i);
                }
            }
        }

        for (int f = 0; f < foundations.length; f++) {
            Carta c = foundations[f].verUltimaCarta();
            if (c == null) continue;

            for (int t = 0; t < tablero.length; t++) {
                if (tablero[t].puedoColocarCarta(c))
                    return EstadoJuego.ft(f, t, c);
            }
        }

        if (celdasLibres > 0) {
            for (int f = 0; f < foundations.length; f++) {
                Carta c = foundations[f].verUltimaCarta();
                if (c == null) continue;

                for (int fc = 0; fc < celdaLibres.length; fc++) {
                    if (celdaLibres[fc].estaVacia())
                        return EstadoJuego.fc(f, fc, c);
                }
            }
        }

        return null;
    }

    public boolean hayMovimientosDisponibles() {

        return obtenerPista() != null;
    }

    public Carta getTopTableau(int col) {
        return tablero[col].peek();
    }

    public Carta getTopFreeCell(int i) {
        return celdaLibres[i].verCarta(0);
    }

    public Carta getTopFoundation(int i) {
        return foundations[i].verUltimaCarta();
    }

    public ListaSimple<Carta> getTableau(int idx) {
        return tablero[idx].getCartas();
    }

    public Tableau getTableauObject(int idx) {
        return tablero[idx];
    }

    public CeldaLibre getFreeCell(int idx) {
        return celdaLibres[idx];
    }

    public Foundation getFoundation(int idx) {
        return foundations[idx];
    }

    public int getFreeCellsDisponibles() {
        int count = 0;
        for (CeldaLibre fc : celdaLibres)
            if (fc.estaVacia()) count++;
        return count;
    }

    public void repartirCartasIniciales() {
        limpiarComponentes();
        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 8; col++) {
                Carta carta = mazo.getMazo().eliminarFin();
                if (carta != null) {
                    tablero[col].pushInicial(carta);
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            Carta carta = mazo.getMazo().eliminarFin();
            if (carta != null) {
                celdaLibres[i].agregarCarta(carta);
            }
        }
    }

    private boolean escaleraValida(ListaSimple<Carta> run) {
        if (run == null || run.getSize() == 0) return false;

        if (run.getSize() == 1) return true;

        for (int i = 0; i < run.getSize() - 1; i++) {
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
}