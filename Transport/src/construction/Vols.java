/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;
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
    private Aeroport departaero;
    private Aeroport arriveaero;
    
    /**
     *
     * @param scan
     */
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

    /**
     *
     * @return
     */
    public String getCodeVol() {
        return codeVol;
    }

    /**
     *
     * @return
     */
    public String getDepart() {
        return depart;
    }

    /**
     *
     * @return
     */
    public String getArrive() {
        return arrive;
    }

    /**
     *
     * @return
     */
    public int getHeure() {
        return heure;
    }

    /**
     *
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return
     */
    public int getDuree() {
        return duree;
    }

    /**
     *
     * @return
     */
    public Aeroport getDepartaero() {
        return departaero;
    }

    /**
     *
     * @return
     */
    public Aeroport getArriveaero() {
        return arriveaero;
    }

    /**
     *
     * @param departaero
     */
    public void setDepartaero(Aeroport departaero) {
        this.departaero = departaero;
    }

    /**
     *
     * @param arriveaero
     */
    public void setArriveaero(Aeroport arriveaero) {
        this.arriveaero = arriveaero;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return this.codeVol+" ; "+this.depart+" ; "+this.arrive+" ; "+this.heure+" ; "+this.min+" ; "+this.duree;
    }
}
