package vue;

import construction.MyWaypoint;
import construction.WaypointRender;
import construction.jXMapviewerCustom;
import modele.Aeroport;
import modele.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

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
import static modele.Aeroport.setAeroport;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * FenetreCarte représente une fenêtre graphique pour visualiser des aéroports et des vols sur une carte.
 */
public class FenetreCarte extends JFrame {
    private static List<Aeroport> ports;
    public static List<Vol> vols;
    private JPanel mapPanel;
    private JLabel carteLabel;
    private JButton coloration;
    public static JButton chargeVol;
    private static JXMapViewer mapViewer;
    private static Set<MyWaypoint> waypoints  = new HashSet<>();
    private static final WaypointRender waypointRenderer = new WaypointRender();
    
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

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setPreferredSize(new Dimension(300, getHeight()));
        GridBagConstraints cont = new GridBagConstraints();
        cont.insets = new Insets(15, 15, 15, 15);

        carteLabel = new JLabel("Graphique de l'aéroport");
        cont.gridx = 0;
        cont.gridy = 0;
        controlPanel.add(carteLabel, cont);

        JButton button = new JButton("Lancer la carte");
        cont.gridy = 1;
        controlPanel.add(button, cont);

        button.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                 ports = loadAeroports(selectedFile);
                if (ports == null) {
                    JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
                    return;
                }
                mapPanel.setVisible(true);
                setAeroport(mapViewer,waypoints,waypointRenderer, ports);
                mapViewer.setOverlayPainter(waypointRenderer);
            }
        });

        chargeVol = new JButton("Charger vols");
        cont.gridy = 2;
        controlPanel.add(chargeVol, cont);

        chargeVol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });


        coloration = new JButton("Fenetre coloration");
        cont.gridy = 3;
        controlPanel.add(coloration, cont);

        coloration.addActionListener((ActionEvent e) -> {
            openSecondaryWindow(new FenetreColoration(), "Coloration");
            this.dispose();
        });

        mapPanel = new JPanel(new BorderLayout());
        initMapPanel();

        add(controlPanel, BorderLayout.EAST);
        add(mapPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    

    /**
     * Ouvre une fenêtre secondaire avec le titre spécifié.
     *
     * @param secondaryFrame la fenêtre secondaire à ouvrir
     * @param title le titre de la fenêtre secondaire
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

    public static void chargeVol(List<Vol> vol){
        org.jxmapviewer.painter.Painter<JXMapViewer> overlay=waypointRenderer.paintVol(vol, mapViewer, ports);
        List<org.jxmapviewer.painter.Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(overlay);
        painters.add(waypointRenderer);
        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }
    
    /**
     * Charge les données des aéroports à partir du fichier spécifié.
     *
     * @param txtFile le fichier contenant les données des aéroports
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
     * @param csvFile le fichier CSV contenant les données des vols
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
     * Initialise le panneau de la carte en créant un JXMapViewer avec un panneau de contrôle.
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
}
