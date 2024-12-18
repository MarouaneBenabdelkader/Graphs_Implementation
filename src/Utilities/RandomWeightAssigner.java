package Utilities;

import Graph.Edge;
import Graph.Graph;

import java.util.Random;

/**
 * Assigns random weights to all edges in a graph.
 */
public class RandomWeightAssigner {
    private final Random rng;

    public RandomWeightAssigner() {
        this.rng = new Random();
    }

    public RandomWeightAssigner(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Assigns a random weight in the range [0,1) to each edge in the graph.
     * @param graph the graph whose edges will be assigned random weights
     */
    public void assignRandomWeights(Graph graph) {
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                for (Edge e : graph.getIncidency().get(v)) {
                    // Assign a random weight
                    e.setWeight(rng.nextDouble());
                }
            }
        }
    }
}
