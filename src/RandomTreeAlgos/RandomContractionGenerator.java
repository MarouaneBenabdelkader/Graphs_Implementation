package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomContractionGenerator {
    private final Random rng;

    public RandomContractionGenerator() {
        this.rng = new Random();
    }

    public RandomContractionGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a random spanning tree using edge contraction.
     */
    public ArrayList<Edge> generateRandomSpanningTree(Graph originalGraph) {
        // Create a working copy of the graph
        Graph graph = originalGraph.copy();
        ArrayList<Edge> treeEdges = new ArrayList<>();

        // Map to keep track of merged vertices
        HashMap<Integer, Integer> vertexMapping = new HashMap<>();
        for (int i = 0; i < graph.upperBound; i++) {
            if (graph.isVertex(i)) {
                vertexMapping.put(i, i);
            }
        }

        while (graph.getActiveVertexCount() > 1) {
            // Get all valid edges for contraction
            ArrayList<Edge> candidates = new ArrayList<>();
            for (int v = 0; v < graph.upperBound; v++) {
                if (graph.isVertex(v)) {
                    for (Edge e : graph.getIncidency().get(v)) {
                        if (e.getSource() == v && e.getSource() != e.getDest()) {
                            candidates.add(e);
                        }
                    }
                }
            }

            if (candidates.isEmpty()) {
                throw new IllegalStateException("No valid edges found in connected graph with multiple vertices");
            }

            // Choose a random edge
            Edge chosen = candidates.get(rng.nextInt(candidates.size()));

            // Map vertices through all merges to get original vertices
            int originalSource = getOriginalVertex(chosen.getSource(), vertexMapping);
            int originalDest = getOriginalVertex(chosen.getDest(), vertexMapping);

            // Add edge between original vertices to tree
            treeEdges.add(new Edge(originalSource, originalDest, chosen.weight));

            // Contract the edge
            contractEdge(graph, chosen, vertexMapping);
        }

        return treeEdges;
    }

    private int getOriginalVertex(int current, HashMap<Integer, Integer> vertexMapping) {
        int original = current;
        while (vertexMapping.get(original) != original) {
            original = vertexMapping.get(original);
        }
        // Path compression - update all intermediate mappings
        if (current != original) {
            vertexMapping.put(current, original);
        }
        return original;
    }

    private void contractEdge(Graph graph, Edge e, HashMap<Integer, Integer> vertexMapping) {
        int u = e.getSource();
        int v = e.getDest();

        // Update vertex mapping
        vertexMapping.put(v, u);

        // Get all edges incident to v
        Set<Edge> edgesToProcess = new HashSet<>(graph.getIncidency().get(v));

        // Process each edge
        for (Edge edge : edgesToProcess) {
            if (edge != e) {  // Skip the contracted edge
                int otherEnd = edge.oppositeExtremity(v);
                if (otherEnd != u) {  // Skip if it would create a self-loop
                    // Remove the old edge
                    graph.removeEdge(edge);
                    // Add new edge connecting u to otherEnd
                    Edge newEdge = new Edge(u, otherEnd, edge.weight);
                    graph.addEdgeIfNotDuplicate(newEdge);
                }
            }
        }

        // Remove the contracted edge and vertex
        graph.removeEdge(e);
        graph.deleteVertex(v);
    }
}