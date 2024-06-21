/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bouton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 *
 * @author bekir
 */
public class StyleBouton extends JButton{
    public StyleBouton(String text){
        this.setText(text);
        this.setBackground(new Color(70, 130, 180));
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setFocusPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        surVol();
    }
    /**
     * Crée un bouton stylisé avec les propriétés spécifiées.
     * @param text Le texte à afficher sur le bouton.
     * @param backgroundColor La couleur de fond du bouton.
     */
    public StyleBouton(String text, Color backgroundColor) {
        this.setText(text);
        this.setBackground(backgroundColor);
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Verdana", Font.BOLD, 20));
        this.setFocusPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        this.setPreferredSize(new Dimension(300, 60));
        this.setToolTipText("Cliquez pour " + text.toLowerCase());
        surVol();
        
    }
    private void surVol(){
        StyleBouton button = this;
        Color backgroundColor=this.getBackground();
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
    }
}
