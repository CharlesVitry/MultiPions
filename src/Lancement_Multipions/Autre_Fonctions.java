package Lancement_Multipions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
// des fonctions diverses copiés sur stack overflow (sauf la première)
public class Autre_Fonctions {

 //on associe à chaque colonne une lettre , sur le modèle d'un plateau d'echec
    public static char getAlgebraicCharacterFromCol(int colonne) {
        char retour;

        if (colonne == 0)
            retour = 'a';
        else if (colonne == 1)
            retour = 'b';
        else if (colonne == 2)
            retour = 'c';
        else if (colonne == 3)
            retour = 'd';
        else if (colonne == 4)
            retour = 'e';
        else if (colonne == 5)
            retour = 'f';
        else if (colonne == 6)
            retour = 'g';
        else if (colonne == 7)
            retour = 'h';
        else
            retour = 'n';

        return retour;
    }

  //on créé le fichier de résumé de partie
    public static void writeToFile(String nomFichier, String contenu) {

        File fichier = new File(nomFichier);


        try {
            if (!fichier.exists())
                fichier.createNewFile();

            FileWriter fw = new FileWriter(fichier.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.append(contenu);

            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    // format correct du temps
    public static String getTime() {
        long timemillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

        Date bonnedate = new Date(timemillis);
        return format.format(bonnedate);
    }

   //date utilisable en nom de fichier
    public static String getTimeNoSpaces() {
        long timemillis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");

        Date resultdate = new Date(timemillis);
        return format.format(resultdate);
    }
}
