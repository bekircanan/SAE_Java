import construction.MyWaypoint;  // Import MyWaypoint class
import construction.EventWaypoint;  // Import EventWaypoint interface
import modele.Aeroport;  // Import Aeroport class from modele package

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.jxmapviewer.viewer.GeoPosition;

public class MyWaypointTest {

    public MyWaypointTest() {
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

    // Mock implementation of EventWaypoint for testing purposes
    private static class MockEventWaypoint implements EventWaypoint {
        private boolean selectedCalled = false;
        private MyWaypoint selectedWaypoint = null;

        @Override
        public void selected(MyWaypoint waypoint) {
            selectedCalled = true;
            selectedWaypoint = waypoint;
        }

        public boolean isSelectedCalled() {
            return selectedCalled;
        }

        public MyWaypoint getSelectedWaypoint() {
            return selectedWaypoint;
        }
    }

    @Test
    public void testMyWaypointWithName() {
        // Arrange
        MockEventWaypoint event = new MockEventWaypoint();
        GeoPosition position = new GeoPosition(40.7128, -74.0060);
        String name = "Test Waypoint";

        // Act
        MyWaypoint waypoint = new MyWaypoint(name, event, position);

        // Assert
        assertEquals("The name should be the same as the one passed in the constructor", name, waypoint.getName());
        assertNotNull("Button should not be null", waypoint.getButton());
        assertEquals("Position should be the same as the one passed in the constructor", position, waypoint.getPosition());
    }

    @Test
    public void testMyWaypointWithAeroport() throws FileNotFoundException {
        // Arrange
        MockEventWaypoint event = new MockEventWaypoint();
        String aeroportData = "JFK , 40.6413, -73.7781";
        Scanner scanner = new Scanner(new ByteArrayInputStream(aeroportData.getBytes()));
        Aeroport aeroport = new Aeroport(scanner);

        // Act
        MyWaypoint waypoint = new MyWaypoint(aeroport, event);

        // Assert
        assertEquals("The name should be the airport code", aeroport.getCodeAero(), waypoint.getName());
        assertNotNull("Button should not be null", waypoint.getButton());
        assertEquals("Position should match the airport coordinates", new GeoPosition(aeroport.getLatitude(), aeroport.getLongitude()), waypoint.getPosition());
        assertEquals("Aeroport object should be the same as the one passed in the constructor", aeroport, waypoint.getAeroport());
    }

    @Test
    public void testButtonAction() {
        // Arrange
        MockEventWaypoint event = new MockEventWaypoint();
        GeoPosition position = new GeoPosition(40.7128, -74.0060);
        MyWaypoint waypoint = new MyWaypoint("Test Waypoint", event, position);
        JButton button = waypoint.getButton();

        // Act
        for (ActionListener listener : button.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null));
        }

        // Assert
        assertTrue("Event's selected method should have been called", event.isSelectedCalled());
        assertEquals("The waypoint passed to the event should be the same as the one created", waypoint, event.getSelectedWaypoint());
    }

    // Additional tests for setters
    @Test
    public void testSetName() {
        // Arrange
        MyWaypoint waypoint = new MyWaypoint("Initial Name", new MockEventWaypoint(), new GeoPosition(40.7128, -74.0060));

        // Act
        waypoint.setName("New Name");

        // Assert
        assertEquals("The name should be updated to 'New Name'", "New Name", waypoint.getName());
    }

    @Test
    public void testSetButton() {
        // Arrange
        MyWaypoint waypoint = new MyWaypoint("Test Waypoint", new MockEventWaypoint(), new GeoPosition(40.7128, -74.0060));
        JButton newButton = new JButton("New Button");

        // Act
        waypoint.setButton(newButton);

        // Assert
        assertEquals("The button should be updated to the new button", newButton, waypoint.getButton());
    }
}
