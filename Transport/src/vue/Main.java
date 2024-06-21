package vue;

import bouton.StyleBouton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principale représentant le menu principal de l'application.
 */
public class Main extends JFrame {
    /**
     * Constructeur de la classe Main.
     * Initialise et configure l'interface utilisateur principale.
     */
    public Main() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Menu principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45)); // Couleur de fond gris foncé
        GridBagConstraints cont = new GridBagConstraints();

        JButton coloration = new StyleBouton("Graphe coloration", new Color(100, 181, 246));
        JButton intersection = new StyleBouton("Carte de France", new Color(236, 64, 122));
        JLabel titre = new JLabel("Choix de la fonctionnalité");

        // Définir les couleurs du texte du titre pour un meilleur contraste
        titre.setForeground(Color.WHITE);

        // Définir la police du titre
        Font titreFont = new Font("Arial", Font.BOLD, 36);
        titre.setFont(titreFont);

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
        panel.add(coloration, cont);

        cont.gridx = 1;
        cont.gridy = 1;
        panel.add(intersection, cont);

        coloration.addActionListener((ActionEvent e) -> {
            openSecondaryWindow(new FenetreCarte(), "Intersection");
            this.dispose();
        });

        intersection.addActionListener((ActionEvent e) -> {
            openSecondaryWindow(new FenetreColoration(), "Coloration");
            this.dispose();
        });
        add(panel);
        setVisible(true);
    }

    

    /**
     * Ouvre une fenêtre secondaire avec le contenu spécifié.
     * @param secondaryFrame La fenêtre secondaire à ouvrir.
     * @param text
     */
    public static void openSecondaryWindow(JFrame secondaryFrame,String text) {
        secondaryFrame.setTitle(text);
        secondaryFrame.setLocationRelativeTo(null);
        secondaryFrame.setVisible(true);
        secondaryFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Hide the main window when secondary window opens
        secondaryFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                secondaryFrame.setVisible(true);
            }
        });
    }

    /**
     * Point d'entrée principal de l'application.
     * Instancie et lance l'interface utilisateur principale.
     * @param args Les arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
