/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Scanner;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Toolkit;
import static org.graphstream.algorithm.Toolkit.diameter;
import org.graphstream.graph.implementations.MultiGraph;



public class Vols {
    private String codeVol;
    private String depart;
    private String arrive;
    private int heure;
    private int min;
    private int duree;
    private Aeroport departaero;
    private Aeroport arriveaero;
    
    public Vols(Scanner scan){
        String[] parts =  scan.nextLine().split(";");
        if (parts.length == 6) {
        this.codeVol=parts[0];
        this.depart=parts[1];
        this.arrive=parts[2];
        this.heure=Integer.parseInt(parts[3]);
        this.min=Integer.parseInt(parts[4]);
        this.duree=Integer.parseInt(parts[5]);
        }
    }

    public String getCodeVol() {
        return codeVol;
    }

    public String getDepart() {
        return depart;
    }

    public String getArrive() {
        return arrive;
    }

    public int getHeure() {
        return heure;
    }

    public int getMin() {
        return min;
    }

    public int getDuree() {
        return duree;
    }

    public void setDepartaero(Aeroport departaero) {
        this.departaero = departaero;
    }

    public void setArriveaero(Aeroport arriveaero) {
        this.arriveaero = arriveaero;
    }
    
    public static void helpVol(List<Vols> vols,List<Aeroport> port,Graph g){
        int taille=vols.size();
        int cpt=0;
        g.setStrict(false);
        for(int i=0;i<taille;i++){
            for(int j=i+1;j<taille;j++){
                if(checkCollision(vols.get(i),vols.get(j),port)){
                    g.addEdge(vols.get(i).codeVol+" - "+cpt, vols.get(i).depart, vols.get(i).arrive);
                    g.addEdge(vols.get(j).codeVol+" - "+cpt, vols.get(j).depart, vols.get(j).arrive);
                    cpt++;
                }
            }
        }
        System.out.println("nbNoeuds :"+g.getNodeCount());
        System.out.println("nbAretes :"+g.getEdgeCount()/2);
        Viewer viewer = g.display();
        viewer.disableAutoLayout();
    }
    
    public static void setVols(List<Vols> vols, List<Aeroport> port){
        Graph g=new MultiGraph("Vols");
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
            n.setAttribute("label",v.depart + "|"+v.arrive);
            n.setAttribute("ui-label", v.depart + "|"+v.arrive);
        }
        for(int i=0;i<taille;i++){
            for(int j=i+1;j<taille;j++){
                if(checkCollision(vols.get(i),vols.get(j),port)){
                    Edge e=g.addEdge(vols.get(i).getCodeVol()+" - "+ vols.get(j).getCodeVol(), vols.get(i).getCodeVol(), vols.get(j).getCodeVol());
                    e.setAttribute("label",Integer.toString(cpt));
                    e.setAttribute("ui-label", Integer.toString(cpt));
                    System.out.println(cpt);
                    cpt++;
                    System.out.println("----------------------------------------------------");
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
            int departureTime1=v1.getHeure() * 60+ v1.getMin();
            int departureTime2=v2.getHeure()* 60+ v2.getMin();
            int arrivalTime1=departureTime1+v1.getDuree();
            int arrivalTime2=departureTime2+v2.getDuree();
            if(((arrivalTime1 >= departureTime2 && arrivalTime1 <= arrivalTime2) || 
            (arrivalTime2 >= departureTime1 && arrivalTime2 <= arrivalTime1))){
                return true;
                
            }
                System.out.println("----------------------------------------------------");
                System.out.println("colinaries :"+v1.depart+" -- "+(v1.getHeure() +" : "+ v1.getMin())+"\n"+v2.depart+" -- "+(v2.getHeure()+" : "+ v2.getMin()));
                System.out.println("colinaries :"+v1.arrive+" -- "+ v1.getDuree()+"\n"+v2.arrive+" -- "+v2.getDuree());
                System.out.println("----------------------------------------------------");
            return false;
        }
        double distanceVol1 = Point.distance(v1.departaero.getX(), v1.departaero.getY(), v1.arriveaero.getX(), v1.arriveaero.getY());
        double distanceVol2 = Point.distance(v2.departaero.getX(), v2.departaero.getY(), v2.arriveaero.getX(), v2.arriveaero.getY());
        double timeVol1 =   (v1.getHeure() * 60 + v1.getMin()) + (Point.distance(v1.departaero.getX(), v1.departaero.getY(), inter.x, inter.y) / distanceVol1 * v1.getDuree());
        double timeVol2 = (v2.getHeure() * 60 + v2.getMin()) + (Point.distance(v2.departaero.getX(), v2.departaero.getY(), inter.x, inter.y) / distanceVol2 * v2.getDuree());
        
            System.out.println("----------------------------------------------------");
            System.out.println(v1.departaero.getX()+" ; "+v1.departaero.getY()+"\n"+v1.arriveaero.getX()+" ; "+v1.arriveaero.getY()+"\n"+v2.departaero.getX()+" ; "+v2.departaero.getY()+"\n"+v2.arriveaero.getX()+" ; "+v2.arriveaero.getY()+"\n"+inter + " " +v1.getDepart()+"|"+v1.getArrive()+ " et "+v2.getDepart()+"|"+v2.getArrive()+" \n" +timeVol1+" et  "+timeVol2+" = "+Math.abs(timeVol1-timeVol2));
        
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
        
        if (u >= 0 && u <= 1 && v >= 0 && v <= 1) {
            double x = x1 + u * ABx;
            double y = y1 + u * ABy;
            return new Point2D.Double(x, y);
        }else{
            return null;
        }
    }

    
    @Override
    public String toString(){
        return this.codeVol+" ; "+this.depart+" ; "+this.arrive+" ; "+this.heure+" ; "+this.min+" ; "+this.duree;
    }
}
