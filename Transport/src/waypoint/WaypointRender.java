package waypoint;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modele.Aeroport;
import modele.Vol;
import org.graphstream.graph.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import vue.FenetreCarte;

/**
 * La classe {@code WaypointRender} est un peintre de waypoints personnalisé pour le composant JXMapViewer.
 * Elle étend {@link WaypointPainter} et gère l'affichage des waypoints avec des boutons associés.
 */
public class WaypointRender extends WaypointPainter<MyWaypoint> {

    private static Color[] cols;
    private final List<Vol> vols=new ArrayList<>();
    
    public WaypointRender() {
    }
    
    public void showFlightsPopup() {
        if (vols.isEmpty()) {
            return;
        }

        JDialog popup = new JDialog(new JFrame(), "Liste des vols", true);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        popup.setSize(800, 600);

        // Create the JTable and its model
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        model.addColumn("Code Vol");
        model.addColumn("Départ");
        model.addColumn("Arrivée");
        model.addColumn("Heure");
        model.addColumn("Minutes");
        model.addColumn("Durée");
        for (Vol vol : vols) {
            model.addRow(new Object[]{
                vol.getCodeVol(),
                vol.getDepart(),
                vol.getArrive(),
                vol.getHeure(),
                vol.getMin(),
                vol.getDuree()
            });
        }
        JScrollPane scrollPane = new JScrollPane(table);
        popup.add(scrollPane, BorderLayout.CENTER);
        popup.setVisible(true);
    }

    
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
    
    public Painter<JXMapViewer> paintVol(List<Vol> vol, JXMapViewer mapViewer, List<Aeroport> aero,Graph graph,int level) {
        if(graph!=null){
            colorierGraphe(graph);
        }
        Painter<JXMapViewer> lineOverlay = new Painter<JXMapViewer>() {
            @Override
            public void paint(Graphics2D g, final JXMapViewer map, final int w, final int h) {
                vols.clear();
                g = (Graphics2D) g.create();
                Rectangle rect = map.getViewportBounds();
                g.translate(-rect.x, -rect.y);
                g.setColor(Color.BLACK);
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
                if(graph!=null){
                    for (Vol v : vol) {
                        for(Node n:graph.getNodeSet()){
                            if(v.getCodeVol().equals(n.getId())&&(level==0 || level==(int)n.getNumber("color"))){
                                g.setColor(cols[(int)n.getAttribute("color")]);
                                Point2D p1 = map.getTileFactory().geoToPixel(new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude()), map.getZoom());
                                Point2D p2 = map.getTileFactory().geoToPixel(new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude()), map.getZoom());
                                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                                if(!vols.contains(v)){
                                    vols.add(v);
                                }
                                break;
                            }
                        }
                    }
                }else{
                    vols.addAll(vol);
                    for (Vol v : vol) {
                        Point2D p1 = map.getTileFactory().geoToPixel(new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude()), map.getZoom());
                        Point2D p2 = map.getTileFactory().geoToPixel(new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude()), map.getZoom());
                        g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
                        
                    }
                }
                g.dispose();
            }
        };
        return lineOverlay;
    }
    private static void colorierGraphe(Graph g) {
        int max = (int)g.getAttribute("kMax");
        cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) ((double) (Math.random()*100000.0)+5.0), 0.8f, 0.9f);
        }
    }
}
