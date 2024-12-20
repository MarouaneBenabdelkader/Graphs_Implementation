# TP3 - Génération d'arbres couvrants aléatoires

**Binôme :** Marouane BENADELKADER & Imad DAIM

## Présentation

Ce projet implémente plusieurs algorithmes de génération d'arbres couvrants aléatoires sur un graphe. L'objectif est de comparer différentes méthodes pour obtenir un arbre couvrant choisi (idéalement) de manière uniforme parmi tous les arbres couvrants du graphe.

## Avancement et Algorithmes Implémentés

Nous avons implémenté les algorithmes suivants, tels que décrits dans le sujet :

1. **Arbres couvrants par affectation de poids aléatoires (3.1)**
2. **Parcours aléatoire (3.2)**
3. **Insertion aléatoire d’arêtes (3.3)**
4. **Algorithme d’Aldous-Broder (3.4)**
5. **Par contraction d’arêtes (3.5)**
6. **Algorithme de Wilson (3.6)**
7. **Par flips successifs (3.7)**


## Utilisation du Projet

1. **Compilation :**  
   Un fichier `Makefile` est fourni. Pour compiler, lancez simplement :  
   `make compile`

2. **Exécution :**  
   Après compilation, lancez :  
   `make run`


---

### (3.1) Arbre couvrant par poids aléatoires (Random MST)

**Idée :**  
Attribuer à chaque arête un poids aléatoire dans [0,1), puis calculer un arbre couvrant de poids minimum (ACM) avec Kruskal (ou Prim). L’ACM obtenu, dépendant de poids aléatoires, est ainsi différent à chaque exécution, donnant un effet aléatoire.

**Pseudocode :**
```
RANDOM_MST_GENERATION(G):
    ASSIGN_RANDOM_WEIGHTS(G)      // Donne un poids aléatoire dans [0,1) à chaque arête
    T = KRUSKAL_MST(G)            // Calcule l’ACM avec l’algorithme de Kruskal
    return T
```

```
ASSIGN_RANDOM_WEIGHTS(G):
    Pour chaque arête e ∈ E(G):
        e.weight = nombre_aleatoire_uniforme_dans_[0,1)
```

```
KRUSKAL_MST(G):
    T = ∅
    Trier toutes les arêtes de G par poids croissant
    UF = structure Union-Find pour les sommets de G
    Pour chaque arête e dans l’ordre trié:
        (u,v) = extrémités de e
        si FIND(UF,u) ≠ FIND(UF,v):
            UNION(UF,u,v)
            T = T ∪ {e}
        si |T| = |V| - 1:
            break
    return T
```

---

### (3.2) Parcours aléatoire / Parcours BFS aléatoire

**Idée :**  
Partir d’un sommet aléatoire (ou fixé), puis parcourir le graphe en choisissant l’ordre d’exploration des arêtes de façon aléatoire. Cela produit un arbre couvrant différent à chaque exécution.

**Parcours aléatoire simple (frontière globale) :**
```
RANDOM_TRAVERSAL_SPANNING_TREE(G):
    Choisir un sommet initial r aléatoire
    visited[v] = faux pour tous v
    visited[r] = vrai
    T = ∅
    frontier = ∅

    Pour chaque arête e=(r,w):
        frontier.add(e)

    Tant que frontier non vide:
        e = arête_au_hasard(frontier)
        frontier.remove(e)
        (u,v) = extrémités de e
        si visited[v] = faux:
            visited[v] = vrai
            T = T ∪ {e}
            Pour chaque arête f=(v,x):
                si visited[x] = faux:
                    frontier.add(f)

    return T
```

**Parcours BFS aléatoire (random BFS) :**  
On réalise un BFS classique mais en mélangeant l’ordre des arêtes à chaque niveau.

```
RANDOM_BFS_SPANNING_TREE(G,r):
    visited[v] = faux pour tous v
    visited[r] = vrai
    T = ∅
    queue = vide

    edges_from_r = arêtes sortantes de r
    Mélanger(edges_from_r)
    pour e dans edges_from_r:
        queue.enqueue(e)

    tant que queue non vide:
        e = queue.dequeue()
        (u,v) = extrémités de e
        si visited[v] = faux:
            visited[v] = vrai
            T = T ∪ {e}

            edges_from_v = arêtes sortantes de v vers sommets non visités
            Mélanger(edges_from_v)
            pour f dans edges_from_v:
                queue.enqueue(f)

    return T
```

---

### (3.3) Insertion aléatoire d’arêtes

**Idée :**  
On part d’aucune arête. On tire des arêtes au hasard dans le graphe. Si l’ajout de cette arête ne crée pas de cycle (vérifié par Union-Find), on l’ajoute à T. On répète jusqu’à obtenir |V|-1 arêtes.

```
RANDOM_INSERTION_SPANNING_TREE(G):
    n = |V|
    T = ∅
    UF = Union-Find(V)
    E_list = liste des arêtes de G

    tant que |T| < n-1:
        e = arête_au_hasard(E_list)
        (u,v) = extrémités de e
        si FIND(UF,u) ≠ FIND(UF,v):
            T = T ∪ {e}
            UNION(UF,u,v)

    return T
```

---

### (3.4) Algorithme d’Aldous-Broder

**Idée :**  
Faire une marche aléatoire jusqu’à ce que tous les sommets soient visités. La première fois qu’on entre dans un sommet, on ajoute l’arête d’entrée dans T. Cet algorithme génère un arbre couvrant uniformément au hasard.

```
ALDOUS_BRODER(G):
    n = |V|
    v0 = sommet_au_hasard(V)
    visited[v0] = vrai
    count = 1
    current = v0
    T = ∅

    tant que count < n:
        neighbors = {w | (current,w) ∈ E}
        next = voisin_au_hasard(neighbors)

        si visited[next] = faux:
            visited[next] = vrai
            count = count+1
            T = T ∪ {(current,next)}

        current = next

    return T
```

---

### (3.5) Par Contraction d’arêtes

**Idée :**  
Choisir une arête non-boucle au hasard, la contracter pour réduire le graphe, puis construire récursivement l’arbre sur le graphe contracté. Ajouter ensuite l’arête contractée.

```
RANDOM_SPANNING_TREE_CONTRACTION(G):
    si |V(G)| = 1:
        return ∅

    E_non_loop = {e ∈ E(G) | src(e)≠dst(e)}
    e = arête_au_hasard(E_non_loop)
    G' = CONTRACT_EDGE(G,e)
    T' = RANDOM_SPANNING_TREE_CONTRACTION(G')
    return T' ∪ {e}
```

```
CONTRACT_EDGE(G,e):
    (u,v) = extrémités de e
    // Fusionner v dans u
    Pour chaque arête f=(v,x):
        Retirer f de G
        si x ≠ u:
            Ajouter (u,x) sauf si boucle
    Retirer les boucles (u,u)
    Supprimer v de G
    return G
```

---

### (3.6) Algorithme de Wilson

**Idée :**  
Comme Aldous-Broder, Wilson produit un arbre couvrant uniformément. On ajoute progressivement des sommets via des marches aléatoires rejoignant l’arbre existant, en supprimant les cycles via le parentage.

```
WILSON(G):
    Choisir un sommet initial v
    T = {v}, parent[] indéfini
    tant que |T| < |V|:
        u = sommet_au_hasard(V \ T)
        current = u
        tandis que current ∉ T:
            neighbors = {w | (current,w) ∈ E}
            next = voisin_au_hasard(neighbors)
            parent[current] = next
            current = next

        // current ∈ T maintenant
        path = ∅
        x = u
        tandis que x ∉ T:
            y = parent[x]
            path = path ∪ {(x,y)}
            x = y

        T = T ∪ tous_les_sommets_de_path
        T = T ∪ toutes_les_arêtes_de_path

    return T
```

---

### (3.7) Par flips successifs

**Idée :**  
On part d’un arbre T. On ajoute une arête hors de T, formant un cycle, puis on enlève une arête du cycle pour garder un arbre. On déplace la racine et ré-oriente les arêtes. Répéter de nombreux flips pour approcher une distribution uniforme.

```
FLIP_BASED_TREE_RANDOMIZER(G,T):
    r = CHOISIR_RACINE(T)
    ORIENTER_VERS_RACINE(T,r)

    pour i de 1 à M: // M grand
        nonTreeEdges = {e=(r,x) ∈ E(G)\T}
        si nonTreeEdges vide:
            // Choisir autre arête, éventuellement changer racine
            e = CHOISIR_NON_TREE_EDGE(E(G)\T)
            // Réorienter, etc.
        sinon:
            e = arête_au_hasard(nonTreeEdges)

        cycleEdges = CYCLE_FORME_PAR(T ∪ {e})
        (r,u) = e, u ≠ r
        e' = arête_de_T_sur_le_cycle_sortant_de_u (u → parent(u))
        T = (T \ {e'}) ∪ {e}
        r = u
        ORIENTER_VERS_RACINE(T,r)

    return T
```
## **Comparaison des Algorithmes**

Une comparaison détaillée des algorithmes a été réalisée sur un échantillon de 10 arbres générés pour chaque méthode. Les résultats suivants montrent les moyennes des métriques obtenues :

| **Algorithme**          | **Excentricité Moy.** | **Indice de Wiener**  | **Diamètre Moy.** | **Nb. Feuilles Moy.** | **Nb. Sommets Deg. 2 Moy.** | **Temps Moy. (ms)** |
|--------------------------|-----------------------|-----------------------|-------------------|-----------------------|----------------------------|---------------------|
| **3.1 Random MST**       | 204.14               | \(4.37 \times 10^{10}\) | 774.9            | 5216.5               | 7320.8                    | 52.0                |
| **3.2 Random Traversal** | 108.34               | \(2.62 \times 10^{10}\) | 429.1            | 5525.8               | 6822.1                    | 17.0                |
| **3.3 Random Insertion** | 226.94               | \(4.86 \times 10^{10}\) | 877.8            | 5211.8               | 7321.0                    | 13.0                |
| **3.4 Aldous-Broder**    | 264.52               | \(5.52 \times 10^{10}\) | 1041.4           | 5002.4               | 7650.2                    | 19.0                |
| **3.5 Contraction**      | 6.75                 | \(1.76 \times 10^9\)    | 32.2             | 10497.3              | 3119.2                    | 6686.0              |
| **3.6 Wilson**           | 256.24               | \(5.36 \times 10^{10}\) | 975.8            | 5014.6               | 7625.0                    | 671.0               |
| **3.7 Flips**            | 92.0                 | \(2.25 \times 10^{10}\) | 367.0            | 174.0                | 16706.0                   | 19.0                |

### **Analyse des Résultats**
1. **Temps d’exécution :**
    - Les algorithmes **3.3 Random Insertion** et **3.2 Random Traversal** sont les plus rapides.
    - Les algorithmes basés sur des marches aléatoires (**3.4 Aldous-Broder**, **3.6 Wilson**) nécessitent plus de temps mais offrent une meilleure uniformité.
    - L’algorithme **3.5 Contraction** est extrêmement coûteux en termes de temps.

2. **Uniformité :**
    - Les méthodes **3.4 Aldous-Broder**, **3.6 Wilson**, et **3.7 Flips** garantissent une uniformité théorique.
    - Les autres méthodes, bien que rapides, ne garantissent pas une distribution uniformément aléatoire.

3. **Structure des arbres :**
    - **3.5 Contraction** produit des arbres très compacts avec peu d'excentricité et de diamètre.
    - **3.7 Flips** tend vers des arbres avec un grand nombre de sommets de degré 2 et peu de feuilles.
    - Les méthodes **3.1 Random MST** et **3.3 Random Insertion** offrent un bon compromis.



---

