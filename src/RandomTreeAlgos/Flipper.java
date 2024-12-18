package RandomTreeAlgos;

import Graph.Edge;
import Graph.Graph;

import java.util.*;

/**
 * This class implements the flipping algorithm described in section 3.7.
 * It starts from a spanning tree T, and repeatedly performs flips to
 * eventually approach a uniform distribution over spanning trees.
 */
public class Flipper {
    private final Random rng;
    private Graph graph;

    // The spanning tree T is stored as a set of edges plus a parent array for orientation.
    private Set<Edge> treeEdges;
    private int[] parent; // parent[u] = parent of u in the oriented tree towards root
    private int root;     // current root of the tree

    public Flipper(Graph graph, Set<Edge> initialTree, int root) {
        this.graph = graph;
        this.treeEdges = new HashSet<>(initialTree);
        this.rng = new Random();
        this.root = root;
        // Initially, orient edges towards root
        reorientTree(root);
    }

    /**
     * Perform M flips on the tree.
     * After many flips, the distribution of T should approach uniform.
     */
    public void performFlips(int M) {
        for (int i = 0; i < M; i++) {
            doOneFlip();
        }
    }

    /**
     * Perform a single flip operation:
     * 1. Pick a non-tree edge (r,u) incident to root r.
     * 2. Add (r,u) to T, forming a cycle.
     * 3. Remove the unique T-edge from u (u,parent[u]).
     * 4. Set u as new root.
     * 5. Reorient edges towards u.
     */
    private void doOneFlip() {
        // Step 1: Find a non-tree edge incident to the current root
        Edge chosenEdge = chooseNonTreeEdgeIncidentToRoot();
        if (chosenEdge == null) {
            // If no non-tree edge is found, we could try another strategy or skip
            return;
        }

        int u = (chosenEdge.getSource() == root) ? chosenEdge.getDest() : chosenEdge.getSource();

        // Now adding (root,u) forms a cycle in T ∪ {chosenEdge}
        // Step 3: The unique T-edge from u is (u,parent[u]) because all edges are oriented towards root
        int p = parent[u];
        Edge edgeToRemove = findEdgeInTree(u, p);
        if (edgeToRemove == null) {
            // Should not happen if parent array is correct
            return;
        }

        // Modify T: Add chosenEdge and remove edgeToRemove
        treeEdges.remove(edgeToRemove);
        treeEdges.add(chosenEdge);

        // Step 4: New root is u
        root = u;

        // Step 5: Reorient edges towards new root
        reorientTree(root);
    }

    /**
     * Choose a non-tree edge incident to the current root.
     * Non-tree edge means an edge e not in treeEdges.
     * Incident to root means one endpoint is root.
     */
    private Edge chooseNonTreeEdgeIncidentToRoot() {
        List<Edge> candidates = new ArrayList<>();
        for (Edge e : graph.getIncidency().get(root)) {
            // Check if e is in T
            if (!treeEdges.contains(e)) {
                candidates.add(e);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        return candidates.get(rng.nextInt(candidates.size()));
    }

    /**
     * Reorient the edges so that they all point towards the new root.
     * We know treeEdges form a spanning tree. We'll do a BFS/DFS from root.
     * For BFS: start at root, parent[root]=-1, and assign parents so that
     * each edge in the tree is directed towards root.
     */
    private void reorientTree(int newRoot) {
        parent = new int[graph.upperBound];
        Arrays.fill(parent, -1);
        // Build adjacency for T from treeEdges (undirected)
        List<List<Integer>> adj = buildTreeAdjList();
        // BFS to assign parents
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(newRoot);
        parent[newRoot] = -1; // root has no parent
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neigh : adj.get(current)) {
                if (neigh != newRoot && parent[neigh] == -1 && neigh != current) {
                    // Assign parent to direct edge towards root
                    parent[neigh] = current;
                    queue.offer(neigh);
                }
            }
        }
    }

    /**
     * Build adjacency list for the current tree (as undirected)
     * from the set treeEdges.
     */
    private List<List<Integer>> buildTreeAdjList() {
        List<List<Integer>> adj = new ArrayList<>(graph.upperBound);
        for (int i = 0; i < graph.upperBound; i++) {
            adj.add(new ArrayList<>());
        }
        for (Edge e : treeEdges) {
            int u = e.getSource();
            int v = e.getDest();
            if (graph.isVertex(u) && graph.isVertex(v)) {
                adj.get(u).add(v);
                adj.get(v).add(u);
            }
        }
        return adj;
    }

    /**
     * Find the edge (u,parentU) or (parentU,u) in the treeEdges.
     */
    private Edge findEdgeInTree(int u, int parentU) {
        for (Edge e : treeEdges) {
            if ((e.getSource() == u && e.getDest() == parentU) ||
                    (e.getSource() == parentU && e.getDest() == u)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Returns the edges of the current tree.
     */
    public Set<Edge> getTreeEdges() {
        return new HashSet<>(treeEdges);
    }

    /**
     * Returns the current root of the tree.
     */
    public int getRoot() {
        return root;
    }
}
