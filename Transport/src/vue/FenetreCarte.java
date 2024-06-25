package vue;

import bouton.StyleBouton;
import static construction.AlgorithmColoration.largestFirstColoring;
import construction.AlgorithmIntersection;
import static construction.FiltreAeroportVol.loadAeroports;
import static construction.FiltreAeroportVol.loadVols;
import static construction.FiltreAeroportVol.selectFile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static modele.Aeroport.setAeroport;
import waypoint.MyWaypoint;
import waypoint.WaypointRender;
import modele.Aeroport;
import modele.Vol;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import static vue.Main.openSecondaryWindow;

/**
 * La classe {@code FenetreCarte} représente une fenêtre graphique pour visualiser des aéroports et des vols sur une carte.
 */
public class FenetreCarte extends JFrame {
    private JButton showFlightsButton;
    private JButton button;
    private JButton updateMargeButton;
    private JButton selectLevelButton;
    private static ArrayList<Aeroport> aeroports = new ArrayList();
    private static ArrayList<Vol> vols = new ArrayList();
    private JPanel mapPanel;
    private Graph graph = new SingleGraph("filtre");
    private JLabel carteLabel;
    private final JButton coloration;
    public static JButton chargeVol;
    private static JXMapViewer mapViewer;
    private static final Set<MyWaypoint> waypoints = new HashSet<>();
    private static final WaypointRender waypointRenderer = new WaypointRender();

    public static List<Vol> getVols() {
        return vols;
    }

    /**
     * Constructeur de la classe {@code FenetreCarte}.
     * Initialise une fenêtre graphique avec des composants pour visualiser une carte et interagir avec les données des aéroports et des vols.
     */
    public FenetreCarte() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        JPanel pan = new JPanel(new GridBagLayout());
        pan.setPreferredSize(new Dimension(300, getHeight()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        carteLabel = new JLabel("Graphique de l'aéroport");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        pan.add(carteLabel, gbc);
        gbc.gridwidth = 1;

        button = new StyleBouton("Charger Aeroport");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        pan.add(button, gbc);
        gbc.gridwidth = 1;

        button.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                loadAeroports(selectedFile, aeroports);
                if (aeroports == null || aeroports.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé ou vide.");
                    return;
                }
                mapPanel.setVisible(true);
                setAeroport(mapViewer, waypoints, waypointRenderer, aeroports);
                mapViewer.setOverlayPainter(waypointRenderer);
            }
        });

        chargeVol = new StyleBouton("Charger vols");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        pan.add(chargeVol, gbc);
        gbc.gridwidth = 1;

        chargeVol.addActionListener(e -> {
            if (aeroports == null || aeroports.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez d'abord charger les aéroports.");
                return;
            }
            File selectedFile = selectFile();
            if (selectedFile != null) {
                try {
                    loadVols(selectedFile, vols);
                    if (!vols.isEmpty()) {
                        chargeVol(vols, null, 15, 0);
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun vol trouvé dans le fichier.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FenetreCarte.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucun fichier sélectionné.");
            }
        });

        updateMargeButton = new StyleBouton("Modifier Marge");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        pan.add(updateMargeButton, gbc);
        gbc.gridwidth = 1;

        updateMargeButton.addActionListener((ActionEvent e) -> {
            if (vols == null || vols.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez d'abord charger les vols.");
                return;
            }
            String minStr = JOptionPane.showInputDialog("Entrez un nombre positif :");
            if (minStr != null) {
                try {
                    int min = Integer.parseInt(minStr);
                    if (min <= 0) {
                        throw new NumberFormatException();
                    }
                    chargeVol(null, null, min, 0);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrée invalide. Veuillez entrer un nombre positif.");
                }
            }
        });

        selectLevelButton = new StyleBouton("Sélectionner le niveau");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        pan.add(selectLevelButton, gbc);

        selectLevelButton.addActionListener((ActionEvent e) -> {
            if (vols == null || vols.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez d'abord charger les vols.");
                return;
            }
            String levelsStr = JOptionPane.showInputDialog("Entrez le nombre de niveaux :");
            if (levelsStr != null) {
                try {
                    int levels = Integer.parseInt(levelsStr);
                    if (levels <= 0) {
                        throw new NumberFormatException();
                    }
                    String selectedLevelStr = JOptionPane.showInputDialog("Entrez le niveau à sélectionner (0 pour tous les niveaux) :");
                    if (selectedLevelStr != null) {
                        try {
                            int selectedLevel = Integer.parseInt(selectedLevelStr);
                            if (selectedLevel < 0 || selectedLevel > levels) {
                                throw new NumberFormatException();
                            }
                            JOptionPane.showMessageDialog(null, "Niveaux sélectionnés : " + (selectedLevel == 0 ? "Tous les niveaux" : selectedLevel));
                            graph.setAttribute("kMax", levels);
                            AlgorithmIntersection.setVolsCollision(graph, vols, aeroports, 15);
                            largestFirstColoring(graph);
                            chargeVol(vols, graph, 15, selectedLevel);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Entrée invalide. Veuillez entrer un nombre valide.");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrée invalide. Veuillez entrer un nombre valide.");
                }
            }
        });

        showFlightsButton = new StyleBouton("Voir les vols");
        gbc.gridx = 0;
        gbc.gridy = 5;
        pan.add(showFlightsButton, gbc);
        gbc.gridwidth = 2;

        showFlightsButton.addActionListener(e -> {
            if (vols == null || vols.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez d'abord charger les vols.");
                return;
            }
            waypointRenderer.showFlightsPopup();
        });

        coloration = new StyleBouton("Fenetre coloration");
        gbc.gridx = 0;
        gbc.gridy = 6;
        pan.add(coloration, gbc);

        coloration.addActionListener(e -> {
            openSecondaryWindow(new FenetreColoration(), "Coloration");
            clearData();
            this.dispose();
        });

        mapPanel = new JPanel(new BorderLayout());
        initMapPanel();

        add(pan, BorderLayout.EAST);
        add(mapPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    
    /**
     * Initialise le panneau de la carte avec les composants nécessaires pour afficher et interagir avec la carte.
     */
    private void initMapPanel() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setDoubleBuffered(true);
        GeoPosition initialPosition = new GeoPosition(46.5768014, 2.6674444);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(13);

        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapPanel.add(mapViewer, BorderLayout.CENTER);
    }

    /**
     * Charge les vols et met à jour l'affichage de la carte.
     * 
     * @param vol la liste des vols à charger
     * @param g le graphe représentant les vols
     * @param marge la marge pour déterminer les collisions
     * @param level le niveau à afficher
     */
    public static void chargeVol(List<Vol> vol, Graph g, int marge, int level) {
        if (vol == null) {
            vol = AlgorithmIntersection.setVolsCollision(null, vols, aeroports, marge);
        }
        org.jxmapviewer.painter.Painter<JXMapViewer> overlay = waypointRenderer.paintVol(vol, mapViewer, aeroports, g, level);
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(overlay);
        painters.add(waypointRenderer);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    /**
     * Efface les données des aéroports et des vols.
     */
    private void clearData() {
        if (aeroports != null) {
            aeroports.clear();
        }
        if (vols != null) {
            vols.clear();
        }
    }
}
