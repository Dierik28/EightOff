package Listas;

import java.util.ArrayList;
import java.util.Collections;

public class ListaDobleCircular <T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    /**
     * Crea una lista doble circular vacía
     */
    public ListaDobleCircular() {
        this.inicio = null;
        this.fin = null;
    }

    /**
     * Agrega un elemento al final de la lista
     * Si está vacía, crea el primer nodo que apunta a sí mismo
     */
    public void insertarFin(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>();
        nodo.setInfo(dato);
        if (estaVacia()) {
            inicio = fin = nodo;
            nodo.setNodoSig(inicio);
            nodo.setNodoAnt(inicio);
        } else {
            nodo.setNodoSig(inicio);
            inicio.setNodoAnt(nodo);
            fin.setNodoSig(nodo);
            nodo.setNodoAnt(fin);
            fin = nodo;
        }
    }

    /**
     * Elimina y devuelve el último elemento de la lista
     */
    public T eliminarFin() {
        T dato = null;
        if (estaVacia()) {
            System.out.println("Lista vacia");
        } else {
            if (inicio == fin) {
                dato = inicio.getInfo();
                inicio = fin = null;
            } else {
                dato = fin.getInfo();
                NodoDoble<T> nodo = fin.getNodoAnt();
                nodo.setNodoSig(inicio);
                inicio.setNodoAnt(nodo);
                fin = nodo;
            }
        }
        return dato;
    }

    /**
     * Verifica si la lista está vacía
     */
    public boolean estaVacia() {
        return inicio == null;
    }

    /**
     * Mezcla aleatoriamente los elementos de la lista
     * Convierte a ArrayList, usa Collections.shuffle y reconstruye la lista
     */
    public void shuffle() {
        if (estaVacia() || inicio == fin) return;

        ArrayList<T> elementos = new ArrayList<>();
        NodoDoble<T> actual = inicio;
        do {
            elementos.add(actual.getInfo());
            actual = actual.getNodoSig();
        } while (actual != inicio);

        Collections.shuffle(elementos);

        inicio = fin = null;
        for (T dato : elementos) {
            insertarFin(dato);
        }
    }

    /**
     * Muestra la lista como texto
     */
    @Override
    public String toString() {
        if (estaVacia()) {
            return "Lista vacía";
        }

        StringBuilder sb = new StringBuilder();
        NodoDoble<T> actual = inicio;
        do {
            sb.append(actual.getInfo()).append(" ⇄ ");
            actual = actual.getNodoSig();
        } while (actual != inicio);

        sb.append("(inicio)");
        return sb.toString();
    }
}