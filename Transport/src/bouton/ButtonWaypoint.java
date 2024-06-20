/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bouton;

import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author bekir
 */
public class ButtonWaypoint extends JButton{

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
