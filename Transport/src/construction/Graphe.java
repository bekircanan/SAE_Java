/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author bekir
 */
public class Graphe {
     static int sommets;
    public static Graph chargerGraphe(String nomFichier) throws FileNotFoundException, IOException{
        Graph g = new SingleGraph("Chaine");
        String arete1,arete2;
        
        g.setStrict(false);
        g.setAutoCreate( true );
        
        try ( Scanner scan = new Scanner(new File("DataTest\\graph-test0.txt"))) {
            g.addAttribute("kMax",scan.next());
            sommets=scan.nextInt();
            //scan.useDelimiter(" ");
            while(scan.hasNext()){
                arete1 = scan.next();
                arete2= scan.next();
                g.addEdge(arete1+"-"+arete2, arete1, arete2);
            }
                
            for(Node n:g){
                n.setAttribute("ui.style", "size:20px;");
                n.setAttribute("ui.label", n.getId());
            }
            for(Edge e:g.getEdgeSet()){
                e.setAttribute("ui.label", e.getId());
            }
        }
        return g;
    }
}
