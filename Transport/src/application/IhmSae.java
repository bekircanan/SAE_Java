package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class IhmSae extends JFrame {
    public IhmSae() {
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setTitle("Menu principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45)); // Couleur de fond gris foncé
        GridBagConstraints cont = new GridBagConstraints();

        JButton coloration = new JButton("Graphe coloration");
        JButton intersection = new JButton("Graphe Intersection/collision");
        JLabel titre = new JLabel("Choix de l'algorithme");

        // Définir les couleurs de fond des boutons
        coloration.setBackground(new Color(100, 181, 246)); // Bleu clair
        intersection.setBackground(new Color(236, 64, 122)); // Rose vif

        // Définir les couleurs du texte des boutons pour un meilleur contraste
        coloration.setForeground(Color.WHITE);
        intersection.setForeground(Color.WHITE);
        titre.setForeground(Color.WHITE);

        // Définir la taille préférée des boutons pour qu'ils aient les mêmes dimensions
        Dimension buttonSize = new Dimension(300, 50);
        coloration.setPreferredSize(buttonSize);
        intersection.setPreferredSize(buttonSize);

        // Définir la police des boutons et du titre
        Font buttonFont = new Font("Verdana", Font.BOLD, 16);
        Font titreFont = new Font("Arial", Font.BOLD, 24);
        coloration.setFont(buttonFont);
        intersection.setFont(buttonFont);
        titre.setFont(titreFont);

        // Définir la bordure des boutons
        coloration.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        intersection.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // Définir le texte de l'infobulle
        coloration.setToolTipText("Cliquez pour commencer la coloration de graphe");
        intersection.setToolTipText("Cliquez pour commencer l'intersection/collision de graphe");

        // Définir les marges autour des boutons et du titre
        cont.insets = new Insets(20, 20, 20, 20);

        // Positionner le titre en haut au centre
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 2;
        cont.anchor = GridBagConstraints.CENTER;
        panel.add(titre, cont);
        cont.gridwidth = 1; // Réinitialiser gridwidth

        // Positionner les boutons à l'aide de GridBagLayout
        cont.gridx = 0;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.CENTER;
        panel.add(coloration, cont);

        cont.gridx = 1;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.CENTER;
        panel.add(intersection, cont);

        coloration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {   
                openSecondaryWindow(new Coloration(), "Coloration");
            }
        });
        
        intersection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSecondaryWindow(new IntersectionIHM(), "Intersection");
            }
        });

        add(panel);
        setVisible(true);
    }

    private void openSecondaryWindow(JFrame secondaryFrame, String title) {
        secondaryFrame.setTitle(title);
        secondaryFrame.setLocationRelativeTo(null);
        secondaryFrame.setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        IhmSae.this.setVisible(false);
        
        secondaryFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                IhmSae.this.setVisible(true);
            }
        });
    }
    
    public static void main(String[] args) {
        new IhmSae();
    }
}
