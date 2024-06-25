/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit ce template
 */
package bouton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Une classe personnalisée JButton pour créer des boutons stylisés.
 * <p>
 * Cette classe permet de créer des boutons avec des styles prédéfinis,
 * tels que des couleurs de fond, des polices et des bordures spécifiques.
 * Elle gère également les effets de survol de la souris.
 * </p>
 * 
 * @autor bekir
 */
public class StyleBouton extends JButton {

    /**
     * Construit un nouveau bouton stylisé avec un texte par défaut.
     * <p>
     * Le bouton est stylisé avec une couleur de fond spécifique, une police Arial en gras de taille 14,
     * et une bordure vide. Un effet de survol de la souris est également ajouté.
     * </p>
     * 
     * @param text Le texte à afficher sur le bouton.
     */
    public StyleBouton(String text) {
        this.setText(text);
        this.setBackground(new Color(70, 130, 180));
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setFocusPainted(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        surVol();
    }

    /**
     * Construit un nouveau bouton stylisé avec un texte et une couleur de fond spécifiques.
     * <p>
     * Le bouton est stylisé avec une couleur de fond et une police Verdana en gras de taille 20,
     * et une bordure vide. Un effet de survol de la souris est également ajouté.
     * </p>
     * 
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

    /**
     * Ajoute un effet de survol de la souris au bouton.
     * <p>
     * Lorsque la souris survole le bouton, sa couleur de fond devient plus foncée.
     * Quand la souris quitte le bouton, la couleur de fond revient à la normale.
     * </p>
     */
    private void surVol() {
        StyleBouton button = this;
        Color backgroundColor = this.getBackground();
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
