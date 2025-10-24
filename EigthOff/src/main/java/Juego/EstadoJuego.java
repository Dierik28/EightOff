package Juego;

import Cartas.Carta;

public class EstadoJuego {

    public enum Tipo {TT, TF, FT, FF, TC, CT, CF, FC}

    public enum OrigenDestino {TABLEAU, FOUNDATION, FREE_CELL}

    public final Tipo tipo;
    public final OrigenDestino from, to;
    public final int fromIdx, toIdx;
    public final Carta carta;
    public int cantidad = 0;

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

    public static EstadoJuego tt(int from, int to, Carta carta) {
        return new EstadoJuego(Tipo.TT,
                OrigenDestino.TABLEAU, from,
                OrigenDestino.TABLEAU, to, carta);
    }

    public static EstadoJuego tt(int from, int to, Carta carta, int cantidad) {
        return new EstadoJuego(Tipo.TT,
                OrigenDestino.TABLEAU, from,
                OrigenDestino.TABLEAU, to, carta, cantidad);
    }

    public static EstadoJuego tf(int fromTableau, int toFoundation, Carta carta) {
        return new EstadoJuego(Tipo.TF,
                OrigenDestino.TABLEAU, fromTableau,
                OrigenDestino.FOUNDATION, toFoundation, carta);
    }

    public static EstadoJuego ft(int fromFoundation, int toTableau, Carta carta) {
        return new EstadoJuego(Tipo.FT,
                OrigenDestino.FOUNDATION, fromFoundation,
                OrigenDestino.TABLEAU, toTableau, carta);
    }

    public static EstadoJuego ff(int fromFoundation, int toFoundation, Carta carta) {
        return new EstadoJuego(Tipo.FF,
                OrigenDestino.FOUNDATION, fromFoundation,
                OrigenDestino.FOUNDATION, toFoundation, carta);
    }

    public static EstadoJuego tc(int fromTableau, int toFreeCell, Carta carta) {
        return new EstadoJuego(Tipo.TC,
                OrigenDestino.TABLEAU, fromTableau,
                OrigenDestino.FREE_CELL, toFreeCell, carta);
    }

    public static EstadoJuego ct(int fromFreeCell, int toTableau, Carta carta) {
        return new EstadoJuego(Tipo.CT,
                OrigenDestino.FREE_CELL, fromFreeCell,
                OrigenDestino.TABLEAU, toTableau, carta);
    }

    public static EstadoJuego cf(int fromFreeCell, int toFoundation, Carta carta) {
        return new EstadoJuego(Tipo.CF,
                OrigenDestino.FREE_CELL, fromFreeCell,
                OrigenDestino.FOUNDATION, toFoundation, carta);
    }

    public static EstadoJuego fc(int fromFoundation, int toFreeCell, Carta carta) {
        return new EstadoJuego(Tipo.FC,
                OrigenDestino.FOUNDATION, fromFoundation,
                OrigenDestino.FREE_CELL, toFreeCell, carta);
    }

    public Tipo getTipo() { return tipo; }
    public OrigenDestino getFrom() { return from; }
    public OrigenDestino getTo() { return to; }
    public int getFromIdx() { return fromIdx; }
    public int getToIdx() { return toIdx; }
    public Carta getCarta() { return carta; }
    public int getCantidad() { return cantidad; }

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