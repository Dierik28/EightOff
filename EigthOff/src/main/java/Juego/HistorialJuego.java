package Juego;

import Listas.ListaDoble;

public class HistorialJuego {
    private ListaDoble<EstadoJuego> movimientos;
    private int posicionActual;
    private boolean modoHistorial;

    public HistorialJuego() {
        this.movimientos = new ListaDoble<>();
        this.posicionActual = -1;
        this.modoHistorial = false;
    }

    /**
     * Agrega un nuevo movimiento al historial
     */
    public void agregarMovimiento(EstadoJuego movimiento) {
        if (posicionActual < movimientos.getSize() - 1) {
            eliminarMovimientosFuturos();
        }
        movimientos.insertarFin(movimiento);
        posicionActual = movimientos.getSize() - 1;
    }

    /**
     * Elimina todos los movimientos después de la posición actual
     */
    private void eliminarMovimientosFuturos() {
        int totalMovimientos = movimientos.getSize();
        for (int i = totalMovimientos - 1; i > posicionActual; i--) {
            movimientos.eliminarEnPosicion(i);
        }
    }

    /**
     * Retrocede en el historial (UNDO)
     */
    public EstadoJuego undo() {
        if (posicionActual >= 0) {
            EstadoJuego movimiento = movimientos.getPosicion(posicionActual);
            posicionActual--;
            return movimiento;
        }
        return null;
    }

    /**
     * Avanza en el historial (REDO)
     */
    public EstadoJuego redo() {
        if (posicionActual < movimientos.getSize() - 1) {
            posicionActual++;
            return movimientos.getPosicion(posicionActual);
        }
        return null;
    }

    public boolean isModoHistorial() {
        return modoHistorial;
    }

    public void setModoHistorial(boolean modoHistorial) {
        this.modoHistorial = modoHistorial;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public int getTotalMovimientos() {
        return movimientos.getSize();
    }

    public void limpiar() {
        movimientos = new ListaDoble<>();
        posicionActual = -1;
    }

    public EstadoJuego getMovimientoActual() {
        if (posicionActual >= 0 && posicionActual < movimientos.getSize()) {
            return movimientos.getPosicion(posicionActual);
        }
        return null;
    }

    /**
     * Verifica si se puede activar el modo historial
     * @return true si hay al menos un movimiento en el historial, false en caso contrario
     */
    public boolean sePuedeActivarHistorial() {
        return movimientos.getSize() > 0;
    }
}