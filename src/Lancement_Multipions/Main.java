package Lancement_Multipions;

import MinMax_IA.Jeu;
import Pions.Pion;

public class Main {


    public static void main(String[] parametres) {
        System.out.println("Lancement Multipions");

        new Jeu();


    }


    private static int taille = 3;



    // Poids pour IA
    private final static int PoidPion = 3;
    private final static int VictoirePoid = 1000000;
    private final static int PoidVictoireDirect = 1000000;
    private final static int EgalitePoid = 0;
    private static double positionalPoid = .05;
    private static double TactiquePoid = .80;
    private final static double coupSmart = -0.1;






    // profondeur recherche , cad la difficulte de l'IA
    private static int profondeurmax = 9;
    private static int profondeurmin = 1;
    private static final int profondeurdefaut = 6;
    private static int profondeur = profondeurdefaut;



    // Mode constants
    private static final String gamemodeDefaut = "JvsIA";
    private static String gameMode = gamemodeDefaut;


    public static int getPoidPiece(Pion pion) {
//avant convertion en jeu d'echec , il n'y a que les pions
        return PoidPion;
    }

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
        return positionalPoid;
    }
    public static double getTactiquePoid() {
        return TactiquePoid;
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

    public static double getPoidVictoireDirect() {
        return PoidVictoireDirect;
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
