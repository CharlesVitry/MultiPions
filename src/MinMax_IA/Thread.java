/*
  @author OkaTravaille

 */

package MinMax_IA;

//thread de L'IA
public class Thread implements Runnable {
  Jeu jeu;
  MinMax minmaxIA;

  public Thread(Jeu jeu1) {
      jeu = jeu1;
      minmaxIA = jeu.getMinmaxIA();
  }

  @Override
  public void run() {

      while (true) {


          minmaxIA.ScoreNodeParent(jeu.TourAuBlanc(), jeu.getnodes.racine);

      }
  }

}
