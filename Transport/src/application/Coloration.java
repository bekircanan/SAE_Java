package application;

import static construction.Algos.Gloutonne;
import static construction.Algos.largestFirstColoring;
import static construction.Algos.welshPowell;
import construction.Graphe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.graphstream.graph.Graph;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

public class Coloration extends JFrame {
    private JPanel graphPanel;
    private JLabel chromLabel;
    private JButton zoomInButton;
    private JButton zoomOutButton;

    public Coloration() {
        setTitle("Coloration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        JButton button = new JButton("Lancer Algorithme");

        zoomInButton = new JButton("+");
        zoomOutButton = new JButton("-");

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Gloutonne", "welshPowell", "largestFirstColoring"});
        chromLabel = new JLabel("Chromatic number: ");
        JLabel kMaxLabel = new JLabel("kMax: ");

        button.addActionListener((var e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor;

            try {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    gcolor = Graphe.chargerGraphe(filePath);

                    int chromaticNumber = 0;

                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> chromaticNumber = Gloutonne(gcolor);
                            case "welshPowell" -> chromaticNumber = welshPowell(gcolor);
                            case "largestFirstColoring" -> chromaticNumber = largestFirstColoring(gcolor);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }
                    }

                    if (gcolor != null) {
                        displayGraph(gcolor);
                        chromLabel.setText("Chromatic number: " + chromaticNumber);
                        kMaxLabel.setText("kMax: " + gcolor.getAttribute("kMax"));
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

        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.LINE_START;
        cont.insets = new Insets(0, 0, 10, 10);
        controlPanel.add(comboBox, cont);

        cont.gridx = 1;
        controlPanel.add(button, cont);

        cont.gridx = 0;
        cont.gridy = 1;
        controlPanel.add(kMaxLabel, cont);

        cont.gridx = 1;
        controlPanel.add(chromLabel, cont);

        cont.gridx = 2;
        controlPanel.add(zoomInButton, cont);

        cont.gridx = 3;
        controlPanel.add(zoomOutButton, cont);

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        add(controlPanel, BorderLayout.NORTH);
        add(graphPanel, BorderLayout.CENTER);
        setVisible(true);

        zoomInButton.addActionListener(new ZoomHandler(1 / 1.1));
        zoomOutButton.addActionListener(new ZoomHandler(1.1));
    }

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

    private void displayGraph(Graph g) {
        graphPanel.removeAll();

        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();  // Optional: for automatic layout of the graph

        // Prevent the opening of an external viewer window
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        View view = viewer.addDefaultView(false);

        // Set a specific size for the graph view
        view.setPreferredSize(new Dimension(500, 500));

        graphPanel.add((Component) view);
        graphPanel.revalidate();
        graphPanel.repaint();
    }
}
