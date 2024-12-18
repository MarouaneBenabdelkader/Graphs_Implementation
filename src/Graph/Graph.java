package Graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    public int order;
    public int upperBound;
    int edgeCardinality;

    private ArrayList<LinkedList<Edge>> incidency;   // Undirected edges
    private ArrayList<LinkedList<Arc>> inIncidency;  // Incoming arcs
    private ArrayList<LinkedList<Arc>> outIncidency; // Outgoing arcs

    public ArrayList<LinkedList<Edge>> getIncidency() {
        return incidency;
    }

    public ArrayList<LinkedList<Arc>> getInIncidency() {
        return inIncidency;
    }

    public ArrayList<LinkedList<Arc>> getOutIncidency() {
        return outIncidency;
    }

    boolean[] active; // track which vertices are currently part of the graph

    public Graph(int upperBound) {
        this.upperBound = upperBound;
        this.order = upperBound; // Initially, assume all vertices are available
        this.edgeCardinality = 0;

        // Initialize lists
        incidency = new ArrayList<>(upperBound);
        inIncidency = new ArrayList<>(upperBound);
        outIncidency = new ArrayList<>(upperBound);

        for (int i = 0; i < upperBound; i++) {
            incidency.add(new LinkedList<>());
            inIncidency.add(new LinkedList<>());
            outIncidency.add(new LinkedList<>());
        }

        // Initially all vertices active
        active = new boolean[upperBound];
        for (int i = 0; i < upperBound; i++) {
            active[i] = true;
        }
    }

    public boolean isVertex(int vertex) {
        return vertex >= 0 && vertex < upperBound && active[vertex];
    }

    public void addVertex(int vertex) {
        // Only makes sense if vertex < upperBound
        // and if it was previously inactive.
        if (vertex >= 0 && vertex < upperBound && !active[vertex]) {
            active[vertex] = true;
            order++;
        }
    }

    public void deleteVertex(int vertex) {
        if (!isVertex(vertex)) return;
        // Remove all edges and arcs involving this vertex
        // First remove from incidency and update the other endpoints
        for (Edge e : incidency.get(vertex)) {
            int other = e.oppositeExtremity(vertex);
            incidency.get(other).removeIf(edge -> edge == e);
            // Remove corresponding arcs
            // outArc from vertex to other and inArc from other to vertex
            outIncidency.get(vertex).removeIf(a -> a.getDest() == other);
            inIncidency.get(other).removeIf(a -> a.getSource() == vertex);

            // Also remove the reverse
            inIncidency.get(vertex).removeIf(a -> a.getSource() == other);
            outIncidency.get(other).removeIf(a -> a.getDest() == vertex);

            edgeCardinality--;
        }
        incidency.get(vertex).clear();
        inIncidency.get(vertex).clear();
        outIncidency.get(vertex).clear();

        // Mark vertex as inactive
        active[vertex] = false;
        order--;
    }

    public void ensureVertex(int vertex) {
        // If not active, re-activate it.
        if (vertex >= 0 && vertex < upperBound && !active[vertex]) {
            active[vertex] = true;
            order++;
        }
    }

    public void addArc(Arc arc) {
        // Add arc to outIncidency of its source and inIncidency of its destination
        int source = arc.getSource();
        int dest = arc.getDest();
        if (!isVertex(source) || !isVertex(dest)) return;
        outIncidency.get(source).add(arc);
        inIncidency.get(dest).add(arc);
    }

    public void addEdge(Edge edge) {
        int u = edge.getSource();
        int v = edge.getDest();
        if (!isVertex(u) || !isVertex(v) || u == v) return;

        // For undirected edge, add to incidency of both endpoints
        incidency.get(u).add(edge);
        incidency.get(v).add(edge);

        // Add corresponding arcs
        Arc forwardArc = new Arc(edge, false); // u -> v
        Arc backwardArc = new Arc(edge, true); // v -> u

        addArc(forwardArc);
        addArc(backwardArc);

        edgeCardinality++;
    }

    public Arc[] outEdges(int vertex) {
        return outIncidency.get(vertex).toArray(new Arc[0]);
    }

    public void removeEdge(Edge edge) {
        int u = edge.getSource();
        int w = edge.getDest();
        incidency.get(u).remove(edge);
        incidency.get(w).remove(edge);

        inIncidency.get(u).removeIf(a -> a.support == edge);
        outIncidency.get(u).removeIf(a -> a.support == edge);
        inIncidency.get(w).removeIf(a -> a.support == edge);
        outIncidency.get(w).removeIf(a -> a.support == edge);

        edgeCardinality--;
    }

    public void addEdgeIfNotDuplicate(Edge newEdge) {
        int u = newEdge.getSource();
        int x = newEdge.getDest();

        // Check for existing edge between u and x
        boolean exists = false;
        for (Edge e : incidency.get(u)) {
            if ((e.getSource() == u && e.getDest() == x) ||
                    (e.getSource() == x && e.getDest() == u)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            addEdge(newEdge);
        }
    }

}
