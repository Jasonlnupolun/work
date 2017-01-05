package com.scala.kanke.arg.graphcluster;

import info.debatty.java.graphs.Graph;
import info.debatty.java.graphs.Node;
import info.debatty.java.graphs.SimilarityInterface;
import info.debatty.java.graphs.build.Brute;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/1/5.
 */
public class BruteGraph {









    public static void main(String[] args) {

        // Generate some random nodes
        Random r = new Random();
        int count = 10;

        ArrayList<Node> nodes = new ArrayList<Node>(count);
        for (int i = 0; i < count; i++) {
            // The value of our nodes will be an int
            nodes.add(new Node<Integer>(String.valueOf(i), r.nextInt(10 * count)));
        }
        Brute builder = new Brute<Integer>();
        builder.setK(10);
        builder.setSimilarity(new SimilarityInterface<Integer>() {

            public double similarity(Integer value1, Integer value2) {
                return 1.0 / (1.0 + Math.abs(value1 - value2));
            }
        });

        // Run the algorithm, and get the resulting neighbor lists
        Graph<Integer> graph = builder.computeGraph(nodes);
        graph.prune(0.05);
        ArrayList<Graph<Integer>> connectedComponents = graph.connectedComponents();
        System.out.println(connectedComponents.size());
        for(int i =0;i<connectedComponents.size();i++){


            Iterable<Node<Integer>> nl = connectedComponents.get(i).getNodes();
            for (Node<Integer> n : nl) {
                System.out.print(n.id+":"+n.value+"          ");
//                System.out.println(nl);
            }
            System.out.println(" --------------- 111-------------  ");
        }

    }
}
