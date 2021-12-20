package Pions;

import Lancement_Multipions.Autre_Fonctions;

//contient les infos d'un pion
public class Pion {

    boolean Blanc;
    boolean Abouger;
    int ligne;
    int colonne;
    int num;

  //constructeur
    public Pion( boolean blanc1, boolean Abouger1, int ligne1, int colonne1, int num1) {

        this.Blanc = blanc1;
        this.Abouger = Abouger1;
        this.ligne = ligne1;
        this.colonne = colonne1;
        this.num = num1;
    }


    public Pion(Pion pionCible) {
        this.Blanc = pionCible.Equipe();
        this.Abouger = pionCible.AdejaBouger();
        this.ligne = pionCible.getligne();
        this.colonne = pionCible.getColonne();
        this.num = pionCible.getID();
    }

    @Override
    public String toString() {
        String texte = "" + Autre_Fonctions.getAlgebraicCharacterFromCol(getColonne()) + getligne();
        if (Blanc)
            texte += " white ";
        else
            texte += " black ";

        return texte ;
    }

    public int getID() {
        return this.num;
    }

    public boolean Equipe() {
        return Blanc;
    }

    public void setBlanc(boolean Blanc) {
        this.Blanc = Blanc;
    }

    public boolean AdejaBouger() {
        return Abouger;
    }

    public void ABouger(boolean Abouger) {
        this.Abouger = Abouger;
    }

    public int getligne() {
        return ligne;
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

}
