package application;

import construction.Aeroport;
import static construction.Aeroport.setAeroport;
import static construction.Intersection.setVolsAeroport;
import static construction.Intersection.setVolsCollision;
import construction.Vols;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
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

public class IntersectionIHM extends JFrame {
    private JPanel graphPanel;
    private JPanel mapPanel;
    private JComboBox<String> comboBox;
    private JButton zoomInButton;
    private JButton zoomOutButton;

    public IntersectionIHM() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        String[] options = {"Graphique de l'aéroport", "Vol"};
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
                        displayGraph(setVolsAeroport(vols, ports, setAeroport(ports)));
                    }

                    case "Vol" -> {
                        mapPanel.setVisible(false);
                        
                        displayGraph(setVolsCollision(vols, ports));
                        break;
                    }

                    default -> JOptionPane.showMessageDialog(null, "Sélection non valide.");
                }
            }
        }
    }

    private List<Aeroport> loadAeroports() {
        List<Aeroport> ports = new ArrayList<>();
        try (Scanner scanAero = new Scanner(new File("DataTest/aeroports.txt"))) {
            while (scanAero.hasNextLine()) {
                ports.add(new Aeroport(scanAero));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return ports;
    }

    private List<Vols> loadVols() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setSelectedFile(new java.io.File("."));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File flightFile = fileChooser.getSelectedFile();
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
            if (graphPanel.getComponentCount() > 0) {
                View view = (View) graphPanel.getComponent(0);
                view.getCamera().setViewPercent(view.getCamera().getViewPercent() * zoomFactor);
            }
        }
    }

    private void initMapPanel() {
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        GeoPosition initialPosition = new GeoPosition(46.7506229, 2.2942103);
        mapViewer.setAddressLocation(initialPosition);
        mapViewer.setZoom(5);

        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        mapPanel.add(mapViewer, BorderLayout.CENTER);
    }

    private void displayGraph(Graph g) {
        graphPanel.removeAll();

        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        View view = viewer.addDefaultView(false);
        view.setPreferredSize(new Dimension(1200, 700));

        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
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
