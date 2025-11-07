package Listas;

public class ListaDoble <T> {
    NodoDoble<T> inicio;

    public ListaDoble() {
        inicio = null;
    }

    public void insertarFin(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>();
        nodo.setInfo(dato);
        nodo.setNodoSig(null);
        if (inicio == null){
            nodo.setNodoAnt(null);
            inicio = nodo;
        } else {
            NodoDoble<T> r = inicio;
            while (r.getNodoSig() != null){
                r = r.getNodoSig();
            }
            r.setNodoSig(nodo);
            nodo.setNodoAnt(r);
        }
    }

    /**
     * Obtiene el elemento en la posición especificada
     */
    public T getPosicion(int pos) {
        if (pos < 0 || pos >= getSize()) return null;

        NodoDoble<T> actual = inicio;
        for (int i = 0; i < pos; i++) {
            actual = actual.getNodoSig();
        }
        return actual.getInfo();
    }

    /**
     * Obtiene el tamaño de la lista
     */
    public int getSize() {
        int size = 0;
        NodoDoble<T> actual = inicio;
        while (actual != null) {
            size++;
            actual = actual.getNodoSig();
        }
        return size;
    }

    /**
     * Elimina el elemento en la posición especificada
     */
    public void eliminarEnPosicion(int pos) {
        if (pos < 0 || pos >= getSize()) return;

        if (pos == 0) {
            // Eliminar primer elemento
            if (inicio.getNodoSig() != null) {
                inicio = inicio.getNodoSig();
                inicio.setNodoAnt(null);
            } else {
                inicio = null;
            }
            return;
        }

        NodoDoble<T> actual = inicio;
        for (int i = 0; i < pos; i++) {
            actual = actual.getNodoSig();
        }

        if (actual.getNodoSig() == null) {
            // Último elemento
            actual.getNodoAnt().setNodoSig(null);
        } else {
            // Elemento en medio
            actual.getNodoAnt().setNodoSig(actual.getNodoSig());
            actual.getNodoSig().setNodoAnt(actual.getNodoAnt());
        }
    }

}