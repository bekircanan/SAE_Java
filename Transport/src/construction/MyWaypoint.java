package construction;

import modele.Aeroport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class MyWaypoint extends DefaultWaypoint {
    private String name;
    private JButton button;
    private Aeroport aeroport; // New field to store the Aeroport object

    public MyWaypoint(String name, EventWaypoint event, GeoPosition coord) {
        super(coord);
        this.name = name;
        initButton(event);
    }

    public MyWaypoint(Aeroport aeroport, EventWaypoint event) {
        super(new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude()));
        this.aeroport = aeroport;
        this.name = aeroport.getCodeAero();
        initButton(event);
    }

    public MyWaypoint() {
    }

    private void initButton(EventWaypoint event) {
        button = new JButton(name);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.selected(MyWaypoint.this);
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

    public Aeroport getAeroport() {
        return aeroport;
    }
}
