package modele;
import construction.MyWaypoint;
import construction.WaypointRender;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
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
    public Aeroport(Scanner scan) throws FileNotFoundException {
        String[] parts = scan.nextLine().split(";");
        if (parts.length == 10) {
            this.codeAero = parts[0];
            this.lieu = parts[1];

            double latDeg = Float.parseFloat(parts[2]);
            double latMin = Float.parseFloat(parts[3]);
            double latSec = Float.parseFloat(parts[4]);
            this.latitude = latDeg + latMin / 60 + latSec / 3600;
            if (parts[5].equals("S") || parts[5].equals("O")) {
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
            this.x = Math.round((R * Math.cos(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(this.longitude))) * 1000000.0) / 1000000.0;
            this.y = Math.round((R * Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(this.longitude))) * 100000.0) / 100000.0;
        }
    }

    /**
     * Crée un graphe des aéroports.
     * <p>
     * La méthode lit une liste d'objets {@code Aeroport} pour créer un graphe où chaque aéroport est représenté par un nœud.
     * </p>
     * 
     * @param port la liste des aéroports
     * @return un objet {@code WaypointPainter<MyWaypoint>} représentant le graphe des aéroports
     */
    public static void setAeroport(JXMapViewer mapViewer,Set<MyWaypoint> waypoints ,WaypointPainter<MyWaypoint> waypointRenderer,List<Aeroport> ports) {

        // Iterate through each airport and add it as a waypoint
        for (Aeroport aeroport : ports) {
            GeoPosition position = new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude());
            MyWaypoint waypoint = new MyWaypoint(aeroport.getCodeAero(), position);
            waypoints.add(waypoint);

            mapViewer.add(waypoint.getButton());
        }
        waypointRenderer.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointRenderer);
    }

    // Getters for the Aeroport attributes
    public String getCodeAero() {
        return codeAero;
    }

    public String getLieu() {
        return lieu;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
