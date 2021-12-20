package Coups;

import Pions.Pion;

//Un coup est défini par la position de départ et la position d'arrivée
// j'ai stocker les coups comme cela , sans faire une seconde classe position.
public class Coup {
    int ligne_depart;
    int colonne_depart;
    int ligne_arrive;
    int colone_arrive;
    Pion pion;
    int score = 0;


    //constructeur non vide
    public Coup(Pion pion1, int ligne_depart1, int colonne_depart1, int ligne_arrive1,
                int colone_arrive1) {
        this.pion = pion1;
        this.ligne_depart = ligne_depart1;
        this.colonne_depart = colonne_depart1;
        this.ligne_arrive = ligne_arrive1;
        this.colone_arrive = colone_arrive1;

    }

    public Coup(Coup coup) {
        this.pion = coup.getPion();
        this.ligne_depart = coup.getLigne_depart();
        this.colonne_depart = coup.getColonne_depart();
        this.ligne_arrive = coup.getLigne_arrive();
        this.colone_arrive = coup.getColone_arrive();
    }

    // renvoit nouvelle coords du pion en String
    public String CoordsEnString() {
        return "Pion en ( " + (this.ligne_arrive + 1) + " , " + (this.colone_arrive + 1) + " )";
    }

    //operateur d'égalité entre deux coups
    public boolean egale(Coup Newcoup) {
        boolean retour = this.colonne_depart == Newcoup.getColonne_depart();

        if (this.colone_arrive != Newcoup.getColone_arrive())
            retour = false;
        if (this.ligne_depart != Newcoup.getLigne_depart())
            retour = false;
        if (this.ligne_arrive != Newcoup.getLigne_arrive())
            retour = false;
        return retour;

    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Pion getPion() {
        return pion;
    }

    public void setPion(Pion pion) {
        this.pion = pion;
    }

    public int getLigne_depart() {
        return ligne_depart;
    }

    public int getColonne_depart() {
        return colonne_depart;
    }

    public int getLigne_arrive() {
        return ligne_arrive;
    }

    public int getColone_arrive() {
        return colone_arrive;
    }


}
