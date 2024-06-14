package construction;

import construction.Aeroport;
import construction.Vols;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * The {@code Intersection} class manages interactions and collisions between flights in a graph.
 * This class allows detection and visualization of collisions between flights and creates graphs of these interactions.
 */
public class Intersection {
    private static int MARGE=15;
    public void setMarge(int nb){
        MARGE=nb;
    }
    /**
     * Associe les vols aux aéroports et ajoute les arêtes correspondantes au graphe.
     * <p>
     * Crée des arêtes dans le graphe pour les vols en collision.
     * </p>
     * 
     * @param vols la liste des vols
     * @param port la liste des aéroports
     * @param g le graphe dans lequel ajouter les arêtes
     * @return 
     */
    public static Graph setVolsAeroport(List<Vols> vols,List<Aeroport> port,Graph g){
        int cpt=0;
        int taille=vols.size();
        g.setStrict(false);
        for(int i=0;i<taille;i++){
            for(int j=i+1;j<taille;j++){
                if(checkCollision(vols.get(i),vols.get(j),port)){
                    g.addEdge(vols.get(i).getCodeVol()+" - "+cpt, vols.get(i).getDepart(), vols.get(i).getArrive());
                    g.addEdge(vols.get(j).getCodeVol()+" - "+cpt, vols.get(j).getDepart(), vols.get(j).getArrive());
                    cpt++;
                }
            }
        }
       
        System.out.println("nbNoeuds :"+g.getNodeCount());
        System.out.println("nbAretes :"+cpt);
        
         
        return g;
    }


    /**
     * Creates and displays a graph of collisions between flights.
     *
     * @param vols  the list of flights
     * @param ports the list of airports
     * @return the graph of collisions
     */
    public static Graph setVolsCollision(List<Vols> vols, List<Aeroport> ports) {
        Graph g = new DefaultGraph("Vols");
        g.setStrict(false);
        collision(vols, ports, g);
        System.out.println("nbNoeuds :" + g.getNodeCount());
        System.out.println("nbAretes :" + g.getEdgeCount());
        System.out.println("degre Moyen : " + (double) (g.getEdgeCount() * 2) / g.getNodeCount());
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(g);
        System.out.println("nb Composantes : " + cc.getConnectedComponentsCount());
        System.out.println("diametre : " + diameter(g));
        return g;
    }

    private static void collision(List<Vols> vols, List<Aeroport> ports, Graph g) {
        int taille = vols.size();
        int cpt = 1;
        for (Vols v : vols) {
            Node n = g.addNode(v.getCodeVol());
            n.setAttribute("label", v.getDepart() + "|" + v.getArrive());
            n.setAttribute("ui-label", v.getDepart() + "|" + v.getArrive());
        }
        for (int i = 0; i < taille; i++) {
            for (int j = i + 1; j < taille; j++) {
                if (checkCollision(vols.get(i), vols.get(j), ports)) {
                    Edge e = g.addEdge(vols.get(i).getCodeVol() + " - " + vols.get(j).getCodeVol(), vols.get(i).getCodeVol(), vols.get(j).getCodeVol());
                    e.setAttribute("label", Integer.toString(cpt));
                    e.setAttribute("ui-label", Integer.toString(cpt));
                    cpt++;
                }
            }
        }
    }

    private static boolean checkCollision(Vols v1, Vols v2, List<Aeroport> ports) {
        Point2D.Double inter = intersection(v1, v2, ports);
        if (inter == null) {
            return false;
        }
        if(inter.x==0 && inter.y==0){
            double departureTime1=v1.getHeure() * 60+ v1.getMin();
            double departureTime2=v2.getHeure()* 60+ v2.getMin();
            double arrivalTime1=departureTime1+v1.getDuree();
            double arrivalTime2=departureTime2+v2.getDuree();
            // A = D , B = C
            if(v1.getDepart().equals(v2.getArrive())&&v1.getArrive().equals(v2.getDepart())){
                return (arrivalTime1+MARGE >= departureTime2 &&arrivalTime2+MARGE  >= departureTime1/*(Math.abs(arrivalTime1-departureTime2)<MARGE)||(Math.abs(arrivalTime2-departureTime1)<MARGE)*/);
            }
            //A = C, B = D
            else if(v1.getDepart().equals(v2.getDepart())&&v1.getArrive().equals(v2.getArrive())){
                return Math.abs(arrivalTime2-arrivalTime1) < MARGE;
            }
        }
        double timeVol1, timeVol2;
        double distanceVol1 = Point.distance(v1.getDepartaero().getX(), v1.getDepartaero().getY(), v1.getArriveaero().getX(), v1.getArriveaero().getY());
        double distanceVol2 = Point.distance(v2.getDepartaero().getX(), v2.getDepartaero().getY(), v2.getArriveaero().getX(), v2.getArriveaero().getY());
        timeVol1 = (v1.getHeure() * 60 + v1.getMin()) + (Point.distance(v1.getDepartaero().getX(), v1.getDepartaero().getY(), inter.x, inter.y) / distanceVol1 * v1.getDuree());
        timeVol2 = (v2.getHeure() * 60 + v2.getMin()) + (Point.distance(v2.getDepartaero().getX(), v2.getDepartaero().getY(), inter.x, inter.y) / distanceVol2 * v2.getDuree());
        
        return Math.abs(timeVol1-timeVol2) < MARGE;
    }

    private static Point2D.Double intersection(Vols v1, Vols v2, List<Aeroport> ports) {
        Point2D.Double pointA = null, pointB = null, pointC = null, pointD = null;
        for (Aeroport a : ports) {
            if (v1.getDepart().equals(a.getCodeAero())) {
                pointA = new Point2D.Double(a.getX(), a.getY());
                v1.setDepartaero(a);
            } else if (v1.getArrive().equals(a.getCodeAero())) {
                pointB = new Point2D.Double(a.getX(), a.getY());
                v1.setArriveaero(a);
            }
            if (v2.getDepart().equals(a.getCodeAero())) {
                pointC = new Point2D.Double(a.getX(), a.getY());
                v2.setDepartaero(a);
            } else if (v2.getArrive().equals(a.getCodeAero())) {
                pointD = new Point2D.Double(a.getX(), a.getY());
                v2.setArriveaero(a);
            }
            if (pointA != null && pointB != null && pointC != null && pointD != null) {
                break;
            }
        }
        if (pointA == null || pointB == null || pointC == null || pointD == null) {
            return null;
        }
        double x1 = pointA.x, y1 = pointA.y;
        double x2 = pointB.x, y2 = pointB.y;
        double x3 = pointC.x, y3 = pointC.y;
        double x4 = pointD.x, y4 = pointD.y;

        double ABx = x2 - x1;
        double ABy = y2 - y1;
        double CDx = x4 - x3;
        double CDy = y4 - y3;

        double denominator = ABx * CDy - ABy * CDx;
        double numerA = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        double numerB = (x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3);

        if (denominator == 0) {
            if ((ABy / ABx) == (CDy / CDx)) {
                return new Point2D.Double(0, 0);
            }
            return null;
        }
        double u = numerA / denominator;
        double v = numerB / denominator;

        if (u >= 0 - 0 && u <= 1 + 0.00001 && v >= 0 && v <= 1 + 0.00001) {
            double x = x1 + u * ABx;
            double y = y1 + u * ABy;

            return new Point2D.Double(x, y);
        } else {
            return null;
        }
    }
}