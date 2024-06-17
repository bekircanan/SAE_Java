package vue;

import construction.MyWaypoint;
import construction.jXMapviewerCustom;
import modele.Aeroport;
import static modele.Aeroport.setAeroport;
import modele.Vol;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import vue.FenetreColoration;

public class FenetreCarte extends JFrame {
    private JPanel mapPanel;
    private JLabel carteLabel;
    private JButton coloration;
    private JButton chargeVol;
    private JXMapViewer mapViewer;
    private Set<MyWaypoint> waypoints = new HashSet<>();

    public FenetreCarte() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setPreferredSize(new Dimension(300, getHeight())); // Adjust the width of the control panel
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
            String selectedOption = carteLabel.getText();
            if (selectedOption != null) {
                File selectedFile = selectFile();
                List<Aeroport> ports = loadAeroports(selectedFile);
                if (ports == null) {
                    JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
                    return;
                }

                switch (selectedOption) {
                    case "Graphique de l'aéroport" -> {
                        mapPanel.setVisible(true);
                        setAeroport(mapViewer,ports);
                    }

                    default -> JOptionPane.showMessageDialog(null, "Sélection non valide.");
                }
            }
        });
                
        
        chargeVol = new JButton("charger vols");
        cont.gridy = 2;
        controlPanel.add(chargeVol, cont);
        
        chargeVol.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            try {
                ArrayList<Vol> vols=loadVols(selectedFile);
                jXMapviewerCustom mapViewer = new jXMapviewerCustom(vols);
            } catch (IOException ex) {
                Logger.getLogger(FenetreCarte.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        coloration = new JButton("Fenetre coloration");
        cont.gridy = 3;
        controlPanel.add(coloration, cont);
        
        

        mapPanel = new JPanel(new BorderLayout());
        initMapPanel();

        add(controlPanel, BorderLayout.EAST);
        add(mapPanel, BorderLayout.CENTER);
        setVisible(true);
        
        coloration.addActionListener((ActionEvent e) -> {
            openSecondaryWindow(new FenetreColoration(), "Coloration");
            this.dispose();
     });
     
     
    }
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

    private void initMapPanel() {
        mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        
        GeoPosition initialPosition = new GeoPosition(46.5768014,2.6674444);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(13);

        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapPanel.add(mapViewer, BorderLayout.CENTER);
    }

    private void displayIntersectionMetrics(double[] intersection) {
        System.out.println("Contenu du tableau intersection : ");
        for (double value : intersection) {
            System.out.print(value + " ");
        }
        System.out.println(); // Pour passer à la ligne
        // Update the UI components (like JLabels) to display the metrics if needed
    }
}
