package application;

import static construction.Algos.Gloutonne;
import static construction.Algos.dsatur;
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
    private JTextField kMaxField;
    private JLabel kMaxLabel;
    private Graph currentGraph;
    private JComboBox<String> comboBox;

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

        comboBox = new JComboBox<>(new String[]{"Gloutonne", "welshPowell", "largestFirstColoring", "Dsatur"});
        chromLabel = new JLabel("Chromatic number: ");
        kMaxLabel = new JLabel("kMax: ");
        kMaxField = new JTextField(5);
        JButton updateKMaxButton = new JButton("Update kMax");

        // Ajouter un espace entre les composants
        cont.insets = new Insets(30, 5, 30, 5);

        button.addActionListener((var e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor;

            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new java.io.File("."));
                fileChooser.setSelectedFile(new java.io.File("."));
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
                            case "Dsatur" -> chromaticNumber = dsatur(gcolor);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }
                    }

                    if (gcolor != null) {
                        currentGraph = gcolor;
                        displayGraph(gcolor, chromaticNumber);
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

        updateKMaxButton.addActionListener((var e) -> {
            try {
                if (currentGraph != null) {
                    int newKMax = Integer.parseInt(kMaxField.getText());
                    currentGraph.setAttribute("kMax", newKMax);
                    kMaxLabel.setText("kMax: " + newKMax);

                    // Recalculate chromatic number with the updated kMax
                    String selectedAlgorithm = (String) comboBox.getSelectedItem();
                    int chromaticNumber = 0;
                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne" -> chromaticNumber = Gloutonne(currentGraph);
                            case "welshPowell" -> chromaticNumber = welshPowell(currentGraph);
                            case "largestFirstColoring" -> chromaticNumber = largestFirstColoring(currentGraph);
                            case "Dsatur" -> chromaticNumber = dsatur(currentGraph);
                            default -> JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                        }
                    }

                    displayGraph(currentGraph, chromaticNumber);
                    chromLabel.setText("Chromatic number: " + chromaticNumber);
                } else {
                    JOptionPane.showMessageDialog(null, "Aucun graphe chargé.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Entrée invalide. Assurez-vous de saisir un nombre valide.");
            }
        });

        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.CENTER;
        cont.gridwidth = 2; // Pour fusionner sur 2 colonnes
        controlPanel.add(comboBox, cont);

        cont.gridy = 1;
        controlPanel.add(button, cont);

        cont.gridx = 0;
        cont.gridy = 2;
        cont.gridwidth = 1;
        controlPanel.add(kMaxLabel, cont);

        cont.gridx = 1;
        controlPanel.add(chromLabel, cont);

        cont.gridx = 0;
        cont.gridy = 3;
        controlPanel.add(new JLabel("Nouveau kMax: "), cont);

        cont.gridx = 1;
        controlPanel.add(kMaxField, cont);

        cont.gridx = 0;
        cont.gridy = 4;
        cont.gridwidth = 2; // Pour fusionner sur 2 colonnes
        controlPanel.add(updateKMaxButton, cont);

        // Ajoute les boutons à droite
        add(controlPanel, BorderLayout.LINE_END);

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        JScrollPane jsp = new JScrollPane(graphPanel);
        // Ajoute le JScrollPane avec le panneau du graphique
        add(jsp, BorderLayout.CENTER);

        cont.gridx = 0;
        cont.gridy = 5;
        cont.gridwidth = 3; // Pour fusionner sur 2 colonnes
        controlPanel.add(zoomInButton, cont);

        cont.gridx = 1;
        controlPanel.add(zoomOutButton, cont);

        // Ajoute le graphique à gauche
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

    private void displayGraph(Graph g, int chromaticNumber) {
        graphPanel.removeAll();

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
}
