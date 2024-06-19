import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import modele.Aeroport;

public class AeroportTest {

    public AeroportTest() {
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
    public void testAeroportConstructor() throws FileNotFoundException {
        // Arrange
        String aeroportData = "JFK;New York;40;38;23;N;73;46;44;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        
        // Act
        Aeroport aeroport = new Aeroport(scanner);
        
        // Assert
        assertEquals("JFK", aeroport.getCodeAero());
        assertEquals("New York", aeroport.getLieu());
        assertEquals(40.6397222, aeroport.getLatitude(), 0.000001);
        assertEquals(73.7788889, aeroport.getLongitude(), 0.000001);
    }

    @Test
    public void testGetCodeAero() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals("LAX", aeroport.getCodeAero());
    }

    @Test
    public void testGetLieu() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals("Los Angeles", aeroport.getLieu());
    }

    @Test
    public void testGetLatitude() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals(33.9425, aeroport.getLatitude(), 0.0001);
    }

    @Test
    public void testGetLongitude() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals(118.4080556, aeroport.getLongitude(), 0.0001);
    }

    @Test
    public void testGetX() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals(4648.915601, aeroport.getX(), 0.0001);
    }

    @Test
    public void testGetY() throws FileNotFoundException {
        // Arrange
        String aeroportData = "LAX;Los Angeles;33;56;33;N;118;24;29;W";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);
        
        // Act & Assert
        assertEquals(-2514.50406, aeroport.getY(), 0.0001);
    }
}
