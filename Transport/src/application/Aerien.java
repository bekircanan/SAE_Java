package application;

import construction.Aeroport;
import construction.Algos;
import construction.Graphe;
import construction.Vols;
import static construction.Vols.intersectent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.graphstream.graph.Graph;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

public class Aerien {

    /**
     * @throws java.io.IOException
     */
    public static void main() throws IOException{
        List<Aeroport> port = new ArrayList<>();
        List<Vols> vol = new ArrayList<>();
        try (Scanner scan = new Scanner(new File("DataTest\\aeroports.txt"))) {
            while (scan.hasNextLine()) {
                port.add(new Aeroport(scan));
            }
        }
        Graphe.setAeroport(port);
        try (Scanner scan = new Scanner(new File("DataTest\\vol-test0.csv"))) {
            while (scan.hasNextLine()) {
                vol.add(new Vols(scan));
            }
        }
        for(Vols v:vol){
            System.out.println(v);
        }
        // Point2D.Double inter = intersectent(vol.getFirst(),vol.getLast(),port); System.out.println(inter);
        /*
        Scanner scan = new Scanner(System.in);
        System.out.println("entrez l'emplacement des fichiers(sans .txt)");
        String file = scan.nextLine();
        Graph g = Graphe.chargerGraphe("DataTest\\"+file+".txt");
        //Gloutonne(g);
        //colorierGraphe(g,"couleur");
        distance(g,g.getNode(0));
        etiqueterDistances(g);
        g.display();
        */
    }
}

