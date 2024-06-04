package application;

import construction.Aeroport;
import construction.Vols;
import static construction.Aeroport.setAeroport;
import static construction.Intersection.setVolsAeroport;
import static construction.Intersection.setVolsCollision;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import org.graphstream.graph.Graph;

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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private String chiffre;
    private JPanel graphPanel;
    private JPanel mapPanel;
    
    

    public IntersectionIHM() {
        setTitle("Intersection");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800); // Set a proper size for the main frame
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        String[] options = {"Graphique de l'aéroport", "Vol"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        cont.gridx = 0;
        cont.gridy = 0;
        panel.add(comboBox, cont);
        
        JButton button = new JButton("Lancer les algos");
        cont.gridx = 0;
        cont.gridy = 1;
        panel.add(button, cont);
        JButton zoomInButton = new JButton("+");
        cont.gridx = 2;
       panel.add(zoomInButton, cont);
       
       JButton zoomOutButton = new JButton("-");
       cont.gridx = 3;
       panel.add(zoomOutButton, cont);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    //TODO fair recherche fichier dans explorateur fichier
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File flightFile = fileChooser.getSelectedFile();
                        List<Vols> vol = new ArrayList<>();
                        try (Scanner scanVol = new Scanner(flightFile)) {
                            while (scanVol.hasNextLine()) {
                                vol.add(new Vols(scanVol));
                            }
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Fichier de vol non trouvé.");
                            return; // Exit the action listener
                        }

                        switch (selectedOption) {
                            case "Graphique de l'aéroport":
                                mapPanel.setVisible(true);
                                setVolsAeroport(vol, port, setAeroport(port));
                                //displayGraph(setVolsAeroport(vol, port, setAeroport(port)));
                                break;

                            case "Vol":
                                mapPanel.setVisible(false);
                                displayGraph(setVolsCollision(vol, port));
                                break;

                            default:
                                JOptionPane.showMessageDialog(null, "Sélection non valide.");
                                break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre valide.");
                    }
                }
            }
        });

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(1200, 700)); // Adjust size to fit properly within the frame

        mapPanel = new JPanel(new BorderLayout());
        mapPanel.setPreferredSize(new Dimension(1200, 700)); // Adjust size to fit properly within the frame
        initMapPanel();
        mapPanel.setVisible(false); // Initially hidden

        add(panel, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        add(mapPanel, BorderLayout.SOUTH);
        setVisible(true);
        
        zoomInButton.addActionListener((ActionListener) new ZoomHandler(1 / 1.1));
        zoomOutButton.addActionListener(new ZoomHandler(1.1));
    }
    //TODO zoomer que pour choix VOL
    private class ZoomHandler implements ActionListener {
        private double zoomFactor;

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

   

    

    // Ajouter les listeners pour interagir avec la carte
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
        view.setPreferredSize(new Dimension(1200, 700)); // Adjust size to fit properly within the panel

        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IntersectionIHM::new);
    }
}
