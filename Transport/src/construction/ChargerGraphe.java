
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
 * La classe {@code ChargerGraphe} représente un graphe construit à partir d'un fichier.
 * Elle utilise la bibliothèque GraphStream pour créer et afficher le graphe.
 * <p>
 * La classe fournit une méthode statique {@code chargerGraphe} pour charger le graphe
 * à partir d'un fichier où les sommets et les arêtes du graphe sont définis.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 Graph graph = ChargerGraphe.chargerGraphe("chemin/vers/fichier.txt");
 }
 * </pre>
 * </p>
 * <p>
 * Le fichier d'entrée doit avoir le format suivant :
 * <ul>
 * <li>La première ligne contient une chaîne représentant un attribut du graphe.</li>
 * <li>La deuxième ligne contient un entier représentant le nombre de sommets.</li>
 * <li>Chaque ligne suivante contient deux chaînes représentant les arêtes entre les sommets.</li>
 * </ul>
 * </p>
 * <p>
 * Les sommets et les arêtes sont automatiquement créés et stylisés en utilisant les attributs de GraphStream.
 * </p>
 * 
 */
public class ChargerGraphe {
     static int sommets;

   /**
     * Charge un graphe à partir d'un fichier.
     * <p>
     * La méthode lit le fichier pour créer un graphe où les sommets et les arêtes sont définis
     * en fonction du contenu du fichier. Le graphe est créé en utilisant la bibliothèque GraphStream.
     * </p>
     * 
     * @param nomFichier le nom du fichier contenant les données du graphe
     * @return un objet Graph représentant le graphe défini dans le fichier
     * @throws FileNotFoundException si le fichier est introuvable
     * @throws IOException si une erreur d'E/S se produit
     */
    public static Graph chargerGraphe(String nomFichier) throws FileNotFoundException, IOException{
        Graph g = new SingleGraph("Chaine");
        String arete1,arete2;
        
        g.setStrict(false);
        g.setAutoCreate( true );
        
        try ( Scanner scan = new Scanner(new File(nomFichier))) {
            g.addAttribute("kMax",scan.next());
            sommets=scan.nextInt();
            
            while(scan.hasNext()){
                arete1 = scan.next();
                arete2= scan.next();
                g.addEdge(arete1+"-"+arete2, arete1, arete2);
            }
            
            for(Node n:g){
                n.addAttribute("degree", 0);
                n.setAttribute("degree", n.getDegree());
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
