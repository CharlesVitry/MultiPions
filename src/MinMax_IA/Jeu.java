package MinMax_IA;

import Coups.Coup;
import Coups.Recherche_Coups;
import Coups.Node;
import Interface_Multipions.GUI;
import Lancement_Multipions.Main;
import Pions.*;
import Pions.ArrayLists;
import Plateau.Get_Set_Plateau;
import Plateau.Listener_Interface;
import Plateau.Regles;
import Lancement_Multipions.Autre_Fonctions;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


//Lien entre l'interface et les classes de jeu et d'IA
public class Jeu {

    GUI gui;
    ArrayLists listes;
    Get_Set_Plateau plateauGet;
    Listener_Interface ListenerGui;
    Regles regle = new Regles(this);
    Recherche_Coups coupRecherche;
    GetNodes getnodes;
    MinMax minmaxIA;
    Runnable minmaxThread;
    java.lang.Thread thread;
    boolean victoire = false;


//constructeur
    public Jeu() {

        listes = new ArrayLists();
        plateauGet = new Get_Set_Plateau(listes);
        coupRecherche = new Recherche_Coups(plateauGet, regle, this);
        ListenerGui = new Listener_Interface(this);
        gui = new GUI(this, plateauGet, ListenerGui,
                listes.getPionsCaptures());
        getnodes = new GetNodes( this);
        minmaxIA = new MinMax(this);
        this.minmaxThread = new Thread(this);
        this.thread = new java.lang.Thread(minmaxThread);
        this.thread.start();
    }
    public void NouvelleTaille(){
        System.out.println("Reconfig des classes");
        listes.PlateauCreation();
        this.listes = new ArrayLists();
        this.plateauGet = new Get_Set_Plateau(listes);
        this.coupRecherche = new Recherche_Coups(plateauGet, regle, this);
        this.ListenerGui = new Listener_Interface(this);

        this.getnodes = new GetNodes( this);
        this.minmaxIA = new MinMax(this);


        gui.CacheFenetre();
        gui = new GUI(this, plateauGet, ListenerGui, listes.getPionsCaptures());

        this.minmaxThread = new Thread(this);
        this.thread = new java.lang.Thread(minmaxThread);
        this.thread.start();
    }



    public void ListenerMouse(MouseEvent e) {
        int ligne = computeRowFromMouseEvent(e);
        int colonne = coordsSourisenCoordsPlateau(e);
        gui.changemementImageDeplace(ligne, colonne);

        ListenerDeplacementSouris(e);
        gui.DeletePionDeplaceLabel(ligne, colonne);
        if (plateauGet.getPionParCoords(ligne, colonne) != null)
            affichageCoupsPossibles();
    }

   // indication coups possible
    private void affichageCoupsPossibles() {

        MouseEvent e = ListenerGui.getClicSouris();

        int ligne = computeRowFromMouseEvent(e);
        int colonne = coordsSourisenCoordsPlateau(e);

        ArrayList<Coup> coupsPossibles = new ArrayList<>();
        coupRecherche.ListeCoupsCoords(coupsPossibles, ligne, colonne);

        for (Coup move : coupsPossibles)
            gui.IndicationCoupPossible(move.getLigne_arrive(), move.getColone_arrive());

    }

// les images sont en 64x64 pixels , -32 => cela ciblera le centre de l'image
    public void ListenerDeplacementSouris(MouseEvent e) {
        gui.deplacementPion(e.getX() - 32, e.getY() - 32);
    }


    public void ListenerMouseRelease(MouseEvent e) {
        gui.DeletePionDeplace();
        boolean finpartie;
        MouseEvent clicSouris = ListenerGui.getClicSouris();
        int ligne_depart= computeRowFromMouseEvent(clicSouris);
        int colonne_depart = coordsSourisenCoordsPlateau(clicSouris);
        int ligne_arrive = computeRowFromMouseEvent(e);
        int colone_arrive = coordsSourisenCoordsPlateau(e);
        boolean limitePlateau = LimitePlateauSouris(ligne_arrive, colone_arrive);
        Pion pion = plateauGet.getPionParCoords(ligne_depart, colonne_depart);
        Coup coup = new Coup(pion, ligne_depart, colonne_depart, ligne_arrive, colone_arrive);

        if (entreeCorrect(pion) && pion != null && limitePlateau)
            ExecutionCoupChoisi(coup);

        gui.suppressionAideCoupsPossible();
        gui.refreshInterface();

        finpartie = FindePartieTest();

        if (FindePartieTest())
            if(gui.finJeu()){
                NouvelleTaille();
                victoire = false;}


        if (listes.getListeCoups().size() != 0)
            gui.IndicationDernierCoup(listes.getListeCoups());

        if (TourIA() && !finpartie) {
            Runnable Thread = new Thread_Lancement(this, minmaxIA, TourAuBlanc());
            java.lang.Thread Thread_minmax = new java.lang.Thread(Thread);
            Thread_minmax.start();

        }


    }


    public void ExecutionCoupChoisi(Coup coup) {
        boolean CoupTrouve = false;
        if (Regles.CoupCorrect(coup, plateauGet)) {
            Node racine = getnodes.getRacine();
            if (racine.getEnfant().size() != 0)
                for (Node node : racine.getEnfant())
                    if (node.getCoup().egale(coup)) {
                        CoupTrouve = true;
                        AjoutCoupNode(node);
                    }
            if (!CoupTrouve) {
                AjoutCoupNode(new Node(coup));}
        }
    }


    public void AjoutCoupNode(Node node) {
        Coup coup = node.getCoup();
        listes.getListeCoups().add(coup);
        CasSpecial(coup);
        refreshListePion(coup);
        plateauGet.setPionCoords(coup.getLigne_arrive(), coup.getColone_arrive(),
                coup.getPion());
        coup.getPion().setColonne(coup.getColone_arrive());
        coup.getPion().setLigne(coup.getLigne_arrive());
        if (plateauGet
                .getPionParCoords(coup.getLigne_depart(), coup.getColonne_depart()) == null) {
            System.out.print("L'IA est battu, elle admet sa défaite");
            victoire = true;
            FindePartieTest();
        } else {
            plateauGet
                    .getPionParCoords(coup.getLigne_depart(), coup.getColonne_depart())
                    .ABouger(true);

            plateauGet.setCoordsNull(coup.getLigne_depart(), coup.getColonne_depart());

            getnodes.setRacine(node);
            node.setParent(null);
        }
    }


    public boolean entreeCorrect(Pion pion) {
        String couleur = CouleurJoueurActuel();
        boolean retour = false;

        if (pion != null) {
            if (couleur.equals("white") && pion.Equipe()
                    && !couleurIAblanche()) {
                retour = true;
            } else if (couleur.equals("black") && !pion.Equipe()
                    && !couleurIAnoir())
                retour = true;
        }
        return retour;
    }


    public boolean TourIA() {
        boolean result = false;
        String couleur = CouleurJoueurActuel();
        if (couleur.equals("white") && couleurIAblanche()) {
            result = true;
        } else if (couleur.equals("black") && couleurIAnoir())
            result = true;

        return result;

    }


    public boolean FindePartieTest() {
        boolean result = false;

        if (victoire)
            return true;

        if (coupRecherche.bloquer(true) || coupRecherche.bloquer(false))
            result = true;
        return result;
    }


    private boolean LimitePlateauSouris(int ligne_arrive, int colone_arrive) {
        return (ligne_arrive >= 0 && ligne_arrive <= (Main.getTaille() -1) && colone_arrive >= 0 && colone_arrive <= (Main.getTaille()-1));
    }


    private void CasSpecial(Coup coup) {
        CaptureDePion(coup);
        VictoirePossible(coup);

    }


    private void VictoirePossible(Coup coup) {

        if (coup.getLigne_arrive() == (Main.getTaille()-1) || coup.getLigne_arrive() == 0) {
            victoire = true;
            FindePartieTest();
        }

    }


    private void refreshListePion(Coup coup) {
        if (plateauGet
                .getPionParCoords(coup.getLigne_arrive(), coup.getColone_arrive()) != null)
            DeletePionListe(coup);

    }


    public Pion CaptureDePion(Coup coup) {
        Pion pionCapture = null;

        if ( coup.getColonne_depart() != coup.getColone_arrive()
                && plateauGet.getPionParCoords(coup.getLigne_arrive(),
                coup.getColone_arrive()) == null) {
            int taille = listes.getListeCoups().size();

            Coup precedent_coup;

            precedent_coup = listes.getListeCoups().get(taille - 2);


            DeletePionListe(precedent_coup);

            pionCapture = precedent_coup.getPion();

            plateauGet.setCoordsNull(precedent_coup.getLigne_arrive(),
                    precedent_coup.getColone_arrive());
        }
        return pionCapture;

    }


    public void DeletePionListe(Coup pion) {

        Pion Pionsupprime = plateauGet.getPionParCoords(pion.getLigne_arrive(),
                pion.getColone_arrive());

        if (Pionsupprime == null)
            System.out.println("Essai de suppression d'une pièce inexistante");
        assert Pionsupprime != null;
        if (Pionsupprime.Equipe()) {
            listes.getPionsBlancs().remove(Pionsupprime);
        } else
            listes.getPionsNoirs().remove(Pionsupprime);

        listes.getPionsCaptures().add(Pionsupprime);

    }

// les images ne sont de 1x1 pixel, on convertit les coords
    public int coordsSourisenCoordsPlateau(MouseEvent e) {
        boolean orientation;

        orientation = gui.getVision();

        int retour = e.getX() / 64;
        if (orientation)
            retour = ( Main.getTaille() -1) - retour;

        return retour;
    }

  //retourne ligne désigné par souris
    public int computeRowFromMouseEvent(MouseEvent e) {

        boolean orientation;

        orientation = gui.getVision();

        int retour = ((Main.getTaille() *64) - e.getY()) / 64;

        if (orientation)
            retour = (Main.getTaille() -1)- retour;

        return retour;
    }

   // On effectue les actions selectionné dans la barre de menu
    public void ActionsBarreMenu(ActionEvent e) {
        if (e.getActionCommand().equals("Nouvelle partie")) {

           if (gui.GetMode() != null) {
                listes.setMode(gui.GetMode() );
                Main.setGameMode(gui.GetMode());
           }

            Main.setTaille (gui.GetTaillePlateau()) ;
            NouvelleTaille();
            victoire = false;
            //model.resetModel();
            gui.refreshInterface();
        } else if (e.getActionCommand().equals("Changement de difficulte")) {
        	System.out.println("changement");
            NouvelleProfondeur();
        } else if (e.getActionCommand().equals("Changement de vue")) {
            gui.refreshdeLorientation();

        } else if (e.getActionCommand().equals("Sauvegarde des coups joués"))
            CreationFichierCoupsjouee();
        else
            System.out.println("erreur 15");

    }

   //changement de la profondeur
    private void NouvelleProfondeur() {
    	System.out.println(gui.GetDifficulte());
        Main.setProfondeur(gui.GetDifficulte());
        if (gui.GetMode() != null) {
            listes.setMode(gui.GetMode() );
            Main.setGameMode(gui.GetMode());
        }

        Main.setTaille (gui.GetTaillePlateau()) ;
        NouvelleTaille();
        victoire = false;
        //model.resetModel();
        gui.refreshInterface();
    }


   //fichier texte des coups Joues
    private void CreationFichierCoupsjouee() {

        ArrayList<Coup> coupListe = listes.getListeCoups();
        String nomfichier = "Partie du "
                + Autre_Fonctions.getTimeNoSpaces() + ".txt";

        StringBuilder contenu = new StringBuilder("Coups joués");
        contenu.append("\n");
        contenu.append("\n  Blanc             Noir");


        for (int i = 0; i < coupListe.size(); i++) {
            if (i % 2 == 0)
                contenu.append("\n").append(i / 2 + 1).append(" ");

            contenu.append(coupListe.get(i).CoordsEnString()).append(" ");
        }

        Autre_Fonctions.writeToFile(nomfichier, contenu.toString());

    }




    public String CouleurJoueurActuel() {
        String result;
        if (TourAuBlanc())
            result = "white";
        else
            result = "black";
        return result;
    }


    public boolean TourAuBlanc() {
        boolean result;
        int turnNumber = listes.getListeCoups().size();

        result = (turnNumber % 2) != 1;
        return result;
    }


    public boolean couleurIAblanche() {

        return listes.getMode().equals("IAvsJ")
                || listes.getMode().equals("IAvsIA");
    }


    public boolean couleurIAnoir() {

        return listes.getMode().equals("IAvsIA")
                || listes.getMode().equals("JvsIA");

    }


    public ArrayLists getListes() {
        return listes;
    }

    public Get_Set_Plateau getPlateauGet() {
        return plateauGet;
    }


    public GUI getGui() {
        return gui;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }


    public Recherche_Coups getCoupRecherche() {
        return coupRecherche;
    }

    public MinMax getMinmaxIA() {
        return minmaxIA;
    }


}
