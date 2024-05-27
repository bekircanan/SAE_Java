package construction;
import java.util.Scanner;

/**
 * La classe {@code Vols} représente un vol avec des informations telles que le code du vol,
 * l'aéroport de départ, l'aéroport d'arrivée, l'heure de départ, les minutes de départ et la durée du vol.
 * <p>
 * Exemple d'utilisation :
 * <pre>
 * {@code
 * Scanner scan = new Scanner(new File("vols.txt"));
 * Vols vol = new Vols(scan);
 * }
 * </pre>
 * </p>
 * <p>
 * Le fichier d'entrée doit avoir le format suivant :
 * <ul>
 * <li>Chaque ligne contient les informations d'un vol séparées par des points-virgules.</li>
 * </ul>
 * </p>
 * 
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
     * Constructeur de la classe {@code Vols}.
     * <p>
     * Initialise un objet {@code Vols} en lisant les informations depuis un {@code Scanner}.
     * </p>
     * 
     * @param scan le scanner à partir duquel les informations du vol sont lues
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
     * Retourne le code du vol.
     * 
     * @return le code du vol
     */
    public String getCodeVol() {
        return codeVol;
    }

    /**
     * Retourne l'aéroport de départ.
     * 
     * @return l'aéroport de départ
     */
    public String getDepart() {
        return depart;
    }

    /**
     * Retourne l'aéroport d'arrivée.
     * 
     * @return l'aéroport d'arrivée
     */
    public String getArrive() {
        return arrive;
    }

    /**
     * Retourne l'heure de départ.
     * 
     * @return l'heure de départ
     */
    public int getHeure() {
        return heure;
    }

    /**
     * Retourne les minutes de départ.
     * 
     * @return les minutes de départ
     */
    public int getMin() {
        return min;
    }

    /**
     * Retourne la durée du vol.
     * 
     * @return la durée du vol
     */
    public int getDuree() {
        return duree;
    }

     /**
     * Retourne l'aéroport de départ sous forme d'objet {@code Aeroport}.
     * 
     * @return l'aéroport de départ
     */
    public Aeroport getDepartaero() {
        return departaero;
    }

    /**
     * Retourne l'aéroport d'arrivée sous forme d'objet {@code Aeroport}.
     * 
     * @return l'aéroport d'arrivée
     */
    public Aeroport getArriveaero() {
        return arriveaero;
    }

    /**
     * Définit l'aéroport de départ.
     * 
     * @param departaero l'aéroport de départ à définir
     */
    public void setDepartaero(Aeroport departaero) {
        this.departaero = departaero;
    }

    /**
     * Définit l'aéroport d'arrivée.
     * 
     * @param arriveaero l'aéroport d'arrivée à définir
     */
    public void setArriveaero(Aeroport arriveaero) {
        this.arriveaero = arriveaero;
    }
    
    /**
    * Retourne une représentation sous forme de chaîne de l'objet {@code Vols}.
    * 
    * @return une chaîne représentant l'objet {@code Vols}
    */
    @Override
    public String toString(){
        return this.codeVol+" ; "+this.depart+" ; "+this.arrive+" ; "+this.heure+" ; "+this.min+" ; "+this.duree;
    }
}
