package Interface_Multipions;

import Pions.Pion;

import javax.swing.*;

//classe qui stocke les infos graphique de chaque case du plateau
public class GUI_Pions extends JPanel {

    JLabel label;
    Pion pion_present;
    int ligne;
    int colonne;

//constructeur avec arguments
    public GUI_Pions(int ligne, int colonne, JLabel Label1, Pion pion_present1) {
        super();

        this.ligne = ligne;
        this.colonne = colonne;
        this.label = Label1;
        this.add(label);
        this.setOpaque(false);
        this.pion_present = pion_present1;
    }

    @Override
    public String toString() {
        return "Pion présent :" + pion_present + ", ligne=" + ligne + ", colonne=" + colonne + "]";
    }

    public JLabel getLabel() {
        return label;
    }


}
