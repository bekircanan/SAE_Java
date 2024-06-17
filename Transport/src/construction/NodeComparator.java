/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package construction;

import java.util.Random;
import java.util.Comparator;
import org.graphstream.graph.Node;

public class NodeComparator implements Comparator<Node> {



    private final Random random = new Random();

    @Override
    public int compare(Node nodeA, Node nodeB) {
        int dsatComparison = Integer.compare(nodeB.getAttribute("dsat"), nodeA.getAttribute("dsat"));
        if (dsatComparison != 0) {
            return dsatComparison;
        } else {

            return random.nextBoolean() ? 1 : -1;
        }
    }

}
