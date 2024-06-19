import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import modele.Vol;
import modele.Aeroport;

public class VolTest {

    public VolTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testVolConstructor() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        
        // Act
        Vol vol = new Vol(scanner);
        
        // Assert
        assertEquals("AA123", vol.getCodeVol());
        assertEquals("JFK", vol.getDepart());
        assertEquals("LAX", vol.getArrive());
        assertEquals(10, vol.getHeure());
        assertEquals(30, vol.getMin());
        assertEquals(300, vol.getDuree());
    }

    @Test
    public void testGetCodeVol() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals("AA123", vol.getCodeVol());
    }

    @Test
    public void testGetDepart() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals("JFK", vol.getDepart());
    }

    @Test
    public void testGetArrive() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals("LAX", vol.getArrive());
    }

    @Test
    public void testGetHeure() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals(10, vol.getHeure());
    }

    @Test
    public void testGetMin() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals(30, vol.getMin());
    }

    @Test
    public void testGetDuree() {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        
        // Act & Assert
        assertEquals(300, vol.getDuree());
    }

    @Test
    public void testSetAndGetDepartaero() throws FileNotFoundException {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        Aeroport departAero = new Aeroport(new Scanner(new ByteArrayInputStream("JFK;New York;40;38;23;N;73;46;44;W".getBytes())));
        
        // Act
        vol.setDepartaero(departAero);
        
        // Assert
        assertEquals(departAero, vol.getDepartaero());
    }

    @Test
    public void testSetAndGetArriveaero() throws FileNotFoundException {
        // Arrange
        String volData = "AA123;JFK;LAX;10;30;300";
        Scanner scanner = new Scanner(new ByteArrayInputStream(volData.getBytes()));
        Vol vol = new Vol(scanner);
        Aeroport arriveAero = new Aeroport(new Scanner(new ByteArrayInputStream("LAX;Los Angeles;33;56;33;N;118;24;29;W".getBytes())));
        
        // Act
        vol.setArriveaero(arriveAero);
        
        // Assert
        assertEquals(arriveAero, vol.getArriveaero());
    }
}
