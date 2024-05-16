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
 *
 * @author bekir
 */
public class Algos {
    
    /**
     *
     * @param g
     */
    public static void Gloutonne(Graph g) {
        int color=0;
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
            
            node.setAttribute("couleur", color);
        }
        System.out.println("The chromatic number of this graph is : "+color+" where kMax is : "+g.getAttribute("kMax"));
    }
    
    /**
     *
     * @param g
     * @param attribut
     */
    public static void colorierGraphe(Graph g, String attribut) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) (Math.random()), 0.8f, 0.9f);
        }
        for(Node n : g){ 
               int col = (int) n.getNumber(attribut);
               n.setAttribute("ui.style", "fill-color:rgba("+cols[col].getRed()+","+cols[col].getGreen()+","+cols[col].getBlue()+",200);" );
        }
    }
    
    /**
     *
     * @param g
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
     public static void largestFirstColoring(Graph g) {
        // Sort nodes by degree in descending order
        List<Node> nodes = new ArrayList<>(g.getNodeSet());
        nodes.sort((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()));

        // Map to store the color of each node
        Map<Node, Integer> colorMap = new HashMap<>();
        int k=0;

        for (Node node : nodes) {
            Set<Integer> usedColors = new HashSet<>();

            // Collect colors of adjacent nodes
            for (Edge edge : node.getEachEdge()) {
                Node adjacent = edge.getOpposite(node);
                if (colorMap.containsKey(adjacent)) {
                    usedColors.add(colorMap.get(adjacent));
                }
            }

            // Find the smallest available color
            int nodeColor = 0;
            while (usedColors.contains(nodeColor)) {
                nodeColor++;
            }
            k=Math.max(k,nodeColor);

            // Assign the color to the node
            colorMap.put(node, nodeColor);
            node.setAttribute("color", nodeColor);
            colorierGraphe(g,"color");
        }
        System.out.println("The chromatic number of this graph is : "+k+" where kMax is : "+g.getAttribute("kMax"));
    }
}
