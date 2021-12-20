package Coups;

import Lancement_Multipions.Main;
import MinMax_IA.Jeu;
import Pions.Pion;
import Plateau.Get_Set_Plateau;
import Plateau.Regles;

import java.util.ArrayList;

// Permet avec l'association de la classe Regle de renvoyer tout les coups possibles
//sans enfreindre les règles du multipion
public class Recherche_Coups {

    Get_Set_Plateau PlateauPion;
    Regles regle;
    Jeu Jeu;

    //constructeur avec arguments
    public Recherche_Coups(Get_Set_Plateau PlateauPion1,
                           Regles regle1, Jeu Jeu1) {
        this.PlateauPion = PlateauPion1;
        this.regle = regle1;
        this.Jeu = Jeu1;
    }

    // entrée : coords d'un pion , sortie : ses coups possible
    public ArrayList<Coup> ListeCoupsCoords(ArrayList<Coup> CoupsPossibles, int ligne, int colonne) {
        Pion pion = PlateauPion.getPionParCoords(ligne, colonne);
        if (pion != null)
            PionCoupsPossible(CoupsPossibles, ligne, colonne);


        return CoupsPossibles;
    }

    //entrée : equipe , sortie : coups possible
    public ArrayList<Coup> ListeCoupsCoords(boolean equipe) {
        Pion pion;
        ArrayList<Coup> CoupsPossibles = new ArrayList<>();
        for (int ligne = 0; ligne < Main.getTaille(); ligne++) {
            for (int colonne = 0; colonne < Main.getTaille(); colonne++) {
                pion = PlateauPion.getPionParCoords(ligne, colonne);
                if (pion != null) {
                    if (equipe == pion.Equipe()) {

                        ListeCoupsCoords(CoupsPossibles, ligne, colonne);
                    }
                }
            }
        }

        return CoupsPossibles;
    }

    // es ce qu'un coup est possible ?
    public boolean bloquer(boolean blanc) {
        Pion pion;
        int ligne = 0;
        int colonne;
        int nbre_coups_possible = 0;
        boolean retour = false;
        ArrayList<Coup> coupsPossible = new ArrayList<>();
        while (ligne < Main.getTaille() && nbre_coups_possible == 0) {
            colonne = 0;
            while (colonne < Main.getTaille() && nbre_coups_possible == 0) {
                pion = PlateauPion.getPionParCoords(ligne, colonne);
                if (pion != null) {
                    if ((blanc == pion.Equipe())) {
                        nbre_coups_possible += ListeCoupsCoords(coupsPossible, ligne, colonne).size();
                    }
                }
                colonne++;
            }
            ligne++;
        }

        if (nbre_coups_possible == 0) {


            retour = true;
        }

        if (retour)
            System.out.println("Bloquer ! ");
        return retour;
    }


    //coup possible pour un pion
    public void PionCoupsPossible(ArrayList<Coup> coupPossibles, int ligne, int colonne) {

        Pion pion = PlateauPion.getPionParCoords(ligne, colonne);
        Coup coup;
        int lignedevant;
        if (pion.Equipe())
            lignedevant = 1;
        else
            lignedevant = -1;

        //avance simple
        coup = new Coup(pion, ligne, colonne, ligne + lignedevant, colonne);
        if (Regles.CoupPossiblePion(coup, PlateauPion)
        ) {
            coupPossibles.add(coup);

        }

        // prise sur la droite
        int newColonne = colonne + 1;
        int NewLigne = ligne + lignedevant;
        coup = new Coup(pion, ligne, colonne, NewLigne, newColonne);
        if (CaseExiste(NewLigne, newColonne)
                && PrisePossible(pion, NewLigne, newColonne)
                && Regles.CoupPossiblePion(coup, PlateauPion)
        ) {

            //	if (move.getEndRow() == 0 || move.getEndRow() == 7) {
            //l'IA a gagné
            //System.out.println("Victoire de l'IA 2 ");
            //controller.victoire = true;
            //controller.isGameOver();


            //}
            coupPossibles.add(coup);
        }

        // prise sur la gauche
        newColonne = colonne - 1;

        coup = new Coup(pion, ligne, colonne, NewLigne, newColonne);
        if (CaseExiste(NewLigne, newColonne)
                && PrisePossible(pion, NewLigne, newColonne)
                && Regles.CoupPossiblePion(coup, PlateauPion)
        ) {

            //	if (move.getEndRow() == 0 || move.getEndRow() == 7) {

            //l'IA a gagné
            //System.out.println("Victoire de l'IA 1 ");
            //controller.victoire = true;
            //controller.isGameOver();


            //	}

            coupPossibles.add(coup);
        }
    }

    //Savoir si la case existe (selon grandeur tableau)
    public boolean CaseExiste(int ligne, int colonne) {

        return ligne >= 0 && ligne <= (Main.getTaille() - 1) && colonne >= 0 && colonne <= (Main.getTaille() - 1);
    }


    //voir si la case cible est un ennemi
    public boolean PrisePossible(Pion pion, int ligne, int colonne) {
        boolean retour = false;
        Pion PionCible = PlateauPion.getPionParCoords(ligne, colonne);

        if (PionCible == null || (PionCible.Equipe() != pion.Equipe()))
            retour = true;
        return retour;
    }


}
