# TP3 - Génération d'arbres couvrants aléatoires

**Binôme :** Marouane BENADELKADER & Imad DAIM

## Présentation

Ce projet implémente plusieurs algorithmes de génération d'arbres couvrants aléatoires sur un graphe. L'objectif est de comparer différentes méthodes pour obtenir un arbre couvrant choisi (idéalement) de manière uniforme parmi tous les arbres couvrants du graphe.

## Avancement et Algorithmes Implémentés

Nous avons implémenté les algorithmes suivants, tels que décrits dans le sujet :

1. **Arbres couvrants par affectation de poids aléatoires (3.1)**
    - On attribue à chaque arête un poids aléatoire dans [0,1), puis on calcule un arbre couvrant de poids minimum (par Kruskal ou Prim).
    - Chaque exécution produit un arbre potentiellement différent, offrant une méthode simple mais efficace pour obtenir un arbre "aléatoire".

2. **Parcours aléatoire (3.2)**
    - On parcourt le graphe en partant d’un sommet aléatoire, en choisissant l’ordre d'exploration des arêtes de façon aléatoire (soit via un "frontier" entièrement aléatoire, soit en randomisant l'ordre d'un parcours BFS ou DFS).
    - Cela génère un arbre couvrant qui varie en fonction des choix aléatoires effectués lors du parcours.

3. **Insertion aléatoire d’arêtes (3.3)**
    - On part d'un ensemble vide et on ajoute des arêtes choisies aléatoirement.
    - On utilise une structure Union-Find pour éviter de former des cycles.
    - On répète jusqu’à obtenir |V|-1 arêtes. L’ensemble obtenu est un arbre couvrant.

4. **Algorithme d’Aldous-Broder (3.4)**
    - On effectue une marche aléatoire sur le graphe jusqu’à ce que tous les sommets soient visités au moins une fois.
    - À chaque nouveau sommet découvert, on ajoute l’arête par laquelle on est arrivé dans l’arbre couvrant.
    - L’algorithme produit un arbre couvrant uniformément choisi parmi tous ceux possibles (en théorie).

5. **Par contraction d’arêtes (3.5)**
    - On choisit une arête aléatoirement, on la contracte, puis on construit récursivement un arbre couvrant sur le graphe contracté.
    - On ajoute ensuite l’arête contractée à l’arbre.
    - Cette méthode, plus complexe à implémenter, permet également d'obtenir un arbre couvrant uniforme.

6. **Algorithme de Wilson (3.6)**
    - Comme Aldous-Broder, Wilson génère un arbre couvrant uniformément au moyen de marches aléatoires.
    - On part d’un sommet initial, puis on incorpore progressivement les sommets extérieurs via des marches aléatoires qui rejoignent l’arbre existant, en supprimant naturellement les cycles formés.

7. **Par flips successifs (3.7)**
    - On part d’un arbre couvrant quelconque T.
    - On effectue des "flips" en ajoutant une arête hors de T, formant un cycle, puis en retirant une arête de ce cycle.
    - Après un grand nombre de flips, la distribution de l’arbre résultant tend vers l’uniformité.
    - Cette méthode peut être plus lente que les précédentes.

## Comparaison des Algorithmes

- **Uniformité :**  
  Aldous-Broder et Wilson garantissent une distribution réellement uniforme sur l'ensemble des arbres couvrants. Les méthodes par poids aléatoires (3.1), insertion aléatoire (3.3) ou contraction (3.5) le promettent également, mais avec plus ou moins de complexité et de garanties théoriques. Le flip (3.7), après de très nombreux flips, s’approche aussi de l’uniformité.

- **Complexité :**
    - L’approche par affectation de poids et calcul MST (3.1) est assez simple à coder et relativement efficace.
    - Les parcours aléatoires (3.2) sont simples et rapides, mais l'uniformité n'est pas toujours garantie (selon la méthode de parcours).
    - L’insertion aléatoire (3.3) et la contraction (3.5) demandent plus de gestion de la structure du graphe (Union-Find, gestion des boucles, etc.).
    - Aldous-Broder (3.4) et Wilson (3.6) sont élégants et garantissent l’uniformité, mais nécessitent des marches aléatoires potentiellement longues.
    - Les flips (3.7) sont conceptuellement simples, mais en pratique demandent beaucoup de temps (de nombreux flips) pour atteindre une distribution quasi uniforme.

- **Facilité d’implémentation :**  
  De manière générale :
    - (3.1) et (3.2) : plus faciles.
    - (3.3), (3.4), (3.6) : intermédiaires.
    - (3.5) et (3.7) : plus complexes.

## Utilisation du Projet

1. **Compilation :**  
   Un fichier `Makefile` est fourni. Pour compiler, lancez simplement :  
   `make`

2. **Exécution :**  
   Après compilation, lancez :  
   `make run`

   Vous pouvez également exécuter directement :  
   `java Main`

3. **Choix de l’algorithme :**  
   Dans le fichier `Main.java`, vous pouvez décommenter la section correspondant à l’algorithme que vous souhaitez tester. Un commentaire dans le code indique clairement quelle ligne décommenter pour chaque algorithme.

4. **Visualisation et statistiques :**  
   Le programme génère des statistiques sur l’arbre couvrant obtenu (diamètre, distribution des degrés, etc.). Il peut également afficher l’arbre sous forme graphique (si une grille est utilisée) et enregistrer une image PNG du résultat.

## Remarques Finales

- Les résultats peuvent varier d’une exécution à l’autre, en particulier pour les méthodes qui reposent sur des choix aléatoires.
- Les tests de performance et la comparaison détaillée des temps d’exécution, ainsi que la vérification de l’uniformité des distributions, sont laissés à l’appréciation de l’utilisateur.

Ci-dessous se trouve une version plus complète et cohérente de l’ensemble des algorithmes implémentés, présentés sous forme de pseudocodes, et déjà mentionnés dans le README. Les algorithmes sont décrits de manière indépendante, avec une terminologie uniforme. Chaque bloc de pseudocode est précédé d’un court rappel de l’objectif et du principe de l’algorithme.

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

---

**Remarque :** Tous ces algorithmes sont décrits à un niveau conceptuel. L’implémentation effective peut nécessiter des détails supplémentaires (structuration du graphe, gestion des sommets, des arêtes, du Union-Find, etc.).

Ces pseudocodes, regroupés et améliorés, reflètent le cœur des algorithmes implémentés dans le projet.