package MSTAlgorithms;

import Graph.Edge;
import Graph.Graph;
import java.util.ArrayList;

public interface IMSTAlgorithm {
    /**
     * Computes the Minimum Spanning Tree (MST) of a given graph.
     * @param graph the input graph
     * @return a list of edges representing the MST
     */
    ArrayList<Edge> computeMST(Graph graph);
}
