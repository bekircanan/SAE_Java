package bouton;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Une classe personnalisée JButton pour afficher une icône de Waypoint.
 * <p>
 * Cette classe étend {@link JButton} et la personnalise pour afficher une icône de Waypoint
 * avec des propriétés spécifiques telles que l'absence de zone de contenu, de bordure ou de peinture de focus.
 * Elle définit également un curseur en forme de main et une taille fixe de 24x24 pixels.
 * </p>
 * <p>
 * Exemple d'utilisation :
 * 
 * {@code
 * ButtonWaypoint bouton = new ButtonWaypoint();
 * }
 * 
 * </p>
 * 
 */
public class ButtonWaypoint extends JButton{

    /**
     * Construit un nouveau ButtonWaypoint avec une icône de Waypoint.
     * <p>
     * Le bouton n'a pas de zone de contenu, pas de bordure et pas de peinture de focus.
     * Il utilise un curseur en forme de main et a une taille fixe de 24x24 pixels.
     * L'icône utilisée est située à "/icon/pin.png".
     * </p>
     */
    public ButtonWaypoint() {
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setBorder(null);
        this.setIcon(new ImageIcon(getClass().getResource("/icon/pin.png")));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setSize(new Dimension(24,24));
    }
    
}
