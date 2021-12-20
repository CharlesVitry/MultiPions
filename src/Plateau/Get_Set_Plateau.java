package Plateau;

import Lancement_Multipions.Main;
import Pions.ArrayLists;
import Pions.Pion;

// On interragit avec les listes de pièces
public class Get_Set_Plateau {

    ArrayLists ListesPion;

//constructeur
    public Get_Set_Plateau(ArrayLists ListesPion1) {
        this.ListesPion = ListesPion1;

    }

   //on cherche quel pion est à ces coords
    public Pion getPionParCoords(int ligne, int colonne) {
        Pion pion = null;
        //System.out.println("On cherche le type de pièce situé en "+ligne+"  "+colonne);
        if (ligne < 0 || ligne > (Main.getTaille()-1) || colonne < 0 || colonne > (Main.getTaille()-1))
            return null;
       // try{
        pion = ListesPion.getPlateau()[ligne][colonne];
   //     }
       // catch (Exception e){
          //  System.out.println("Pion chercher en "+ligne +" "+colonne+" alors que taille : "+Infos.getTaille());}

        //System.out.print(pion);
        return pion;
    }

  //place un pion à ces coords
    public void setPionCoords(int ligne, int colonne, Pion pion) {

        if (ligne >= Main.getTaille()|| ligne < 0) {
            System.out.println("erreur 29");
        } else if (colonne >= Main.getTaille() || colonne < 0) {
            System.out.println("erreur 30");
        } else if (pion == null)
            System.out.println("erreur 31");
        else {
            ListesPion.getPlateau()[ligne][colonne] = pion;

        }
    }
    //on set cet emplacement du plateau à null (vide)
    public void setCoordsNull(int ligne, int colonne) {

        if (ligne >= Main.getTaille() || ligne < 0)
            System.out.println("erreur 32");
        else if (colonne >= Main.getTaille() || colonne < 0)
            System.out.println("erreur 33");
        else {
            ListesPion.getPlateau()[ligne][colonne] = null;

        }
    }
}
