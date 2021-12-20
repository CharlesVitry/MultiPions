package MinMax_IA;

import Coups.Coup;
import Coups.Node;
import Pions.Pion;

// On mets en place le système avec des nodes dans cette classe
public class GetNodes {
    Jeu jeu;
    Node racineNode;
    Node racine = new Node(null);


    public GetNodes(Jeu jeu) {

        this.jeu = jeu;
        racineNode = new Node(new Coup(new Pion( false, false, 0, 0, 0), 0, 0, 0, 0));

    }

    public Node getRacine() {
        return racine;
    }

    public void setRacine(Node racineUse) {
        boolean refrsh = false;
        for (Node node : racine.getEnfant()) {
            if (node.getCoup().egale(racineUse.getCoup())) {
                this.racine = node;
                refrsh = true;
            }
        }

        if (!refrsh) {

            System.out.println("Coup non existant en coords :  " + racineUse.getCoup().CoordsEnString() );

            this.racine = racineUse;

        }
    }
}
