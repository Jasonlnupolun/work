package graph;

import info.debatty.java.graphs.Graph;
import info.debatty.java.graphs.Node;
import info.debatty.java.graphs.SimilarityInterface;
import info.debatty.java.graphs.build.Brute;

import java.util.ArrayList;
import java.util.Random;

public class BruteExample {

    public static void main(String[] args) {

        // Generate some random nodes
        Random r = new Random();
        int count = 1000;

        ArrayList<Node> nodes = new ArrayList<Node>(count);
        for (int i = 0; i < count; i++) {
            // The value of our nodes will be an int
            nodes.add(new Node<Integer>(String.valueOf(i), r.nextInt(10 * count)));
        }

        // Instantiate and configure the brute-force graph building algorithm
        // The minimum is to define k (number of edges per node)
        // and a similarity metric between nodes
        Brute builder = new Brute<Integer>();
        builder.setK(10);
        builder.setSimilarity(new SimilarityInterface<Integer>() {

            public double similarity(Integer value1, Integer value2) {
                return 1.0 / (1.0 + Math.abs(value1 - value2));
            }
        });



        // Run the algorithm, and get the resulting neighbor lists
        Graph<Integer> graph = builder.computeGraph(nodes);



        System.out.println("111111111111111111111111111");

        graph.prune(0.05);
        ArrayList<Graph<Integer>> connectedComponents = graph.connectedComponents();
        System.out.println(connectedComponents.size());
        System.out.println(connectedComponents.get(0));
    }
}
