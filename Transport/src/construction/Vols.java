/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author bekir
 */
public class Vols {
    private String codeVol;
    private String depart;
    private String arrive;
    private int heure;
    private int min;
    private int duree;
    
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
    
    
    
    public static Point2D.Double intersectent(Vols v1,Vols v2,List<Aeroport> port){
        Point2D.Double pointA = null, pointB = null, pointC = null, pointD = null;
        for (Aeroport a : port) {
            if (pointA != null && pointB != null && pointC != null && pointD != null) {
                break;
            }
            if (v1.getDepart().equals(a.getCodeAero())) {
                pointA = new Point2D.Double(a.getX(), a.getY());
            } else if (v1.getArrive().equals(a.getCodeAero())) {
                pointB = new Point2D.Double(a.getX(), a.getY());
            }
            if (v2.getDepart().equals(a.getCodeAero())) {
                pointC = new Point2D.Double(a.getX(), a.getY());
            } else if (v2.getArrive().equals(a.getCodeAero())) {
                pointD = new Point2D.Double(a.getX(), a.getY());
            }
        }
        if (pointA == null || pointB == null || pointC == null || pointD == null) {
            return null;
        }
        double x1 = pointA.x, y1 = pointA.y;
        double x2 = pointB.x, y2 = pointB.y;
        double x3 = pointC.x, y3 = pointC.y;
        double x4 = pointD.x, y4 = pointD.y;
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0) {
            return null;
        }
      
        double x = ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator;
        double y = ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator;
        return new Point2D.Double(x,y);
    }
    
    @Override
    public String toString(){
        return this.codeVol+" ; "+this.depart+" ; "+this.arrive+" ; "+this.heure+" ; "+this.min+" ; "+this.duree;
    }
}
