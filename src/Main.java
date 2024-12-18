import Graph.*;
import GraphClasses.*;
import RandomTreeAlgos.BreadthFirstSearch;
import Graphics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Main {

    @SuppressWarnings("unused")
    private final static Random gen = new Random();

    static Grid grid = null;


    public static void main(String argv[]) throws InterruptedException {

        Graph graph = chooseFromGraphFamily();
        ArrayList<Edge> randomTree = null;

        int noOfSamples = 10;
        Stats stats = new Stats(noOfSamples);
        for (int i = 0; i < noOfSamples; i++) {
            randomTree = genTree(graph);
            stats.update(randomTree);
        }
        stats.print();

        if (grid != null) showGrid(grid, randomTree);
    }

    private static Graph chooseFromGraphFamily() {
        // Parametriser ici cette fonction afin de pouvoir choisir
        // quelle classe de graphe utiliser

        grid = new Grid(1920 / 11, 1080 / 11);
        Graph graph = grid.graph;
        //Graph graph = new Complete(400).graph;
        //Graph graph = new ErdosRenyi(1_000, 100).graph;
        //Graph graph = new Lollipop(1_000).graph;
        return graph;
    }

    public static ArrayList<Edge> genTree(Graph graph) {
        ArrayList<Edge> randomTree;

        // TOOO : modifier l'algorithme utilisé ici
        // ou bien parametriser à l'aide de la ligne de commande

        // Non-random BFS
        ArrayList<Arc> randomArcTree =
                BreadthFirstSearch.generateTree(graph, 0);

        randomTree = new ArrayList<>();
        for (Arc a : randomArcTree) randomTree.add(a.support);
        return randomTree;
    }


    private static class Stats {
        public int nbrOfSamples = 10;
        private int diameterSum = 0;
        private double eccentricitySum = 0;
        private long wienerSum = 0;
        private int degreesSum[] = {0, 0, 0, 0, 0};
        private int degrees[];
        long startingTime = 0;

        public Stats(int noOfSamples) {
            int nbrOfSamples = noOfSamples;
            long startingTime = System.nanoTime();
        }

        public void print() {
            long delay = System.nanoTime() - startingTime;

            System.out.println("On " + nbrOfSamples + " samples:");
            System.out.println("Average eccentricity: "
                    + (eccentricitySum / nbrOfSamples));
            System.out.println("Average wiener index: "
                    + (wienerSum / nbrOfSamples));
            System.out.println("Average diameter: "
                    + (diameterSum / nbrOfSamples));
            System.out.println("Average number of leaves: "
                    + (degreesSum[1] / nbrOfSamples));
            System.out.println("Average number of degree 2 vertices: "
                    + (degreesSum[2] / nbrOfSamples));
            System.out.println("Average computation time: "
                    + delay / (nbrOfSamples * 1_000_000) + "ms");

        }

        public void update(ArrayList<Edge> randomTree) {
            RootedTree rooted = new RootedTree(randomTree, 0);
//			rooted.printStats();
            diameterSum = diameterSum + rooted.getDiameter();
            eccentricitySum = eccentricitySum + rooted.getAverageEccentricity();
            wienerSum = wienerSum + rooted.getWienerIndex();

            degrees = rooted.getDegreeDistribution(4);
            for (int j = 1; j < 5; j++) {
                degreesSum[j] = degreesSum[j] + degrees[j];
            }
        }

    }

    private static void showGrid(
            Grid grid,
            ArrayList<Edge> randomTree
    ) throws InterruptedException {
        RootedTree rooted = new RootedTree(randomTree, 0);

        JFrame window = new JFrame("solution");
        final Labyrinth laby = new Labyrinth(grid, rooted);

        laby.setStyleBalanced();
//		laby.setShapeBigNodes();
//		laby.setShapeSmallAndFull();
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

        // Pour générer un fichier image.
        try {
            laby.saveImage("resources/random.png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}
