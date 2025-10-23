package Cartas;

import Listas.ListaDobleCircular;

public class Mazo {
    private ListaDobleCircular <Carta> mazo;

    public Mazo() {
        mazo = new ListaDobleCircular<>();

    }

    private void llenar() {
        for (int i = 2; i <=14 ; i++) {
            for (Palo palo : Palo.values()){
                CartaInglesa carta = new CartaInglesa(i, palo, palo.getColor());
                carta.makeFaceUp();
                mazo.insertarFin(carta);
            }
        }
    }

    private void mezclar(){
        mazo.shuffle();
    }

    public ListaDobleCircular <Carta> getMazo() {
        return mazo;
    }

}
