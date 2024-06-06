package application;

import construction.Aeroport;
import construction.Vols;
import static construction.Aeroport.setAeroport;
import static construction.Intersection.setVolsAeroport;
import static construction.Intersection.setVolsCollision;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class IntersectionIHM extends JFrame {
    private String chiffre;

    public IntersectionIHM() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        String[] options = {"Graphique de l'aéroport", "Vol"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox);
        
        JLabel nbNoeuds = new JLabel("nbNoeuds : ");
        JLabel nbAretes = new JLabel("nbAretes : ");
        JLabel degMoy = new JLabel("degMoy : ");
        JLabel nbComposants = new JLabel("nbComposants : ");
        JLabel diametre = new JLabel("diametre : ");

        JButton button = new JButton("Lancer les algos");
        button.addActionListener((ActionEvent e) -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption != null) {
                List<Aeroport> port = new ArrayList<>();
                try (Scanner scanAero = new Scanner(new File("DataTest/aeroports.txt"))) {
                    while (scanAero.hasNextLine()) {
                        port.add(new Aeroport(scanAero));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                String input = JOptionPane.showInputDialog("Entrez un nombre (entre 0 et 19):");
                if (input != null) {
                    chiffre = input; // Store input for file name
                    
                    List<Vols> vol = new ArrayList<>();
                    try (Scanner scanVol = new Scanner(new File("DataTest/vol-test" + chiffre + ".csv"))) {
                        while (scanVol.hasNextLine()) {
                            vol.add(new Vols(scanVol));
                        }
                    } catch (FileNotFoundException ex) {
                        Logger. getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, "Fichier de vol non trouvé.");
                        return; // Exit the action listener
                    }
                    
                    switch (selectedOption) {
                        case "Graphique de l'aéroport" -> {
                            setVolsAeroport(vol, port, setAeroport(port));
                            int[] intersectionInfo = setVolsAeroport(vol, port, setAeroport(port));
                            nbNoeuds.setText("nbNoeuds : " + intersectionInfo[0]);
                            nbAretes.setText("nbAretes : " + intersectionInfo[1]);
                            
                        }
                        
                        case "Vol" -> {
    setVolsCollision(vol, port);
    double[] intersection = setVolsCollision(vol, port); // Modification du type de retour à double[]
    System.out.println("Contenu du tableau intersection : ");
    for (double value : intersection) {
        System.out.print(value + " ");
    }
    System.out.println(); // Pour passer à la ligne
    nbNoeuds.setText("nbNoeuds : " + intersection[0]);
    nbAretes.setText("nbAretes : " + intersection[1]);
    degMoy.setText("degMoy : " + intersection[2]);
    nbComposants.setText("nbComposants : " + intersection[3]);
    diametre.setText("diametre : " + intersection[4]);
}




                        
                        default -> JOptionPane.showMessageDialog(null, "Sélection non valide.");
                    }
                } else {                    
                    JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.");
                }
                
            }
        });
        
        panel.add(button);
        cont.gridx = 1;
                                cont.gridy = 1;
                                //cont.anchor = GridBagConstraints.LINE_END; // Align right
                                panel.add(nbNoeuds, cont);

                                cont.gridx = 1;
                                cont.gridy = 2;
                                //cont.anchor = GridBagConstraints.LINE_END; // Align right
                                panel.add(nbAretes, cont);

                                cont.gridx = 1;
                                cont.gridy = 3;
                                //cont.anchor = GridBagConstraints.LINE_END; // Align right
                                panel.add(degMoy , cont);
                                
                                cont.gridx = 1;
                                cont.gridy = 4;
                                //cont.anchor = GridBagConstraints.LINE_END; // Align right
                                panel.add(nbComposants , cont);
                                
                                
                                cont.gridx = 1;
                                cont.gridy = 5;
                                //cont.anchor = GridBagConstraints.LINE_END; // Align right
                                panel.add(diametre , cont);
                                
                                
                                

        add(panel);
        pack();
        setVisible(true);
    }
    
    

}

