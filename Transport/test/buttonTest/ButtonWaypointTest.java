package buttonTest;

import bouton.ButtonWaypoint;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ButtonWaypoint.
 */
public class ButtonWaypointTest {
    
    private ButtonWaypoint button;

    public ButtonWaypointTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        button = new ButtonWaypoint();
    }
    
    @After
    public void tearDown() {
        button = null;
    }

    @Test
    public void testContentAreaFilled() {
        assertFalse("Content area should not be filled", button.isContentAreaFilled());
    }
    
    @Test
    public void testBorderPainted() {
        assertFalse("Border should not be painted", button.isBorderPainted());
    }
    
    @Test
    public void testFocusPainted() {
        assertFalse("Focus should not be painted", button.isFocusPainted());
    }
    
    @Test
    public void testBorder() {
        assertNull("Border should be null", button.getBorder());
    }
    
    @Test
    public void testIcon() {
        assertNotNull("Icon should not be null", button.getIcon());
        assertTrue("Icon should be of type ImageIcon", button.getIcon() instanceof ImageIcon);
    }
    
    @Test
    public void testCursor() {
        assertEquals("Cursor should be HAND_CURSOR", Cursor.HAND_CURSOR, button.getCursor().getType());
    }
    
    @Test
    public void testSize() {
        Dimension expectedSize = new Dimension(24, 24);
        assertEquals("Size should be 24x24", expectedSize, button.getSize());
    }
}
