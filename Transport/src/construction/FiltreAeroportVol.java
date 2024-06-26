/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modele.Aeroport;
import modele.Vol;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import vue.FenetreColoration;

/**
 *
 * @author bekir
 */
public class FiltreAeroportVol {

    /**
     * Sélectionne les vols à afficher dans le graphe selon l'heure spécifiée.
     *
     * @param heure l'heure à laquelle les vols doivent être sélectionnés
     * @param minute
     * @param vol la liste des vols
     * @return une liste des vols filtrés par heure
     */
    public static List<Vol> volParHeure(int heure,List<Vol> vol) {
        List<Vol> volsFiltres = new ArrayList<>();
        for (Vol v : vol) {
            if (v.getHeure() == heure) {
                volsFiltres.add(v);
            }
        }
        return volsFiltres;
    }

    /**
     * Sélectionne les vols à afficher dans le graphe selon l'aéroport spécifié.
     *
     * @param aeroport le code de l'aéroport à sélectionner
     * @param vol la liste des vols
     * @return
     */
    public static List<Vol> selectAeroport(String aeroport, List<Vol> vol) {
        List<Vol> result = new ArrayList<>();
        for (Vol v : vol) {
            if (v.getDepart().equals(aeroport)) {
                result.add(v);
            }
        }
        return result;
    }

    /**
     * Sélectionne les vols à afficher dans le graphe selon le niveau de couleur spécifié.
     *
     * @param level le niveau de couleur à sélectionner
     * @param vol la liste des vols
     * @param g le graphe des vols
     * @return
     */
    public static List<Vol> selectLevel(int level, List<Vol> vol, Graph g) {
        List<Vol> result = new ArrayList<>();
        for (Node n : g) {
            if ((int) n.getNumber("color") == level) {
                for (Vol v : vol) {
                    if (n.equals(g.getNode(v.getCodeVol()))) {
                        result.add(v);
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
    * Sélectionne un fichier à ouvrir via une boîte de dialogue.
    * @return Le fichier sélectionné ou null si aucun fichier n'est sélectionné.
    */
    public static File selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
    
    /**
     * Charge les aéroports à partir d'un fichier texte.
     * @param txtFile Le fichier texte contenant les données des aéroports.
     * @param aeroports
     */
    public static void loadAeroports(File txtFile, ArrayList<Aeroport> aeroports) {
        if (!txtFile.exists()) {
            JOptionPane.showMessageDialog(null, "Fichier d'aéroport non trouvé.");
        }
        try (Scanner scanAero = new Scanner(txtFile)) {
            while (scanAero.hasNextLine()) {
                aeroports.add(new Aeroport(scanAero));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenetreColoration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Charge les vols à partir d'un fichier CSV.
     * @param csvFile Le fichier CSV contenant les données des vols.
     * @param vols
     * @throws IOException Si une erreur de lecture du fichier se produit.
     */
    public static void loadVols(File csvFile,ArrayList<Vol> vols) throws IOException {
        try (Scanner scanVol = new Scanner(csvFile)) {
            while (scanVol.hasNextLine()) {
                 try {
                    vols.add(new Vol(scanVol));
                } catch (Exception e) {
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FenetreColoration.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException("File not found: " + csvFile.getAbsolutePath(), ex);
        }
    }
    
    
}
