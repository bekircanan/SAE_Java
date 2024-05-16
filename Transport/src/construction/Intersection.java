/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import org.graphstream.algorithm.ConnectedComponents;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.swingViewer.Viewer;

/**
 *
 * @author bekir
 */
public class Intersection {
    
    /**
     *
     * @param vols
     * @param port
     * @param g
     */
    public static void setVolsAeroport(List<Vols> vols,List<Aeroport> port,Graph g){
        int cpt=0;
        /*for(Vols v:vols){
            String edgeId=v.getDepart()+"|"+v.getArrive();
            if(g.getEdge(edgeId)==null){
                g.addEdge(edgeId, v.getDepart(), v.getArrive());
            }
            cpt++;
        }*/
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
        System.out.println("nbAretes :"+/*g.getEdgeCount()*/cpt);
        Viewer viewer = g.display();
        viewer.disableAutoLayout();
    }
    
    /**
     *
     * @param vols
     * @param port
     */
    public static void setVolsCollision(List<Vols> vols, List<Aeroport> port){
        Graph g=new DefaultGraph("Vols");
        g.setStrict(false);
        collision(vols,port,g);
        System.out.println("nbNoeuds :"+g.getNodeCount());
        System.out.println("nbAretes :"+g.getEdgeCount());
        System.out.println("degre Moyen : "+(double)(g.getEdgeCount()*2)/g.getNodeCount());
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(g);
        System.out.println("nb Composantes : "+cc.getConnectedComponentsCount());
        System.out.println("diametre : "+ diameter(g));
        g.display();
    }
    
    private static void collision(List<Vols> vols,List<Aeroport> port,Graph g){
        int taille=vols.size();
        int cpt=1;
        for(Vols v:vols){
            Node n =g.addNode(v.getCodeVol());
            n.setAttribute("label",v.getDepart() + "|"+v.getArrive());
            n.setAttribute("ui-label", v.getDepart() + "|"+v.getArrive());
        }
        for(int i=0;i<taille;i++){
            for(int j=i+1;j<taille;j++){
                if(checkCollision(vols.get(i),vols.get(j),port)){
                    Edge e=g.addEdge(vols.get(i).getCodeVol()+" - "+ vols.get(j).getCodeVol(), vols.get(i).getCodeVol(), vols.get(j).getCodeVol());
                    e.setAttribute("label",Integer.toString(cpt));
                    e.setAttribute("ui-label", Integer.toString(cpt));
                    cpt++;
                }
            }
        }
    }
    
    private static boolean checkCollision(Vols v1, Vols v2, List<Aeroport> port) {
        Point2D.Double inter = intersection(v1, v2, port);
        if (inter == null) {
            return false;
        }
        if(inter.x==0 && inter.y==0){
            double departureTime1=v1.getHeure() * 60+ v1.getMin();
            double departureTime2=v2.getHeure()* 60+ v2.getMin();
            double arrivalTime1=departureTime1+v1.getDuree();
            double arrivalTime2=departureTime2+v2.getDuree();
            return ((arrivalTime1 >= departureTime2 && arrivalTime1 <= arrivalTime2) || 
                    (arrivalTime2 >= departureTime1 && arrivalTime2 <= arrivalTime1));
        }
        double timeVol1,timeVol2;
        double distanceVol1 = Point.distance(v1.getDepartaero().getX(), v1.getDepartaero().getY(), v1.getArriveaero().getX(), v1.getArriveaero().getY());
        double distanceVol2 = Point.distance(v2.getDepartaero().getX(), v2.getDepartaero().getY(), v2.getArriveaero().getX(), v2.getArriveaero().getY());
        timeVol1 = (v1.getHeure() * 60 + v1.getMin()) + (Point.distance(v1.getDepartaero().getX(), v1.getDepartaero().getY(), inter.x, inter.y) / distanceVol1 * v1.getDuree());
        timeVol2 = (v2.getHeure() * 60 + v2.getMin()) + (Point.distance(v2.getDepartaero().getX(), v2.getDepartaero().getY(), inter.x, inter.y) / distanceVol2 * v2.getDuree());
        
        return Math.abs(timeVol1-timeVol2) < 15;
    }
    
    private static Point2D.Double intersection(Vols v1,Vols v2,List<Aeroport> port){
        Point2D.Double pointA = null, pointB = null, pointC = null, pointD = null;
        for (Aeroport a : port) {
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
            if((ABy/ABx)==(CDy/CDx)){
                return new Point2D.Double(0,0);
            }
            return null;
        }
        double u = numerA / denominator;
        double v = numerB / denominator;
        
        
        if (u >= 0 && u <= 1+0.00001 && v >= 0 && v <= 1+0.00001) {
            double x = Math.round((x1 + u * ABx)*1000000.0)/1000000.0;
            double y = Math.round((y1 + u * ABy)*100000.0)/100000.0;
            
            return new Point2D.Double(x, y);
        }else{
            return null;
        }
    }
}
