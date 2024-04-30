/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.io.FileNotFoundException;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.Scanner;


public class Aeroport {
    private String codeAero;
    private String lieu;
    private float latitude;
    private float longitude;
    private double x;
    private double y;
    
    public Aeroport(Scanner scan) throws FileNotFoundException{
            String[] parts =  scan.nextLine().split(";");
            if (parts.length == 10) { 
                this.codeAero = parts[0];
                this.lieu=parts[1];
                this.latitude=0;
                this.longitude=0;
                if(parts[5].equals("N") ||parts[5].equals("E")){
                    this.latitude=1*(Float.parseFloat(parts[2])+Float.parseFloat(parts[3])/60+Float.parseFloat(parts[4])/3600);
                }else{
                    this.latitude=-1*(Float.parseFloat(parts[2])+Float.parseFloat(parts[3])/60+Float.parseFloat(parts[4])/3600);
                }
                if(parts[9].equals("N") ||parts[9].equals("E")){
                    this.longitude=1*(Float.parseFloat(parts[6])+(Float.parseFloat(parts[7])/60)+Float.parseFloat(parts[8])/3600);
                }else{
                    this.longitude=-1*(Float.parseFloat(parts[6])+(Float.parseFloat(parts[7])/60)+Float.parseFloat(parts[8])/3600);
                }
                this.x = (6371*cos(Math.toRadians(this.latitude))*sin(Math.toRadians(this.longitude)));
                this.y = (6371*cos(Math.toRadians(this.latitude))*cos(Math.toRadians(this.longitude)));
            }
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
