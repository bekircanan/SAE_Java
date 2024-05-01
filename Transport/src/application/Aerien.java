package application;

import construction.Aeroport;
import static construction.Aeroport.setAeroport;
import static construction.Algos.*;
import construction.Graphe;
import construction.Vols;
import static construction.Vols.helpVol;
import static construction.Vols.setVols;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
                switch(choix){
                    case 1 :
                        setAeroport(port);
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
                        //helpVol(vol,port,setAeroport(port));
                        setVols(vol,port);
                        System.out.print(": fait");
                        break;
                    case 3 :
                        try(BufferedReader scanGraph = new BufferedReader(new InputStreamReader(System.in))){
                            System.out.println("entrer un nombre.(entre 0 et 19)");
                            System.out.print(": ");
                            chiffre = scanGraph.readLine();
                        }
                        Graph g = Graphe.chargerGraphe("DataTest\\graph-test"+chiffre+".txt");
                        System.out.println("----------------------------------------------------------");
                        System.out.println("| ecrire le numero 1 pour le coloration de Domination");
                        System.out.println("| ecrire le numero 2 pour le coloration de Gloutonne");
                        System.out.println("| inscrire le numero 3 pour l'algo de distance");
                        System.out.println("----------------------------------------------------------");
                        System.out.print(": ");
                        switch(choix){
                            case 1 :
                                Domination(g);
                                break;
                            case 2 :
                                Gloutonne(g);
                                colorierGraphe(g,"couleur");
                                break;
                            case 3 :
                                distance(g,g.getNode(0));
                                etiqueterDistances(g);
                                break;
                        }
                        g.display();
                        System.out.print(": fait");
                        break;
                    case 0 :
                        System.out.println("Bye bye");
                        break;
                }
            }
    }
}

