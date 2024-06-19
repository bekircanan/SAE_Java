package test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import construction.AlgorithmColoration;

public class AlgosTest {
    
    private Graph g;

    @Before
    public void init() {
        // Création et initialisation d'un graphe pour les tests
        g = new SingleGraph("TestGraph");
        g.addNode("A");
        g.addNode("B");
        g.addNode("C");
        g.addNode("D");
        g.addEdge("AB", "A", "B");
        g.addEdge("AC", "A", "C");
        g.addEdge("AD", "A", "D");
        g.addEdge("BC", "B", "C");
        g.addEdge("BD", "B", "D");
        g.addEdge("CD", "C", "D");

        // Ajouter un attribut kMax au graphe
        g.addAttribute("kMax", 3);
    }

    @Test
    public void testGloutonne() {
        // Appeler la méthode Gloutonne sur le graphe
        int totalConflicts = AlgorithmColoration.Gloutonne(g);
        System.out.println(totalConflicts);
        // Vérifier le nombre de conflits
        Assert.assertEquals(1, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertNotEquals(colorA, colorC);
        Assert.assertEquals(colorA, colorD);
        Assert.assertNotEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }
    
    @Test
    public void welshPowellTest() {
        // Appeler la méthode Welsh-Powell sur le graphe
        int totalConflicts = AlgorithmColoration.welshPowell(g);
        
        // Vérifier le nombre de conflits
        Assert.assertEquals(0, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertNotEquals(colorA, colorC);
        Assert.assertNotEquals(colorA, colorD);
        Assert.assertNotEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }
    
    //@Test
    //Dsatur pas possible car aléatoire
    /*public void dsaturTest() {
        // Appeler la méthode DSATUR sur le graphe
        int totalConflicts = AlgorithmColoration.dsatur(g);
        
        // Vérifier le nombre de conflits
        //Assert.assertEquals(1, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertEquals(colorA, colorC);
        Assert.assertNotEquals(colorA, colorD);
        Assert.assertEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }*/
    
    @Test
    public void largestFirstColoringTest(){
    // Appeler la méthode Welsh-Powell sur le graphe
        int totalConflicts = AlgorithmColoration.largestFirstColoring(g);
        
        // Vérifier le nombre de conflits
        Assert.assertEquals(1, totalConflicts);

        // Vérifier les couleurs des nœuds
        int colorA = g.getNode("A").getAttribute("color");
        int colorB = g.getNode("B").getAttribute("color");
        int colorC = g.getNode("C").getAttribute("color");
        int colorD = g.getNode("D").getAttribute("color");

        // Vérifier que les nœuds adjacents ont des couleurs différentes
        Assert.assertNotEquals(colorA, colorB);
        Assert.assertNotEquals(colorA, colorC);
        Assert.assertEquals(colorA, colorD);
        Assert.assertNotEquals(colorB, colorC);
        Assert.assertNotEquals(colorB, colorD);
        Assert.assertNotEquals(colorC, colorD);
    }
    
    
}

