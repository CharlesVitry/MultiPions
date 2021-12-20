

package Plateau;

import MinMax_IA.Jeu;

import java.awt.event.*;


public class Listener_Interface implements MouseListener, MouseMotionListener, ActionListener, KeyListener {

    Jeu jeu;
    MouseEvent clicSouris;
    MouseEvent DeplacementSouris;

    public MouseEvent getClicSouris() {
        return clicSouris;
    }

    public Listener_Interface(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        jeu.ListenerDeplacementSouris(e);
        DeplacementSouris = e;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

        clicSouris = e;
        jeu.ListenerMouse(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        jeu.ListenerMouseRelease(e);
        clicSouris = null;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        jeu.ActionsBarreMenu(e);
    }

    // des méthodes que je pensais utiliser pour des raccourcis clavier
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
