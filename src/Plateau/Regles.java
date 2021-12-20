package Plateau;

import Coups.Coup;
import MinMax_IA.Jeu;
import Pions.Pion;
import Pions.Pions_List;

import java.util.ArrayList;

//application des regles du multipion
public class Regles {

    private static Jeu jeu;

    public Regles(Jeu jeu1) {
        Regles.jeu = jeu1;
    }


// coup correct ou non
    public static boolean CoupCorrect(Coup coup, Get_Set_Plateau get_set_plateau) {


        boolean retour = true;


        if (!PasMemeCoords(coup)) {
            retour = false;
        }
        if (retour) {
            if (!CaptureMemePion(coup, get_set_plateau)) {
                retour = false;
            }
        }
        if (retour) {
            if (!GetCoupPossible(coup, get_set_plateau)) {
                retour = false;
            }
        }


        return retour;
    }

   //regarde si position depart == position d'arrivee
    public static boolean PasMemeCoords(Coup coup) {

        boolean retour;
        retour = !(coup.getColonne_depart() == coup.getColone_arrive() && coup.getLigne_depart() == coup
                .getLigne_arrive());

        return retour;
    }

    //regarde si se capture lui meme
    public static boolean CaptureMemePion(Coup coup, Get_Set_Plateau get_set_plateau) {
        boolean retour;
        Pion piondepart = get_set_plateau.getPionParCoords(coup.getLigne_depart(),
                coup.getColonne_depart());
        Pion pionarrivee = get_set_plateau.getPionParCoords(coup.getLigne_arrive(),
                coup.getColone_arrive());

        if (pionarrivee == null)
            retour = true;

        else retour = piondepart.Equipe() != pionarrivee.Equipe();

        return retour;
    }

    //retourne si le coup indiqué est possible (un get)
    public static boolean GetCoupPossible(Coup coup, Get_Set_Plateau get_set_plateau) {return CoupPossiblePion(coup, get_set_plateau);}


   //regarde si coup effectue fait bien partit des coups jouables pour un pion
    public static boolean CoupPossiblePion(Coup coup,
                                           Get_Set_Plateau get_set_plateau) {
        boolean retour = false;

        if (coup.getColonne_depart() == coup.getColone_arrive()
                && get_set_plateau.getPionParCoords(coup.getLigne_arrive(),
                coup.getColone_arrive()) == null) {
            int lignecible = coup.getLigne_arrive() - coup.getLigne_depart();

            if (coup.getColone_arrive() == coup.getColonne_depart()) {

                if (lignecible == 1 && coup.getPion().Equipe())
                    retour = true;


                else if (lignecible == -1 && !coup.getPion().Equipe())
                    retour = true;

            }
        } else

            if (get_set_plateau.getPionParCoords(coup.getLigne_arrive(), coup.getColone_arrive()) != null) {

                int colonneCible = LongueurDeplacementColonne(coup);
                int lignecible = coup.getLigne_arrive() - coup.getLigne_depart();

                if (colonneCible == 1) {


                    if (coup.getPion().Equipe() && lignecible == 1)
                        retour = true;

                    else if (!coup.getPion().Equipe() && lignecible == -1)
                        retour = true;

                }
            }


            else if (!jeu.getListes().getListeCoups().isEmpty()) {
                Coup coupPrecedent = jeu.getListes().getListeCoups()
                        .get(jeu.getListes().getListeCoups().size() - 1);

                boolean couleurCoupPrecedent = coupPrecedent.getPion()
                        .Equipe() != coup.getPion().Equipe();
                boolean ColonneDesigne = coup.getColone_arrive() == coupPrecedent.getColonne_depart()
                        && LongueurDeplacementColonne(coup) == 1;

                if ( couleurCoupPrecedent
                        && ColonneDesigne) {
                    if (coup.getPion().Equipe()) {
                        if (LongueurDeplacementLigne(coupPrecedent) == -2
                                && coup.getLigne_arrive() == (coupPrecedent.getLigne_arrive() + 1)
                                && coup.getLigne_depart() == (coupPrecedent.getLigne_arrive()))
                            retour = true;
                    }

                    else {
                        if (LongueurDeplacementLigne(coupPrecedent) == 2
                                && coup.getLigne_arrive() == (coupPrecedent.getLigne_arrive() - 1)
                                && coup.getLigne_depart() == (coupPrecedent.getLigne_arrive()))
                            retour = true;

                    }
                }
            }

        return retour;
    }



    public static void ChangementDelete(Pion PionCapture, Coup coup) {
        if (PionCapture != null) {
            Pions_List pions;
            if (PionCapture.Equipe())
                pions = jeu.getListes().getPionsBlancs();
            else
                pions = jeu.getListes().getPionsNoirs();

            pions.add(PionCapture);

            jeu.getListes().getPionsCaptures().remove(PionCapture);

            jeu.getPlateauGet().setPionCoords(coup.getLigne_arrive(),
                    coup.getColone_arrive(), PionCapture);

        }

       //on retire ce coup de la liste
        ArrayList<Coup> CoupsListe = jeu.getListes().getListeCoups();
        CoupsListe.remove(coup);

        // on retire le coup du plateau
        jeu.getPlateauGet().setCoordsNull(coup.getLigne_arrive(),
                coup.getColone_arrive());

        // piece capture a son emplacement precedent
        if (PionCapture != null)
            jeu.getPlateauGet().setPionCoords(PionCapture.getligne(), PionCapture.getColonne(), PionCapture);


        jeu.getPlateauGet().setPionCoords(coup.getLigne_depart(), coup.getColonne_depart(), coup.getPion());

        coup.getPion().setLigne(coup.getLigne_depart());
        coup.getPion().setColonne(coup.getColonne_depart());

    }


  // applique le coup au plateau et renvoie les pions captures
    public static Pion ApplicationCoup(Coup coup) {
        Pion PionCapture = jeu.getPlateauGet().getPionParCoords(coup.getLigne_arrive(), coup.getColone_arrive());

        if (jeu.getPlateauGet().getPionParCoords(coup.getLigne_depart(), coup.getColonne_depart()) == null)
            System.out.println("erreur 4");

        ArrayList<Coup> CoupListe = jeu.getListes().getListeCoups();
        CoupListe.add(coup);

      //on retire pion capture
        if (PionCapture != null) {
            jeu.DeletePionListe(coup);
        }

        Pion PionCible = jeu.CaptureDePion(coup);

        if (PionCible != null)
            PionCapture = PionCible;


        jeu.getPlateauGet().setPionCoords(coup.getLigne_arrive(),
                coup.getColone_arrive(), coup.getPion());
        jeu.getPlateauGet().setCoordsNull(coup.getLigne_depart(),
                coup.getColonne_depart());

        coup.getPion().setLigne(coup.getLigne_arrive());
        coup.getPion().setColonne(coup.getColone_arrive());



        return PionCapture;
    }


    public static int LongueurDeplacementColonne(Coup move) {
        return Math.abs(move.getColonne_depart() - move.getColone_arrive());
    }

    public static int LongueurDeplacementLigne(Coup move) {
        return move.getLigne_arrive() - move.getLigne_depart();
    }

}
