package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements the Aldous-Broder algorithm.
 * It performs a random walk on the graph until all vertices have been visited.
 * Each time a new vertex is visited, the edge leading to it is added to the spanning tree.
 */
public class AldousBroderGenerator {
    private final Random rng;

    public AldousBroderGenerator() {
        this.rng = new Random();
    }

    public AldousBroderGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a uniformly random spanning tree using the Aldous-Broder algorithm.
     * @param graph The input graph (assumed connected).
     * @return A list of edges forming a random spanning tree.
     */
    public List<Edge> generateRandomSpanningTree(Graph graph) {
        int n = graph.order;
        boolean[] visited = new boolean[graph.upperBound];
        int visitedCount = 0;

        // Collect all active vertices
        ArrayList<Integer> activeVertices = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                activeVertices.add(v);
            }
        }

        // Choose a random initial vertex
        int start = activeVertices.get(rng.nextInt(activeVertices.size()));
        visited[start] = true;
        visitedCount = 1;

        List<Edge> spanningTree = new ArrayList<>();
        int current = start;

        // Perform random walk until all vertices are visited
        while (visitedCount < n) {
            // Get all edges from 'current'
            List<Edge> edgesFromCurrent = graph.getIncidency().get(current);

            // Pick a random edge (current, next)
            Edge chosen = edgesFromCurrent.get(rng.nextInt(edgesFromCurrent.size()));
            int next = (chosen.getSource() == current) ? chosen.getDest() : chosen.getSource();

            // If 'next' is unvisited, mark it visited and add edge to spanning tree
            if (!visited[next]) {
                visited[next] = true;
                visitedCount++;
                spanningTree.add(chosen);
            }

            // Move to the next vertex
            current = next;
        }

        return spanningTree;
    }
}
