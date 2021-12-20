package MinMax_IA;

import java.util.ArrayList;

import Coups.Coup;
import Coups.Node;
import Lancement_Multipions.Main;
import Pions.Pion;
import Pions.Pions_List;
import Plateau.Regles;

// Classe d'intelligence artificiel recherchant le meilleur coup
public class MinMax {

    Jeu jeu;
    int nodeVisite = 0;
    int profondeur;
    Node meilleurNode;
    ArrayList<Node> ListeTest;
    ArrayList<Node> ListeValide = new ArrayList<>();
    ArrayList<ArrayList<Coup>> ListeCoupSmart = new ArrayList<>(
            Main.getProfondeur() + 1);
    Node[] listeNodes;
    int[] Nbre_Nodes;
    static Operator_Node node_operateur;

    int nbre_Branche;
    int numero_branche;

    public MinMax(Jeu jeu2) {
        this.jeu = jeu2;

        for (int i = 0; i < Main.getProfondeur() + 1; i++) {
            ListeCoupSmart.add(new ArrayList<Coup>());
        }

        listeNodes = new Node[10];
        node_operateur = new Operator_Node();


        for (int i = 0; i < 10; i++)
            listeNodes[i] = null;

        Nbre_Nodes = new int[20];
    }
//lance la recherche du meilleur coup
    public Node coup(boolean TourAuBlanc) {
        Node node;
        choixCoup(TourAuBlanc);
        node = meilleurNode;
        nodeVisite = 0;
        return node;
    }

  // méthode alpha beta utilisé avec elagage pour éviter
    // un temps de recherche dépassant 30 secondes pour les grands plateau
    public void choixCoup(boolean touraublanc) {


        double alpha = -Main.getPoidVictoireDirect();
        double beta = Main.getPoidVictoireDirect();
        nbre_Branche = 0;
        Node parent = jeu.getnodes.racine;

        ListeCoupsSmartStart();

        // recherche avec profondeur/difficulté souhaité
        for (int profondeur = 1; profondeur <= Main.getProfondeur(); profondeur++) {
            this.profondeur = profondeur;

            if (profondeur == Main.getProfondeur()) {
                numero_branche = parent.getEnfant().size();
            }


            recherche(alpha, beta, profondeur, touraublanc, parent);

            ListeValide = new ArrayList<>();
            ListeValide.addAll(this.ListeTest);

        }
        //si il n'y a plus aucun coup possible pour l'IA
        if (this.ListeValide.size() == 0) {
            jeu.victoire = true;
            jeu.FindePartieTest();

        } else {
            //si non le meilleur est le premier
            meilleurNode = this.ListeValide.get(0);
        }

    }

// vide listes
    private void ListeCoupsSmartStart() {

        for (int i = 0; i <= Main.getProfondeur(); i++)
            ListeCoupSmart.get(i).removeAll(ListeCoupSmart.get(i));

    }


    double recherche(double alpha, double beta, int profondeurReste, boolean tourAublanc, Node parent) {
        double score;
        boolean Abouger;
        boolean recherche = true;

        PlateauCorrect();


        if (profondeurReste == 0) {
            this.ListeTest = new ArrayList<>();
            this.ListeTest.add(parent);
            return resultatRecherche(alpha, beta, tourAublanc, parent, profondeurReste);
        }

        ArrayList<Node> ListeTest = new ArrayList<>();

        if (parent.getEnfant().size() == 0)
            childrenPossible(parent, tourAublanc, profondeurReste);



        // tant que la profondeur n'est pas atteinte , on continu la recherche
        boolean ProfondeurAtteinte = ListeValide.size() > this.profondeur + 1 - profondeurReste;

        if (ProfondeurAtteinte && ListeTestOK(parent, profondeurReste)) {
            NoeudPrincipale(parent, this.ListeValide.get(this.profondeur + 1 - profondeurReste));
        }

        for (int j = 0; j < parent.getEnfant().size(); j++) {

            refreshProfondeur(profondeurReste, j);
            Node node = parent.getEnfant().get(j);
            Coup coup = node.getCoup();

            Pion pioncapture = Regles.ApplicationCoup(coup);
            Abouger = coup.getPion().AdejaBouger();
            coup.getPion().ABouger(true);


            if (recherche) {
                score = -recherche(-beta, -alpha, profondeurReste - 1, !tourAublanc,
                        node);
            } else {
                score = -recherche(-alpha - 0.00000001, -alpha, profondeurReste - 1,
                        !tourAublanc, node);
                if (score > alpha) {
                    //tant que score superieur a alpha , on continu de chercher
                    score = -recherche(-beta, -alpha, profondeurReste - 1, !tourAublanc, node);
                }
            }


            Regles.ChangementDelete(pioncapture, coup);
            coup.getPion().ABouger(Abouger);


            if (score >= beta) {


                boolean nodeTrouve = false;
                for (Coup smartNode : ListeCoupSmart.get(profondeurReste))
                    if (smartNode.egale(node.getCoup()))
                        nodeTrouve = true;
                if (!nodeTrouve) {
                    ListeCoupSmart.get(profondeurReste).add(node.getCoup());

                }
                return beta;
            }


            if (score > alpha) {
                alpha = score;
                ListeTest = this.ListeTest;

               // on sauvegarde le meilleur coup si la profondeur max est atteinte
                if (profondeurReste == Main.getProfondeur()) {
                    meilleurNode = node;
                }
            }

            recherche = false;
        }

        ArrayList<Node> ListeNodeTeste = new ArrayList<>();
        if (profondeurReste != Main.getProfondeur())
            ListeNodeTeste.add(parent);
        ListeNodeTeste.addAll(ListeTest);
        this.ListeTest = ListeNodeTeste;
        return alpha;
    }

    private void refreshProfondeur(int profondeur, int nbre_branches) {
        if (profondeur == Main.getProfondeur())
            nbre_Branche = nbre_branches;

    }


  //on regarde si l'array liste plateau correspond bien
    private void PlateauCorrect() {


        for (int x = 0; x < Main.getTaille(); x++) {
            for (int y = 0; y < Main.getTaille(); y++)
                if (jeu.getPlateauGet().getPionParCoords(x, y) != null)
                    if (jeu.getPlateauGet().getPionParCoords(x, y).getligne() != x || jeu.getPlateauGet().getPionParCoords(x, y).getColonne() != y) {
                        System.out.print("Le plateau ne correspond pas à ce qu'il devrait être");

                        return;
                    }
        }
    }


   //recherche Alpha Beta mais sans elagage (recherche de coups pouvant atteindre 21 minutes)
    double alphaBeta(double alpha, double beta, int profondeurReste, boolean Blanc,
                     Node parent) {
        double score;
        boolean abougee;


        if (profondeurReste == 0) {
            this.ListeTest = new ArrayList<>();
            this.ListeTest.add(parent);
            return resultatRecherche(alpha, beta, Blanc, parent, profondeurReste);
        }
        ArrayList<Node> ListeTestee = new ArrayList<>();
        if (parent.getEnfant().size() == 0)
            childrenPossible(parent, Blanc, profondeurReste);

        boolean ProfondeurAtteinte = ListeValide.size() > this.profondeur - profondeurReste + 1;

        if (ProfondeurAtteinte && ListeTestOK(parent, profondeurReste))
            NoeudPrincipale(parent, this.ListeValide.get(this.profondeur - profondeurReste + 1));

        for (int j = 0; j < parent.getEnfant().size(); j++) {

            Node node = parent.getEnfant().get(j);
            Coup coup = node.getCoup();

            Pion pioncapture = Regles.ApplicationCoup(coup);
            abougee = coup.getPion().AdejaBouger();
            coup.getPion().ABouger(true);

            score = -alphaBeta(-beta, -alpha, profondeurReste - 1, !Blanc, node);


            Regles.ChangementDelete(pioncapture, coup);
            coup.getPion().ABouger(abougee);

            if (score >= beta) {
                boolean trouver = false;
                for (Coup coupSmart : ListeCoupSmart.get(profondeurReste))
                    if (coupSmart.egale(node.getCoup()))
                        trouver = true;
                if (!trouver)
                    ListeCoupSmart.get(profondeurReste).add(node.getCoup());
                return beta;
            }


            if (score > alpha) {
                alpha = score;
                ListeTestee = this.ListeTest;


                if (profondeurReste == Main.getProfondeur()) {
                    meilleurNode = node;

                }
            }
        }


        ArrayList<Node> ListeNodeTesteFinal = new ArrayList<>();
        if (profondeurReste != Main.getProfondeur())
            ListeNodeTesteFinal.add(parent);
        ListeNodeTesteFinal.addAll(ListeTestee);
        this.ListeTest = ListeNodeTesteFinal;
        return alpha;
    }

   //applique la recherche et trouve le meilleur compromis
    public double resultatRecherche(double alpha, double beta, boolean blanc,
                                    Node parent, int profondeurRestante) {
        Nbre_Nodes[this.profondeur]++;

        double evalNode = ScoreNodeParent(blanc, parent);
        if (evalNode >= beta)
            return beta;
        if (alpha < evalNode)
            alpha = evalNode;
        if (parent.getEnfant().size() == 0)
            childrenPossible(parent, blanc, profondeurRestante);

        for (Node node : parent.getEnfant()) {
            Coup coup = node.getCoup();
            int ligne = coup.getLigne_arrive();
            int colonne = coup.getColone_arrive();

            jeu.plateauGet.getPionParCoords(ligne,
                    colonne);


        }
        return alpha;
    }


    private boolean ListeTestOK(Node parent, int profondeurRestante) {

        int i = this.profondeur - profondeurRestante;
        boolean retour = true;

        while (i >= 0 && retour) {
            if (parent == ListeValide.get(i)) {

                parent = parent.getParent();
                i--;
            } else
                retour = false;

        }

        return retour;
    }

   // on set le premier node a analyser
    public void NoeudPrincipale(Node parent, Node children) {
        boolean deletreReussie = parent.getEnfant().remove(children);
        if (deletreReussie)
            parent.getEnfant().add(0, children);
    }

   //coups possible pour ce node parent
    public void childrenPossible(Node parent, boolean blanc,
                                 int profondeurrestante) {

        ArrayList<Coup> coupsPossible = jeu.getCoupRecherche().ListeCoupsCoords(
                blanc);
        for (Coup coup : coupsPossible) {

            Node node = new Node(coup);

            parent.getEnfant().add(node);
            node.setParent(parent);
        }

        CoupsOrdonneParPoid(parent.getEnfant(), profondeurrestante);
    }


  //on ordonne les coups par leur poids
    public void CoupsOrdonneParPoid(ArrayList<Node> nodes, int profondeurRestante) {

        for (Node node : nodes) {

            Pion pionCible = jeu.plateauGet.getPionParCoords(node
                    .getCoup().getLigne_arrive(), node.getCoup().getColone_arrive());

           //si capture possible
            if (pionCible != null) {
                int materialScore = 0;
                node.setScore(materialScore);
            }
            else {
                boolean nodeTrouve = false;
                for (Coup coupSmart : ListeCoupSmart.get(profondeurRestante))
                    if (coupSmart.egale(node.getCoup()))
                        nodeTrouve = true;
                if (nodeTrouve) {
                    node.setScore(Main.getCoupSmart());
                }
            }
        }
        nodes.sort(node_operateur);

    }

  //retourne poid du coup
    public double ScoreNodeParent(boolean Blanc, Node node) {
        if (this.profondeur == Main.getProfondeur())
            nodeVisite++;
        double retour;

        int positionalScore = PoidPosition(Blanc, node);
        int materialScore = calculTactiqueScore();


        double weightedPositionalScore = positionalScore
                * Main.getPositionalPoid();
        double weightedMaterialScore = materialScore
                * Main.getTactiquePoid();



        retour = weightedPositionalScore + weightedMaterialScore;
        // on inverse car on a calculé par rapport au joueur blanc
        if (!Blanc)
            retour = retour * -1.0;

        // pas de coups possible
        if (node.getEnfant().size() == 0)
            retour = Main.getEgalitePoid();

        return retour;
    }


    public int PoidPosition(boolean Blanc, Node node) {
        int BlancCoups;
        int NoirsCoups;
        int profondeur = 0;

        if (Blanc) {
            if (node.getEnfant().size() == 0)
                childrenPossible(node, true, profondeur);

            BlancCoups = node.getEnfant().size();

            NoirsCoups = jeu.getCoupRecherche().ListeCoupsCoords(false).size();
        } else {

            if (node.getEnfant().size() == 0)
                childrenPossible(node, false, profondeur);
            NoirsCoups = node.getEnfant().size();

            BlancCoups = jeu.getCoupRecherche().ListeCoupsCoords(true).size();

        }

        return BlancCoups - NoirsCoups;
    }

  //retourne difference de l'avantage
    public int calculTactiqueScore() {
        int retour;
        int BlancScore = 0;
        int NoirScore = 0;

        Pions_List PionsBlancs = jeu.getListes().getPionsBlancs();
        Pions_List PionsNoirs = jeu.getListes().getPionsNoirs();

        for (int i = 0; i < Pions_List.nbre_Liste; i++) {
            Pion piece = PionsBlancs.getPiece(i);
            if (piece != null) {
                BlancScore += Main.getPoidPiece();


                // Victoire possible
                if ( piece.getligne() == (Main.getTaille()-1) ){
                    System.out.println("Victoire possible pour Blanc");

                    BlancScore += Main.VictoirePoid() ;
                }
            }
        }
        for (int i = 0; i < Pions_List.nbre_Liste; i++) {
            Pion pion = PionsNoirs.getPiece(i);
            if (pion != null) {
                NoirScore += Main.getPoidPiece();

                if ( pion.getligne() == 0) {
                    System.out.println("Victoire possible pour Noir");
                    NoirScore += Main.VictoirePoid() ;
                }
            }
        }


        retour = BlancScore - NoirScore;

        return retour;
    }




}

