import construction.ChargerGraphe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class ChargerGrapheTest {
    private String filePath;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public ChargerGrapheTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        filePath = "..//graphe.txt";
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testChargerGraphe() throws FileNotFoundException, IOException {
        // Charger le graphe à partir du fichier
        Graph g = ChargerGraphe.chargerGraphe(filePath);

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

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() throws FileNotFoundException, IOException {
        // Utiliser un chemin de fichier qui n'existe pas
        String invalidFilePath = "invalid/path/to/nonexistentfile.txt";
        // Charger le graphe, ce qui devrait lancer une FileNotFoundException
        ChargerGraphe.chargerGraphe(invalidFilePath);
    }

    @Test(expected = InputMismatchException.class)
    public void testInputMismatchException() throws IOException {
        // Créer un fichier temporaire mal formé
        File tempFile = folder.newFile("malformedGraphe.txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("invalid data\n");
        }
        // Charger le graphe, ce qui devrait lancer une InputMismatchException
        ChargerGraphe.chargerGraphe(tempFile.getAbsolutePath());
    }
}
