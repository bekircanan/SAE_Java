package application;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class MyWaypoint extends DefaultWaypoint {
    private String codeAero;

    public MyWaypoint(GeoPosition coord, String codeAero) {
        super(coord);
        this.codeAero = codeAero;
    }

    public String getCodeAero() {
        return codeAero;
    }
}
