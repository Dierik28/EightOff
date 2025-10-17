package Listas;

public class NodoDoble <T> {
    private NodoDoble<T> nodoSig;
    private NodoDoble<T> nodoAnt;
    private T info;

    public NodoDoble(T info, NodoDoble<T> nodoSig, NodoDoble<T> nodoAnt) {
        this.info = info;
        this.nodoSig = nodoSig;
        this.nodoAnt = nodoAnt;
    }

    public NodoDoble(){
        this.info = null;
        this.nodoSig = null;
        this.nodoAnt = null;
    }

    public T getInfo() {
        return info;
    }

    public NodoDoble<T> getNodoSig() {
        return nodoSig;
    }

    public NodoDoble<T> getNodoAnt() {
        return nodoAnt;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public void setNodoSig(NodoDoble<T> nodoSig) {
        this.nodoSig = nodoSig;
    }

    public void setNodoAnt(NodoDoble<T> nodoAnt) {
        this.nodoAnt = nodoAnt;
    }

    @Override
    public String toString() {
        return (info != null) ? info.toString() : "null";
    }
}
