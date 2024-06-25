package waypoint;

import bouton.ButtonWaypoint;
import construction.FiltreAeroportVol;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import modele.Vol;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import vue.FenetreCarte;

/**
 * Classe de point de chemin personnalisé qui étend DefaultWaypoint pour inclure un bouton et une fonctionnalité supplémentaire.
 */
public class MyWaypoint extends DefaultWaypoint {
    private String name;
    private JButton button;
    
    /**
     * Construit un objet MyWaypoint avec un nom et une position géographique.
     * 
     * @param name Le nom du Waypoint.
     * @param coord La position géographique du Waypoint.
     */
    public MyWaypoint(String name, GeoPosition coord) {
        super(coord);
        this.name = name;
        initButton();
    }
    
    /**
     * Initialise le bouton associé au Waypoint.
     * Le bouton a une infobulle avec le nom du Waypoint et un écouteur d'action
     * qui filtre les vols en fonction du nom de l'aéroport et met à jour l'affichage de la carte.
     */    
    private void initButton() {
        button = new ButtonWaypoint();
        button.setToolTipText(name);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(FenetreCarte.getVols()!=null){
                    List<Vol> volsfiltre=FiltreAeroportVol.selectAeroport(name,FenetreCarte.getVols());
                    FenetreCarte.chargeVol(volsfiltre,null,15,0);
                    
                }
            }
            
        });
    }

    /**
     * Obtient le nom du Waypoint.
     * 
     * @return Le nom du Waypoint.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Définit le nom du Waypoint.
     * 
     * @param name Le nouveau nom du Waypoint.
     */
    public void setName(String name) {
        this.name = name;
    }
/**
     * Obtient le bouton associé au Waypoint.
     * 
     * @return Le bouton associé au Waypoint.
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Définit le bouton associé au Waypoint.
     * 
     * @param button Le nouveau bouton à associer au Waypoint.
     */
    public void setButton(JButton button) {
        this.button = button;
    }
}