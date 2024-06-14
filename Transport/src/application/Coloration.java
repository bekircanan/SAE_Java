package application;

import static construction.Algos.Gloutonne;
import static construction.Algos.dsatur;
import static construction.Algos.largestFirstColoring;
import static construction.Algos.welshPowell;
import construction.Graphe;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

public class Coloration extends JFrame {
    private JPanel graphPanel;
   
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JSlider zoomSlider;
    private JTextField kMaxField;
    private JLabel nbConflits;
    
    private JLabel nbNoeud;
    private JLabel nbAretes;
    private JLabel nbSommets;
    private JLabel CC;
    private JLabel DegMoy;
    private JLabel Diametre;
    private JLabel Degre;
    
    private Graph currentGraph;
    private static final double MIN_ZOOM = 0.1;
    private static final double MAX_ZOOM = 2.0;
    private static final int ZOOM_SLIDER_MIN = 10;
    private static final int ZOOM_SLIDER_MAX = 200;
    private static final int ZOOM_SLIDER_INIT = 100;

    public Coloration() {
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setTitle("Coloration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        
        JButton button = new JButton("Lancer Algorithme");
        JButton ButtonAirport = new JButton("Charger un aéroport");
        JLabel LabelAirport = new JLabel("Aucun aéroport chargé");
        zoomInButton = new JButton("+");
        zoomOutButton = new JButton("-");

        zoomInButton = new JButton("-");
        zoomOutButton = new JButton("+");
         nbConflits = new JLabel("Conflit : ");
        nbNoeud = new JLabel("Noeuds : ");
        nbAretes = new JLabel("Arêtes : ");
        nbSommets = new JLabel("Sommets : ");
        CC = new JLabel("Composantes connexes : ");
        DegMoy = new JLabel("Degré moyen : ");
        Diametre = new JLabel("Diamètre : ");
        Degre = new JLabel("Degré : ");
        zoomSlider = new JSlider(JSlider.HORIZONTAL, ZOOM_SLIDER_MIN, ZOOM_SLIDER_MAX, ZOOM_SLIDER_INIT);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Gloutonne", "welshPowell", "largestFirstColoring","Dsatur"});
        
        nbConflits = new JLabel("Conflit : ");
        kMaxField = new JTextField(10);
        
        JButton updateKMaxButton = new JButton("Modifier kMax");
        
        // Ajouter un espace entre les composants
        cont.insets = new Insets(30, 5, 30, 5);
        
        button.addActionListener((var e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor;
            
            try {
                
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File("."));
                fileChooser.setSelectedFile(new java.io.File("."));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    gcolor = Graphe.chargerGraphe(filePath);

                    if (selectedAlgorithm != null) {
                        int kMax = 0; // Initialisez kMax à une valeur par défaut
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> kMax = Gloutonne(gcolor);
                            case "welshPowell" -> kMax = welshPowell(gcolor);
                            case "largestFirstColoring" -> kMax = largestFirstColoring(gcolor);
                            case "Dsatur" -> kMax = dsatur(gcolor);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }

                        // Affichez la valeur de kMax dans le JTextField
                        kMaxField.setText(String.valueOf(kMax));
                    }

                    if (gcolor != null) {
                        currentGraph = gcolor;
                        displayGraph(gcolor);
                        
                        nbConflits.setText("Conflits : " + gcolor.getAttribute(""));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun fichier sélectionné.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Entrée invalide. Assurez-vous de saisir un nombre valide.");
            } catch (IOException ex) {
                Logger.getLogger(Coloration.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement du fichier du graphe.");
            }
        });

        updateKMaxButton.addActionListener((var e) -> {
            try {
                if (currentGraph != null) {
                    int newKMax = Integer.parseInt(kMaxField.getText());
                    currentGraph.setAttribute("kMax", newKMax);
                    
                    String selectedAlgorithm = (String) comboBox.getSelectedItem();
                    
                    if (selectedAlgorithm != null) {
                        int kMax = 0; // Initialisez kMax à une valeur par défaut
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> kMax = Gloutonne(currentGraph);
                            case "welshPowell" -> kMax = welshPowell(currentGraph);
                            case "largestFirstColoring" -> kMax = largestFirstColoring(currentGraph);
                            case "Dsatur" -> kMax = dsatur(currentGraph);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }

                        // Affichez la valeur de kMax dans le JTextField
                        kMaxField.setText(String.valueOf(kMax));
                    }
                    
                    
                    nbConflits.setText("Conflit : " + newKMax);
                                          nbConflits.setText("Conflits : " + currentGraph.getAttribute("conflit"));
        nbAretes.setText("Aretes : " + currentGraph.getEdgeCount());
        Diametre.setText("Diametre : " + diameter(currentGraph));
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(currentGraph);
        CC.setText("Composant : " + cc.getConnectedComponentsCount());
        DegMoy.setText("Degre Moyen : " + (double)(currentGraph.getEdgeCount()*2)/currentGraph.getNodeCount());
        nbNoeud.setText("Noeud : " + currentGraph.getNodeCount());
        nbSommets.setText("Sommets : " + currentGraph.getAttribute("kMax"));
                    
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun graphe chargé.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Entrée invalide. Assurez-vous de saisir un nombre valide.");
            }
            
        });
        
        // Initial configuration for GridBagConstraints
cont.anchor = GridBagConstraints.CENTER;

// Row 0: ComboBox, spans 2 columns
// Utilisation de GridBagConstraints pour mieux organiser la mise en page


// ComboBox en row 0, spans 2 colonnes
cont.gridx = 0;
cont.gridy = 0;
cont.gridwidth = 2;
cont.insets = new Insets(10, 10, 10, 10); // Espacement autour du ComboBox
controlPanel.add(comboBox, cont);

// Bouton en row 1, spans 2 colonnes
cont.gridy = 1;
cont.gridwidth = 2;
controlPanel.add(button, cont);

// Label nbConflits en row 2, spans 2 colonnes
cont.gridy = 2;
controlPanel.add(nbConflits, cont);

// JLabel "kMax :" en row 3, 1ère colonne
cont.gridy = 3;
cont.gridx = 0;
cont.gridwidth = 1;
controlPanel.add(new JLabel("kMax : "), cont);

// kMaxField en row 3, 2ème colonne
cont.gridx = 1;
controlPanel.add(kMaxField, cont);

// Bouton updateKMaxButton en row 4, spans 2 colonnes
cont.gridx = 0;
cont.gridy = 4;
cont.gridwidth = 2;
controlPanel.add(updateKMaxButton, cont);

// Label nbSommets en row 5, spans 2 colonnes
cont.gridy = 5;
cont.gridwidth = 2;
controlPanel.add(nbSommets, cont);

// Label nbAretes en row 6, spans 2 colonnes
cont.gridy = 6;
controlPanel.add(nbAretes, cont);

// Espacement en row 7
cont.gridy = 7;
controlPanel.add(Box.createVerticalStrut(20), cont);

// Diametre en row 8, 1ère colonne
cont.gridy = 8;
cont.gridx = 0;
cont.gridwidth = 1;
controlPanel.add(Diametre, cont);

// zoomInButton en row 8, 2ème colonne
cont.gridx = 1;
controlPanel.add(zoomInButton, cont);

// CC en row 9, 1ère colonne
cont.gridx = 0;
cont.gridy = 9;
controlPanel.add(CC, cont);

// zoomOutButton en row 9, 2ème colonne
cont.gridx = 1;
controlPanel.add(zoomOutButton, cont);

// DegMoy en row 10, spans 2 colonnes
cont.gridx = 0;
cont.gridy = 10;
cont.gridwidth = 2;
controlPanel.add(DegMoy, cont);

// Espacement en row 11
cont.gridy = 11;
controlPanel.add(Box.createVerticalStrut(20), cont);

// zoomSlider en row 12, spans 2 colonnes
cont.gridy = 12;
cont.gridwidth = 2;
controlPanel.add(zoomSlider, cont);

// Ajout du panel de contrôle à droite de la fenêtre principale
add(controlPanel, BorderLayout.LINE_END);

// Panel pour afficher le graphique au centre avec un JScrollPane
graphPanel = new JPanel(new BorderLayout());
graphPanel.setPreferredSize(new Dimension(600, 400));



        // Ajoute les boutons à droite
        add(controlPanel, BorderLayout.LINE_END);

        cont.gridx = 0;
        cont.gridy = 5;
        cont.insets = new Insets(10, 5, 10, 5); // Reset the space
        controlPanel.add(new JLabel("Nouveau kMax: "), cont);

        cont.gridx = 1;
        controlPanel.add(kMaxField, cont);

        cont.gridx = 0;
        cont.gridy = 6;
        cont.gridwidth = 2;
        controlPanel.add(updateKMaxButton, cont);

        add(controlPanel, BorderLayout.LINE_END);

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        JScrollPane jsp = new JScrollPane(graphPanel);
        // Ajoute le JScrollPane avec le panneau du graphique
        add(jsp, BorderLayout.CENTER);

        // Ajoute les actions des boutons de zoom avec des vérifications de limites
        zoomInButton.addActionListener(e -> zoomGraph(1.1));
        zoomOutButton.addActionListener(e -> zoomGraph(1 / 1.1));
        zoomSlider.addChangeListener(e -> {
            double zoomValue = zoomSlider.getValue() / 100.0;
            setGraphZoom(zoomValue);
        });

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void zoomGraph(double zoomFactor) {
        if (graphPanel.getComponentCount() > 0) {
            View view = (View) graphPanel.getComponent(0);
            double currentZoom = view.getCamera().getViewPercent();
            double newZoom = currentZoom * zoomFactor;

            if (newZoom < MIN_ZOOM) {
                newZoom = MIN_ZOOM;
                JOptionPane.showMessageDialog(null, "Zoom minimum atteint.");
            } else if (newZoom > MAX_ZOOM) {
                newZoom = MAX_ZOOM;
                JOptionPane.showMessageDialog(null, "Zoom maximum atteint.");
            }

            view.getCamera().setViewPercent(newZoom);
            zoomSlider.setValue((int) (newZoom * 100));
        }
    }

    private void setGraphZoom(double zoomValue) {
        if (graphPanel.getComponentCount() > 0) {
            View view = (View) graphPanel.getComponent(0);
            view.getCamera().setViewPercent(zoomValue);
        }
    }

    private void displayGraph(Graph g) {
        graphPanel        .removeAll();

        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();  // Optional: for automatic layout of the graph

        // Prevent the opening of an external viewer window
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        View view = viewer.addDefaultView(false);

        // Set a specific size for the graph view
        view.setPreferredSize(new Dimension(500, 500));

        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    
}