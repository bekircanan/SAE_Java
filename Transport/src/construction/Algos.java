package construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * La classe {@code Algos} fournit plusieurs algorithmes de coloration pour les graphes.
 * <p>
 * Les algorithmes incluent une méthode gloutonne, l'algorithme de Welsh-Powell et une méthode
 * de coloration en fonction de la plus grande première.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 * Graph g = new SingleGraph("Graphe");
 * Algos.Gloutonne(g);
 * Algos.welshPowell(g);
 * Algos.largestFirstColoring(g);
 * }
 * </pre>
 * </p>
 * <p>
 * Ces méthodes permettent de colorier les nœuds du graphe et de déterminer le nombre chromatique du graphe.
 * </p>
 * 
 */
public class Algos {
    
    /**
     * Applique une méthode gloutonne pour colorier le graphe.
     * <p>
     * Chaque nœud du graphe se voit attribuer la plus petite couleur non utilisée par ses voisins.
     * </p>
     * 
     * @param graph
     * @param g le graphe à colorier
     * @return 
     */
    public static int Gloutonne(Graph graph) {
        // Nombre maximum de couleurs possibles
        // (au pire cas, chaque sommet a une couleur différente)
        int maxColors = graph.getNodeCount();
        // Tableau pour stocker les couleurs déjà utilisées -première couleur 1
        int[] usedColors = new int[maxColors+1];
        // Ajout de l'attribut dynamique couleur et initialisation à 0
        for (Node n : graph) {
        n.addAttribute("couleur", 0);
        }
        // Pour chaque sommet, attribution d'une couleur non utilisée par ses voisins
        for (Node node : graph) {
        // Réinitialiser le tableau des couleurs utilisées à 0
        for (int i = 0; i <= maxColors; i++) {
        usedColors[i] = 0;
        }
        // Parcourir les voisins du sommet pour marquer les couleurs utilisées
        Iterator<Node> it = node.getNeighborNodeIterator();
        while (it.hasNext()) {
        Node neighbor = it.next();
        int color = (int)neighbor.getNumber("color");
        if (color >= 1 && color <= maxColors) {
        usedColors[color] = color;
        }
        }
        // Trouver la première couleur non utilisée
        int color = 1;
        while (usedColors[color] != 0) {
        color++;
        }
        // Attribuer la couleur au sommet
        node.setAttribute("color", color);
        }
        colorierGraphe(graph);
        return (int)graph.getNumber("kMax");
    }

    
    /**
     * Applique l'algorithme de Welsh-Powell pour colorier le graphe.
     * <p>
     * Cet algorithme colore les nœuds du graphe en utilisant un nombre minimum de couleurs.
     * </p>
     * 
     * @param g le graphe à colorier
     * @return 
     */
    public static int welshPowell(Graph g){
        WelshPowell wp=new WelshPowell("color");
        wp.init(g);
        wp.compute();
        System.out.println("The chromatic number of this graph is : "+wp.getChromaticNumber()+" where kMax is : "+g.getAttribute("kMax"));
        Color[] cols = new Color[wp.getChromaticNumber()];
        for(int i=0;i< wp.getChromaticNumber();i++){
               cols[i]=Color.getHSBColor((float) (Math.random()), 0.8f, 0.9f);
        }
        for(Node n : g){ 
               int col = (int) n.getNumber("color");
               n.setAttribute("ui.style", "fill-color:rgba("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+",200);" );
        }
        return (wp.getChromaticNumber());
    }
    
    public static int largestFirstColoring(Graph g) {
        List<Node> nodes = new ArrayList<>(g.getNodeSet());
    nodes.sort((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()));
    
    Map<Node, Integer> colorMap = new HashMap<>();
    int maxColorUsed = 0;
    int totalConflicts = 0;
    int kMax=(int)g.getNumber("kMax");
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
    }
    g.addAttribute("totalConflicts", totalConflicts);
    colorierGraphe(g);
    System.out.println(totalConflicts);
    return kMax;
    }
    
    private static void colorierGraphe(Graph g) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) ((double) (Math.random()*100000.0)+5.0), 0.8f, 0.9f);
        }
        for(Node n : g){ 
               int col = (int) n.getNumber("color");
               n.setAttribute("ui.style", "fill-color:rgb("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+");" );
        }
    }
    
    public static int dsatur(Graph g) {
        int kMax=(int)g.getNumber("kMax");
        int totalConflicts = 0;
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

            int color = 0;
            while (neighborColors.contains(color) && color < kMax) {
                color++;
            }

            if (color >= kMax) {
                Map<Integer, Integer> colorConflicts = new HashMap<>();
                for (int c = 0; c < kMax; c++) {
                    colorConflicts.put(c, 0);
                }
                for (Edge edge : node.getEachEdge()) {
                    Node neighbor = edge.getOpposite(node);
                    if (nodeColor.containsKey(neighbor)) {
                        int neighborColor = nodeColor.get(neighbor);
                        colorConflicts.put(neighborColor, colorConflicts.get(neighborColor) + 1);
                    }
                }
                color = colorConflicts.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
                totalConflicts += colorConflicts.get(color);
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
                } else if (nodeColor.get(node).equals(nodeColor.get(neighbor))) {
                    edge.setAttribute("ui.style", "fill-color: red;");
                }
            }
        }
        colorierGraphe(g);
        return totalConflicts;
    }
    
    public static void recursiveLargestFirst(Graph graph) {
        List<Node> nodes = new ArrayList<>(graph.getNodeSet());
        nodes.sort(Comparator.comparing(n -> (Integer) n.getAttribute("degree"), Comparator.reverseOrder()));

        Map<Node, Integer> nodeColor = new HashMap<>();
        int currentColor = 0;

        while (!nodes.isEmpty()) {
            Node node = nodes.get(0);
            assignColor(node, nodeColor, currentColor);
            nodes.removeIf(n -> nodeColor.containsKey(n));
            currentColor++;
        }
    }

    private static void assignColor(Node node, Map<Node, Integer> nodeColor, int color) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodeColor.put(current, color);
            current.setAttribute("color", color);

            List<Node> neighbors = new ArrayList<>();
            for (Edge edge : current.getEachEdge()) {
                Node neighbor = edge.getOpposite(current);
                if (!nodeColor.containsKey(neighbor)) {
                    neighbors.add(neighbor);
                }
            }
            neighbors.sort(Comparator.comparing(n -> (int) n.getAttribute("degree"), Comparator.reverseOrder()));
            queue.addAll(neighbors);
        }
    }
}
