package vue;

import modele.Aeroport;
import static construction.AlgorithmColoration.Gloutonne;
import static construction.AlgorithmColoration.dsatur;
import static construction.AlgorithmColoration.largestFirstColoring;
import static construction.AlgorithmColoration.welshPowell;
import construction.ChargerGraphe;
import construction.AlgorithmIntersection;
import modele.Vol;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

public class FenetreColoration extends JFrame {
    private JPanel graphPanel;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JSlider zoomSlider;
    private JTextField kMaxField;
    private JLabel nbConflits;
    private JLabel LabelAirport;
    private JLabel nbNoeud;
    private JLabel nbAretes;
    private JLabel nbSommets;
    private JLabel CC;
    private JLabel DegMoy;
    private JLabel Diametre;
    private JLabel Degre;
    private ArrayList<Aeroport> airports;
    private Graph currentGraph;
    private static final double MIN_ZOOM = 0.1;
    private static final double MAX_ZOOM = 2.0;
    private static final int ZOOM_SLIDER_MIN = 10;
    private static final int ZOOM_SLIDER_MAX = 200;
    private static final int ZOOM_SLIDER_INIT = 100;

    public FenetreColoration() {
        
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
        
        kMaxField = new JTextField(10);
        
        JButton updateKMaxButton = new JButton("Modifier kMax");
        
        // Ajouter un espace entre les composants
        cont.insets = new Insets(30, 5, 30, 5);
        ButtonAirport.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                airports = loadAeroports(selectedFile);
                if (airports != null) {
                    LabelAirport.setText(selectedFile.getName());
                }
            }
        });
        button.addActionListener((ActionEvent e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor;
            
            try {
                File selectedFile = airports != null ? selectFile() : null;
                if (selectedFile != null) {
                    if (selectedFile.getName().endsWith(".txt")) {
                        gcolor = ChargerGraphe.chargerGraphe(selectedFile.getAbsolutePath());
                    } else if (selectedFile.getName().endsWith(".csv")) {
                        ArrayList<Vol> vols = loadVols(selectedFile);
                        // Utilisez les aéroports déjà chargés pour l'intersection
                        gcolor = AlgorithmIntersection.setVolsCollision(vols, airports);
                        gcolor.addAttribute("kMax", 1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Format de fichier non supporté.");
                        return;
                    }
                    int conflit = 0;
                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> conflit = Gloutonne(gcolor);
                            case "welshPowell" -> conflit = welshPowell(gcolor);
                            case "largestFirstColoring" -> conflit = largestFirstColoring(gcolor);
                            case "Dsatur" -> conflit = dsatur(gcolor);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }

                        // Affichez la valeur de kMax dans le JTextField
                        kMaxField.setText(String.valueOf((int)gcolor.getNumber("kMax")));
                    }

                    if (gcolor != null) {
                        currentGraph = gcolor;
                        displayGraph(gcolor);
                        
                        nbConflits.setText("Conflits : " + conflit);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun fichier sélectionné.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Entrée invalide. Assurez-vous de saisir un nombre valide.");
            } catch (IOException ex) {
                Logger.getLogger(FenetreColoration.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur lors du chargement du fichier du graphe.");
            }
        });

        updateKMaxButton.addActionListener((var e) -> {
            try {
                if (currentGraph != null) {
                    for (Edge edge : currentGraph.getEdgeSet()) {
                        edge.setAttribute("ui.style", "fill-color: black;");
                    }
                    int newKMax = Integer.parseInt(kMaxField.getText());
                    currentGraph.setAttribute("kMax", newKMax);
                    
                    String selectedAlgorithm = (String) comboBox.getSelectedItem();
                    int conflit = 0;
                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> conflit = Gloutonne(currentGraph);
                            case "welshPowell" -> conflit = welshPowell(currentGraph);
                            case "largestFirstColoring" -> conflit = largestFirstColoring(currentGraph);
                            case "Dsatur" -> conflit = dsatur(currentGraph);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }

                        // Affichez la valeur de kMax dans le JTextField
                        kMaxField.setText(String.valueOf((int)currentGraph.getNumber("kMax")));
                    }
                    
                    
                    nbConflits.setText("Conflit : " + conflit);
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
        cont.insets = new Insets(10, 10, 10, 10); // Espacement autour du ComboBox
        // Row 0: ComboBox, spans 2 columns
        // Utilisation de GridBagConstraints pour mieux organiser la mise en page

         cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 2;
        controlPanel.add(LabelAirport, cont);

        cont.gridy = 1;
        controlPanel.add(ButtonAirport, cont);
        // ComboBox en row 0, spans 2 colonnes
        cont.gridx = 0;
        cont.gridy = 2;
        cont.gridwidth = 2;
        controlPanel.add(comboBox, cont);

        // Bouton en row 1, spans 2 colonnes
        cont.gridy = 3;
        cont.gridwidth = 2;
        controlPanel.add(button, cont);

        // Label nbConflits en row 2, spans 2 colonnes
        cont.gridy = 4;
        controlPanel.add(nbConflits, cont);

        cont.gridx = 0;
        cont.gridy = 6;
        cont.gridwidth = 1;
        controlPanel.add(new JLabel("kMax :"), cont);

        // kMaxField en row 3, 2ème colonne
        cont.gridx = 1;
        controlPanel.add(kMaxField, cont);

        // Bouton updateKMaxButton en row 4, spans 2 colonnes
        cont.gridx = 0;
        cont.gridy = 8;
        cont.gridwidth = 2;
        controlPanel.add(updateKMaxButton, cont);

        // Label nbSommets en row 5, spans 2 colonnes
        cont.gridy = 5;
        cont.gridx = 0;
        cont.gridwidth = 2;
        controlPanel.add(nbSommets, cont);

        // Label nbAretes en row 6, spans 2 colonnes

        cont.gridy = 11;
        controlPanel.add(nbAretes, cont);


        controlPanel.add(Box.createVerticalStrut(20), cont);

        // Diametre en row 8, 1ère colonne
        cont.gridy = 12;
        cont.gridx = 0;
        controlPanel.add(Diametre, cont);

        // CC en row 9, 1ère colonne
        cont.gridx = 0;
        cont.gridy = 13;
        controlPanel.add(CC, cont);


        // DegMoy en row 10, spans 2 colonnes
        cont.gridx = 0;
        cont.gridy = 14;
        cont.gridwidth = 2;
        controlPanel.add(DegMoy, cont);

        // Espacement en row 11
        cont.gridy = 15;
        controlPanel.add(Box.createVerticalStrut(20), cont);

        // zoomSlider en row 12, spans 2 colonnes
        cont.gridy = 16;
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
        cont.gridy = 6;
        cont.insets = new Insets(10, 5, 10, 5); // Reset the space
        controlPanel.add(new JLabel("kMax: "), cont);

        cont.gridx = 1;
        controlPanel.add(kMaxField, cont);

        cont.gridx = 0;
        cont.gridy = 7;
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
    private ArrayList<Aeroport> loadAeroports(File txtFile) {
        if (!txtFile.exists()) {
            JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
            return null;
        }
        ArrayList<Aeroport> ports = new ArrayList<>();
        try (Scanner scanAero = new Scanner(txtFile)) {
            while (scanAero.hasNextLine()) {
                ports.add(new Aeroport(scanAero));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenetreColoration.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ports;
    }
    private ArrayList<Vol> loadVols(File csvFile) throws IOException {
        ArrayList<Vol> vols = new ArrayList<>();
        try (Scanner scanVol = new Scanner(csvFile)) {
            while (scanVol.hasNextLine()) {
                vols.add(new Vol(scanVol));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenetreColoration.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("File not found: " + csvFile.getAbsolutePath(), ex);
        }

        return vols;
    }
    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
}