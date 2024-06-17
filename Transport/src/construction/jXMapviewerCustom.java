package construction;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.List;
import modele.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointRenderer;
import org.jxmapviewer.painter.Painter;

/**
 * Custom JXMapViewer class to draw flights between airports.
 */
public class jXMapviewerCustom extends JXMapViewer {
    private List<Vol> vol;

    public jXMapviewerCustom(List<Vol> vol) {
        this.vol = vol;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle rect = this.getViewportBounds();
        g.translate(-rect.x, -rect.y);  // Adjust for the viewport

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Vol v : vol) {
            GeoPosition departure = new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude());
            GeoPosition arrival = new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude());

            // Convert GeoPosition to pixel coordinates
            Point depPoint = (Point) getTileFactory().geoToPixel(departure, getZoom());
            Point arrPoint = (Point) getTileFactory().geoToPixel(arrival, getZoom());

            // Draw the line
            g2d.drawLine(depPoint.x, depPoint.y, arrPoint.x, arrPoint.y);
        }

        g2d.dispose();
    }
}
