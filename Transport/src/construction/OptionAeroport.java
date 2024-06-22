/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.util.ArrayList;
import java.util.List;
import modele.Vol;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 *
 * @author bekir
 */
public class OptionAeroport {

    /**
     * Sélectionne les vols à afficher dans le graphe selon l'heure spécifiée.
     *
     * @param heure l'heure à laquelle les vols doivent être sélectionnés
     * @param minute
     * @param vol la liste des vols
     * @return une liste des vols filtrés par heure
     */
    public static List<Vol> volParHeure(int heure, int minute, List<Vol> vol) {
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
    
}
