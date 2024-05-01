/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


public class Aeroport {
    private String codeAero;
    private String lieu;
    private float latitude;
    private float longitude;
    private double x;
    private double y;
    
    public Aeroport(Scanner scan) throws FileNotFoundException{
            String[] parts = scan.nextLine().split(";");
            if (parts.length == 10) {
                this.codeAero = parts[0];
                this.lieu = parts[1];

                float latDeg = Float.parseFloat(parts[2]);
                float latMin = Float.parseFloat(parts[3]);
                float latSec = Float.parseFloat(parts[4]);
                this.latitude = latDeg + latMin / 60 + latSec / 3600;
                if (parts[5].equals("S")||parts[5].equals("O")) {
                    this.latitude *= -1;
                }

                float lonDeg = Float.parseFloat(parts[6]);
                float lonMin = Float.parseFloat(parts[7]);
                float lonSec = Float.parseFloat(parts[8]);
                this.longitude = lonDeg + lonMin / 60 + lonSec / 3600;
                if (parts[9].equals("S") || parts[9].equals("O")) {
                    this.longitude *= -1;
                }

                double R = 6371;
                this.x = R * Math.cos(Math.toRadians(this.latitude)) * Math.sin(Math.toRadians(this.longitude));
                this.y = R * Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(this.longitude));
            }
    }
    
    public static Graph setAeroport(List<Aeroport> port){
        Graph g = new SingleGraph("Aerien france");
        for(Aeroport a:port){
            Node n=g.addNode(a.getCodeAero());
            n.setAttribute("x", a.getX());
            n.setAttribute("y", -a.getY());
            n.setAttribute("label", a.getCodeAero());
            n.setAttribute("ui-label", a.getCodeAero());
        }
        return g;
    }

    public String getCodeAero() {
        return codeAero;
    }

    public String getLieu() {
        return lieu;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    
    @Override
    public String toString(){
        return this.codeAero+";"+this.lieu+"; "+this.latitude+" ; "+this.longitude+" ; "+this.x+" ; "+this.y;
    }
}
