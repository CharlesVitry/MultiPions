package Interface_Multipions;


import Lancement_Multipions.Main;
import Plateau.Get_Set_Plateau;
import MinMax_IA.Jeu;
import Plateau.Listener_Interface;
import Coups.Coup;
import Pions.Pion;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;


public class GUI {
    //décle de la frame et des panels
    JLabel[][] PossiblesCoupsPanel = new JLabel[Main.getTaille()][Main.getTaille()];

    JFrame  frmMultipions = new JFrame("Multipion Projet M1");
    JPanel panel = new JPanel();
    JPanel panel_1 = new JPanel();
    JPanel panel_2 = new JPanel();
    JPanel PanelPion = new JPanel(new GridLayout(Main.getTaille(), Main.getTaille(), 0, 0));
    JMenuBar menuBar = new JMenuBar();
    JPanel PlateauPanel = new JPanel(new GridLayout(Main.getTaille(), Main.getTaille(), 0, 0));
    JPanel DeplacementPanel = new JPanel();
    JLayeredPane MultiplePanelPlateau = new JLayeredPane();
    JPanel PossiblePanel;
    JPanel CapturePanel;
    JPanel GauchePanel = new JPanel();

    Get_Set_Plateau Plateau;
    ArrayList<Pion> PionCapture;
    Listener_Interface ListenerGui;
    GUI_Pions[][] ListePions;

    JLabel PionDeplace;
    JSlider slider = new JSlider();
    JSlider slider_1 = new JSlider();
    JSlider slider_2 = new JSlider();



    boolean Vision;
    Jeu jeu;

    HashMap<String, ImageIcon> Images;

    //String [] columnNames = {"Tour","Blanc","Noir"};
    //Object [][] data = new String[100][3];

    DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane();
    
    //  DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();


    public GUI(Jeu jeu1, Get_Set_Plateau get_set_plateau1,
               Listener_Interface listener_interface1, ArrayList<Pion> pions1) {
    	
    	//comme le __init__ de python
    	//-----------------------------------------------------------------------
        this.Plateau = get_set_plateau1;
        this.ListenerGui = listener_interface1;
        this.PionCapture = pions1;
        this.jeu = jeu1;
        this.Vision = false;


       // System.out.print(Arrays.deepToString(this.highlightArray) + "       ");
        //on load les images en premier
        //----------------------------------------------------------------
        DeclaImages();
        
        // on set la frame
        frmMultipions.setSize(new Dimension(1000, 700));
        frmMultipions.setTitle("Multipions");
        frmMultipions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmMultipions.setVisible(true);
        frmMultipions.getContentPane().setLayout(new BorderLayout());

        //-------------------------
        PlateauPanel.setPreferredSize(new Dimension(64* Main.getTaille(), 64* Main.getTaille()));
        PlateauPanel.setBounds(0, 0, 64* Main.getTaille(), 64* Main.getTaille());
        for (int ligne = 0; ligne < Main.getTaille(); ligne++) {
            for (int colonne = 0; colonne < Main.getTaille(); colonne++) {
                JPanel CapturesP = new JPanel(new BorderLayout());

                JLabel Blanc = new JLabel(Images.get("CaseClair"));
                JLabel Noir = new JLabel(Images.get("CaseFonce"));

                if ((ligne % 2) == (colonne % 2))
                    CapturesP.add(Blanc, BorderLayout.CENTER);
                else
                    CapturesP.add(Noir, BorderLayout.CENTER);

                PlateauPanel.add(CapturesP);
            }
        }



        //----------------------------------------------------------------
        PossiblePanel = new JPanel(new GridLayout(Main.getTaille(), Main.getTaille(), 0, 0));
        PossiblePanel.setOpaque(false);
        PossiblePanel.setPreferredSize(new Dimension(Main.getTaille()*64, Main.getTaille()*64));
        PossiblePanel.setBounds(0, 0, Main.getTaille()*64, Main.getTaille()*64);

        //----------------------------------------------------------------

        for (int ligne = (Main.getTaille()-1); ligne >= 0; ligne--) {
            for (int colonne = 0; colonne < Main.getTaille(); colonne++) {
                JLabel label = new JLabel(Images.get("Rien"));
                PossiblesCoupsPanel[ligne][colonne] = label;
                PossiblePanel.add(label);
            }
        }

        CapturePanel = new JPanel();

        //https://stackoverflow.com/questions/22920046/how-to-set-fix-size-of-jlabel
        CapturePanel.setMinimumSize(new Dimension(40, 600));
        CapturePanel.setPreferredSize(new Dimension(40, 600));
        CapturePanel.setMaximumSize(new Dimension(40,600));
        
        panel.add(CapturePanel);
        CapturePanel.setLayout(new GridLayout(2, 1, 0, 0));
     
        
       // JLabel labelCap = new JLabel("Captures");
      //  capturedPiecePanel.add(labelCap);
      

        

        CapturePanel.add(panel_1);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
        

        CapturePanel.add(panel_2);
        panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));


//--------------------------------------------------
        PanelPion.setOpaque(false);
        PanelPion.setPreferredSize(new Dimension(Main.getTaille()*64, Main.getTaille()*64));
        PanelPion.setBounds(0, 0, Main.getTaille()*64, Main.getTaille()*64);
        ListePions = new GUI_Pions[Main.getTaille()][Main.getTaille()];

        //----------------------------------------

        refreshInterface();


//--------------------------------------------------------------------------------


      //  menuBar.setBackground(new Color(245, 222, 179));
        JMenu FichierMenu = new JMenu("Jeu");
        JMenu OptionsMenu = new JMenu("Options");

        JMenuItem NouvellePartie = new JMenuItem("Nouvelle partie");
        NouvellePartie.setActionCommand("Nouvelle partie");
        NouvellePartie.addActionListener(this.ListenerGui);

        JMenuItem changement_de_vue_item = new JMenuItem("Changement de vue");
        changement_de_vue_item.setActionCommand("Changement de vue");
        changement_de_vue_item.addActionListener(ListenerGui);

        FichierMenu.add(NouvellePartie);

        JSeparator separator = new JSeparator();
        FichierMenu.add(separator);
        if (Main.getGameMode() == "JvsJ")
            slider.setValue(11);
        else if (Main.getGameMode() == "JvsIA")
            slider.setValue(30);
        else if (Main.getGameMode() == "IAvsJ")
            slider.setValue(80);
        else
            slider.setValue(98);


        slider.setMinimum(1);
        FichierMenu.add(slider);

        JLabel lblJvjJviaIavj = new JLabel("Joueur vs Joueur || Joueur vs  Intelligence Artificiel || Intelligence Artificiel  vs Joueur");
        lblJvjJviaIavj.setHorizontalAlignment(SwingConstants.LEFT);
        FichierMenu.add(lblJvjJviaIavj);

        JSeparator separator_1 = new JSeparator();
        FichierMenu.add(separator_1);

        JMenuItem mntmSauvegardeDesCoups = new JMenuItem("Sauvegarde des coups joués");
        mntmSauvegardeDesCoups.setActionCommand("Sauvegarde des coups joués");
        mntmSauvegardeDesCoups.addActionListener(ListenerGui);
        OptionsMenu.add(mntmSauvegardeDesCoups);
        OptionsMenu.add(changement_de_vue_item);

        menuBar.add(FichierMenu);
        slider_2.setValue((int) Math.round((  Main.getTaille() -2 )*9.4) -4);
        FichierMenu.add(slider_2);

        JLabel lblxxx = new JLabel("3x3           4x4           5x5          6x6           7x7            8x8           9x9           10x10 ");
        FichierMenu.add(lblxxx);
        menuBar.add(OptionsMenu);

        JSeparator separator_2 = new JSeparator();
        OptionsMenu.add(separator_2);
        slider_1.setValue((int) Math.round((  Main.getProfondeur() +1  )*8.5) -9);
        OptionsMenu.add(slider_1);

        JLabel lblDifficult = new JLabel("1   2   3   4   5   6   7   8   9");
        OptionsMenu.add(lblDifficult);

        JSeparator separator_3 = new JSeparator();
        OptionsMenu.add(separator_3);

        JMenuItem mntmChangementDeDifficult = new JMenuItem("Changement de difficulte");
        mntmChangementDeDifficult.setActionCommand("Changement de difficulte");
        mntmChangementDeDifficult.addActionListener(ListenerGui);
        OptionsMenu.add(mntmChangementDeDifficult);


        frmMultipions.setJMenuBar(menuBar);
        GauchePanel.setLayout(new BorderLayout(0, 0));
//----------------------------------------------------------------



   //     sidePanel.setPreferredSize(new Dimension(250, 600));


    //    sidePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));


        GauchePanel.add(scrollPane);

        model.addColumn("Tour");
        model.addColumn("Blanc");
        model.addColumn("Noir");
        
        
        scrollPane.setViewportView(table);

        //    table.getColumnModel().getColumn(0).setPreferredWidth(10);
        //   table.getColumnModel().getColumn(1).setPreferredWidth(30);
        //    table.getColumnModel().getColumn(2).setPreferredWidth(30);
        //   table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);



        //https://stackoverflow.com/questions/953972/java-jtable-setting-column-width


        //centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);


     //   for (int i = 0; i < 3; i++)
    //        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);





//----------------------------------------------------------------

        DeplacementPanel.setOpaque(false);
        DeplacementPanel.setPreferredSize(new Dimension(Main.getTaille()*64, Main.getTaille()*64));
        DeplacementPanel.setBounds(0, 0, Main.getTaille()*64, Main.getTaille()*64);
        DeplacementPanel.addMouseListener(ListenerGui);
        DeplacementPanel.addMouseMotionListener(ListenerGui);

        PionDeplace = new JLabel();
        DeplacementPanel.add(PionDeplace);





        MultiplePanelPlateau.setPreferredSize(new Dimension(Main.getTaille()*64, Main.getTaille()*64));

        MultiplePanelPlateau.setVisible(true);

       MultiplePanelPlateau.add(PlateauPanel, Integer.valueOf(0));
        MultiplePanelPlateau.add(PanelPion, Integer.valueOf(1));
        MultiplePanelPlateau.add(PossiblePanel, Integer.valueOf(2));
        MultiplePanelPlateau.add(DeplacementPanel, Integer.valueOf(3));

        //ajout de tout les panels créé à la frame
        frmMultipions.getContentPane().add(panel, BorderLayout.EAST);
        frmMultipions.getContentPane().add(MultiplePanelPlateau, BorderLayout.CENTER);
        frmMultipions.getContentPane().add(GauchePanel, BorderLayout.WEST);
        frmMultipions.pack();

      
    }

    public void CacheFenetre(){
        frmMultipions.setVisible(false);
        frmMultipions.dispose();
    }
    public int GetDifficulte() {

    	 int difficulte = (int) Math.round((  slider_1.getValue() + 9 )/ 8.5) -1;

        if (0 == difficulte || difficulte > 9)

             difficulte = 9;
    	 System.out.println("difficulté : "+difficulte);
        return difficulte;
    }

    public boolean finJeu(){
        int Fin = JOptionPane.showConfirmDialog(frmMultipions,"Fin du jeu \n Nouvelle partie ?");

        if(Fin == JOptionPane.YES_OPTION)
        {
            return true;
        }
        else
            return false;
    }




    public String GetMode() {
        String mode = null;
        int valeur =slider.getValue();
        if ( 0 < valeur && valeur < 21 )
            mode = "JvsJ";
        else if ( 21 < valeur && valeur < 56 )
            mode = "JvsIA";
        else if ( 56 < valeur && valeur < 95 )
            mode = "IAvsJ";
        else if ( 95 < valeur )
            mode = "IAvsIA";
        System.out.println("mode : "+mode);
        return mode;
    }
    public int GetTaillePlateau() {
        int taille = (int) Math.round((  slider_2.getValue()+4  )/ 9.5) +2;

        if (0 == taille || taille > 10)

            taille = 3;
        System.out.println("taille : "+taille);


        return taille;
    }




  //on suit le curseur de la souris
    public void deplacementPion(int x, int y) {
        PionDeplace.setLocation(x, y);
    }

   //on cree l'image à décplace au point de départ
    public void changemementImageDeplace(int row, int col) {
        PionDeplace.setIcon(GenereImagesPieces(row, col));
    }

//application des changements fait dans le jeu à l'interface graphique
    public void refreshInterface() {

        PanelPion.removeAll();
        this.ListePions = new GUI_Pions[Main.getTaille()][Main.getTaille()];




        if (!Vision) {
            for (int row = (Main.getTaille() -1 ); row >= 0; row--) {
                for (int col = 0; col < Main.getTaille(); col++) {

               //     try {
                        ListePions[row][col] = new GUI_Pions(row, col,
                                genereJlabel(row, col),
                                Plateau.getPionParCoords(row, col));
                        PanelPion.add(ListePions[row][col]);
                   // }
                  //  catch (Exception e) {
                  //      System.out.println("Index dépassé");
                 //   }
                }
            }
        } else {

            for (int row = 0; row < Main.getTaille(); row++) {
                for (int col = (Main.getTaille()-1); col >= 0; col--) {
                    ListePions[row][col] = new GUI_Pions(row, col,
                            genereJlabel(row, col),
                            Plateau.getPionParCoords(row, col));
                    PanelPion.add(ListePions[row][col]);

                }
            }


        }
        refreshPionCapture();
        refreshListsCoups(jeu.getListes().getListeCoups());
        suppressionAideCoupsPossible();
        IndicationDernierCoup(jeu.getListes().getListeCoups());
        PanelPion.repaint();
        PanelPion.revalidate();
        frmMultipions.pack();

    }

    @SuppressWarnings("unchecked")
    private void refreshPionCapture() {
        //on retire les anciennes images pour ne pas les avoir en double
        panel_1.removeAll();
        panel_2.removeAll();
        //System.out.println("update capture");
        ArrayList<Pion> BlancsListe = new ArrayList<>();
        ArrayList<Pion> NoirsListe = new ArrayList<>();


        for (Pion piece : PionCapture) {
            if (piece.Equipe())
                BlancsListe.add(piece);

            else
                NoirsListe.add(piece);
        }



        for (Pion piece : BlancsListe) {

                //https://stackoverflow.com/questions/3775373/java-how-to-add-image-to-jlabel
                ImageIcon image = Images.get("PionBlancPetit");
                panel_1.add(new JLabel(image));

                System.out.println("affichage capture");


        }

        for (Pion piece : NoirsListe) {

               ImageIcon image = Images.get("PionNoirPetit");
              panel_2.add(new JLabel(image));



        }


    }





    public void refreshListsCoups(ArrayList<Coup> ListeCoups) {
        // https://stackoverflow.com/questions/3179136/jtable-how-to-refresh-table-model-after-insert-delete-or-update-the-data
        if (ListeCoups.isEmpty()) {
            System.out.println("liste vide");
        } else {
            String dernier = ListeCoups.get(ListeCoups.size() - 1).CoordsEnString();
            System.out.println(dernier);
            //System.out.println("liste : "	+ListeCoups.size());

            if (ListeCoups.size() % 2 == 0) {
                model.addRow(new Object[]{ListeCoups.size() / 2,
                        ListeCoups.get(ListeCoups.size() - 2).CoordsEnString(),
                        ListeCoups.get(ListeCoups.size() - 1).CoordsEnString()});


            }


        }
    }

//retourne la bonne image selon les coords
    public ImageIcon GenereImagesPieces(int ligne, int colonne) {

        Pion piece = Plateau.getPionParCoords(ligne, colonne);
        ImageIcon image = null;

        if (piece == null)
            image = Images.get("Rien");

        else  if (piece.Equipe())
            image = Images.get("PionBlanc");
        else
            image = Images.get("PionNoir");



        return image;
    }

  //genere label à partir de l'image
    public JLabel genereJlabel(int row, int col) {
        return new JLabel(GenereImagesPieces(row, col));
    }




   //toutes les images dans une HashMap
    public void DeclaImages() {
        Images = new HashMap<>();

        Images.put("Rien", RechercheImageDansSourcePackage("Rien"));
        Images.put("Coup", RechercheImageDansSourcePackage("Coup"));
        Images.put("Ancien", RechercheImageDansSourcePackage("Ancien"));

        Images.put("CaseClair", RechercheImageDansSourcePackage("CaseClair"));
        Images.put("CaseFonce", RechercheImageDansSourcePackage("CaseFonce"));

        Images.put("PionBlanc", RechercheImageDansSourcePackage("PionBlanc"));
        Images.put("PionBlancPetit", RechercheImageDansSourcePackage("PionBlancPetit"));

        Images.put("PionNoir", RechercheImageDansSourcePackage("PionNoir"));
        Images.put("PionNoirPetit", RechercheImageDansSourcePackage("PionNoirPetit"));


    }

   //on va chercher l'image dans le package source qui contient les images
    public ImageIcon RechercheImageDansSourcePackage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass()
                    .getResource("/" + fileName + ".png")));

        } catch (IOException e) {

            e.printStackTrace();
        }

        assert image != null;
        return new ImageIcon(image);
    }



  //on delete l'image du pion deplace pour un meilleur rendu visuel
    public void DeletePionDeplace() {
        PionDeplace.setIcon(Images.get("Rien"));
    }

   //on delete le pion déplace dans le label
    public void DeletePionDeplaceLabel(int row, int col) {
        System.out.println("piece de coords retirer pour beau visuel : "+row+" , "+col);
        ListePions[row][col].getLabel().setIcon(Images.get("Rien"));

    }

    public JFrame getFrame() {
        return frmMultipions;
    }

    public void suppressionAideCoupsPossible() {

        for (int ligne = 0; ligne < Main.getTaille(); ligne++)
            for (int colonne = 0; colonne < Main.getTaille(); colonne++)
                PossiblesCoupsPanel[ligne][colonne].setIcon(Images.get("Rien"));
    }

    public boolean getVision() {
        return Vision;
    }

 //pour changer de cote la vue du plateau
    public void refreshdeLorientation() {
        if (Vision) {
            Vision = false;
            refreshInterface();
        } else  {
            Vision = true;
            refreshInterface();
        } 
    }
//affichage de l'image du coup possible
    public void IndicationCoupPossible(int ligne, int colonne) {
        if (Vision) {
            ligne = (Main.getTaille()-1) - ligne;
            colonne = (Main.getTaille()-1) - colonne;
        }


        PossiblesCoupsPanel[ligne][colonne].setIcon(Images.get("Coup"));

    }


    public void IndicationDernierCoup(ArrayList<Coup> ListeCoups) {
        if (ListeCoups.size() != 0) {
            int taille = ListeCoups.size();
            Coup coup = ListeCoups.get(taille - 1);

            IndicationAffichageDernierCoup(coup.getLigne_depart(), coup.getColonne_depart());
            IndicationAffichageDernierCoup(coup.getLigne_arrive(), coup.getColone_arrive());

        }

    }
//affichage de l'image "ancien" à l'emplacement du déplacement (départ et arrivée)
    public void IndicationAffichageDernierCoup(int ligne, int colonne) {
        int nouvelle_ligne = ligne;
        int nouvelle_colonne = colonne;

        if (Vision) {
            nouvelle_ligne = (Main.getTaille()-1) - ligne;
            nouvelle_colonne = (Main.getTaille()-1) - colonne;
        }

        PossiblesCoupsPanel[nouvelle_ligne][nouvelle_colonne].setIcon(Images.get("Ancien"));
    }
}
