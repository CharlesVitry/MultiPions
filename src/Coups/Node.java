package Coups;

import java.util.ArrayList;

public class Node {
    double score;
    int profondeur;
    Coup coup;
    Node parent;
    ArrayList<Node> enfant;

    public Node(Coup coup) {
        super();
        score = 1000;
        profondeur = 0;
        enfant = new ArrayList<>();
        this.coup = coup;

    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getEnfant() {
        return enfant;
    }

    public Coup getCoup() {
        return coup;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double d) {
        this.score = d;
    }


}
