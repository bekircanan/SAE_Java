package vue;

import static construction.AlgorithmColoration.Gloutonne;
import static construction.AlgorithmColoration.dsatur;
import static construction.AlgorithmColoration.largestFirstColoring;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import static construction.AlgorithmIntersection.volParHeure;
import static modele.Aeroport.setAeroport;

import construction.MyWaypoint;
import construction.WaypointRender;
import construction.jXMapviewerCustom;
import modele.Aeroport;
import modele.Vol;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * FenetreCarte représente une fenêtre graphique pour visualiser des aéroports et des vols sur une carte.
 */
public class FenetreCarte extends JFrame {
    private static List<Aeroport> ports;
    public static List<Vol> vols;
    private JPanel mapPanel;
    private JPanel graph;
    private JLabel carteLabel;
    private JButton coloration;
    public static JButton chargeVol;
    private static JXMapViewer mapViewer;
    private static Set<MyWaypoint> waypoints = new HashSet<>();
    private static final WaypointRender waypointRenderer = new WaypointRender();

    private JTextField hourField;
    private JTextField minuteField;
    private JButton volheure;
    private int hours = 0;
    private int minutes = 0;
    
    private JLabel marge;
    private JTextField editmarge;

    /**
     * Constructeur de la classe FenetreCarte.
     * Initialise une fenêtre graphique avec des composants pour visualiser une carte et interagir avec les données des aéroports et des vols.
     */
    public FenetreCarte() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        graph = new JPanel();
        JPanel timeGbcrolPanel = new JPanel(new GridBagLayout());
        timeGbcrolPanel.setPreferredSize(new Dimension(300, getHeight()));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        carteLabel = new JLabel("Graphique de l'aéroport");
        gbc.gridx = 0;
        gbc.gridy = 0;
        timeGbcrolPanel.add(carteLabel, gbc);

        JButton button = new JButton("Lancer la carte");
        gbc.gridy = 1;
        timeGbcrolPanel.add(button, gbc);

        button.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                ports = loadAeroports(selectedFile);
                if (ports == null) {
                    JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
                    return;
                }
                mapPanel.setVisible(true);
                setAeroport(mapViewer, waypoints, waypointRenderer, ports);
                mapViewer.setOverlayPainter(waypointRenderer);
            }
        });

        chargeVol = new JButton("Charger vols");
        gbc.gridy = 2;
        timeGbcrolPanel.add(chargeVol, gbc);

        chargeVol.addActionListener(e -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                try {
                    vols = loadVols(selectedFile);
                    if (!vols.isEmpty()) {
                        chargeVol(vols);
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun vol trouvé dans le fichier.");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FenetreCarte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        hourField = new JTextField(2);
        minuteField = new JTextField(2);
        editmarge = new JTextField(100);
        hourField.setText(String.format("%02d", hours));
        minuteField.setText(String.format("%02d", minutes));
        marge = new JLabel("Marge : ");
        
        JButton updateMargeButton = new JButton("Modifier Marge");
        JButton hourUpButton = new JButton("+1h");
        JButton hourDownButton = new JButton("-1h");
        JButton minuteUpButton = new JButton("+5m");
        JButton minuteDownButton = new JButton("-5m");
        volheure = new JButton("Afficher les vols à l'heure +- 30 minutes");

        hourUpButton.addActionListener(e -> adjustHours(1));
        hourDownButton.addActionListener(e -> adjustHours(-1));
        minuteUpButton.addActionListener(e -> adjustMinutes(5));
        minuteDownButton.addActionListener(e -> adjustMinutes(-5));

        volheure.addActionListener(e -> {
            if (vols != null) {
                try {
                    int heure = Integer.parseInt(hourField.getText());
                    int minute = Integer.parseInt(minuteField.getText());

                    // Filtrer les vols pour afficher seulement ceux à l'heure spécifiée
                    List<Vol> volsFiltres = volParHeure(heure,minute, vols);

                    // Charger les vols filtrés sur la carte
                    chargeVol(volsFiltres);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Veuillez entrer une heure et des minutes valides.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez charger les vols avant de continuer.");
            }
        });
        
        /*updateMargeButton.addActionListener((ActionEvent e) -> {
            
            int newmarge = Integer.parseInt(editmarge.getText());
            List<Vol> currentGraph = setMarge(newmarge);
            
            currentGraph.setAttribute("MARGE", newmarge);
            
           
           
                
                editmarge.setText(String.valueOf((int)currentGraph.getNumber("MARGE")));
            
            
           
            
            displayGraph(currentGraph); // Recharge le graphique après la mise à jour
        
});*/
        
        
        

        // Ajout des composants au panneau de timeGbcrôle
        // Initialisation de GridBagConstraints pour timeGbcrolPanel


// Ajout de l'étiquette "Heures et Minutes:"
gbc.gridy = 4;
timeGbcrolPanel.add(new JLabel("Heures et Minutes:"), gbc);

// Création et configuration de timePanel avec ses composants
JPanel timePanel = new JPanel(new GridBagLayout());
GridBagConstraints timeGbc = new GridBagConstraints();
timeGbc.insets = new Insets(5, 5, 5, 5);
timeGbc.gridx = 0;
timeGbc.gridy = 0;
timePanel.add(hourUpButton, timeGbc);
timeGbc.gridx = 1;
timePanel.add(minuteUpButton, timeGbc);
timeGbc.gridx = 0;
timeGbc.gridy = 1;
timePanel.add(hourField, timeGbc);
timeGbc.gridx = 1;
timePanel.add(minuteField, timeGbc);
timeGbc.gridx = 0;
timeGbc.gridy = 2;
timePanel.add(hourDownButton, timeGbc);
timeGbc.gridx = 1;
timePanel.add(minuteDownButton, timeGbc);
timeGbc.gridx = 0;
timeGbc.gridy = 3;
timeGbc.gridwidth = 2;
timePanel.add(volheure, timeGbc);

// Ajout des autres composants avec des contraintes appropriées
timeGbc.gridx = 0;
timeGbc.gridy = 8;
timeGbc.gridwidth = 1;
timeGbcrolPanel.add(marge, timeGbc);

timeGbc.gridx = 15;
timeGbcrolPanel.add(editmarge, timeGbc);

timeGbc.gridy = 9;
timeGbc.gridx = 0;
timeGbc.gridwidth = 2;
timeGbcrolPanel.add(updateMargeButton, timeGbc);

// Ajout de timePanel dans timeGbcrolPanel
gbc.gridy = 5;
timeGbcrolPanel.add(timePanel, gbc);

// Ajout du bouton "Fenetre coloration"
coloration = new JButton("Fenetre coloration");
gbc.gridy = 3;
timeGbcrolPanel.add(coloration, gbc);

coloration.addActionListener(e -> {
    openSecondaryWindow(new FenetreColoration(), "Coloration");
    this.dispose();
});

// Configuration de mapPanel
mapPanel = new JPanel(new BorderLayout());
initMapPanel();

// Ajout des panneaux à la fenêtre principale
add(timeGbcrolPanel, BorderLayout.EAST);
add(mapPanel, BorderLayout.CENTER);
setVisible(true);
    }

    /**
     * Ouvre une fenêtre secondaire avec le titre spécifié.
     *
     * @param secondaryFrame la fenêtre secondaire à ouvrir
     * @param title          le titre de la fenêtre secondaire
     */
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
    }

    /**
     * Charge les données des aéroports à partir du fichier spécifié.
     *
     * @param txtFile le fichier timeGbcenant les données des aéroports
     * @return une liste d'objets Aeroport chargés depuis le fichier
     */
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
            Logger.getLogger(FenetreCarte.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ports;
    }

    /**
     * Charge les données des vols à partir du fichier CSV spécifié.
     *
     * @param csvFile le fichier CSV timeGbcenant les données des vols
     * @return une liste d'objets Vol chargés depuis le fichier
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la lecture du fichier
     */
    private ArrayList<Vol> loadVols(File csvFile) throws IOException {
        ArrayList<Vol> vols = new ArrayList<>();
        try (Scanner scanVol = new Scanner(csvFile)) {
            while (scanVol.hasNextLine()) {
                vols.add(new Vol(scanVol));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenetreCarte.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("File not found: " + csvFile.getAbsolutePath(), ex);
        }
        return vols;
    }

    /**
     * Ouvre une boîte de dialogue pour sélectionner un fichier.
     *
     * @return le fichier sélectionné, ou null si aucun fichier n'a été sélectionné
     */
    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Initialise le panneau de la carte en créant un JXMapViewer avec un panneau de timeGbcrôle.
     */
    private void initMapPanel() {
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition initialPosition = new GeoPosition(46.5768014, 2.6674444);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(13);

        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapPanel.add(mapViewer, BorderLayout.CENTER);
    }

    /**
     * Ajuste l'heure en fonction de l'incrément spécifié.
     *
     * @param increment l'incrément (+1 pour augmenter, -1 pour diminuer)
     */
    private void adjustHours(int increment) {
        hours = (hours + increment + 24) % 24;
        hourField.setText(String.format("%02d", hours));
    }

    /**
     * Ajuste les minutes en fonction de l'incrément spécifié.
     *
     * @param increment l'incrément (+5 pour augmenter, -5 pour diminuer)
     */
    private void adjustMinutes(int increment) {
        minutes = (minutes + increment) % 60;
        if (minutes < 0) {
            minutes += 60;
            adjustHours(-1);
        } else if (minutes >= 60) {
            adjustHours(1);
        }
        minuteField.setText(String.format("%02d", minutes));
    }

    /**
     * Charge les vols spécifiés sur la carte.
     *
     * @param vol la liste des vols à charger sur la carte
     */
    public static void chargeVol(List<Vol> vol) {
        org.jxmapviewer.painter.Painter<JXMapViewer> overlay = waypointRenderer.paintVol(vol, mapViewer, ports);
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(overlay);
        painters.add(waypointRenderer);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }
}

       
