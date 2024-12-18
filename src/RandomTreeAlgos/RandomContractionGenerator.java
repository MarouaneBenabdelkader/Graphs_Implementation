package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a random spanning tree by recursively contracting random edges.
 */
public class RandomContractionGenerator {
    private final Random rng;

    public RandomContractionGenerator() {
        this.rng = new Random();
    }

    public RandomContractionGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a random spanning tree of the given connected graph using edge contraction.
     *
     * This method:
     * 1. If the graph has only one vertex, return an empty set of edges.
     * 2. Otherwise, pick a random non-loop edge, contract it, recursively get a spanning
     *    tree of the smaller graph, and add the chosen edge.
     *
     * @param graph a connected graph
     * @return a set of edges forming a spanning tree
     */
    public ArrayList<Edge> generateRandomSpanningTree(Graph graph) {
        // Base case: if only one vertex, no edges are needed
        if (graph.order == 1) {
            return new ArrayList<>();
        }

        // Collect all non-loop edges
        ArrayList<Edge> candidates = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                for (Edge e : graph.getIncidency().get(v)) {
                    if (e.getSource() == v && e.getSource() != e.getDest()) {
                        // Add only once per undirected edge and ensure it's not a loop
                        candidates.add(e);
                    }
                }
            }
        }

        // Pick a random edge from candidates
        if (candidates.isEmpty()) {
            // If no candidates, return empty set (should not happen if graph >1 vertex and connected)
            return new ArrayList<>();
        }

        Edge chosen = candidates.get(rng.nextInt(candidates.size()));

        // Contract this edge
        contractEdge(graph, chosen);

        // Recursively find a spanning tree of the contracted graph
        ArrayList<Edge> subtree = generateRandomSpanningTree(graph);

        // Add chosen edge to subtree to form a spanning tree of the original graph
        subtree.add(chosen);
        return subtree;
    }

    /**
     * Contract the given edge in the graph:
     * 1. Identify the vertices u, v of edge e (u,v).
     * 2. Remove v from the graph and merge it into u.
     * 3. Replace all occurrences of v in edges with u.
     * 4. Remove loops (edges (u,u)) that may have been formed.
     *
     * After contraction:
     * - The graph has one fewer vertex.
     * - Some edges may need to be updated or removed.
     */
    private void contractEdge(Graph graph, Edge e) {
        int u = e.getSource();
        int v = e.getDest();
        // After contraction, v will be merged into u.

        // Step 1: We'll iterate over all edges currently incident to v
        // and redirect them to u.
        ArrayList<Edge> edgesToRedirect = new ArrayList<>(graph.getIncidency().get(v));

        // Delete vertex v at the end, but first handle edges
        for (Edge edge : edgesToRedirect) {
            int x = edge.oppositeExtremity(v);
            if (x == u) {
                // edge (v,x) = (v,u) will become (u,u) -> loop
                // Remove it after redirection
                continue;
            } else {
                // This edge connected v to x.
                // Now it should connect u to x (unless x == u)
                // Remove the old edge and add a new one (u,x) with the same weight.
                graph.removeEdge(edge);

                if (u != x) {
                    Edge newEdge = new Edge(u, x, edge.weight);
                    graph.addEdgeIfNotDuplicate(newEdge);
                }
            }
        }

        // Remove loops at u (if any)
        removeLoopsAt(graph, u);

        // Delete vertex v
        graph.deleteVertex(v);
    }

    /**
     * Remove loops at a given vertex (edges of form (v,v)).
     */
    private void removeLoopsAt(Graph graph, int v) {
        ArrayList<Edge> incidentEdges = new ArrayList<>(graph.getIncidency().get(v));
        for (Edge edge : incidentEdges) {
            if (edge.getSource() == edge.getDest()) {
                graph.removeEdge(edge);
            }
        }
    }
}
