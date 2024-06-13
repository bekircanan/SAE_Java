package application;

import construction.Aeroport;
import construction.Graphe;
import construction.Vols;
import construction.Algos;
import construction.Intersection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.graphstream.graph.Graph;
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
    private JLabel LabelAirport;
    private List<Aeroport> airports;

    public Coloration() {
        setTitle("Coloration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();

        JButton button = new JButton("Lancer Algorithme");
        JButton ButtonAirport = new JButton("Charger un aéroport");
        LabelAirport = new JLabel("Aucun aéroport chargé");
        zoomInButton = new JButton("+");
        zoomOutButton = new JButton("-");

        comboBox = new JComboBox<>(new String[]{"Gloutonne", "Welsh-Powell", "Largest First Coloring", "Dsatur"});
        chromLabel = new JLabel("Chromatic number: ");
        kMaxLabel = new JLabel("kMax: ");
        kMaxField = new JTextField(5);
        JButton updateKMaxButton = new JButton("Update kMax");

        cont.insets = new Insets(30, 5, 30, 5);

        ButtonAirport.addActionListener((ActionEvent e) -> {
            File selectedFile = selectFile();
            if (selectedFile != null) {
                airports = loadAeroports(selectedFile);
                if (airports != null) {
                    LabelAirport.setText(selectedFile.getName());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucun fichier sélectionné.");
            }
        });

        // Initialisez votre bouton "Lancer Algorithme"
        button.addActionListener((ActionEvent e) -> {
            String selectedAlgorithm = (String) comboBox.getSelectedItem();
            Graph gcolor;

            try {
                File selectedFile = airports != null ? selectFile() : null; // Utilisez le fichier d'aéroport déjà chargé
                if (selectedFile != null) {
                    if (selectedFile.getName().endsWith(".txt")) {
                        gcolor = Graphe.chargerGraphe(selectedFile.getAbsolutePath());
                    } else if (selectedFile.getName().endsWith(".csv")) {
                        List<Vols> vols = loadVols(selectedFile);
                        
                        // Utilisez les aéroports déjà chargés pour l'intersection
                        gcolor = Intersection.setVolsCollision(vols, airports);
                    } else {
                        JOptionPane.showMessageDialog(null, "Format de fichier non supporté.");
                        return;
                    }

                    int chromaticNumber = 0;

                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne":
                                chromaticNumber = Algos.Gloutonne(gcolor);
                                break;
                            case "Welsh-Powell":
                                chromaticNumber = Algos.welshPowell(gcolor);
                                break;
                            case "Largest First Coloring":
                                chromaticNumber = Algos.largestFirstColoring(gcolor);
                                break;
                            case "Dsatur":
                                chromaticNumber = Algos.dsatur(gcolor);
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                                break;
                        }
                    }

                    if (gcolor != null) {
                        currentGraph = gcolor;
                        displayGraph(gcolor, chromaticNumber);
                        chromLabel.setText("Nombre chromatique : " + chromaticNumber);
                        kMaxLabel.setText("kMax : " + gcolor.getAttribute("kMax"));
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


        updateKMaxButton.addActionListener((ActionEvent e) -> {
            try {
                if (currentGraph != null) {
                    int newKMax = Integer.parseInt(kMaxField.getText());
                    currentGraph.setAttribute("kMax", newKMax);
                    kMaxLabel.setText("kMax: " + newKMax);

                    String selectedAlgorithm = (String) comboBox.getSelectedItem();
                    int chromaticNumber = 0;
                    if (selectedAlgorithm != null) {
                        switch (selectedAlgorithm) {
                            case "Gloutonne":
                                chromaticNumber = Algos.Gloutonne(currentGraph);
                                break;
                            case "Welsh-Powell":
                                chromaticNumber = Algos.welshPowell(currentGraph);
                                break;
                            case "Largest First Coloring":
                                chromaticNumber = Algos.largestFirstColoring(currentGraph);
                                break;
                            case "Dsatur":
                                chromaticNumber = Algos.dsatur(currentGraph);
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "Sélection d'algorithme non valide.");
                                break;
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

       
        // Add spacing before the first two components
        cont.insets = new Insets(20, 5, 5, 5);

        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.CENTER;
        cont.gridwidth = 2;
        controlPanel.add(LabelAirport, cont);

        cont.gridy = 1;
        controlPanel.add(ButtonAirport, cont);

        // Add spacing between other components
        cont.insets = new Insets(10, 5, 10, 5);

        cont.gridy = 2;
        controlPanel.add(comboBox, cont);

        cont.gridy = 3;
        controlPanel.add(button, cont);

        // Add spacing between kMaxLabel and chromLabel
        cont.gridy = 4;
        cont.gridwidth = 1;
        controlPanel.add(kMaxLabel, cont);

        cont.gridx = 1;
        cont.insets = new Insets(10, 15, 10, 5); // Add more space on the left
        controlPanel.add(chromLabel, cont);

        cont.gridx = 0;
        cont.gridy = 5;
        cont.insets = new Insets(10, 5, 10, 5); // Reset the space
        controlPanel.add(new JLabel("Nouveau kMax: "), cont);

        cont.gridx = 1;
        controlPanel.add(kMaxField, cont);

        cont.gridx = 0;
        cont.gridy = 6;
        cont.gridwidth = 2;
        controlPanel.add(updateKMaxButton, cont);

        add(controlPanel, BorderLayout.LINE_END);

        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setPreferredSize(new Dimension(600, 400));

        JScrollPane jsp = new JScrollPane(graphPanel);
        add(jsp, BorderLayout.CENTER);

        cont.gridx = 0;
        cont.gridy = 7;
        cont.gridwidth = 1;
        controlPanel.add(zoomInButton, cont);

        cont.gridx = 1;
        controlPanel.add(zoomOutButton, cont);

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

    private File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private List<Vols> loadVols(File csvFile) throws IOException {
        List<Vols> vols = new ArrayList<>();
        try (Scanner scanVol = new Scanner(csvFile)) {
            while (scanVol.hasNextLine()) {
                vols.add(new Vols(scanVol));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Coloration.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("File not found: " + csvFile.getAbsolutePath(), ex);
        }

        return vols;
    }

    private List<Aeroport> loadAeroports(File txtFile) {
    if (!txtFile.exists()) {
        JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
        return null;
    }
    List<Aeroport> ports = new ArrayList<>();
    try (Scanner scanAero = new Scanner(txtFile)) {
        while (scanAero.hasNextLine()) {
            ports.add(new Aeroport(scanAero));
        }
    } catch (FileNotFoundException ex) {
        Logger.getLogger(Coloration.class.getName()).log(Level.SEVERE, null, ex);
        return null;
    }
    return ports;
}


    private void displayGraph(Graph g, int chromaticNumber) {
        graphPanel.removeAll();

        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

        View view = viewer.addDefaultView(false);
        view.setPreferredSize(new Dimension(500, 500));

        graphPanel.add((Component) view, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Coloration::new);
    }
}
