package MSTAlgorithms;

import Graph.Edge;
import Graph.Graph;

import java.util.ArrayList;
import java.util.Collections;

public class KruskalMSTAlgorithm implements IMSTAlgorithm {

    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++)
                parent[i] = i;
        }

        public int find(int x) {
            if (parent[x] == x) return x;
            parent[x] = find(parent[x]);
            return parent[x];
        }

        public boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) return false;

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

    @Override
    public ArrayList<Edge> computeMST(Graph graph) {
        // Collect all edges from the graph
        ArrayList<Edge> allEdges = new ArrayList<>();
        for (int v = 0; v < graph.upperBound; v++) {
            if (graph.isVertex(v)) {
                for (Edge e : graph.getIncidency().get(v)) {
                    // To avoid duplication, only add edges where v is the source
                    // This ensures each undirected edge is added once
                    if (e.getSource() == v) {
                        allEdges.add(e);
                    }
                }
            }
        }

        // Sort edges by weight
        Collections.sort(allEdges);

        UnionFind uf = new UnionFind(graph.upperBound);
        ArrayList<Edge> mst = new ArrayList<>();
        for (Edge e : allEdges) {
            if (uf.union(e.getSource(), e.getDest())) {
                mst.add(e);
                // If we have V-1 edges, MST is complete
                if (mst.size() == graph.order - 1) break;
            }
        }
        return mst;
    }
}
