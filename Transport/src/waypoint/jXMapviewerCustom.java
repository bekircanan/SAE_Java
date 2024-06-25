package waypoint;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.List;
import modele.Aeroport;
import modele.Vol;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * Classe personnalisée JXMapViewer pour dessiner des vols entre les aéroports.
 */
public class jXMapviewerCustom extends JXMapViewer {
    private final List<Vol> vol;
    private JXMapViewer map;

     /**
     * Constructeur de la classe jXMapviewerCustom.
     *
     * @param vols la liste des vols à dessiner
     * @param aero la liste des aéroports
     * @param mapViewer le composant JXMapViewer sur lequel dessiner les vols
     */
    public jXMapviewerCustom(List<Vol> vol,List<Aeroport> aero,JXMapViewer mapViewer) {
        this.vol = vol;
        this.map=mapViewer;
        for(Vol v : vol){
            for(Aeroport a:aero){
                if(v.getArrive().equals(a.getCodeAero())){
                    v.setArriveaero(a);
                }else if(v.getDepart().equals(a.getCodeAero())){
                    v.setDepartaero(a);
                }
            }
        }
        repaint();
    }

     /**
     * Redéfinition de la méthode paintComponent pour dessiner les vols.
     *
     * @param g le contexte graphique pour le rendu
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Rectangle rect = this.getViewportBounds();
        g.translate(0, 0);  // Adjust for the viewport

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Vol v : vol) {
            /*
                Point2D p1 = map.getTileFactory().geoToPixel(new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude()), map.getZoom());
                Point2D p2 = map.getTileFactory().geoToPixel(new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude()), map.getZoom());
                g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
            */
            paint(g2d,this,getWidth(),getHeight(),v);
        }
        g2d.dispose();
    }
    
    /**
     * Méthode pour dessiner un vol spécifique entre deux aéroports.
     *
     * @param g le contexte graphique pour le rendu
     * @param map le composant JXMapViewer sur lequel dessiner le vol
     * @param w la largeur du composant
     * @param h la hauteur du composant
     * @param v le vol à dessiner
     */
    private void paint(Graphics2D g,JXMapViewer map,int w,int h,Vol v){
        g=(Graphics2D) g.create();
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Point2D p1 = map.getTileFactory().geoToPixel(new GeoPosition(v.getDepartaero().getLatitude(), v.getDepartaero().getLongitude()), map.getZoom());
        Point2D p2 = map.getTileFactory().geoToPixel(new GeoPosition(v.getArriveaero().getLatitude(), v.getArriveaero().getLongitude()), map.getZoom());
        g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
        g.dispose();
    }
}
