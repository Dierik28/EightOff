package Listas;

public class ListaSimple <T> {
    private Nodo<T> inicio;

    public ListaSimple() {
        this.inicio = null;
    }

    public void insertarInicio (T dato){
        Nodo <T> n = new Nodo(dato);
        n.setSiguiente(inicio);
        inicio = n;
    }

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

    public T eliminarInicio(){
        T dato;
        if(inicio == null){
            dato = null;
            System.out.println("Lista vacia");
        }else{
            dato = inicio.getInfo();
            inicio = inicio.getSiguiente();
        }
        return dato;
    }

    public T eliminarFin(){
        T dato;
        if(inicio == null){
            dato = null;
            System.out.println("Lista vacia");
        }else{
            if(inicio.getSiguiente() == null){
                dato = inicio.getInfo();
                inicio = null;
            }else{
                Nodo <T> r = inicio;
                Nodo <T> a = r;
                while(r.getSiguiente() != null){
                    a = r;
                    r = r.getSiguiente();
                }
                dato = r.getInfo();
                a.setSiguiente(null);
            }
        }
        return dato;
    }

    public int getSize(){
        Nodo <T> n = inicio;
        int size = 1;
        if(n == null){
            System.out.println("Lista vacia");
            size = 0;
        }else{
            while(n.getSiguiente() != null){
                n = n.getSiguiente();
                size++;
            }
        }
        return size;
    }

    public T getInicio() {
        return inicio.getInfo();
    }

    public T getFinal(){
        Nodo <T> n = inicio;
        if(inicio == null){
            System.out.println("Lista vacia");
            n = null;
        }else{
            while(n.getSiguiente() != null){
                n = n.getSiguiente();
            }
        }
        return n.getInfo();
    }

    public T peekInicio() {
        return inicio != null ? inicio.getInfo() : null;
    }

    public T peekFin() {
        if (inicio == null) return null;
        Nodo<T> n = inicio;
        while (n.getSiguiente() != null) n = n.getSiguiente();
        return n.getInfo();
    }

    public boolean estaVacia() {
        return inicio == null;
    }

    public void recorrerLista() {
        Nodo<T> n = inicio;
        while(n != null) {
            System.out.print(n.getInfo() + " ");
            n = n.getSiguiente();
        }
        System.out.println("null");
    }

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
