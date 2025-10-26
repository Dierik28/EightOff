package Juego;

import Cartas.Carta;

public class EstadoJuego {

    // Tipos de movimientos posibles en el juego
    public enum Tipo {TT, TF, FT, FF, TC, CT, CF, FC}

    // Orígenes y destinos posibles para los movimientos
    public enum OrigenDestino {TABLEAU, FOUNDATION, FREE_CELL}

    // Atributos de la clase
    public final Tipo tipo;
    public final OrigenDestino from, to;
    public final int fromIdx, toIdx;
    public final Carta carta;
    public int cantidad = 0;

    /**
     * Crea un estado de juego para un movimiento simple
     */
    public EstadoJuego(Tipo tipo,
                       OrigenDestino from, int fromIdx,
                       OrigenDestino to, int toIdx,
                       Carta carta) {
        this.tipo = tipo;
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
        this.carta = carta;
        this.from = from;
        this.to = to;
    }

    /**
     * Crea un estado de juego para movimientos con múltiples cartas
     */
    public EstadoJuego(Tipo tipo,
                       OrigenDestino from, int fromIdx,
                       OrigenDestino to, int toIdx,
                       Carta carta, int cantidad) {
        this.tipo = tipo;
        this.fromIdx = fromIdx;
        this.toIdx = toIdx;
        this.carta = carta;
        this.from = from;
        this.to = to;
        this.cantidad = cantidad;
    }

    /**
     * Movimiento de Tableau a Tableau
     */
    public static EstadoJuego tt(int from, int to, Carta carta, int cantidad) {
        return new EstadoJuego(Tipo.TT,
                OrigenDestino.TABLEAU, from,
                OrigenDestino.TABLEAU, to, carta, cantidad);
    }

    /**
     * Movimiento de Tableau a Foundation
     */
    public static EstadoJuego tf(int fromTableau, int toFoundation, Carta carta) {
        return new EstadoJuego(Tipo.TF,
                OrigenDestino.TABLEAU, fromTableau,
                OrigenDestino.FOUNDATION, toFoundation, carta);
    }

    /**
     * Movimiento de Foundation a Tableau
     */
    public static EstadoJuego ft(int fromFoundation, int toTableau, Carta carta) {
        return new EstadoJuego(Tipo.FT,
                OrigenDestino.FOUNDATION, fromFoundation,
                OrigenDestino.TABLEAU, toTableau, carta);
    }

    /**
     * Movimiento de Tableau a FreeCell
     */
    public static EstadoJuego tc(int fromTableau, int toFreeCell, Carta carta) {
        return new EstadoJuego(Tipo.TC,
                OrigenDestino.TABLEAU, fromTableau,
                OrigenDestino.FREE_CELL, toFreeCell, carta);
    }

    /**
     * Movimiento de FreeCell a Tableau
     */
    public static EstadoJuego ct(int fromFreeCell, int toTableau, Carta carta) {
        return new EstadoJuego(Tipo.CT,
                OrigenDestino.FREE_CELL, fromFreeCell,
                OrigenDestino.TABLEAU, toTableau, carta);
    }

    /**
     * Movimiento de FreeCell a Foundation
     */
    public static EstadoJuego cf(int fromFreeCell, int toFoundation, Carta carta) {
        return new EstadoJuego(Tipo.CF,
                OrigenDestino.FREE_CELL, fromFreeCell,
                OrigenDestino.FOUNDATION, toFoundation, carta);
    }

    /**
     * Movimiento de Foundation a FreeCell
     */
    public static EstadoJuego fc(int fromFoundation, int toFreeCell, Carta carta) {
        return new EstadoJuego(Tipo.FC,
                OrigenDestino.FOUNDATION, fromFoundation,
                OrigenDestino.FREE_CELL, toFreeCell, carta);
    }

    // Getters de la clase
    public Tipo getTipo() { return tipo; }
    public int getFromIdx() { return fromIdx; }
    public int getToIdx() { return toIdx; }
    public int getCantidad() { return cantidad; }

    /**
     * Representación en texto del movimiento para debugging
     */
    @Override
    public String toString() {
        String fromStr = from.toString() + "[" + fromIdx + "]";
        String toStr = to.toString() + "[" + toIdx + "]";

        switch (tipo) {
            case TT:
                return String.format("Tableau[%d] -> Tableau[%d]: %s (cantidad: %d)",
                        fromIdx, toIdx, carta, cantidad);
            case TF:
                return String.format("Tableau[%d] -> Foundation[%d]: %s",
                        fromIdx, toIdx, carta);
            case FT:
                return String.format("Foundation[%d] -> Tableau[%d]: %s",
                        fromIdx, toIdx, carta);
            case FF:
                return String.format("Foundation[%d] -> Foundation[%d]: %s",
                        fromIdx, toIdx, carta);
            case TC:
                return String.format("Tableau[%d] -> FreeCell[%d]: %s",
                        fromIdx, toIdx, carta);
            case CT:
                return String.format("FreeCell[%d] -> Tableau[%d]: %s",
                        fromIdx, toIdx, carta);
            case CF:
                return String.format("FreeCell[%d] -> Foundation[%d]: %s",
                        fromIdx, toIdx, carta);
            case FC:
                return String.format("Foundation[%d] -> FreeCell[%d]: %s",
                        fromIdx, toIdx, carta);
            default:
                return "Movimiento desconocido: " + fromStr + " -> " + toStr;
        }
    }
}