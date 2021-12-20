package Pions;


public class Pions_List {

    private final Pion[] array;
    public static int nbre_Liste = 250;


    public Pions_List() {
        array = new Pion[nbre_Liste];
    }



    public void add(Pion pion) {
        array[pion.getID()] = pion;
    }

    public Pion getPiece(int num) {
        return array[num];
    }

    public void remove(Pion pion) {
        array[pion.getID()] = null;
    }



}
