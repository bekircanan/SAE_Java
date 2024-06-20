/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application;

/**
 *
 * @author A
 */
import org.jxmapviewer.viewer.DefaultWaypointRenderer;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import org.jxmapviewer.JXMapViewer;

public class WaypointRender implements WaypointRenderer<MyWaypoint> {
    private final DefaultWaypointRenderer defaultRenderer = new DefaultWaypointRenderer();

    /**
     *
     * @param g
     * @param map
     * @param waypoint
     * @return
     */
    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, MyWaypoint waypoint) {
        defaultRenderer.paintWaypoint(g, map, (Waypoint) waypoint);
        // You can customize the waypoint rendering here if needed
        
    }
}
