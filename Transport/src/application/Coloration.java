package application;

import static construction.Algos.Gloutonne;
import static construction.Algos.colorierGraphe;
import static construction.Algos.largestFirstColoring;
import static construction.Algos.welshPowell;
import construction.Graphe;
import org.graphstream.graph.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Coloration extends JFrame {

    public Coloration() {
        setTitle("Coloration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400); 
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        JButton button = new JButton("Lancer Algorithme");

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Gloutonne", "welshPowell", "largestFirstColoring"});

        JLabel kMaxLabel = new JLabel("kMax: ");
        
        button.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedAlgorithm = (String) comboBox.getSelectedItem();
        Graph gcolor = null;

        try {
            String input = JOptionPane.showInputDialog("Entrez un nombre (entre 0 et 19):");
            int choix2 = Integer.parseInt(input);
            gcolor = Graphe.chargerGraphe("DataTest\\graph-test" + choix2 + ".txt");

            if (selectedAlgorithm != null) {
                switch (selectedAlgorithm) {
                    case "Gloutonne":
                        Gloutonne(gcolor);
                        colorierGraphe(gcolor, "couleur");
                        break;

                    case "welshPowell":
                        welshPowell(gcolor);
                        break;

                    case "largestFirstColoring":
                        largestFirstColoring(gcolor);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        break;
                }
            }

            if (gcolor != null) {
                org.graphstream.ui.swingViewer.Viewer viewer = gcolor.display();
                        gcolor.setAttribute("ui.quality");
                        gcolor.setAttribute("ui.antialias");
                        viewer.enableAutoLayout();
                        kMaxLabel.setText("kMax: " + gcolor.getAttribute("kMax"));
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Entrée invalide. Assurez-vous de saisir un nombre valide.");
        } catch (IOException ex) {
            Logger.getLogger(Coloration.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erreur lors du chargement du fichier du graphe.");
        }
    }
});


        cont.gridx = 3;
        cont.gridy = 2;
        cont.anchor = GridBagConstraints.LINE_END; // Align right
        cont.insets = new Insets(0, 0, 10, 10); // Add some padding
        panel.add(comboBox, cont);
        
        cont.gridx = 3;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.LINE_END; // Align right
        cont.insets = new Insets(0, 0, 10, 10); // Add some padding
        panel.add(button, cont);
        
        cont.gridx = 3;
        cont.gridy = 3;
        cont.anchor = GridBagConstraints.LINE_END; // Align right
        cont.insets = new Insets(0, 0, 10, 10); // Add some padding
        panel.add(kMaxLabel , cont);
        
        

        add(panel, BorderLayout.NORTH);
        setVisible(true);
    }
}