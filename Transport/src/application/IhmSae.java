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
import javax.swing.JLabel;

public class IhmSae extends JFrame {
    public IhmSae() {
        setTitle("Menu principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.BLACK); // Définir la couleur de fond du panneau en noir
        GridBagConstraints cont = new GridBagConstraints();

        JButton coloration = new JButton("Graphe coloration");
        JButton intersection = new JButton("Graphe Intersection/collision");
        JLabel titre = new JLabel("Choix de l'algorithme");

        // Définir les couleurs de fond des boutons
        coloration.setBackground(Color.CYAN);
        intersection.setBackground(Color.MAGENTA);

        // Définir les couleurs du texte des boutons pour un meilleur contraste
        coloration.setForeground(Color.BLACK);
        intersection.setForeground(Color.WHITE);
        titre.setForeground(Color.WHITE);

        // Définir la taille préférée des boutons pour qu'ils aient les mêmes dimensions
        Dimension buttonSize = new Dimension(300, 40);
        coloration.setPreferredSize(buttonSize);
        intersection.setPreferredSize(buttonSize);

        // Définir la police des boutons et du titre
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Font titreFont = new Font("Arial", Font.BOLD, 18);
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
        cont.insets = new Insets(10, 10, 10, 10);

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
                new Coloration();
            }
        });

        add(panel);
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new IhmSae();
    }
}
