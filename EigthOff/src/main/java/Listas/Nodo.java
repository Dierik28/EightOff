package Listas;

public class Nodo <T> {
    private T info;           // Información almacenada en el nodo
    private Nodo<T> siguiente; // Referencia al siguiente nodo

    /**
     * Crea un nodo con la información especificada
     */
    public Nodo(T info) {
        this.info = info;
    }

    /**
     * Obtiene la información almacenada en el nodo
     */
    public T getInfo() {
        return info;
    }

    /**
     * Obtiene la referencia al siguiente nodo
     */
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece la referencia al siguiente nodo
     */
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * Representación en texto del nodo
     */
    public String toString() {
        return info.toString();
    }
}