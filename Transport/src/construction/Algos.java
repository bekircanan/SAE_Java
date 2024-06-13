package construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
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
     * @param g le graphe à colorier
     * @return 
     */
    public static int Gloutonne(Graph g) {
    int color, kMax = (int)g.getNumber("kMax");
    int maxColors = g.getNodeCount();
    int totalConflicts = 0;

    // Initialize all nodes with color 0
    for (Node n : g) {
        n.addAttribute("color", 0);
    }

    // Iterate through each node to assign colors
    for (Node node : g) {
        boolean[] couleursUtilisees = new boolean[maxColors + 1];
        Iterator<Node> it = node.getNeighborNodeIterator();

        // Mark the colors used by neighboring nodes
        while (it.hasNext()) {
            Node neighbor = it.next();
            color = neighbor.getAttribute("color");
            if (color >= 1 && color <= maxColors) {
                couleursUtilisees[color] = true;
            }
        }

        // Find the first available color
        color = 1;
        while (color <= kMax && couleursUtilisees[color]) {
            color++;
        }

        // If no available color found, find the color with the least conflicts
        if (color > kMax) {
            int[] colorConflicts = new int[kMax];
            for (int i = 0; i < kMax; i++) {
                colorConflicts[i] = 0;
            }
            it = node.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node neighbor = it.next();
                int neighborColor = neighbor.getAttribute("color");
                if (neighborColor >= 1 && neighborColor <= kMax) {
                    colorConflicts[neighborColor - 1]++;
                }
            }

            int minConflicts = Integer.MAX_VALUE;
            int bestColor = 1;
            for (int i = 0; i < kMax; i++) {
                if (colorConflicts[i] < minConflicts) {
                    minConflicts = colorConflicts[i];
                    bestColor = i + 1;
                }
            }

            color = bestColor;
        }

        // Update kMax and assign color to the node
        kMax = Math.max(kMax, color);
        node.setAttribute("color", color);

        // Calculate conflicts for the current node
        it = node.getNeighborNodeIterator();
        while (it.hasNext()) {
            Node neighbor = it.next();
            int neighborColor = neighbor.getAttribute("color");
            if (neighborColor == color) {
                totalConflicts++;
            }
        }
    }

    // Color the graph nodes visually
    colorierGraphe(g, "color");

    System.out.println(totalConflicts);
    return totalConflicts;
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
    colorierGraphe(g,"color");
    System.out.println(totalConflicts);
    return kMax;
    }
    
    /**
     * Applique une méthode de coloration en fonction de la plus grande première.
     * <p>
     * Les nœuds sont triés par ordre décroissant de degré et colorés de manière à minimiser le nombre de couleurs utilisées.
     * </p>
     * 
     * @param g le graphe à colorier
     * @return 
     */
    /*
     public static int largestFirstColoring(Graph g) {
        
        List<Node> nodes = new ArrayList<>(g.getNodeSet());
        nodes.sort((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()));
        
        Map<Node, Integer> colorMap = new HashMap<>();
        int kMax=0;

        for (Node node : nodes) {
            Set<Integer> usedColors = new HashSet<>();

            for (Edge edge : node.getEachEdge()) {
                Node adjacent = edge.getOpposite(node);
                if (colorMap.containsKey(adjacent)) {
                    usedColors.add(colorMap.get(adjacent));
                }
            }

            
            int nodeColor = 1;
            while (usedColors.contains(nodeColor)) {
                nodeColor++;
            }
            kMax=Math.max(kMax,nodeColor);

            colorMap.put(node, nodeColor);
            node.setAttribute("color", nodeColor);
            colorierGraphe(g,"color");
        }
        System.out.println("The chromatic number of this graph is : "+kMax+" where kMax is : "+g.getAttribute("kMax"));
        return(kMax);
    }
    */
    private static void colorierGraphe(Graph g, String attribut) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) ((double) (Math.random()*100000.0)+5.0), 0.8f, 0.9f);
        }
        for(Node n : g){ 
               int col = (int) n.getNumber(attribut);
               n.setAttribute("ui.style", "fill-color:rgb("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+");" );
        }
    }
    
    public static int getConflit(Graph g){
        int conf=0,now,k,next;
        k=(int) g.getNumber("kMax")-1;
        for (Node node : g) {
            Iterator<Node> it = node.getNeighborNodeIterator();
            now=(int)node.getNumber("color");
            while (it.hasNext()) {
                Node neighbor = it.next();
                next=(int)neighbor.getNumber("color");
                if(now>k){
                    if(next<=k){
                        System.out.println("conflit avec moin kMax : "+node+" != "+neighbor);
                        conf++;
                    }else if(next>now){
                        System.out.println("conflit avec plus kMax : "+node+" != "+neighbor);
                        conf++;
                    }
                }
            }
        }
        return conf;
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
        Map<Node, Integer> conflictCount = new HashMap<>();

        for (Node node : g) {
            node.addAttribute("dsat", 0);
            nodeQueue.add(node);
            conflictCount.put(node, 0);
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
                // Find the color that causes the least conflict
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
                }
            }
        }
        colorierGraphe(g, "color");
        System.out.println(totalConflicts);
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

    public static int localSearch(Graph graph) {
        int totalConflicts = 0;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (Node node : graph) {
                int originalColor = node.getAttribute("color");
                int bestColor = originalColor;
                int leastConflicts = countConflicts(node, originalColor);

                for (int color = 0; color < graph.getNodeCount(); color++) {
                    if (color != originalColor) {
                        int conflicts = countConflicts(node, color);
                        if (conflicts < leastConflicts) {
                            leastConflicts = conflicts;
                            bestColor = color;
                        }
                    }
                }

                if (bestColor != originalColor) {
                    node.setAttribute("color", bestColor);
                    improved = true;
                    totalConflicts += leastConflicts;
                }
            }
        }
        return totalConflicts;
    }

    public static void hillClimbing(Graph graph, int kMax) {
        boolean improved = true;
        while (improved) {
            improved = false;
            for (Node node : graph) {
                int originalColor = node.getAttribute("color");
                int leastConflicts = countConflicts(node, originalColor);
                int bestColor = originalColor;

                // Try each color from 0 to kMax-1 or original color if it's within kMax
                int startColor = Math.max(0, originalColor - 1);
                int endColor = Math.min(kMax - 1, originalColor + 1);
                for (int color = startColor; color <= endColor; color++) {
                    if (color != originalColor) {
                        node.setAttribute("color", color);
                        int conflicts = countTotalConflicts(graph);

                        // Check if this color reduces conflicts
                        if (conflicts < leastConflicts) {
                            leastConflicts = conflicts;
                            bestColor = color;
                        }
                    }
                }

                // Apply the best color found
                if (bestColor != originalColor) {
                    node.setAttribute("color", bestColor);
                    improved = true;
                }
            }
        }
    }
    
    private static int countConflicts(Node node, int color) {
        int conflicts = 0;
        for (Edge edge : node.getEachEdge()) {
            Node neighbor = edge.getOpposite(node);
            if (neighbor.getAttribute("color").equals(color)) {
                conflicts++;
            }
        }
        return conflicts;
    }

    public static int countTotalConflicts(Graph graph) {
        int totalConflicts = 0;
        for (Node node : graph) {
            totalConflicts += countConflicts(node, (int) node.getAttribute("color"));
        }
        return totalConflicts;
    }

    public static int runAlgorithm(String selectedAlgorithm, Graph gcolor) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
