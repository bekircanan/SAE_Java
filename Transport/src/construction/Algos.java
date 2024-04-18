package construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Algos {
    public static void Domination(Graph g){
        for (Node n : g) {
            n.addAttribute("dominant", 0);
        }
        for (Node n : g) {
            boolean dejaDomine = false;
            Iterator<Node> it = n.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node voisin = it.next();
                if ((int)voisin.getAttribute("dominant") == 1) {
                    dejaDomine = true;
                }
            }
            if (!dejaDomine) {
                n.setAttribute("dominant", 1);
            }
        }
        for (Node n : g) {
            if ((int) n.getAttribute("dominant") == 1) {
                n.setAttribute("ui.style", "fill-color: red; size: 30px;");
            }
        }
    }
    
    public static void Gloutonne(Graph graph) {
        int maxColors = graph.getNodeCount();
        for (Node n : graph) {
            n.addAttribute("couleur", 0);
        }
        for (Node node : graph) {
            boolean[] couleursUtilisees = new boolean[maxColors+1];
            Iterator<Node> it = node.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node neighbor = it.next();
                int color = neighbor.getAttribute("couleur");
                if (color >= 1 && color <= maxColors) {
                    couleursUtilisees[color] = true;
                }
            }
            
            int color = 1;
            while (couleursUtilisees[color]) {
                color++;
            }
            node.setAttribute("couleur", color);
        }
    }
    
    public static void colorierGraphe(Graph g, String attribut) {
        int max = g.getNodeCount();
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) /*(Math.random())*/i/max, 0.8f, 0.9f);
        }
        for (Node n : g) {
            int col = n.getAttribute(attribut);
            if (n.hasAttribute("ui.style")) {
                n.setAttribute("ui.style", "fill-color:rgba("+ cols[col].getRed() + "," + cols[col].getGreen()+ "," + cols[col].getBlue() + ",200);");
            }else {
                n.addAttribute("ui.style", "fill-color:rgba("+ cols[col].getRed() + "," + cols[col].getGreen()+ "," + cols[col].getBlue() + ",200);");
            }
        }
    }
    
    public static void distance(Graph g, Node s) {
        for (Node n : g) {
            if (!n.hasAttribute("distance")) {
                n.addAttribute("distance", -1);
            if (n==s)
                s.setAttribute("distance", 0);
            }
        }
        List<Node> liste=new ArrayList<>();
        liste.add(s);
        while (!liste.isEmpty()){
            Node n=liste.get(0);
            Iterator<Node> it = n.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node voisin = it.next(); // voisin
                if ((int) voisin.getAttribute("distance") == -1){
                    voisin.setAttribute("distance",(int) n.getAttribute("distance") + 1);
                    liste.add(voisin);
                }
            }
            liste.remove(n);
        }
    }

    public static void etiqueterDistances(Graph g) {
        for (Node n : g) {
            n.setAttribute("ui.label", n.getAttribute("distance").toString());
            n.setAttribute("ui.style", "text-mode: normal; "+ "text-style:bold; text-size:16; text-background-mode:plain;");
            if ((int) n.getAttribute("distance") == 0) {
                n.setAttribute("ui.style", "fill-color: red; "+ "size: 30px; text-style:bold; text-size:16;");
            }
        }
    }
}
