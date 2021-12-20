package MinMax_IA;

import Interface_Multipions.GUI;

//un thread à part permet que le GUI n'attendent pas que l'IA est finit ses recherches
public class Thread_Lancement implements Runnable {

    boolean Blanc;
    Jeu Jeu;
    MinMax minMax;

    //constructeur
    public Thread_Lancement(Jeu jeu1, MinMax minMax1, boolean blanc1) {
        Blanc = blanc1;
        Jeu = jeu1;
        minMax = minMax1;
    }

    @Override
    public void run() {


        synchronized (Jeu.getListes()) {
            Jeu.AjoutCoupNode(minMax.coup(Blanc));

            GUI view = Jeu.getGui();
            view.refreshInterface();
        }
    }
}
