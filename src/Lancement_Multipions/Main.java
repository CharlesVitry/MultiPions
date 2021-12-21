package Lancement_Multipions;

import MinMax_IA.Jeu;


public class Main {


    public static void main(String[] parametres) {
        System.out.println("Lancement Multipions");

        new Jeu();


    }
    private static int taille = 4;
    // Poids pour IA
    private final static int PoidPion = 50;
    private final static int VictoirePoid = 500;
    private final static int PoidTotale = 5000;
    private final static int EgalitePoid = 0;
    private final static double coupSmart = -0.1;


    // profondeur recherche , cad la difficulte de l'IA
    private static final int profondeurdefaut = 6;
    private static int profondeur = profondeurdefaut;



    // Mode constants
    private static final String gamemodeDefaut = "JvsIA";
    private static String gameMode = gamemodeDefaut;


    public static int getPoidPiece() {return PoidPion;}

    public static int VictoirePoid() {

        return VictoirePoid;
    }


    public static int getTaille() {
        return taille;
    }

    public static void setTaille(int i) {
        taille= i;

    }

    public static double getPositionalPoid() {
        return .7;
    }
    public static double getTactiquePoid() {
        return .3;
    }
    public static int getProfondeur() {
        return profondeur;
    }
    public static double getCoupSmart() {
        return coupSmart;
    }


    public static void setProfondeur(int i) {
        profondeur = i;

    }

    public static double getPoidTotale() {
        return PoidTotale;
    }

    public static double getEgalitePoid() {
        return EgalitePoid;
    }

    public static String getGameMode() {
        return gameMode;
    }

    public static void setGameMode(String gameMode) {
        Main.gameMode = gameMode;
    }




}
