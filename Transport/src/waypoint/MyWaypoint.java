package waypoint;

import bouton.ButtonWaypoint;
import construction.AlgorithmIntersection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import modele.Vol;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import vue.FenetreCarte;

public class MyWaypoint extends DefaultWaypoint {
    private String name;
    private JButton button;

    public MyWaypoint(String name, GeoPosition coord) {
        super(coord);
        this.name = name;
        initButton();
    }
    
    private void initButton() {
        button = new ButtonWaypoint();
        button.setToolTipText(name);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(FenetreCarte.vols!=null){
                    List<Vol> volsfiltre=AlgorithmIntersection.selectAeroport(name,FenetreCarte.vols);
                    FenetreCarte.chargeVol(volsfiltre);
                    
                }
            }
            
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }
}
