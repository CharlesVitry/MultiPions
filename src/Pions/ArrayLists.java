package Pions;

import java.util.ArrayList;
import java.util.Arrays;

import Coups.Coup;
import Lancement_Multipions.Main;

// les différentes liste de pièces
public class ArrayLists {

    Pion[][] Plateau;
    Pions_List ListesBlancs;
    Pions_List ListesNoirs;
    ArrayList<Pion> PionsCapture;
    ArrayList<Coup> ListeCoups;
    String Mode;

   //constructeur
    public ArrayLists() {
        Mode = Main.getGameMode();
        ListesBlancs = new Pions_List();
        ListesNoirs = new Pions_List();
        PionsCapture = new ArrayList<>();
        ListeCoups = new ArrayList<>();

        System.out.println("Création plateau de taille "+ Main.getTaille());
        Plateau = new Pion[Main.getTaille()][Main.getTaille()];

        PlateauCreation();
        AjoutPionsListe();
    }




  //initialisation du PLateau avec ses pièces selon sa taille
    public void PlateauCreation() {

        Plateau = new Pion[Main.getTaille()][Main.getTaille()];
        boolean Abouger = false;
        boolean blanc = true;
        boolean Noir = false;

       //placement des lignes vides
        for (int ligne = 1; ligne < Main.getTaille() - 2; ligne++)
            for (int colonne = 0; colonne < Main.getTaille(); colonne++)
                Plateau[ligne][colonne] = null;

       //placement pion blancs
        for (int i = 0; i < Main.getTaille(); i++) {
            Plateau[0][i] = new Pion( blanc, Abouger, 1, i, i + Main.getTaille());
        }


       //placement pions noirs
        for (int i = 0; i < Main.getTaille(); i++) {
            Plateau[(Main.getTaille()-1)][i] = new Pion( Noir, Abouger, 6, i, i + Main.getTaille());
        }
        System.out.println("plateau : "+ Arrays.deepToString(Plateau));

    }


   //ajout au liste de pièces
    public void AjoutPionsListe() {
        for (int lignes = 0; lignes < Main.getTaille(); lignes++) {
            for (int colonne = 0; colonne < Main.getTaille(); colonne++)
                if (Plateau[lignes][colonne] != null && Plateau[lignes][colonne].Equipe())
                    ListesBlancs.add(Plateau[lignes][colonne]);

        }
        for (int ligne = 0; ligne < Main.getTaille(); ligne++) {
            for (int colonne = 0; colonne < Main.getTaille(); colonne++) {
                if (Plateau[ligne][colonne] != null && !Plateau[ligne][colonne].Equipe())
                    ListesNoirs.add(Plateau[ligne][colonne]);
            }

        }
    }

    public Pion[][] getPlateau() {
        return Plateau;
    }

    public void setPlateau(Pion[][] plateau) {
        this.Plateau = plateau;
    }

    public ArrayList<Coup> getListeCoups() {
        return ListeCoups;
    }

    public Pions_List getPionsBlancs() {
        return ListesBlancs;
    }

    public Pions_List getPionsNoirs() {
        return ListesNoirs;
    }

    public String getMode() {
        return Mode;
    }

    public ArrayList<Pion> getPionsCaptures() {
        return PionsCapture;
    }

    public void setMode(String mode) {
        this.Mode = mode;
    }

}
