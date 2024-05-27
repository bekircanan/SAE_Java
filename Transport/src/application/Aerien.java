package application;

import construction.Aeroport;
import static construction.Aeroport.setAeroport;
import static construction.Algos.*;
import construction.Graphe;
import static construction.Intersection.setVolsAeroport;
import construction.Vols;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.Viewer;
import static construction.Intersection.setVolsCollision;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author bekir
 */


public class Aerien {

    /**
     * @throws java.io.IOException
     */
    public static void main() throws IOException{
        List<Aeroport> port = new ArrayList<>();
        try (Scanner scanAero = new Scanner(new File("DataTest\\aeroports.txt"))) {
            while (scanAero.hasNextLine()) {
                port.add(new Aeroport(scanAero));
            }
        }
        int choix;
            System.out.println("----------------------------------------------------------");
            System.out.println("| Ecrire le numero 1 pour le graphique de l'aeroport");
            System.out.println("| Ecrire le numero 2 pour les vols");
            System.out.println("| Inscrire le numero 3 pour le graphique de coloration");
            System.out.println("| Ecrire le numero 0 pour quitter");
            System.out.println("----------------------------------------------------------");
            System.out.print(": ");
            try(Scanner scan = new Scanner(System.in)){
                choix=scan.nextInt();
                String chiffre;
                int choix2;
                switch(choix){
                    case 1 :
                        Graph g=setAeroport(port);
                        g.display();
                        System.out.print(": fait");
                        break;
                    case 2 :
                        try(BufferedReader scanFile = new BufferedReader(new InputStreamReader(System.in))){
                            System.out.println("Entrer un nombre(entre 0 et 9).");
                            System.out.print(": ");
                            chiffre = scanFile.readLine();
                        }
                        List<Vols> vol = new ArrayList<>();
                        try (Scanner scanVol = new Scanner(new File("DataTest\\vol-test"+chiffre+".csv"))) {
                            while (scanVol.hasNextLine()) {
                                vol.add(new Vols(scanVol));
                            }
                        }
                        //setVolsAeroport(vol,port,setAeroport(port));
                        setVolsCollision(vol,port);
                        System.out.print(": fait");
                        break;
                    case 3 :
                        Graph gcolor;
                        try(Scanner scanFile = new Scanner(System.in)){
                            System.out.println("Entrer un nombre(entre 0 et 19).");
                            System.out.print(": ");
                            choix2 = scanFile.nextInt();
                        gcolor = Graphe.chargerGraphe("DataTest\\graph-test"+choix2+".txt");
                        System.out.println("----------------------------------------------------------");
                        System.out.println("| ecrire le numero 1 pour l'algo de coloration de Gloutonne");
                        System.out.println("| ecrire le numero 2 pour l'algo de coloration de welshPowell");
                        System.out.println("| ecrire le numero 3 pour l'ago de coloration(largestFirstColoring)");
                        System.out.println("----------------------------------------------------------");
                        System.out.print(": ");
                            System.out.println("entrer un nombre.");
                            System.out.print(": ");
                            choix2 = scanFile.nextInt();
                        }
                        switch(choix2){
                            case 1 :
                                Gloutonne(gcolor);
                                break;
                            case 2 :
                                welshPowell(gcolor);
                                break;
                            case 3 :
                                largestFirstColoring(gcolor);
                                break;
                                
                        }
                        Viewer viewer = gcolor.display();
                        gcolor.setAttribute("ui.quality");
                        gcolor.setAttribute("ui.antialias");
                        viewer.enableAutoLayout();
                        System.out.print(": fait");
                        break;
                    case 0 :
                        System.out.println("Bye bye");
                        break;
                }
            }
            
    }
}

