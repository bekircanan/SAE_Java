package ex;

/*
 * (C) 2004 - Geotechnical Software Services
 * 
 * Ce code est un logiciel libre ; vous pouvez le redistribuer et/ou
 * le modifier selon les termes de la Licence publique générale restreinte GNU
 * telle que publiée par la Free Software Foundation ; soit
 * la version 2.1 de la Licence, soit (à votre gré) toute version ultérieure.
 *
 * Ce code est distribué dans l'espoir qu'il sera utile,
 * mais SANS AUCUNE GARANTIE ; sans même la garantie implicite de
 * QUALITÉ MARCHANDE ou d'ADÉQUATION À UN USAGE PARTICULIER. Consultez la
 * Licence publique générale restreinte GNU pour plus de détails.
 *
 * Vous devriez avoir reçu une copie de la Licence publique générale restreinte GNU
 * avec ce programme ; sinon, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA  02111-1307, USA.
 */
import java.util.Collection;
import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import no.geosoft.cc.graphics.*;



/**
 * Programme de démonstration G. Démontre :
 *
 * <ul>
 * <li>Interaction de sélection personnalisée
 * <li>Fonctionnalités de détection d'objets
 * <li>Héritage et manipulation de styles
 * <li>Configuration dynamique des styles
 * </ul>
 * 
 * @author <a href="mailto:info@geosoft.no">GeoSoft</a>
 */   
public class Demo6 extends JFrame
  implements GInteraction, ActionListener
{
  private JButton     typeButton_;
  private GStyle      selectionStyle_;
  private GStyle      textStyle_;
  private GStyle      selectedTextStyle_;
  private GScene      scene_;
  private GObject     rubberBand_;
  private Collection  selection_;
  private int         x0_, y0_;
  
  
  /**
   * Classe pour créer le canevas de démonstration et gérer les événements Swing.
   */   
  public Demo6()
  {
    super ("Bibliothèque de graphiques G - Démo 6");
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    selectionStyle_ = new GStyle();
    selectionStyle_.setForegroundColor (new Color (255, 255, 150));
    selectionStyle_.setLineWidth (2);        

    selectedTextStyle_ = new GStyle();
    selectedTextStyle_.setForegroundColor (new Color (255, 255, 255));
    selectedTextStyle_.setFont (new Font ("Dialog", Font.BOLD, 14));

    selection_ = null;
      
    // Créer l'interface utilisateur graphique
    JPanel topLevel = new JPanel();
    topLevel.setLayout (new BorderLayout());
    getContentPane().add (topLevel);        

    JPanel buttonPanel = new JPanel();
    buttonPanel.add (new JLabel ("Mettre en surbrillance les lignes "));

    typeButton_ = new JButton ("à l'intérieur");
    typeButton_.addActionListener (this);
    buttonPanel.add (typeButton_);    

    buttonPanel.add (new JLabel (" bande de sélection"));
    topLevel.add (buttonPanel,   BorderLayout.NORTH);

    // Créer le canevas graphique
    GWindow window = new GWindow();
    topLevel.add (window.getCanvas(), BorderLayout.CENTER);    

    // Créer une scène avec des paramètres par défaut de la vue et de l'étendue mondiale
    scene_ = new GScene (window, "Scène");

    // Créer quelques objets graphiques
    GObject testObject = new TestObject (scene_, 40);
    scene_.add (testObject);

    rubberBand_ = new GObject ("Interaction");
    GStyle rubberBandStyle = new GStyle();
    rubberBandStyle.setBackgroundColor (new Color (1.0f, 0.0f, 0.0f, 0.2f));
    rubberBand_.setStyle (rubberBandStyle);
    scene_.add (rubberBand_);
    
    pack();
    setSize (new Dimension (500, 500));
    setVisible (true);

    window.startInteraction (this);
  }


  
  public void actionPerformed (ActionEvent event)
  {
    String text = typeButton_.getText();
    if (text.equals ("à l'intérieur")) typeButton_.setText ("intersectant");
    else                        typeButton_.setText ("à l'intérieur");
  }

  
  
  public void event (GScene scene, int event, int x, int y)
  {
    switch (event) {
      case GWindow.BUTTON1_DOWN :
        x0_ = x;
        y0_ = y;
        rubberBand_.addSegment (new GSegment());
        break;
        
      case GWindow.BUTTON1_UP :
        rubberBand_.removeSegments();
        
        // Annuler la sélection visuelle de la sélection actuelle
        if (selection_ != null) {
          for (Iterator i = selection_.iterator(); i.hasNext(); ) {
            GSegment line = (GSegment) i.next();
            GText text = line.getText();
            text.setStyle (textStyle_);
            line.setStyle (null);
          }
        }

        scene_.refresh();
        break;
        
      case GWindow.BUTTON1_DRAG :
        int[] xRubber = new int[] {x0_, x, x, x0_, x0_};
        int[] yRubber = new int[] {y0_, y0_, y, y, y0_};      

        GSegment rubberBand = rubberBand_.getSegment();
        rubberBand.setGeometry (xRubber, yRubber);

        // Annuler la sélection visuelle de la sélection actuelle
        if (selection_ != null) {
          for (Iterator i = selection_.iterator(); i.hasNext(); ) {
            GSegment line = (GSegment) i.next();
            GText text = line.getText();
            text.setStyle (textStyle_);
            line.setStyle (null);
          }
        }

        if (typeButton_.getText().equals ("à l'intérieur"))
          selection_ = scene_.findSegmentsInside (Math.min (x0_, x),
                                                  Math.min (y0_, y),
                                                  Math.max (x0_, x),
                                                  Math.max (y0_, y));
        else
          selection_ = scene_.findSegments (Math.min (x0_, x),
                                            Math.min (y0_, y),
                                            Math.max (x0_, x),
                                            Math.max (y0_, y));

        // Supprimer la bande de sélection de la sélection
        selection_.remove (rubberBand);
        
        // Définir l'apparence visuelle de la nouvelle sélection
        for (Iterator i = selection_.iterator(); i.hasNext(); ) {
          GSegment line = (GSegment) i.next();
          line.setStyle (selectionStyle_);
          GText text = line.getText();
          text.setStyle (selectedTextStyle_);
        }}}


  public static void main (String[] args)
  {
    new Demo6();
  }
}

