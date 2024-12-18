package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WilsonGenerator {
    private final Random rng;

    public WilsonGenerator() {
        this.rng = new Random();
    }

    public WilsonGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a uniformly random spanning tree using Wilson's algorithm.
     * @param graph the input graph (assumed connected)
     * @return a list of edges forming a random spanning tree
     */
    public List<Edge> generateRandomSpanningTree(Graph graph) {
        int n = graph.order;
        boolean[] inTree = new boolean[graph.upperBound];
        for (int i = 0; i < inTree.length; i++) {
            inTree[i] = false;
        }

        // Choose initial vertex (e.g., vertex with max degree)
        int root = chooseInitialVertex(graph);
        inTree[root] = true;

        List<Edge> spanningTree = new ArrayList<>();

        // While not all vertices are in the tree
        while (!allVerticesInTree(inTree, n)) {
            int u = chooseRandomOutsideVertex(inTree, graph);
            // parent[v] = the next vertex visited after v in the random walk
            // parent is used to reconstruct the path
            int[] parent = new int[graph.upperBound];
            for (int i = 0; i < parent.length; i++) parent[i] = -1;

            int current = u;
            while (!inTree[current]) {
                // Perform random walk step
                ArrayList<Edge> edges = new ArrayList<>(graph.getIncidency().get(current));
                Edge chosen = edges.get(rng.nextInt(edges.size()));
                int next = (chosen.getSource() == current) ? chosen.getDest() : chosen.getSource();
                parent[current] = next;
                current = next;
            }

            // current is now in the tree
            // Reconstruct cycle-free path from u to current using parent pointers
            // and add it to the tree.
            addPathToTree(u, current, parent, inTree, spanningTree, graph);
        }

        return spanningTree;
    }

    /**
     * Choose the initial vertex for Wilson's algorithm.
     * According to instructions, prefer a vertex of maximum degree.
     */
    private int chooseInitialVertex(Graph graph) {
        int maxDeg = -1;
        int vertex = 0;
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                int deg = graph.getIncidency().get(v).size();
                if (deg > maxDeg) {
                    maxDeg = deg;
                    vertex = v;
                }
            }
        }
        return vertex;
    }

    /**
     * Check if all active vertices are in the tree.
     */
    private boolean allVerticesInTree(boolean[] inTree, int n) {
        int count = 0;
        for (int i = 0; i < inTree.length; i++) {
            if (inTree[i]) count++;
        }
        return count == n;
    }

    /**
     * Choose a random vertex that is not yet in the tree.
     */
    private int chooseRandomOutsideVertex(boolean[] inTree, Graph graph) {
        ArrayList<Integer> outside = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v) && !inTree[v]) {
                outside.add(v);
            }
        }
        return outside.get(rng.nextInt(outside.size()));
    }

    /**
     * Add the path from u to current (which is in T) following parent pointers.
     * This path does not contain cycles because parent overwriting removed them.
     * Once we've reached a vertex in T, we can stop.
     */
    private void addPathToTree(int u, int current, int[] parent,
                               boolean[] inTree, List<Edge> spanningTree, Graph graph) {
        // We'll reconstruct the path backwards from u until we hit a vertex in T
        // The path is: u -> parent[u] -> parent[ parent[u] ] -> ... until we reach 'current' in T
        // Actually, we know current is in T, so we climb from u up until we find a vertex in T.
        int x = u;
        while (!inTree[x]) {
            int y = parent[x]; // y is the next vertex in the chain
            // Add edge (x,y) to the spanning tree
            Edge e = findEdgeInGraph(graph, x, y);
            if (e != null) {
                spanningTree.add(e);
            }
            inTree[x] = true;
            x = y; // move upwards
        }
    }

    /**
     * Find the edge (x,y) or (y,x) in the graph incidency list.
     */
    private Edge findEdgeInGraph(Graph graph, int x, int y) {
        for (Edge e : graph.getIncidency().get(x)) {
            if ((e.getSource() == x && e.getDest() == y) ||
                    (e.getSource() == y && e.getDest() == x)) {
                return e;
            }
        }
        return null; // should not happen if graph is connected
    }
}
