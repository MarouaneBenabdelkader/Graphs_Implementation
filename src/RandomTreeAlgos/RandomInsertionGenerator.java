package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a random spanning tree by randomly inserting edges:
 * 1. Start with empty set F.
 * 2. Pick a random edge e from E(G).
 * 3. If adding e does not create a cycle (checked via Union-Find), add it to F.
 * 4. Repeat until |F| = |V| - 1.
 */
public class RandomInsertionGenerator {
    private final Random rng;

    public RandomInsertionGenerator() {
        this.rng = new Random();
    }

    public RandomInsertionGenerator(long seed) {
        this.rng = new Random(seed);
    }

    public List<Edge> generateRandomSpanningTree(Graph graph) {
        int n = graph.order;
        // Collect all edges (undirected) once
        ArrayList<Edge> allEdges = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                for (Edge e : graph.getIncidency().get(v)) {
                    // To avoid duplicates, only add if v is the source
                    if (e.getSource() == v) {
                        allEdges.add(e);
                    }
                }
            }
        }

        UnionFind uf = new UnionFind(graph.upperBound);
        ArrayList<Edge> spanningTree = new ArrayList<>();

        // Keep going until we have |V| - 1 edges in the spanning tree
        while (spanningTree.size() < n - 1) {
            // Pick a random edge from allEdges
            Edge chosen = allEdges.get(rng.nextInt(allEdges.size()));
            int u = chosen.getSource();
            int v = chosen.getDest();

            // Try to unite their sets
            if (uf.union(u, v)) {
                // If union succeeded without cycle, add edge to spanning tree
                spanningTree.add(chosen);
            }
            // If a cycle would have been formed, do nothing and pick another edge
        }

        return spanningTree;
    }

    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);

            if (rootA == rootB) return false; // Cycle detected

            if (rank[rootA] < rank[rootB]) {
                parent[rootA] = rootB;
            } else if (rank[rootA] > rank[rootB]) {
                parent[rootB] = rootA;
            } else {
                parent[rootB] = rootA;
                rank[rootA]++;
            }
            return true;
        }
    }
}
