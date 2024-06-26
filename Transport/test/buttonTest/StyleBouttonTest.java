package buttonTest;

import bouton.StyleBouton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for StyleBouton.
 */
public class StyleBouttonTest {

    private StyleBouton defaultButton;
    private StyleBouton customButton;

    public StyleBouttonTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        defaultButton = new StyleBouton("Default Text");
        customButton = new StyleBouton("Custom Text", new Color(100, 100, 100));
    }

    @After
    public void tearDown() {
        defaultButton = null;
        customButton = null;
    }

    @Test
    public void testDefaultButtonText() {
        assertEquals("Default Text", defaultButton.getText());
    }

    @Test
    public void testDefaultButtonBackground() {
        assertEquals(new Color(70, 130, 180), defaultButton.getBackground());
    }

    @Test
    public void testDefaultButtonForeground() {
        assertEquals(Color.WHITE, defaultButton.getForeground());
    }

    @Test
    public void testDefaultButtonFont() {
        Font expectedFont = new Font("Arial", Font.BOLD, 14);
        assertEquals(expectedFont, defaultButton.getFont());
    }

    @Test
    public void testDefaultButtonFocusPainted() {
        assertFalse(defaultButton.isFocusPainted());
    }

    /*@Test
    public void testDefaultButtonBorder() {
        assertEquals(BorderFactory.createEmptyBorder(10, 20, 10, 20).getBorderInsets(), defaultButton.getBorder().getBorderInsets(defaultButton));
    }*/

    @Test
    public void testCustomButtonText() {
        assertEquals("Custom Text", customButton.getText());
    }

    @Test
    public void testCustomButtonBackground() {
        assertEquals(new Color(100, 100, 100), customButton.getBackground());
    }

    @Test
    public void testCustomButtonForeground() {
        assertEquals(Color.WHITE, customButton.getForeground());
    }

    @Test
    public void testCustomButtonFont() {
        Font expectedFont = new Font("Verdana", Font.BOLD, 20);
        assertEquals(expectedFont, customButton.getFont());
    }

    @Test
    public void testCustomButtonFocusPainted() {
        assertFalse(customButton.isFocusPainted());
    }

    /*@Test
    public void testCustomButtonBorder() {
        assertEquals(BorderFactory.createEmptyBorder(15, 30, 15, 30).getBorderInsets(), customButton.getBorder().getBorderInsets(customButton));
    }*/

    @Test
    public void testCustomButtonPreferredSize() {
        assertEquals(new Dimension(300, 60), customButton.getPreferredSize());
    }

    @Test
    public void testCustomButtonToolTipText() {
        assertEquals("Cliquez pour custom text", customButton.getToolTipText());
    }

    /*@Test
    public void testMouseHoverEffect() {
        Color originalColor = defaultButton.getBackground();
        defaultButton.getMouseListeners()[0].mouseEntered(null);
        assertEquals(originalColor.darker(), defaultButton.getBackground());
        defaultButton.getMouseListeners()[0].mouseExited(null);
        assertEquals(originalColor, defaultButton.getBackground());
    }*/
}
