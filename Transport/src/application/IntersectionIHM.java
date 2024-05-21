package application;

import construction.Intersection;
import construction.Aeroport;
import construction.Vols;
import application.Aerien;
import static construction.Aeroport.setAeroport;
import static construction.Intersection.setVolsAeroport;
import static construction.Intersection.setVolsCollision;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class IntersectionIHM extends JFrame {
    public IntersectionIHM() {
        
        setTitle("Intersection");

        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      
        setSize(300, 200);

        
        setLocationRelativeTo(null);

      
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

      
        String[] options = { "Graphique de l'aéroport", "Vol" };
        JComboBox<String> comboBox = new JComboBox<>(options);
        panel.add(comboBox);

   
        JButton button = new JButton("Lancer les algos");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();

                
                List<Aeroport> port = new ArrayList<>();
                try (Scanner scanAero = new Scanner(new File("DataTest\\aeroports.txt"))) {
                    while (scanAero.hasNextLine()) {
                        port.add(new Aeroport(scanAero));
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                }
                

                if (selectedOption != null) {
                    switch (selectedOption) {
                        case "Graphique de l'aéroport":
                        Graph g=setAeroport(port);
                        g.display();
                        System.out.print(": fait");
                        break;

                        case "Vol":
                            
                            /*try(BufferedReader scanFile = new BufferedReader(new InputStreamReader(System.in))){
                            System.out.println("Entrer un nombre(entre 0 et 9).");
                            System.out.print(": ");
                            String chiffre = scanFile.readLine();
                            } catch (IOException ex) {
                            Logger.getLogger(IntersectionIHM.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            List<Vols> vol = new ArrayList<>();
                            try (Scanner scanVol = new Scanner(new File("DataTest\\vol-test"+chiffre+".csv"))) {
                                while (scanVol.hasNextLine()) {
                                    vol.add(new Vols(scanVol));
                                }
                            }
                            setVolsAeroport(vol,port,setAeroport(port));
                            setVolsCollision(vol,port);
                            System.out.print(": fait");
                           
                            
                            break;*/


                        default:
                            JOptionPane.showMessageDialog(null, "Sélection non valide.");
                            break;
                    }
                }
            }
        });
        panel.add(button);

        
        add(panel);

     
        setVisible(true);
    }

    
}
