/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bouton;

import java.awt.Color;
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
    }
}
