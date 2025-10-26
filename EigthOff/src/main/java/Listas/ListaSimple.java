package Listas;

public class ListaSimple <T> {
    private Nodo<T> inicio;

    /**
     * Crea una lista simplemente enlazada vacía
     */
    public ListaSimple() {
        this.inicio = null;
    }

    /**
     * Agrega un elemento al inicio de la lista
     */
    public void insertarInicio (T dato){
        Nodo <T> n = new Nodo(dato);
        n.setSiguiente(inicio);
        inicio = n;
    }

    /**
     * Agrega un elemento al final de la lista
     */
    public void insertarFin(T dato){
        Nodo <T> n = new Nodo(dato);
        if(inicio == null){
            n.setSiguiente(inicio);
            inicio = n;
        }else{
            Nodo <T> r = inicio;
            while(r.getSiguiente() != null){
                r = r.getSiguiente();
            }
            r.setSiguiente(n);
            n.setSiguiente(null);
        }
    }

    /**
     * Elimina y devuelve el último elemento de la lista
     */
    public T eliminarFin(){
        if (inicio == null) {
            System.out.println("Lista vacia");
            return null;
        }

        if (inicio.getSiguiente() == null) {
            T dato = inicio.getInfo();
            inicio = null;
            return dato;
        } else {
            Nodo<T> r = inicio;
            Nodo<T> a = r;
            while (r.getSiguiente() != null) {
                a = r;
                r = r.getSiguiente();
            }
            T dato = r.getInfo();
            a.setSiguiente(null);
            return dato;
        }
    }

    /**
     * Devuelve la cantidad de elementos en la lista
     */
    public int getSize(){
        if (inicio == null) {
            return 0;
        }

        Nodo<T> n = inicio;
        int size = 1;
        while (n.getSiguiente() != null) {
            n = n.getSiguiente();
            size++;
        }
        return size;
    }

    /**
     * Obtiene el último elemento de la lista
     */
    public T getFinal(){
        if (inicio == null) {
            return null;
        }

        Nodo<T> n = inicio;
        while (n.getSiguiente() != null) {
            n = n.getSiguiente();
        }
        return n.getInfo();
    }

    /**
     * Verifica si la lista está vacía
     */
    public boolean estaVacia() {
        return inicio == null;
    }

    /**
     * Obtiene el elemento en una posición específica
     */
    public T getPosicion(int pos) {
        if (pos < 0 || pos >= getSize()) return null;
        Nodo<T> n = inicio;
        int i = 0;
        while (n != null) {
            if (i == pos) {
                return n.getInfo();
            }
            n = n.getSiguiente();
            i ++;
        }
        return null;
    }
}