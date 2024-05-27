package construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
     */
    public static void Gloutonne(Graph g) {
        int color,k=0;
        int maxColors = g.getNodeCount();
        for (Node n : g) {
            n.addAttribute("couleur", 0);
        }
        for (Node node : g) {
            boolean[] couleursUtilisees = new boolean[maxColors+1];
            Iterator<Node> it = node.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node neighbor = it.next();
                 color = neighbor.getAttribute("couleur");
                if (color >= 1 && color <= maxColors) {
                    couleursUtilisees[color] = true;
                }
            }
            
             color = 1;
            while (couleursUtilisees[color]) {
                color++;
            }
            k=Math.max(k, color);
            node.setAttribute("couleur", color);
        }
        System.out.println("The chromatic number of this graph is : "+k+" where kMax is : "+g.getAttribute("kMax"));
        colorierGraphe(g,"couleur");
    }
    
    
    
    /**
     * Applique l'algorithme de Welsh-Powell pour colorier le graphe.
     * <p>
     * Cet algorithme colore les nœuds du graphe en utilisant un nombre minimum de couleurs.
     * </p>
     * 
     * @param g le graphe à colorier
     */
    public static void welshPowell(Graph g){
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
    }
    
    /**
     * Applique une méthode de coloration en fonction de la plus grande première.
     * <p>
     * Les nœuds sont triés par ordre décroissant de degré et colorés de manière à minimiser le nombre de couleurs utilisées.
     * </p>
     * 
     * @param g le graphe à colorier
     */
     public static void largestFirstColoring(Graph g) {
        
        List<Node> nodes = new ArrayList<>(g.getNodeSet());
        nodes.sort((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()));
        
        Map<Node, Integer> colorMap = new HashMap<>();
        int k=0;

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
            k=Math.max(k,nodeColor);

            colorMap.put(node, nodeColor);
            node.setAttribute("color", nodeColor);
            colorierGraphe(g,"color");
        }
        System.out.println("The chromatic number of this graph is : "+k+" where kMax is : "+g.getAttribute("kMax"));
    }
     
    private static void colorierGraphe(Graph g, String attribut) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) (Math.random()), 0.8f, 0.9f);
        }
        for(Node n : g){ 
               int col = (int) n.getNumber(attribut);
               n.setAttribute("ui.style", "fill-color:rgb("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+");" );
        }
    }
}
