# TP3
ici votre rapport


Below are pseudocode representations of the main algorithms discussed so far. We present the algorithms at a conceptual level, not tied to any particular programming language. Each piece of pseudocode is self-contained and follows standard algorithmic conventions.

## 1. Breadth-First Search (BFS) Tree Generation

**Goal:** Given a graph *G = (V, E)* and a starting vertex *r*, we want to produce a spanning tree of *G* by performing a BFS from *r*. This yields a tree structure where each edge used connects a vertex to its parent in the BFS.

**Pseudocode:**

```
BFS_GENERATE_TREE(G, r):
    Initialize a queue Q to empty
    Initialize an array visited of size |V|, set all visited[v] = false
    Initialize an empty list TREE_ARCS
    visited[r] = true
    Enqueue r into Q

    while Q is not empty:
        u = Dequeue(Q)
        for each edge (u, w) in G:
            if visited[w] == false:
                visited[w] = true
                Enqueue(w, Q)
                Add arc (u -> w) to TREE_ARCS
    return TREE_ARCS
```

**Result:** `TREE_ARCS` is a list of arcs that form a spanning tree (if G is connected).

---

## 2. Assigning Random Weights to Edges

**Goal:** Assign a random weight in [0,1) to each edge in the graph.

**Pseudocode:**

```
ASSIGN_RANDOM_WEIGHTS(G):
    for each vertex v in G:
        for each edge e in incidency[v]:
            if e.weight not already set:
                e.weight = random number in [0, 1)
```

**Note:** Typically, to avoid assigning the same edge twice (since edges appear in the adjacency of both endpoints), either ensure each undirected edge is only assigned once or store the assignment somewhere centralized.

---

## 3. Kruskal’s MST Algorithm

**Goal:** Given a weighted, connected graph *G = (V, E)*, compute its Minimum Spanning Tree (MST) using Kruskal's algorithm.

**Key Idea:** Sort edges by weight and then add them one by one to the MST if they don't form a cycle, using a Union-Find (Disjoint Set) data structure.

**Pseudocode:**

```
KRUSKAL_MST(G):
    MST_EDGES = empty list
    Sort all edges of G by weight in non-decreasing order
    Initialize a Union-Find structure UF for vertices in V

    for each edge e in sorted order:
        (u, v) = endpoints of e
        if FIND(UF, u) != FIND(UF, v):
            UNION(UF, u, v)
            Add e to MST_EDGES
        if |MST_EDGES| = |V| - 1:
            break

    return MST_EDGES
```

Where `FIND` and `UNION` are operations on the Union-Find data structure:
- `FIND(UF, x)` returns the representative of the set containing x.
- `UNION(UF, a, b)` merges the sets of a and b.

---

## 4. Random MST Generation via Random Weights

**Goal:** Generate a random spanning tree by assigning random weights and then finding the MST. Every time it runs, it produces a (likely) different MST, giving the impression of randomness.

**Approach:**
1. Assign random weights to each edge.
2. Run MST algorithm (e.g., Kruskal or Prim) to get the MST.

**Pseudocode:**

```
RANDOM_MST_GENERATION(G):
    ASSIGN_RANDOM_WEIGHTS(G)
    MST_EDGES = KRUSKAL_MST(G)
    return MST_EDGES
```

---

## 5. Prim’s MST Algorithm (Optional Alternative)

**Goal:** Given a weighted, connected graph *G = (V, E)*, compute its MST using Prim’s algorithm. This is just for completeness in case you choose Prim instead of Kruskal.

**Pseudocode:**

```
PRIM_MST(G, start_vertex):
    Initialize a priority queue PQ
    visited[v] = false for all v in V
    MST_EDGES = empty list
    visited[start_vertex] = true

    for each edge (start_vertex, w) in G:
        PQ.insert(edge (start_vertex, w) with weight w)

    while MST_EDGES.size < |V|-1:
        (u, v) = edge with minimum weight extracted from PQ
        if visited[v] == false:
            visited[v] = true
            Add (u, v) to MST_EDGES
            for each edge (v, x) in G:
                if visited[x] == false:
                    PQ.insert(edge (v, x))

    return MST_EDGES
```

---

**In summary:**

- **BFS_GENERATE_TREE:** Simple BFS that picks tree edges from discovery.
- **ASSIGN_RANDOM_WEIGHTS:** Assign random weights to all edges.
- **KRUSKAL_MST:** Build MST by sorting edges and using Union-Find.
- **RANDOM_MST_GENERATION:** Combine random weight assignment and MST computation to get a random spanning tree.
- **PRIM_MST (Optional):** An alternative MST method using a priority queue.

Random Traversal to Generate a Spanning Tree (Fully Random Frontier Selection)

Input: A connected graph
𝐺
=
(
𝑉
,
𝐸
)
G=(V,E)

Output: A spanning tree
𝑇
⊆
𝐸
T⊆E

Pseudocode:

csharp
Copy code
RANDOM_TRAVERSAL_SPANNING_TREE(G):
V = vertices of G
E = edges of G

    // Pick a random starting vertex
    r = a random vertex from V

    // Initialize data structures
    For each v in V:
        visited[v] = false
    visited[r] = true

    T = empty set of edges  // Will store the resulting spanning tree
    frontier = empty list of edges

    // Add edges from the starting vertex to the frontier
    For each edge e = (r, w) in E:
        frontier.add(e)

    While frontier is not empty:
        // Pick a random edge from the frontier
        e = a random edge from frontier
        frontier.remove(e)

        Let e = (u, v)
        If visited[v] = false:
            // This edge leads to a new vertex
            visited[v] = true
            Add e to T

            // Add all edges from v leading to unvisited vertices to the frontier
            For each edge f = (v, x) in E:
                If visited[x] = false:
                    frontier.add(f)

    return T
Alternative Approaches:

Random BFS:
Instead of picking a random edge from the entire frontier, maintain a queue-like structure but when adding a new layer of vertices, randomize the order of the edges you enqueue. For example:

When you discover new vertices at the same distance level, insert their outgoing edges into the frontier in a random order.
Pseudocode sketch for random BFS:

sql
Copy code
RANDOM_BFS_SPANNING_TREE(G):
choose random r from V
visited[v] = false for all v
visited[r] = true
T = empty set
queue = empty queue

    // Insert all edges from r in random order
    edges_from_r = all edges from r
    Shuffle(edges_from_r)
    for e in edges_from_r:
        queue.enqueue(e)

    while queue not empty:
        e = queue.dequeue()
        (u, v) = endpoints of e
        if visited[v] = false:
            visited[v] = true
            T.add(e)

            // Add edges from v in random order
            edges_from_v = all edges from v to unvisited nodes
            Shuffle(edges_from_v)
            for f in edges_from_v:
                queue.enqueue(f)
    return T
## 3.3 Pseudocode:

java
Copy code
INPUT: Graph G = (V, E)  // assumed connected
OUTPUT: A random spanning tree T

RANDOM_INSERTION_SPANNING_TREE(G):
n = number of vertices in G
T = empty set of edges
UF = UnionFind structure over the vertices of G
E_list = list of all edges in G

    // Initialize Union-Find
    for v in V:
        MAKE-SET(UF, v)

    // Repeat until we have n-1 edges
    while |T| < n - 1:
        // Randomly select an edge e = (u, v) from E_list
        e = E_list[random index]

        // Check if adding e creates a cycle
        if FIND(UF, u) ≠ FIND(UF, v):
            // No cycle, we can add this edge
            T = T ∪ {e}
            UNION(UF, u, v)
        else
            // Adding e would create a cycle, ignore this edge and continue

    return T
Key Steps Explained:

Initialization:

T is initially empty.
Use Union-Find to keep track of sets of connected components.
Initially, each vertex is in its own set (MAKE-SET).
Random Edge Selection:

On each iteration, choose a random edge from E. This edge choice is uniformly at random and independent of previous choices.
Cycle Detection (Union-Find):

To decide whether adding the chosen edge creates a cycle, we simply check if both endpoints belong to the same set using FIND(UF, u) and FIND(UF, v).
If they are different, joining them does not create a cycle. We add e to T and call UNION(UF, u, v).
If they are the same, adding e would create a cycle, so we skip it.
Termination:

Stop once we have n-1 edges in T. At that point, T is a spanning tree of G.


## 3.4 Pseudocode:

Idea:
Perform a random walk on the graph until every vertex has been visited at least once. For each vertex (except the initial one), record the first edge that brought you into that vertex. These recorded edges form a random spanning tree.

Pseudocode:

sql
Copy code
INPUT: A connected undirected graph G = (V, E)
OUTPUT: A random spanning tree T

ALDOUS_BRODER(G):
// Initialization
n = |V|
Choose an initial vertex v0 uniformly at random from V
visited[v0] = true
countVisited = 1
current = v0
T = empty set of edges

    while countVisited < n:
        // Pick a random neighbor of current
        neighbors = { w | (current, w) ∈ E }
        next = a random vertex chosen uniformly from neighbors

        if visited[next] = false:
            // This is the first time we reach 'next'
            visited[next] = true
            countVisited = countVisited + 1
            Add the edge (current, next) to T  // The edge that leads into 'next'
        
        current = next
    
    return T
Key Steps:

Starting Point:
Select a random starting vertex
𝑣
0
v
0
​
from
𝑉
V. Mark it as visited and set current = v_0.

Random Walk:
From the current vertex, choose a random neighbor uniformly and move current to that neighbor. This step simulates a random walk on the graph.

Visiting New Vertices:
If this newly reached vertex was not visited before, record the edge used to reach it in the spanning tree
𝑇
T. Mark the vertex as visited and increment the count of visited vertices.

Termination:
Continue the random walk until all vertices are visited. At that point,
𝑇
T will have exactly
𝑛
−
1
n−1 edges, forming a spanning tree.

Result:
The Aldous-Broder algorithm produces a uniformly chosen random spanning tree of
𝐺
G, meaning each spanning tree of
𝐺
G is equally likely to be chosen.

3.5 Pseudocode:

Idea:
To construct a random spanning tree of a connected graph
𝐺
G:

If
𝐺
G has only one vertex, return an empty edge set.
Otherwise, pick a random non-loop edge
𝑒
e in
𝐺
G.
Contract
𝑒
e in
𝐺
G to produce a smaller graph
𝐺
′
G
′
with one fewer vertex.
Recursively find a random spanning tree
𝑇
′
T
′
of
𝐺
′
G
′
.
Add
𝑒
e to
𝑇
′
T
′
to form a spanning tree
𝑇
T of
𝐺
G.
Pseudocode:

scss
Copy code
RANDOM_SPANNING_TREE_CONTRACTION(G):
if |V(G)| = 1:
return ∅  // A single vertex has no edges, so empty tree

    // Step 1: Choose a non-loop edge at random
    E_non_loop = { e ∈ E(G) | srcG(e) != dstG(e) }
    e = a random edge from E_non_loop

    // Step 2: Contract edge e
    G' = CONTRACT_EDGE(G, e)

    // Step 3: Recursively compute a random spanning tree of G'
    T' = RANDOM_SPANNING_TREE_CONTRACTION(G')

    // Step 4: Add edge e to T' to form a spanning tree of G
    T = T' ∪ {e}
    return T
Edge Contraction Subroutine:

vbnet
Copy code
CONTRACT_EDGE(G, e):
// Let e connect vertices u and v
u = srcG(e)
v = dstG(e)

    // Step 1: Remove vertex v from G, merging it into u.
    // Redirect all edges incident to v to u.
    For each edge f = (v, x) in E(G):
        Remove f from G
        If x ≠ u:
            Add edge (u, x) to G (unless it creates a loop)
    
    // Remove loops of the form (u, u) if they appear
    Remove any loops (u, u) from G

    Delete vertex v from G

    return G
Key Points:

Loop Edges: Edges where the source and destination are the same vertex after contraction should be removed because they do not contribute to a spanning structure.
Parallel Edges: If multiple edges become parallel after contraction, it is not strictly a problem for the spanning tree construction. They can be kept as is, or you may choose to remove duplicates if desired.
Uniformity: If every non-loop edge is chosen with equal probability, this algorithm produces a uniformly random spanning tree.
Result:
After recursively contracting edges until only one vertex remains in the reduced graphs, we accumulate exactly
∣
𝑉
(
𝐺
)
∣
−
1
∣V(G)∣−1 edges that form a spanning tree. Each run can yield a different random spanning tree, making the selection uniform over the space of all spanning trees.


## 3.6 Pseudocode:

Idea:

Start with a single vertex
𝑣
v in the tree
𝑇
T.
While not all vertices are in
𝑇
T:
Pick a vertex
𝑢
u not in
𝑇
T.
Perform a random walk starting from
𝑢
u until hitting some vertex
𝑤
w in
𝑇
T.
During the walk, record the "parent" of each visited vertex by the edge used to enter it.
Once the walk reaches
𝑤
w in
𝑇
T, use the recorded parents to backtrack from
𝑢
u to
𝑤
w, naturally skipping any cycles formed (since cycle edges are overwritten by later edges).
Add the resulting path (no cycles) to
𝑇
T.
Pseudocode:

csharp
Copy code
WILSON(G):
Choose an initial vertex v ∈ V(G)
T = {v}  // tree initially contains one vertex and no edges
parent[] = array of size |V|, initially undefined

    while |T| < |V|:
        // Pick a vertex u not in T
        choose u ∈ V(G)\T

        // Random walk from u until hitting T
        current = u
        while current ∉ T:
            neighbors = { w | (current,w) ∈ E(G) }
            next = a random vertex chosen uniformly from neighbors
            parent[current] = next
            current = next
        // now current is in T

        // Reconstruct the path from u to current by following parent marks
        // This automatically removes cycles because parent[] is overwritten 
        // when a cycle is about to form.
        path = empty list
        x = u
        while x ∉ T:
            y = parent[x]
            path.add((x,y))
            x = y

        // Add vertices and edges from path to T
        for each edge (a,b) in path:
            T = T ∪ {a,b} and edges connecting them

    return T
Key Steps Explained:

Random Walk and Marking Parents:
Each new vertex encountered in the random walk (not in
𝑇
T) is given a "parent" (the edge from which it was entered). If a cycle would occur, the parent of a vertex on the cycle path is overwritten by the next random step, effectively breaking the cycle.

Cycle Removal Via Backtracking:
Once the random walk hits the tree at some vertex
𝑤
w, we have a chain of parents leading back to
𝑢
u. Following these parents from
𝑢
u towards
𝑤
w yields a simple path (no cycles), as any cycle edges would have been overwritten.

Adding the New Path to the Tree:
The discovered path is added to the tree
𝑇
T. This increases the number of vertices in
𝑇
T. Repeat until all vertices are included.

Result:
Wilson’s algorithm, like Aldous-Broder, produces a uniformly random spanning tree of the graph. Each spanning tree is equally likely to be chosen.

## 3.7 Pseudocode:

Idea of the Flip Operation:

Initially, have a spanning tree
𝑇
T.
Pick a non-tree edge
𝑒
e (i.e., an edge in
𝐸
(
𝐺
)
∖
𝑇
E(G)∖T).
Adding
𝑒
e to
𝑇
T forms a unique cycle since
𝑇
T was a tree.
Choose an edge
𝑒
′
e
′
from this cycle that is already in
𝑇
T.
Update
𝑇
T by removing
𝑒
′
e
′
and adding
𝑒
e. This is the "flip".
Repeat the flip many times.
A Simplified Approach (Root-based):

Root the tree
𝑇
T at some vertex
𝑟
r.
Orient all edges of
𝑇
T towards
𝑟
r.
To perform a flip:
Choose an edge
(
𝑟
,
𝑢
)
(r,u) that is not in
𝑇
T but is incident to
𝑟
r.
In the unique cycle formed by
𝑇
∪
{
(
𝑟
,
𝑢
)
T∪{(r,u), find the outgoing edge from
𝑢
u in
𝑇
T.
Remove that edge from
𝑇
T and add
(
𝑟
,
𝑢
)
(r,u).
Make
𝑢
u the new root.
Reorient the tree towards
𝑢
u.
Pseudocode:

arduino
Copy code
FLIP_BASED_TREE_RANDOMIZER(G, T):
// Input:
// G = (V,E) connected graph
// T = spanning tree of G

    // Choose an initial root r in T
    r = CHOOSE_ROOT(T)
    ORIENT_EDGES_TOWARDS_ROOT(T, r)

    // Repeat the flipping process many times
    for i in 1 to M:  // M large enough to approach uniform distribution
        // Step 1: Choose a non-tree edge incident to r
        nonTreeEdges = { e = (r, x) ∈ E(G)\T }
        if nonTreeEdges is empty:
            // If no such edge, choose another way or pick a different root
            // or pick any non-tree edge and reroot accordingly
            e = CHOOSE_NON_TREE_EDGE(E(G)\T)
            // Reroot T so that an endpoint of e is the root 
            // and try again. (Not strictly in original instructions, 
            // but a fallback scenario)
        else:
            e = a random edge chosen from nonTreeEdges

        // Step 2: Add e to T, forming a cycle
        cycleEdges = FIND_CYCLE(T ∪ {e})

        // Step 3: In the cycle, find the unique edge e' that is part of T
        // but leads away from the root in T's orientation.
        // According to the simplification:
        // If e = (r,u), then the cycle must contain the edge (u, parentOfUInT) or 
        // more simply, the unique outgoing edge from u in T.
        u = endpoint of e other than r
        e' = the outgoing edge from u in T  // since all edges in T point towards r, u→parent(u)

        // Step 4: Flip the edges
        T = (T \ {e'}) ∪ {e}

        // Step 5: Update root to u and reorient edges towards u
        r = u
        ORIENT_EDGES_TOWARDS_ROOT(T, r)

    return T
Details:

Choosing the root
𝑟
r:
Start with any vertex as root or pick the vertex with the highest degree, or choose randomly.

Orienting Edges:
Once
𝑟
r is chosen, for each vertex other than
𝑟
r, set its parent to be the vertex towards which the unique path to
𝑟
r leads. In other words, each vertex except
𝑟
r has exactly one outgoing edge towards
𝑟
r.

Cycle Detection:
When you add the chosen non-tree edge
𝑒
=
(
𝑟
,
𝑢
)
e=(r,u) to
𝑇
T, there is exactly one cycle. With the orientation, it's easy to identify the unique outgoing edge from
𝑢
u on that cycle.

Runtime:
The algorithm requires performing many flips (the instructions mention
𝑂
(
∣
𝑉
∣
3
)
O(∣V∣
3
) flips) to get close to a uniform distribution over all spanning trees. Each flip modifies
𝑇
T slightly, and after enough steps, the distribution of
𝑇
T will approximate a uniform random spanning tree.

Result: Repeated flips will, in the long run, lead to a near-uniform random spanning tree, although it may be slower compared to other algorithms like Aldous-Broder or Wilson’s algorithm.