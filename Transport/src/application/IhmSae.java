package application;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IhmSae extends JFrame {
    public IhmSae() {
        setTitle("Menu principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton coloration = new JButton("Graphe coloration");
        JButton intersection = new JButton("Graphe Intersection/collision");

     
        coloration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Coloration();
            }
        });

        intersection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IntersectionIHM();
            }
        });

        panel.add(coloration);
        panel.add(intersection);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new IhmSae();
    }
}
