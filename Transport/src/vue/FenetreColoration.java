package vue;

import bouton.StyleBouton;
import modele.Aeroport;
import static construction.AlgorithmColoration.Gloutonne;
import static construction.AlgorithmColoration.dsatur;
import static construction.AlgorithmColoration.largestFirstColoring;
import construction.ChargerGraphe;
import construction.AlgorithmIntersection;
import static construction.FiltreAeroportVol.loadAeroports;
import static construction.FiltreAeroportVol.loadVols;
import static construction.FiltreAeroportVol.selectFile;
import modele.Vol;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static modele.Vol.exportTXT;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.layout.Layouts;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import static vue.Main.openSecondaryWindow;

/**
 * Fenêtre de coloration des graphes.
 * Cette fenêtre permet de charger un graphe à partir d'un fichier, d'appliquer des algorithmes
 * de coloration et d'afficher les résultats.
 */
public class FenetreColoration extends JFrame {
    private JPanel graphPanel;
    private JSlider zoomSlider;
    private JButton startAlgo;
    private JButton ButtonAirport;
    private JLabel LabelAirport;
    private JLabel kMax;
    private JLabel nbConflits;
    private JLabel nbAretes;
    private JLabel nbSommets;
    private JLabel CC;
    private JLabel DegMoy;
    private JLabel Diametre;
    private JButton updateKMaxButton;
    private JButton intersection;
    private JButton extraire;
    private JComboBox<String> comboBox;
    private ArrayList<Aeroport> aeroports = new ArrayList<>();
    private ArrayList<Vol> vols = new ArrayList<>();
    private Graph currentGraph;
    private static final int ZOOM_SLIDER_MIN = 10;
    private static final int ZOOM_SLIDER_MAX = 200;
    private static final int ZOOM_SLIDER_INIT = 100;

    /**
     * Constructeur de la fenêtre de coloration.
     * Initialise tous les composants de l'interface utilisateur.
     */
    public FenetreColoration() {
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setTitle("Coloration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        
        startAlgo = new StyleBouton("Lancer Algorithme");
        ButtonAirport = new StyleBouton("Charger un aéroport");
        LabelAirport = new JLabel("Aucun aéroport chargé");
        intersection = new StyleBouton("Carte de france");
        nbConflits = new JLabel("Conflit : ");
        nbAretes = new JLabel("Arêtes : ");
        nbSommets = new JLabel("Sommets : ");
        CC = new JLabel("Composantes connexes : ");
        DegMoy = new JLabel("Degré moyen : ");
        Diametre = new JLabel("Diamètre : ");
        extraire = new StyleBouton("Extraire");
        zoomSlider = new JSlider(JSlider.HORIZONTAL, ZOOM_SLIDER_MIN, ZOOM_SLIDER_MAX, ZOOM_SLIDER_INIT);
        comboBox = new JComboBox<>(new String[]{"Gloutonne", "welshPowell","Dsatur"});
        kMax=new JLabel("kMax :");
        updateKMaxButton = new StyleBouton("Modifier kMax");
        
        cont.insets = new Insets(30, 5, 30, 5);
        ButtonAirport.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                loadAeroports(selectedFile, aeroports);
                if (aeroports != null) {
                    LabelAirport.setText(selectedFile.getName());
                }
            }
        });
        startAlgo.addActionListener((ActionEvent e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor = new DefaultGraph("Vols");
            
            try {
                File selectedFile = aeroports != null ? selectFile() : null;
                if (selectedFile != null) {
                    if (selectedFile.getName().endsWith(".txt")) {
                        gcolor = ChargerGraphe.chargerGraphe(selectedFile.getAbsolutePath());
                    } else if (selectedFile.getName().endsWith(".csv")) {
                        loadVols(selectedFile, vols);
                        AlgorithmIntersection.setVolsCollision(gcolor,vols, aeroports,15);
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "Format de fichier non supporté.");
                        return;
                    }
                    int conflit = 0;
                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne" : 
                                conflit = Gloutonne(gcolor);
                                break;
                            case "welshPowell" : 
                                conflit = largestFirstColoring(gcolor);
                                break;
                            case "Dsatur" : 
                                conflit = dsatur(gcolor);
                                break;
                            default : 
                                JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                                break;
                        }
                        kMax.setText("kMax: "+String.valueOf((int)gcolor.getNumber("kMax")));
                    }

                    if (gcolor != null) {
                        currentGraph = gcolor;
                        displayGraph(currentGraph);
                        nbConflits.setText("Conflit : " + conflit);
                        nbAretes.setText("Aretes : " + currentGraph.getEdgeCount());
                        Diametre.setText("Diametre : " + (int)diameter(currentGraph));
                        ConnectedComponents cc = new ConnectedComponents();
                        cc.init(currentGraph);
                        CC.setText("Composant : " + cc.getConnectedComponentsCount());
                        DegMoy.setText("Degre Moyen : " + (double)(currentGraph.getEdgeCount()*2)/currentGraph.getNodeCount());
                        nbSommets.setText("Sommets : " + currentGraph.getNodeCount());
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
            String input = JOptionPane.showInputDialog("Entrez un nombre positif pour kMax :");
            if (input != null) {
                try {
                    int newKMax = Integer.parseInt(input);
                    if (newKMax <= 0) {
                        throw new NumberFormatException();
                    }
                    if (currentGraph != null) {
                        for (Edge edge : currentGraph.getEdgeSet()) {
                            edge.setAttribute("ui.style", "fill-color: black;");
                        }
                        currentGraph.setAttribute("kMax", newKMax);
                        kMax.setText("Kmax:"+String.valueOf(newKMax));

                        String selectedAlgorithm = (String) comboBox.getSelectedItem();
                        int conflit = 0;
                        if (selectedAlgorithm != null) {switch (selectedAlgorithm) {
                            case "Gloutonne" : 
                                conflit = Gloutonne(currentGraph);
                                break;
                            case "welshPowell" : 
                                conflit = largestFirstColoring(currentGraph);
                                break;
                            case "Dsatur" : 
                                conflit = dsatur(currentGraph);
                                break;
                            default : 
                                JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                                break;
                        }
                        }

                        nbConflits.setText("Conflit : " + conflit);
                        nbAretes.setText("Aretes : " + currentGraph.getEdgeCount());
                        Diametre.setText("Diametre : " + (int)diameter(currentGraph));
                        ConnectedComponents cc = new ConnectedComponents();
                        cc.init(currentGraph);
                        CC.setText("Composant : " + cc.getConnectedComponentsCount());
                        DegMoy.setText("Degre Moyen : " + (double)(currentGraph.getEdgeCount() * 2) / currentGraph.getNodeCount());
                        nbSommets.setText("Sommets : " + currentGraph.getNodeCount());
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun graphe chargé.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrée invalide. Veuillez entrer un nombre positif.");
                }
            }
        });
        cont.anchor = GridBagConstraints.CENTER;
        cont.insets = new Insets(10, 10, 10, 10);
        cont.gridx = 0;
        cont.gridy = 0;
        cont.gridwidth = 2;
        controlPanel.add(LabelAirport, cont);

        cont.gridy = 1;
        controlPanel.add(ButtonAirport, cont);
        
        cont.gridy = 2;
        controlPanel.add(comboBox, cont);

        cont.gridy = 3;
        controlPanel.add(startAlgo, cont);

        cont.gridy = 4;
        controlPanel.add(nbConflits, cont);

        cont.gridy = 5;
        controlPanel.add(nbSommets, cont);
        
        cont.gridy = 11;
        controlPanel.add(nbAretes, cont);

        cont.gridy = 12;
        controlPanel.add(Diametre, cont);

        cont.gridy = 13;
        controlPanel.add(CC, cont);

        cont.gridy = 14;
        controlPanel.add(DegMoy, cont);

        cont.gridy = 15;
        controlPanel.add(Box.createVerticalStrut(20), cont);

        add(controlPanel, BorderLayout.LINE_END);

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        add(controlPanel, BorderLayout.LINE_END);

        cont.gridy = 6;
        cont.insets = new Insets(10, 5, 10, 5);
        controlPanel.add(kMax, cont);

        cont.gridy = 7;
        controlPanel.add(updateKMaxButton, cont);

        cont.gridy = 16;
        controlPanel.add(zoomSlider, cont);

        cont.gridy = 17;
        controlPanel.add(intersection, cont);
        
        cont.gridy = 18;
        controlPanel.add(extraire, cont);
        extraire.addActionListener((ActionEvent e) ->{
            if(currentGraph!=null){
                exportTXT(currentGraph);
                JOptionPane.showMessageDialog(null, "le graphe a bien été extrait et s'appelle "+currentGraph.getId()+".txt" );
            }else{
                JOptionPane.showMessageDialog(null, "Aucun graphe charge");
            }
        });

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        JScrollPane jsp = new JScrollPane(graphPanel);
        add(jsp, BorderLayout.CENTER);
        
        intersection.addActionListener((ActionEvent e) -> {
            openSecondaryWindow(new FenetreCarte(), "Intersection");
            this.dispose();
        });
        zoomSlider.addChangeListener(e -> {
            double zoomValue = zoomSlider.getValue() / 100.0;
            setGraphZoom(zoomValue);
        });

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setGraphZoom(double zoomValue) {
        if (graphPanel.getComponentCount() > 0) {
            View view = (View) graphPanel.getComponent(0);
            view.getCamera().setViewPercent(zoomValue);
        }
    }

    private void displayGraph(Graph g){
        graphPanel.removeAll();

        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout(new SpringBox());
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        View view = viewer.addDefaultView(false);
        view.setPreferredSize(new Dimension(800, 600));
        viewer.enableAutoLayout(Layouts.newLayoutAlgorithm());

        for (org.graphstream.graph.Node node : g) {
            node.addAttribute("ui.style", "size: 10px; shape: circle; text-size: 15px; text-alignment: center; text-style: bold; text-background-mode: rounded-box;");
        }

        for (org.graphstream.graph.Edge edge : g.getEachEdge()) {
            edge.addAttribute("ui.style", "size: 2px;");
        }
        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }
    
}