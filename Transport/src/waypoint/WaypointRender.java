package waypoint;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JButton;
import modele.Aeroport;
import modele.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * La classe {@code WaypointRender} est un peintre de waypoints personnalisé pour le composant JXMapViewer.
 * Elle étend {@link WaypointPainter} et gère l'affichage des waypoints avec des boutons associés.
 */
public class WaypointRender extends WaypointPainter<MyWaypoint> {

    /**
     * Effectue le rendu des waypoints sur le composant JXMapViewer.
     *
     * @param g le contexte graphique pour le rendu
     * @param map le composant JXMapViewer sur lequel effectuer le rendu
     * @param width la largeur du composant
     * @param height la hauteur du composant
     */
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        for (MyWaypoint wp : getWaypoints()) {
            Point2D p = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
            Rectangle rec = map.getViewportBounds();
            int x = (int) (p.getX() - rec.getX());
            int y = (int) (p.getY() - rec.getY());
            JButton cmd = wp.getButton();
            cmd.setLocation(x - cmd.getWidth() / 2, y - cmd.getHeight());
        }
    }
    
    public Painter<JXMapViewer> paintVol(List<Vol> vol, JXMapViewer mapViewer, List<Aeroport> aero) {
        Painter<JXMapViewer> lineOverlay = (Graphics2D g, final JXMapViewer map, final int w, final int h) -> {
            g = (Graphics2D) g.create();
            Rectangle rect = map.getViewportBounds();
            g.translate(-rect.x, -rect.y);

            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(2));
            for (Vol v : vol) {
                for (Aeroport a : aero) {
                    if (v.getArrive().equals(a.getCodeAero())) {
                        v.setArriveaero(a);
                    } else if (v.getDepart().equals(a.getCodeAero())) {
                        v.setDepartaero(a);
                    }
                }
            }
            for (Vol v : vol) {
                Point2D p1 = map.getTileFactory().geoToPixel(new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude()), map.getZoom());
                Point2D p2 = map.getTileFactory().geoToPixel(new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude()), map.getZoom());
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            }

            g.dispose();
        };
        // Add the line overlay painter to the mapViewer
        return lineOverlay;
    }
}
