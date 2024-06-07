package construction;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.graphstream.graph.Edge;
    import org.graphstream.graph.Graph;
    import org.graphstream.graph.Node;
    import org.graphstream.graph.implementations.MultiGraph;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;


/**
 * La classe {@code Aeroport} représente un aéroport avec des informations telles que le code,
 * le lieu, la latitude et la longitude. Elle permet également de créer un graphe des aéroports.
 * <p>
 * Cette classe utilise la bibliothèque GraphStream pour créer et afficher un graphe des aéroports.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 * Scanner scan = new Scanner(new File("aeroport.txt"));
 * Aeroport aeroport = new Aeroport(scan);
 * }
 * </pre>
 * </p>
 * <p>
 * Le fichier d'entrée doit avoir le format suivant :
 * <ul>
 * <li>Chaque ligne contient les informations d'un aéroport séparées par des points-virgules.</li>
 * </ul>
 * </p>
 * <p>
 * Les informations comprennent le code de l'aéroport, le lieu, la latitude (degrés, minutes, secondes),
 * la direction (N/S/E/O) et la longitude (degrés, minutes, secondes), et la direction (N/S/E/O).
 * </p>
 * 
 */
public class Aeroport {
        private String codeAero;
        private String lieu;
        private double latitude;
        private double longitude;
        private double x;
        private double y;
        
    /**
     * Constructeur de la classe {@code Aeroport}.
     * <p>
     * Initialise un objet {@code Aeroport} en lisant les informations depuis un {@code Scanner}.
     * </p>
     * 
     * @param scan le scanner à partir duquel les informations de l'aéroport sont lues
     * @throws FileNotFoundException si le fichier est introuvable
     */
    public Aeroport(Scanner scan) throws FileNotFoundException{
                String[] parts = scan.nextLine().split(";");
                if (parts.length == 10) {
                    this.codeAero = parts[0];
                    this.lieu = parts[1];

                    double latDeg = Float.parseFloat(parts[2]);
                    double latMin = Float.parseFloat(parts[3]);
                    double latSec = Float.parseFloat(parts[4]);
                    this.latitude = latDeg + latMin / 60 + latSec / 3600;
                    if (parts[5].equals("S")||parts[5].equals("O")) {
                        this.latitude *= -1;
                    }

                    double lonDeg = Float.parseFloat(parts[6]);
                    double lonMin = Float.parseFloat(parts[7]);
                    double lonSec = Float.parseFloat(parts[8]);
                    this.longitude = lonDeg + lonMin / 60 + lonSec / 3600;
                    if (parts[9].equals("S") || parts[9].equals("O")) {
                        this.longitude *= -1;
                    }

                    double R = 6371;
                    this.x = Math.round((R * Math.cos(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(this.longitude)))*1000000.0)/1000000.0;
                    this.y = Math.round((R * Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(this.longitude)))*100000.0)/100000.0;
                }
        }

    /**
     * Crée un graphe des aéroports.
     * <p>
     * La méthode lit une liste d'objets {@code Aeroport} pour créer un graphe où chaque aéroport est représenté par un nœud.
     * </p>
     * 
     * @param port la liste des aéroports
     * @return un objet {@code Graph} représentant le graphe des aéroports
     */
    public static void setAeroport(JXMapViewer mapViewer, List<Aeroport> ports) {
    WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
    Set<Waypoint> waypoints = new HashSet<>();
    
    // Iterate through each airport and add it as a waypoint
    for (Aeroport aeroport : ports) {
        GeoPosition position = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
        DefaultWaypoint waypoint = new DefaultWaypoint(position);
        waypoints.add(waypoint); // Add each waypoint to the set
    }
    
    // Set the waypoints to the waypoint painter after all airports are added
    waypointPainter.setWaypoints(waypoints);
    
    // Set the overlay painter of the mapViewer
    mapViewer.setOverlayPainter(waypointPainter);
}


    
    public static Graph volParHeure(Graph g,int heure){
        for(Node n:g){
            for(Edge e:n){
                
            }
        }
        return null;
    }

    /**
     * Retourne le code de l'aéroport.
     * 
     * @return le code de l'aéroport
     */
    public String getCodeAero() {
            return codeAero;
        }

    /**
     * Retourne le lieu de l'aéroport.
     * 
     * @return le lieu de l'aéroport
     */
    public String getLieu() {
            return lieu;
        }

    /**
     * Retourne la latitude de l'aéroport.
     * 
     * @return la latitude de l'aéroport
     */
    public double getLatitude() {
            return latitude;
        }

    /**
     * Retourne la longitude de l'aéroport.
     * 
     * @return la longitude de l'aéroport
     */
    public double getLongitude() {
            return longitude;
        }

    /**
     * Retourne la coordonnée X de l'aéroport.
     * 
     * @return la coordonnée X de l'aéroport
     */
    public double getX() {
            return x;
        }

    /**
     * Retourne la coordonnée Y de l'aéroport.
     * 
     * @return la coordonnée Y de l'aéroport
     */
    public double getY() {
            return y;
        }

    /**
     * Retourne une représentation sous forme de chaîne de l'objet {@code Aeroport}.
     * 
     * @return une chaîne représentant l'objet {@code Aeroport}
     */
    @Override
    public String toString(){
        return this.codeAero+";"+this.lieu+"; "+this.latitude+" ; "+this.longitude+" ; "+this.x+" ; "+this.y;
        }
}
