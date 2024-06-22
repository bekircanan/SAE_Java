package construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * La classe {@code AlgorithmColoration} fournit plusieurs algorithmes de coloration pour les graphes.
 * <p>
 * Les algorithmes incluent une méthode gloutonne, l'algorithme de Welsh-Powell et une méthode
 * de coloration en fonction de la plus grande première.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 * Graph g = new SingleGraph("Graphe");
 * AlgorithmColoration.Gloutonne(g);
 * AlgorithmColoration.welshPowell(g);
 * AlgorithmColoration.largestFirstColoring(g);
 * }
 * </pre>
 * </p>
 * <p>
 * Ces méthodes permettent de colorier les nœuds du graphe et de déterminer le nombre chromatique du graphe.
 * </p>
 */
public class AlgorithmColoration {
    
    /**
     * Applique une méthode gloutonne pour colorier le graphe.
     * <p>
     * Chaque nœud du graphe se voit attribuer la plus petite couleur non utilisée par ses voisins.
     * </p>
     * 
     * @param g le graphe à colorier
     * @return le nombre total de conflits après coloration
     */

    public static int Gloutonne(Graph g) {
        int maxColors = g.getNodeCount();
        int[] usedColors = new int[maxColors+1];
        for (Node node : g) {
        for (int i = 0; i <= maxColors; i++) {
        usedColors[i] = 0;
        }
        Iterator<Node> it = node.getNeighborNodeIterator();
        while (it.hasNext()) {
        Node neighbor = it.next();
        int color = (int)neighbor.getNumber("color");
        if (color >= 1 && color <= maxColors) {
        usedColors[color] = color;
        }
        }
        int color = 1;
        while (usedColors[color] != 0) {
        color++;
        }
        node.setAttribute("color", color);
        }
        int con =recolorGraph(g);
        colorierGraphe(g);
        return con;
    }
    
     /**
     * Applique une méthode de coloration en fonction de la plus grande première pour colorier le graphe.
     * <p>
     * Cette méthode colore les nœuds en commençant par les nœuds de plus grand degré.
     * </p>
     * 
     * @param g le graphe à colorier
     * @return le nombre total de conflits après coloration
     */
    public static int largestFirstColoring(Graph g) {
        List<Node> nodes = new ArrayList<>(g.getNodeSet());
        nodes.sort((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()));

        Map<Node, Integer> colorMap = new HashMap<>();
        int maxColorUsed = 0;
        int totalConflicts = 0;
        int kMax = (int) g.getNumber("kMax");

        for (Node node : nodes) {
            Set<Integer> usedColors = new HashSet<>();

            for (Edge edge : node.getEachEdge()) {
                Node adjacent = edge.getOpposite(node);
                if (colorMap.containsKey(adjacent)) {
                    usedColors.add(colorMap.get(adjacent));
                }
            }

            int nodeColor = 1;
            while (usedColors.contains(nodeColor) && nodeColor <= kMax) {
                nodeColor++;
            }

            if (nodeColor > kMax) {
                Map<Integer, Integer> colorConflicts = new HashMap<>();
                for (int color = 1; color <= kMax; color++) {
                    colorConflicts.put(color, 0);
                }

                for (Edge edge : node.getEachEdge()) {
                    Node adjacent = edge.getOpposite(node);
                    if (colorMap.containsKey(adjacent)) {
                        int adjColor = colorMap.get(adjacent);
                        colorConflicts.put(adjColor, colorConflicts.get(adjColor) + 1);
                    }
                }

                nodeColor = colorConflicts.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(1);

                totalConflicts += colorConflicts.get(nodeColor);
            }

            maxColorUsed = Math.max(maxColorUsed, nodeColor);
            colorMap.put(node, nodeColor);
            node.addAttribute("color", nodeColor);

            for (Edge edge : node.getEachEdge()) {
                Node adjacent = edge.getOpposite(node);
                if (colorMap.containsKey(adjacent) && colorMap.get(adjacent).equals(nodeColor)) {
                    edge.setAttribute("ui.style", "fill-color: red;");
                }
            }
        }
        g.addAttribute("totalConflicts", totalConflicts);
        colorierGraphe(g);
        return totalConflicts;
    }
    
     /**
     * Applique les styles de couleur aux nœuds et aux arêtes du graphe pour visualisation.
     * 
     * @param g le graphe à visualiser
     */
    private static void colorierGraphe(Graph g) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) ((double) (Math.random()*100000.0)+5.0), 0.8f, 0.9f);
        }
        for (Node n : g) {
            int col = (int) n.getNumber("color");
            if (col >= 0 && col <= max) {
                n.setAttribute("ui.style", "fill-color:rgb(" + cols[col].getRed() + "," + cols[col].getGreen() + "," + cols[col].getBlue() + ");");
            } else {
                System.err.println("Invalid color index: " + col + " for node: " + n.getId());
            }
        }
    }
    
     /**
     * Applique l'algorithme DSATUR pour colorier le graphe.
     * <p>
     * Cet algorithme utilise une stratégie basée sur le degré de saturation des nœuds pour la coloration.
     * </p>
     * 
     * @param g le graphe à colorier
     * @return le nombre total de conflits après coloration
     */

    public static int dsatur(Graph g) {
        PriorityQueue<Node> nodeQueue = new PriorityQueue<>((a, b) -> {
            int dsatA = a.getAttribute("dsat");
            int dsatB = b.getAttribute("dsat");
            if (dsatA == dsatB) {
                return b.getDegree() - a.getDegree();
            }
            return dsatB - dsatA;
        });

        Map<Node, Integer> nodeColor = new HashMap<>();

        for (Node node : g) {
            node.addAttribute("dsat", 0);
            node.addAttribute("color", 0);
            nodeQueue.add(node);
        }

        while (!nodeQueue.isEmpty()) {
            Node node = nodeQueue.poll();
            Set<Integer> neighborColors = new HashSet<>();
            for (Edge edge : node.getEachEdge()) {
                Node neighbor = edge.getOpposite(node);
                if (nodeColor.containsKey(neighbor)) {
                    neighborColors.add(nodeColor.get(neighbor));
                }
            }

            int color = 1;
            while (neighborColors.contains(color)) {
                color++;
            }
            nodeColor.put(node, color);
            node.setAttribute("color", color);

            for (Edge edge : node.getEachEdge()) {
                Node neighbor = edge.getOpposite(node);
                if (!nodeColor.containsKey(neighbor)) {
                    int dsat = neighbor.getAttribute("dsat");
                    neighbor.setAttribute("dsat", dsat + 1);
                    nodeQueue.remove(neighbor);
                    nodeQueue.add(neighbor);
                }
            }
        }
        int con =recolorGraph(g);
        colorierGraphe(g);
        return con;
    }
    
     /**
     * Réapplique la coloration au graphe après résolution des conflits de couleur.
     * 
     * @param g le graphe à recolorier
     * @return le nombre total de conflits après recoloration
     */
    private static int recolorGraph(Graph g) {
        int totalConflicts = 0;
        int kMax=(int)g.getNumber("kMax");
        for (Node node : g) {
            int color = (int) node.getAttribute("color");
            if (color > kMax) {
                Map<Integer, Integer> neighborColorCount = new HashMap<>();
                Iterator<? extends Node> neighbors = node.getNeighborNodeIterator();
                while (neighbors.hasNext()) {
                    Node neighbor = neighbors.next();
                    int neighborColor = (int) neighbor.getAttribute("color");
                    neighborColorCount.put(neighborColor, neighborColorCount.getOrDefault(neighborColor, 0) + 1);
                }

                int newColor = -1;
                int minConflicts = Integer.MAX_VALUE;

                for (int i = 1; i <= kMax; i++) {
                    int conflicts = neighborColorCount.getOrDefault(i, 0);
                    if (conflicts < minConflicts) {
                        minConflicts = conflicts;
                        newColor = i;
                    }
                }
                node.setAttribute("color", newColor);
            }
        }
        for (Edge edge : g.getEdgeSet()) {
            Node node0 = edge.getNode0();
            Node node1 = edge.getNode1();
            if (node0.getAttribute("color").equals(node1.getAttribute("color"))) {
                edge.setAttribute("ui.style", "fill-color: red;");
                totalConflicts++;
            }
        }
        return totalConflicts;
    }
}
