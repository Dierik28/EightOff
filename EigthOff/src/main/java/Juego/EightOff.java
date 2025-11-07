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
    private final HistorialJuego historial;

    /**
     * Crea el juego Eight Off con mazo, tablero, celdas libres y fundaciones
     */
    public EightOff() {
        mazo = new Mazo();
        tablero = new Tableau[8];
        celdaLibres = new CeldaLibre[8];
        foundations = new Foundation[4];
        historial = new HistorialJuego();
        iniciarComponentes();
    }

    /**
     * Inicializa todos los componentes del juego vacíos
     */
    public void iniciarComponentes() {
        for (int i = 0; i < tablero.length; i++) tablero[i] = new Tableau();
        for (int i = 0; i < celdaLibres.length; i++) celdaLibres[i] = new CeldaLibre();
        Palo[] ordenPalos = Palo.values();
        for (int i = 0; i < 4; i++) foundations[i] = new Foundation(ordenPalos[i]);
    }

    /**
     * Limpia todos los componentes del juego para empezar de nuevo
     */
    public void limpiarComponentes() {
        for (Tableau t : tablero) t.clear();
        for (CeldaLibre fc : celdaLibres) while (fc.sacarCarta(0) != null) {}
        for (Foundation f : foundations) f.clear();
        historial.limpiar();
    }

    /**
     * Mueve carta del tableau a celda libre
     */
    public boolean moverTableauAFreeCell(int fromTableau, int toFreeCell) {
        if (!validarIndices(fromTableau, toFreeCell, 8, 8)) return false;
        Carta c = tablero[fromTableau].peek();
        if (c == null) return false;
        if (celdaLibres[toFreeCell].agregarCarta(c)) {
            tablero[fromTableau].pop();
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.tc(fromTableau, toFreeCell, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve carta de celda libre al tableau
     */
    public boolean moverFreeCellATableau(int fromFreeCell, int toTableau) {
        if (!validarIndices(fromFreeCell, toTableau, 8, 8)) return false;
        Carta c = celdaLibres[fromFreeCell].verCarta(0);
        if (c == null) return false;
        if (tablero[toTableau].puedoColocarCarta(c)) {
            celdaLibres[fromFreeCell].sacarCarta(0);
            tablero[toTableau].push(c);
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.ct(fromFreeCell, toTableau, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve carta del tableau a fundación
     */
    public boolean moverTableauAFoundation(int fromTableau) {
        Carta c = tablero[fromTableau].peek();
        if (c == null) return false;
        int idx = getIdxFoundation(c.getFigura());
        if (foundations[idx].puedeAgregar(c)) {
            tablero[fromTableau].pop();
            foundations[idx].agregarCarta(c);
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.tf(fromTableau, idx, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve carta de celda libre a fundación
     */
    public boolean moverFreeCellAFoundation(int fromFreeCell) {
        Carta c = celdaLibres[fromFreeCell].verCarta(0);
        if (c == null) return false;
        int idx = getIdxFoundation(c.getFigura());
        if (foundations[idx].puedeAgregar(c)) {
            celdaLibres[fromFreeCell].sacarCarta(0);
            foundations[idx].agregarCarta(c);
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.cf(fromFreeCell, idx, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve carta de fundación al tableau
     */
    public boolean moverFoundationATableau(int fromFoundation, int toTableau) {
        if (!validarIndices(fromFoundation, toTableau, 4, 8)) return false;
        Carta c = foundations[fromFoundation].verUltimaCarta();
        if (c == null) return false;
        if (tablero[toTableau].puedoColocarCarta(c)) {
            foundations[fromFoundation].sacarCarta();
            tablero[toTableau].push(c);
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.ft(fromFoundation, toTableau, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve carta de fundación a celda libre
     */
    public boolean moverFoundationAFreeCell(int fromFoundation, int toFreeCell) {
        if (!validarIndices(fromFoundation, toFreeCell, 4, 8)) return false;
        Carta c = foundations[fromFoundation].verUltimaCarta();
        if (c == null) return false;
        if (celdaLibres[toFreeCell].agregarCarta(c)) {
            foundations[fromFoundation].sacarCarta();
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.fc(fromFoundation, toFreeCell, c));
            }
            return true;
        }
        return false;
    }

    /**
     * Mueve cartas entre columnas del tableau
     */
    public boolean moverCartaTableauATableau(int from, int to) {
        if (from == to) return false;
        if (!validarIndices(from, to, tablero.length, tablero.length)) return false;
        if (tablero[from].estaVacia()) return false;

        int size = tablero[from].size();

        for (int i = 0; i < size; i++) {
            if (!tablero[from].esEscaleraValidaDesde(i)) continue;

            Carta primera = tablero[from].getCarta(i);
            if (tablero[to].puedoColocarCarta(primera)) {
                int cantidad = size - i;
                ListaSimple<Carta> pack = tablero[from].popN(cantidad);
                tablero[to].pushEscalera(pack);
                if (!historial.isModoHistorial()) {
                    historial.agregarMovimiento(EstadoJuego.tt(from, to, primera, cantidad));
                }
                return true;
            }
        }

        Carta carta = tablero[from].peek();
        if (carta == null) return false;

        if (tablero[to].puedoColocarCarta(carta)) {
            Carta cartaMovida = tablero[from].pop();
            tablero[to].push(cartaMovida);
            if (!historial.isModoHistorial()) {
                historial.agregarMovimiento(EstadoJuego.tt(from, to, cartaMovida, 1));
            }
            return true;
        }
        return false;
    }

    /**
     * Valida que los índices estén dentro de los límites
     */
    private boolean validarIndices(int a, int b, int maxA, int maxB) {
        return a >= 0 && a < maxA && b >= 0 && b < maxB;
    }

    /**
     * Encuentra la fundación correspondiente al palo de la carta
     */
    private int getIdxFoundation(String figuraPalo) {
        for (int i = 0; i < foundations.length; i++)
            if (foundations[i].getPalo().getFigura().equals(figuraPalo)) return i;
        return -1;
    }

    /**
     * Verifica si todas las fundaciones están completas
     */
    public boolean evaluarVictoria() {
        for (Foundation f : foundations) {
            if (!f.estaCompleta()) return false;
        }
        return true;
    }

    /**
     * Deshace el último movimiento realizado
     */
    public boolean undo() {
        if (historial.isModoHistorial()) {
            return undoDesdeHistorial();
        } else {
            EstadoJuego ultimoMov = historial.undo();
            if (ultimoMov != null) {
                ultimoMov.revertir(this);
                return true;
            }
            return false;
        }
    }

    /**
     * Deshace movimiento desde el historial
     */
    boolean undoDesdeHistorial() {
        EstadoJuego movimiento = historial.undo();
        if (movimiento != null) {
            movimiento.revertir(this);
            return true;
        }
        return false;
    }

    /**
     * Verifica si se puede activar el modo historial
     * @return true si hay movimientos en el historial, false en caso contrario
     */
    public boolean sePuedeActivarHistorial() {
        return historial.sePuedeActivarHistorial();
    }

    /**
     * Rehace movimiento desde el historial
     */
    public boolean redoDesdeHistorial() {
        EstadoJuego movimiento = historial.redo();
        if (movimiento != null) {
            movimiento.ejecutar(this);
            return true;
        }
        return false;
    }

    /**
     * Aplica el estado actual del historial al juego
     */
    public void aplicarHistorial() {
        historial.setModoHistorial(false);
    }

    /**
     * Activa el modo historial
     */
    public void activarModoHistorial() {
        historial.setModoHistorial(true);
    }

    /**
     * Busca y devuelve un movimiento sugerido
     */
    public EstadoJuego obtenerPista() {
        for (int fc = 0; fc < celdaLibres.length; fc++) {
            Carta c = celdaLibres[fc].peek();
            if (c == null) continue;

            int idxF = getIdxFoundation(c.getFigura());
            if (idxF != -1 && foundations[idxF].puedeAgregar(c)) {
                return EstadoJuego.cf(fc, idxF, c);
            }
        }

        for (int t = 0; t < tablero.length; t++) {
            Carta c = tablero[t].peek();
            if (c == null) continue;

            int idxF = getIdxFoundation(c.getFigura());
            if (idxF != -1 && foundations[idxF].puedeAgregar(c)) {
                return EstadoJuego.tf(t, idxF, c);
            }
        }
        for (int from = 0; from < tablero.length; from++) {
            Tableau origen = tablero[from];
            if (origen.estaVacia()) continue;

            int size = origen.size();
            for (int i = 0; i < size; i++) {
                if (!origen.esEscaleraValidaDesde(i)) continue;

                ListaSimple<Carta> run = origen.obtenerEscaleraDesde(i);
                Carta cartaSuperior = run.getPosicion(0);

                for (int to = 0; to < tablero.length; to++) {
                    if (from == to) continue;
                    Tableau destino = tablero[to];
                    if (destino.estaVacia() || destino.puedoColocarEscalera(run)) {
                        return EstadoJuego.tt(from, to, cartaSuperior, run.getSize());
                    }
                }
            }
        }
        for (int fc = 0; fc < celdaLibres.length; fc++) {
            Carta c = celdaLibres[fc].peek();
            if (c == null) continue;

            for (int t = 0; t < tablero.length; t++) {
                Tableau destino = tablero[t];
                if (destino.estaVacia() || destino.puedoColocarCarta(c)) {
                    return EstadoJuego.ct(fc, t, c);
                }
            }
        }

        for (int t = 0; t < tablero.length; t++) {
            Carta c = tablero[t].peek();
            if (c == null) continue;

            for (int fc = 0; fc < celdaLibres.length; fc++) {
                if (celdaLibres[fc].estaVacia()) {
                    return EstadoJuego.tc(t, fc, c);
                }
            }
        }

        for (int f = 0; f < foundations.length; f++) {
            Carta c = foundations[f].verUltimaCarta();
            if (c == null) continue;

            for (int t = 0; t < tablero.length; t++) {
                Tableau destino = tablero[t];
                if (destino.estaVacia() || destino.puedoColocarCarta(c)) {
                    return EstadoJuego.ft(f, t, c);
                }
            }
        }

        return null;
    }

    /**
     * Verifica si hay movimientos disponibles
     */
    public boolean hayMovimientosDisponibles() {
        return obtenerPista() != null;
    }

    /**
     * Obtiene la carta superior de una celda libre
     */
    public Carta getTopFreeCell(int i) {
        return celdaLibres[i].verCarta(0);
    }

    /**
     * Obtiene la carta superior de una fundación
     */
    public Carta getTopFoundation(int i) {
        return foundations[i].verUltimaCarta();
    }

    /**
     * Obtiene un objeto tableau por índice
     */
    public Tableau getTableauObject(int idx) {
        return tablero[idx];
    }

    /**
     * Obtiene un objeto celda libre por índice
     */
    public CeldaLibre getFreeCellObject(int idx) {
        return celdaLibres[idx];
    }

    /**
     * Reparte las cartas iniciales del juego
     */
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

    /**
     * Obtiene el historial del juego
     */
    public HistorialJuego getHistorial() {
        return historial;
    }
}