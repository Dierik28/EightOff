package Listas;

public class ListaDobleCircular <T> {
    private NodoDoble<T> inicio;
    private NodoDoble<T> fin;

    public ListaDobleCircular() {
        this.inicio = null;
        this.fin = null;
    }

    public void insertarInicio(T dato) {
        NodoDoble<T> nodo = new NodoDoble<>();
        nodo.setInfo(dato);

        if (estaVacia()) {
            inicio = fin = nodo;
            nodo.setNodoSig(inicio);
            nodo.setNodoAnt(inicio);
        } else {
            nodo.setNodoSig(inicio);
            inicio.setNodoAnt(nodo);
            inicio = nodo;
            fin.setNodoSig(inicio);
            nodo.setNodoAnt(fin);
        }
    }

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

    public T eliminarInicio() {
        T dato = null;

        if (estaVacia()) {
            System.out.println("Lista vacia");
        } else {
            if (inicio == fin) {
                dato = inicio.getInfo();
                inicio = fin = null;
            } else {
                dato = inicio.getInfo();
                fin.setNodoSig(inicio.getNodoSig());
                inicio = inicio.getNodoSig();
                inicio.setNodoAnt(fin);
            }
        }
        return dato;
    }

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

    public boolean estaVacia() {
        return inicio == null;
    }

    public NodoDoble<T> getInicio() {
        return inicio;
    }

    public NodoDoble<T> getFin() {
        return fin;
    }

    public void recorrerLista() {
        if (inicio == null) {
            System.out.println("Lista vacía.");
            return;
        }

        NodoDoble<T> actual = inicio;
        System.out.println("Contenido de la lista:");

        do {
            System.out.println(" " + actual.getInfo());
            actual = actual.getNodoSig();
        } while (actual != inicio);

    }

    public NodoDoble<T> buscar(T dato) {
        if (estaVacia()) return null;
        NodoDoble<T> actual = inicio;
        do {
            if (actual.getInfo().equals(dato)) {
                return actual;
            }
            actual = actual.getNodoSig();
        } while (actual != inicio);
        return null;
    }

    public int tamaño() {
        if (estaVacia()) return 0;
        int contador = 0;
        NodoDoble<T> actual = inicio;
        do {
            contador++;
            actual = actual.getNodoSig();
        } while (actual != inicio);
        return contador;
    }

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
