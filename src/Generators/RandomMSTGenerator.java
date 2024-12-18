package Generators;

import Graph.Edge;
import Graph.Graph;
import MSTAlgorithms.IMSTAlgorithm;
import Utilities.RandomWeightAssigner;

import java.util.ArrayList;

/**
 * Generates a random spanning tree by:
 * 1. Assigning a random weight in [0,1) to each edge independently
 * 2. Running an MST algorithm (e.g., Kruskal or Prim) to find the MST.
 */
public class RandomMSTGenerator {
    private final IMSTAlgorithm mstAlgorithm;
    private final RandomWeightAssigner weightAssigner;

    public RandomMSTGenerator(IMSTAlgorithm mstAlgorithm, RandomWeightAssigner weightAssigner) {
        this.mstAlgorithm = mstAlgorithm;
        this.weightAssigner = weightAssigner;
    }

    /**
     * Generates a random spanning tree.
     * @param graph the graph from which the spanning tree is generated
     * @return the edges of the randomly generated spanning tree
     */
    public ArrayList<Edge> generateRandomMST(Graph graph) {
        weightAssigner.assignRandomWeights(graph);
        return mstAlgorithm.computeMST(graph);
    }
}
