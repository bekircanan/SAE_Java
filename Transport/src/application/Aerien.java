package application;

import construction.Graphe;
import java.io.IOException;
import java.util.Scanner;
import org.graphstream.graph.Graph;

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
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("entrez l'emplacement des fichiers(sans .txt)");
        String file = scan.nextLine();
        Graph g = Graphe.chargerGraphe("DataTest\\"+file+".txt");
        g.display();
    }
}

