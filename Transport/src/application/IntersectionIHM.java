package application;

import construction.Aeroport;
import static construction.Aeroport.setAeroport;
import static construction.Intersection.setVolsAeroport;
import construction.Vols;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

public class IntersectionIHM extends JFrame {
    private JPanel graphPanel;
    private JPanel mapPanel;
    private JComboBox<String> comboBox;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JXMapViewer mapViewer;
    private Set<MyWaypoint> waypoints = new HashSet<>();

    public IntersectionIHM() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        String[] options = {"Graphique de l'aéroport", /*"Vol"*/};
        comboBox = new JComboBox<>(options);
        cont.gridx = 0;
        cont.gridy = 0;
        panel.add(comboBox, cont);

        JButton button = new JButton("Lancer les algos");
        cont.gridy = 1;
        panel.add(button, cont);

        zoomInButton = new JButton("+");
        cont.gridx = 2;
        panel.add(zoomInButton, cont);

        zoomOutButton = new JButton("-");
        cont.gridx = 3;
        panel.add(zoomOutButton, cont);

        button.addActionListener(new AlgoButtonListener());
        zoomInButton.addActionListener(new ZoomHandler(1 / 1.1));
        zoomOutButton.addActionListener(new ZoomHandler(1.1));

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(1200, 700));

        mapPanel = new JPanel(new BorderLayout());
        mapPanel.setPreferredSize(new Dimension(1200, 700));
        initMapPanel();
        mapPanel.setVisible(false);

        add(panel, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        add(mapPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private class AlgoButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedOption = (String) comboBox.getSelectedItem();
            if (selectedOption != null) {
                List<Aeroport> ports = loadAeroports();
                if (ports == null) {
                    JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
                    return;
                }

                List<Vols> vols = loadVols();
                if (vols == null) {
                    JOptionPane.showMessageDialog(null, "Fichier de vol non trouvé.");
                    return;
                }

                switch (selectedOption) {
                    case "Graphique de l'aéroport" -> {
                        mapPanel.setVisible(true);
                        graphPanel.setVisible(false);
                        setAeroport(mapViewer, ports); // Appel de la méthode setAeroport() pour afficher les aéroports sur la carte
                        //setVolsAeroport(vols, ports , mapViewer);
                        
}

                    /*case "Vol" -> {
                        mapPanel.setVisible(false);
                        graphPanel.setVisible(true);
                        displayGraph(setVolsCollision(vols, ports));
                        break;
                    }*/

                    default -> JOptionPane.showMessageDialog(null, "Sélection non valide.");
                }
            }
        }
    }

    private List<Aeroport> loadAeroports() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setSelectedFile(new java.io.File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File airportFile = fileChooser.getSelectedFile();
            if (!airportFile.exists()) {
                JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
                return null;
            }
            List<Aeroport> ports = new ArrayList<>();
            try (Scanner scanAero = new Scanner(airportFile)) {
                while (scanAero.hasNextLine()) {
                    ports.add(new Aeroport(scanAero));
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            return ports;
        }
        return null;
    }

    private List<Vols> loadVols() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setSelectedFile(new java.io.File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File flightFile = fileChooser.getSelectedFile();
            if (!flightFile.exists()) {
                JOptionPane.showMessageDialog(null, "Fichier de vol non trouvé.");
                return null;
            }
            List<Vols> vols = new ArrayList<>();
            try (Scanner scanVol = new Scanner(flightFile)) {
                while (scanVol.hasNextLine()) {
                    vols.add(new Vols(scanVol));
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            return vols;
        }
        return null;
    }

    private class ZoomHandler implements ActionListener {
        private final double zoomFactor;

        public ZoomHandler(double zoomFactor) {
            this.zoomFactor = zoomFactor;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (graphPanel.isVisible() && graphPanel.getComponentCount() > 0) {
                View view = (View) graphPanel.getComponent(0);
                view.getCamera().setViewPercent(view.getCamera().getViewPercent() * zoomFactor);
            } else if (mapViewer != null) {
                int zoom = mapViewer.getZoom();
                mapViewer.setZoom((int) (zoom * zoomFactor));
            }
        }
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

    private void displayAirportsOnMap(List<Aeroport> airports) {
        waypoints.clear();
        EventWaypoint event = new EventWaypoint() {
            @Override
            public void selected(MyWaypoint waypoint) {
                JOptionPane.showMessageDialog(null, "Aéroport sélectionné : " + waypoint.getAeroport().getLieu());
            }
        };

        for (Aeroport aeroport : airports) {
            MyWaypoint waypoint = new MyWaypoint(aeroport, event);
            waypoints.add(waypoint);
        }

        WaypointPainter<MyWaypoint> waypointPainter = new WaypointRender();
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    private void displayGraph(Graph graph) {
        graphPanel.removeAll();
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);
        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    private Graph setVolsCollision(List<Vols> vols, List<Aeroport> aeroports) {
        // Implement this method based on your collision detection logic
        return null;
    }

}
