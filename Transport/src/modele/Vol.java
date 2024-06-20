package modele;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * La classe {@code Vol} représente un vol avec des informations telles que le code du vol,
 * l'aéroport de départ, l'aéroport d'arrivée, l'heure de départ, les minutes de départ et la durée du vol.
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 Scanner scan = new Scanner(new File("vols.txt"));
 Vol vol = new Vol(scan);
 }
 * </pre>
 * </p>
 * <p>
 * Le fichier d'entrée doit avoir le format suivant :
 * <ul>
 * <li>Chaque ligne contient les informations d'un vol séparées par des points-virgules.</li>
 * </ul>
 * </p>
 * 
 */
public class Vol {
    private String codeVol;
    private String depart;
    private String arrive;
    private int heure;
    private int min;
    private int duree;
    private Aeroport departaero;
    private Aeroport arriveaero;
    
    /**
     * Constructeur de la classe {@code Vols}.
     * <p>
     * Initialise un objet {@code Vols} en lisant les informations depuis un {@code Scanner}.
     * </p>
     * 
     * @param scan le scanner à partir duquel les informations du vol sont lues
     */
    public Vol(Scanner scan){
        String[] parts =  scan.nextLine().split(";");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid input format");
        }
        try {
            this.codeVol = parts[0];
            this.depart = parts[1];
            this.arrive = parts[2];
            if (depart.length() != 3 || arrive.length() != 3) {
                throw new IllegalArgumentException("Airport codes must be 3 letters long");
            }
            this.heure = Integer.parseInt(parts[3]);
            this.min = Integer.parseInt(parts[4]);
            this.duree = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
        }
    }

    /**
     * Retourne le code du vol.
     * 
     * @return le code du vol
     */
    public String getCodeVol() {
        return codeVol;
    }

    /**
     * Retourne l'aéroport de départ.
     * 
     * @return l'aéroport de départ
     */
    public String getDepart() {
        return depart;
    }

    /**
     * Retourne l'aéroport d'arrivée.
     * 
     * @return l'aéroport d'arrivée
     */
    public String getArrive() {
        return arrive;
    }

    /**
     * Retourne l'heure de départ.
     * 
     * @return l'heure de départ
     */
    public int getHeure() {
        return heure;
    }

    /**
     * Retourne les minutes de départ.
     * 
     * @return les minutes de départ
     */
    public int getMin() {
        return min;
    }

    /**
     * Retourne la durée du vol.
     * 
     * @return la durée du vol
     */
    public int getDuree() {
        return duree;
    }

     /**
     * Retourne l'aéroport de départ sous forme d'objet {@code Aeroport}.
     * 
     * @return l'aéroport de départ
     */
    public Aeroport getDepartaero() {
        return departaero;
    }

    /**
     * Retourne l'aéroport d'arrivée sous forme d'objet {@code Aeroport}.
     * 
     * @return l'aéroport d'arrivée
     */
    public Aeroport getArriveaero() {
        return arriveaero;
    }

    /**
     * Définit l'aéroport de départ.
     * 
     * @param departaero l'aéroport de départ à définir
     */
    public void setDepartaero(Aeroport departaero) {
        this.departaero = departaero;
    }

    /**
     * Définit l'aéroport d'arrivée.
     * 
     * @param arriveaero l'aéroport d'arrivée à définir
     */
    public void setArriveaero(Aeroport arriveaero) {
        this.arriveaero = arriveaero;
    }
    
    public static void exportTXT(Graph g){
        try(FileWriter fichier = new FileWriter("..//..//filename.txt")) {
            int now,next;
            fichier.write(g.getNodeCount()+"\n");
            fichier.write("0\n");
            for (Node node : g) {
                now=node.getIndex();
                Iterator<Node> it = node.getNeighborNodeIterator();
                while (it.hasNext()) {
                    Node neighbor = it.next();
                    next=neighbor.getIndex();
                    if(next>now){
                        fichier.write(now +" "+next+"\n");
                    }
                }
            }
        } catch (IOException e) {
          System.out.println("Probleme avec fichier");
        }
    }
}
