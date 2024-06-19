package construction;

import modele.Aeroport;
import modele.Vol;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

/**
 * La classe {@code AlgorithmIntersection} gère les interactions et les collisions entre les vols dans un graphe.
 * Elle permet la détection et la visualisation des collisions entre les vols et crée des graphes de ces interactions.
 */
public class AlgorithmIntersection {
    private static int MARGE=15;
    
     /**
     * Définit la marge utilisée pour la vérification des collisions entre vols.
     *
     * @param nb la valeur de la marge
     */
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
     * @param ports la liste des aéroports
     * @param g le graphe dans lequel ajouter les arêtes
     * @return le graphe mis à jour avec les arêtes des vols en collision
     */
    public static Graph setVolsAeroport(List<Vol> vols,List<Aeroport> port,Graph g){
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
        return g;
    }


    /**
     * Crée et affiche un graphe des collisions entre les vols.
     *
     * @param vols la liste des vols
     * @param ports la liste des aéroports
     * @return le graphe des collisions entre les vols
     */
    public static Graph setVolsCollision(List<Vol> vols, List<Aeroport> ports) {
        Graph g = new DefaultGraph("Vols");
        g.setStrict(false);
        collision(vols, ports, g);
        return g;
    }
    
    static void collision(List<Vol> vols, List<Aeroport> ports, Graph g) {
        int taille = vols.size();
        int cpt = 1;
        for (Vol v : vols) {
            Node n = g.addNode(v.getCodeVol());
            n.setAttribute("label", v.getDepart() + "|" + v.getArrive());
            n.setAttribute("ui-label", v.getDepart() + "|" + v.getArrive());
        }
        for (int i = 0; i < taille; i++) {
            for (int j = i + 1; j < taille; j++) {
                if (checkCollision(vols.get(i), vols.get(j), ports)) {
                    Edge e = g.addEdge(vols.get(i).getCodeVol() + " - " + vols.get(j).getCodeVol(), vols.get(i).getCodeVol(), vols.get(j).getCodeVol());
                    cpt++;
                }
            }
        }
    }

    private static boolean checkCollision(Vol v1, Vol v2, List<Aeroport> ports) {
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

    private static Point2D.Double intersection(Vol v1, Vol v2, List<Aeroport> ports) {
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
     /**
     * Sélectionne les vols à afficher dans le graphe selon l'heure spécifiée.
     *
     * @param g le graphe des vols
     * @param heure l'heure à laquelle les vols doivent être sélectionnés
     * @param vol la liste des vols
     */
    public static List<Vol> volParHeure(int heure,List<Vol> vol){
        for(Vol v:vol){
            if(v.getHeure()!=heure){
                vol.remove(v);
            }
        }
        return vol;
    }
    
     /**
     * Sélectionne les vols à afficher dans le graphe selon l'aéroport spécifié.
     *
     * @param aeroport le code de l'aéroport à sélectionner
     * @param vol la liste des vols
     * @param g le graphe des vols
     */
    public static List<Vol> selectAeroport(String aeroport, List<Vol> vol) {
        List<Vol> result = new ArrayList<>();
        for (Vol v : vol) {
            if (v.getDepart().equals(aeroport)) {
                result.add(v);
            }
        }
        return result;
    }
     /**
     * Sélectionne les vols à afficher dans le graphe selon le niveau de couleur spécifié.
     *
     * @param level le niveau de couleur à sélectionner
     * @param vol la liste des vols
     * @param g le graphe des vols
     */
    public static List<Vol> selectLevel(int level, List<Vol> vol, Graph g) {
        List<Vol> result = new ArrayList<>();
        for (Node n : g) {
            if ((int) n.getNumber("color") == level) {
                for (Vol v : vol) {
                    if (n.equals(g.getNode(v.getCodeVol()))) {
                        result.add(v);
                        break;
                    }
                }
            }
        }
        return result;
    }
}