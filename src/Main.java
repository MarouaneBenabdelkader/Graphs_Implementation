import Generators.RandomMSTGenerator;
import Graph.*;
import GraphClasses.*;
import MSTAlgorithms.KruskalMSTAlgorithm;
import RandomTreeAlgos.*;
import Graphics.*;
import Utilities.RandomWeightAssigner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JFrame;

public class Main {

    private static final Random gen = new Random();
    static Grid grid = null;

    public static void main(String[] argv) throws InterruptedException {
        // Choose a graph family
        Graph graph = chooseFromGraphFamily();

        // Display a menu to the user
        System.out.println("Choisissez l'algorithme à exécuter :");
        System.out.println("1) (3.1) Random MST (Poids aléatoires + Kruskal)");
        System.out.println("2) (3.2) Parcours aléatoire (frontière)");
        System.out.println("3) (3.3) Insertion aléatoire d'arêtes");
        System.out.println("4) (3.4) Aldous-Broder");
        System.out.println("5) (3.5) Contraction d'arêtes");
        System.out.println("6) (3.6) Wilson");
        System.out.println("7) (3.7) Flips successifs");
        System.out.print("Votre choix (1-7): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        ArrayList<Edge> randomTree;

        switch (choice) {
            case 1: // (3.1) Random MST
                randomTree = genRandomMSTTree(graph);
                break;
            case 2: // (3.2) Random Traversal
                randomTree = genRandomTraversalTree(graph);
                break;
            case 3: // (3.3) Insertion aléatoire
            {
                RandomInsertionGenerator insertionGen = new RandomInsertionGenerator();
                randomTree = new ArrayList<>(insertionGen.generateRandomSpanningTree(graph));
            }
            break;
            case 4: // (3.4) Aldous-Broder
            {
                AldousBroderGenerator aldousBroderGen = new AldousBroderGenerator();
                randomTree = new ArrayList<>(aldousBroderGen.generateRandomSpanningTree(graph));
            }
            break;
            case 5: // (3.5) Contraction
            {
                RandomContractionGenerator contractionGen = new RandomContractionGenerator();
                randomTree = contractionGen.generateRandomSpanningTree(graph);
            }
            break;
            case 6: // (3.6) Wilson
            {
                WilsonGenerator wilsonGen = new WilsonGenerator();
                randomTree = new ArrayList<>(wilsonGen.generateRandomSpanningTree(graph));
            }
            break;
            case 7: // (3.7) Flipper
            {
                // For flipper, we need an initial spanning tree T.
                // We can pick a BFS tree from vertex 0.
                ArrayList<Edge> bfsTree = bfsTreeFromGraph(graph, 0);
                Set<Edge> initialTree = new HashSet<>(bfsTree);
                Flipper flipper = new Flipper(graph, initialTree, 0);

                // Perform many flips to approach uniformity
                System.out.print("Combien de flips voulez-vous effectuer ? (ex: 10000) : ");
                int flips = sc.nextInt();
                flipper.performFlips(flips);

                randomTree = new ArrayList<>(flipper.getTreeEdges());
            }
            break;
            default:
                System.out.println("Choix invalide, utilisation par défaut de l'algorithme d'Aldous-Broder.");
                AldousBroderGenerator aldousBroderGen = new AldousBroderGenerator();
                randomTree = new ArrayList<>(aldousBroderGen.generateRandomSpanningTree(graph));
        }

        // Compute statistics
        int noOfSamples = 10;
        Stats stats = new Stats(noOfSamples);
        for (int i = 0; i < noOfSamples; i++) {
            // Here we re-generate a tree each time for stats if needed
            // or just reuse the same randomTree for demonstration
            stats.update(randomTree);
        }
        stats.print();

        // Print out the chosen tree edges
        System.out.println("Random Spanning Tree Edges:");
        for (Edge e : randomTree) {
            System.out.println(e.getSource() + " -- " + e.getDest() + " : " + e.weight);
        }

        if (grid != null) showGrid(grid, randomTree);
        sc.close();
    }

    private static Graph chooseFromGraphFamily() {
        // You can parameterize this to choose between different graph classes
        grid = new Grid(1920 / 11, 1080 / 11);
        // Other possibilities:
        // Graph graph = new Complete(400).graph;
        // Graph graph = new ErdosRenyi(1_000, 100).graph;
        // Graph graph = new Lollipop(1_000).graph;
        return grid.graph;
    }

    // (3.1) Generate a random MST by assigning random weights and running MST algorithm
    public static ArrayList<Edge> genRandomMSTTree(Graph graph) {
        RandomMSTGenerator generator = new RandomMSTGenerator(
                new KruskalMSTAlgorithm(),
                new RandomWeightAssigner()
        );
        return generator.generateRandomMST(graph);
    }

    // (3.2) Generate a random spanning tree by random traversal approach
    public static ArrayList<Edge> genRandomTraversalTree(Graph graph) {
        RandomTraversalGenerator traversalGen = new RandomTraversalGenerator();
        return new ArrayList<>(traversalGen.generateRandomSpanningTree(graph));
    }

    /**
     * A helper method for (3.7) or whenever we need an initial spanning tree.
     * This generates a BFS tree from the given graph starting at 'rootVertex'.
     */
    public static ArrayList<Edge> bfsTreeFromGraph(Graph graph, int rootVertex) {
        ArrayList<Arc> treeArcs = BreadthFirstSearch.generateTree(graph, rootVertex);
        ArrayList<Edge> edges = new ArrayList<>();
        for (Arc a : treeArcs) {
            edges.add(a.support);
        }
        return edges;
    }

    private static class Stats {
        public int nbrOfSamples;
        private int diameterSum = 0;
        private double eccentricitySum = 0;
        private long wienerSum = 0;
        private int degreesSum[] = {0, 0, 0, 0, 0};
        private int[] degrees;
        long startingTime;

        public Stats(int noOfSamples) {
            this.nbrOfSamples = noOfSamples;
            this.startingTime = System.nanoTime();
        }

        public void print() {
            long delay = System.nanoTime() - startingTime;

            System.out.println("On " + nbrOfSamples + " samples:");
            System.out.println("Average eccentricity: " + (eccentricitySum / nbrOfSamples));
            System.out.println("Average wiener index: " + (wienerSum / nbrOfSamples));
            System.out.println("Average diameter: " + (diameterSum / nbrOfSamples));
            System.out.println("Average number of leaves: " + (degreesSum[1] / nbrOfSamples));
            System.out.println("Average number of degree 2 vertices: " + (degreesSum[2] / nbrOfSamples));
            System.out.println("Average computation time: " + (delay / (nbrOfSamples * 1_000_000)) + "ms");
        }

        public void update(ArrayList<Edge> randomTree) {
            RootedTree rooted = new RootedTree(randomTree, 0);
            diameterSum = diameterSum + rooted.getDiameter();
            eccentricitySum = eccentricitySum + rooted.getAverageEccentricity();
            wienerSum = wienerSum + rooted.getWienerIndex();

            degrees = rooted.getDegreeDistribution(4);
            for (int j = 1; j < 5; j++) {
                degreesSum[j] = degreesSum[j] + degrees[j];
            }
        }
    }

    private static void showGrid(Grid grid, ArrayList<Edge> randomTree) throws InterruptedException {
        RootedTree rooted = new RootedTree(randomTree, 0);

        JFrame window = new JFrame("solution");
        final Labyrinth laby = new Labyrinth(grid, rooted);

        laby.setStyleBalanced();
        // laby.setShapeBigNodes();
        // laby.setShapeSmallAndFull();
        laby.setShapeSmoothSmallNodes();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(laby);
        window.pack();
        window.setLocationRelativeTo(null);

        for (final Edge e : randomTree) {
            laby.addEdge(e);
        }
        laby.drawLabyrinth();

        window.setVisible(true);

        // Save the image
        try {
            laby.saveImage("resources/random.png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
