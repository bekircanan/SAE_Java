/*
 * Cliquez sur nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt pour modifier cette licence
 * Cliquez sur nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java pour modifier ce modèle
 */
package construction;

import java.util.Random;
import java.util.Comparator;
import org.graphstream.graph.Node;

/**
 * La classe {@code NodeComparator} est un comparateur de nœuds basé sur le degré de saturation (dsat) et un critère aléatoire.
 * Elle implémente l'interface {@link Comparator} pour comparer des objets de type {@link Node}.
 */
public class NodeComparator implements Comparator<Node> {

    private final Random random = new Random();

    /**
     * Compare deux nœuds en fonction de leur degré de saturation (dsat).
     * En cas d'égalité de dsat, utilise un critère aléatoire pour déterminer l'ordre.
     *
     * @param nodeA le premier nœud à comparer
     * @param nodeB le deuxième nœud à comparer
     * @return un entier négatif, zéro ou positif si le premier nœud est inférieur, égal ou supérieur au deuxième nœud
     */
    @Override
    public int compare(Node nodeA, Node nodeB) {
        int dsatComparison = Integer.compare(nodeB.getAttribute("dsat"), nodeA.getAttribute("dsat"));
        if (dsatComparison != 0) {
            return dsatComparison;
        } else {
            // En cas d'égalité de dsat, utilise un critère aléatoire
            return random.nextBoolean() ? 1 : -1;
        }
    }

}
