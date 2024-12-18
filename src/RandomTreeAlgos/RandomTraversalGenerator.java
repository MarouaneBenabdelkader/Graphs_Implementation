package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class provides a method to generate a random spanning tree using a random traversal:
 * 1. Start from a random vertex.
 * 2. Maintain a "frontier" of edges leading out of visited vertices.
 * 3. Pick a random edge from the frontier.
 * 4. If it leads to an unvisited vertex, add it to the tree and update the frontier.
 * 5. Repeat until all vertices are visited or no more frontier edges are available.
 */
public class RandomTraversalGenerator {
    private final Random rng;

    public RandomTraversalGenerator() {
        this.rng = new Random();
    }

    public RandomTraversalGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a random spanning tree (or spanning forest if not connected) of the given graph.
     * @param graph The input graph.
     * @return A list of edges that form the random spanning tree.
     */
    public List<Edge> generateRandomSpanningTree(Graph graph) {
        // Choose a random starting vertex
        int start = chooseRandomStartVertex(graph);

        boolean[] visited = new boolean[graph.upperBound];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        visited[start] = true;

        List<Edge> treeEdges = new ArrayList<>();
        List<Edge> frontier = new ArrayList<>();

        // Add edges from start vertex
        for (Edge e : graph.getIncidency().get(start)) {
            // Add any edge that leads to an unvisited vertex (which should be all at this point)
            int other = e.oppositeExtremity(start);
            if (!visited[other]) {
                frontier.add(e);
            }
        }

        // While we have edges in the frontier
        while (!frontier.isEmpty()) {
            // Pick a random edge from the frontier
            int index = rng.nextInt(frontier.size());
            Edge chosen = frontier.remove(index);

            int u = chosen.getSource();
            int v = chosen.getDest();

            // Determine which endpoint is not visited yet
            int newVertex = visited[u] ? v : u;

            if (!visited[newVertex]) {
                // This edge leads to a new vertex, so add it to the tree
                treeEdges.add(chosen);
                visited[newVertex] = true;

                // Add all outgoing edges from this new vertex leading to unvisited vertices
                for (Edge e : graph.getIncidency().get(newVertex)) {
                    int w = e.oppositeExtremity(newVertex);
                    if (!visited[w]) {
                        frontier.add(e);
                    }
                }
            }
        }

        return treeEdges;
    }

    private int chooseRandomStartVertex(Graph graph) {
        // Gather all active vertices
        ArrayList<Integer> activeVertices = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                activeVertices.add(v);
            }
        }

        // Pick one at random
        int randomIndex = rng.nextInt(activeVertices.size());
        return activeVertices.get(randomIndex);
    }
}
