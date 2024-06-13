package test;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import construction.Graphe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GrapheTest {

    private String filePath;

    @Before
    public void setUp() {
        // Assurez-vous que le fichier de test est accessible
        filePath = "graphe.txt";
    }

    @Test
    public void testChargerGraphe() throws FileNotFoundException, IOException {
        // Charger le graphe à partir du fichier
        Graph g = Graphe.chargerGraphe(filePath);

        // Vérifier que le graphe a été chargé correctement
        Assert.assertNotNull(g);
        Assert.assertEquals("Chaine", g.getId());

        // Vérifier le nombre de sommets
        Assert.assertEquals(4, g.getNodeCount());

        // Vérifier le nombre d'arêtes
        Assert.assertEquals(4, g.getEdgeCount());

        // Vérifier les attributs des nœuds
        Node nodeA = g.getNode("A");
        Node nodeB = g.getNode("B");
        Node nodeC = g.getNode("C");
        Node nodeD = g.getNode("D");

        Assert.assertNotNull(nodeA);
        Assert.assertNotNull(nodeB);
        Assert.assertNotNull(nodeC);
        Assert.assertNotNull(nodeD);

        // Vérifier les degrés des nœuds
        Assert.assertEquals(2, nodeA.getDegree());
        Assert.assertEquals(2, nodeB.getDegree());
        Assert.assertEquals(2, nodeC.getDegree());
        Assert.assertEquals(2, nodeD.getDegree());

        // Vérifier les arêtes
        Edge edgeAB = g.getEdge("A-B");
        Edge edgeAC = g.getEdge("A-C");
        Edge edgeBD = g.getEdge("B-D");
        Edge edgeCD = g.getEdge("C-D");

        Assert.assertNotNull(edgeAB);
        Assert.assertNotNull(edgeAC);
        Assert.assertNotNull(edgeBD);
        Assert.assertNotNull(edgeCD);

        // Vérifier les attributs des arêtes
        Assert.assertEquals("A-B", edgeAB.getId());
        Assert.assertEquals("A-C", edgeAC.getId());
        Assert.assertEquals("B-D", edgeBD.getId());
        Assert.assertEquals("C-D", edgeCD.getId());
    }
}
