package com.scala.kanke.arg.graphcluster;

import breeze.linalg.DenseVector;
import com.java.kanke.utils.bean.Video;
import com.java.kanke.utils.mysql.DBCommon;
import com.scala.kanke.arg.knn.Distance;
import com.scala.kanke.arg.knn.FeatureBean;
import com.scala.kanke.arg.knn.Vectoriza;
import com.scala.kanke.common.Constant;
import com.scala.kanke.common.TagConfigClass;
import info.debatty.java.graphs.Graph;
import info.debatty.java.graphs.Node;
import info.debatty.java.graphs.build.Brute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */
public class BruteGraph {
    // 查询数据库中的数据特征向量话
   public static  Map<String,Video> mapVideo = new HashMap<String,Video>();
    public static List<FeatureBean>  initVector(){
        String sql = TagConfigClass.all();
        List<Video> video = DBCommon.queryForBean(sql,null,Video.class);
        List<FeatureBean> featureBeans = new ArrayList<FeatureBean>();
        for(Video v:video){
            featureBeans.add(Vectoriza.transferFeatureBean(v, Constant.coordinate()));
            mapVideo.put(v.getKankeid(),v);
        }
        return featureBeans;
    }

    public static void main(String[] args) {
        // Generate some random nodes
        List<FeatureBean> featureBeens = initVector();
        ArrayList<Node> nodes = new ArrayList<Node>(featureBeens.size());
        for (int i = 0; i < featureBeens.size(); i++) {
            // The value of our nodes will be an int
            FeatureBean f = featureBeens.get(i);
            nodes.add(new Node(f.getKankeid(), f.getTags()));
        }
        Brute builder = new Brute<DenseVector<Double>>();
        builder.setK(10);
        builder.setSimilarity(new Distance());
        // Run the algorithm, and get the resulting neighbor lists
        Graph<DenseVector<Double>> graph = builder.computeGraph(nodes);
        graph.prune(0.6);
        ArrayList<Graph<DenseVector<Double>>> connectedComponents = graph.connectedComponents();
        System.out.println(connectedComponents.size());

        for(int i =0;i<connectedComponents.size();i++){
            Iterable<Node<DenseVector<Double>>> nl = connectedComponents.get(i).getNodes();
            System.out.print(connectedComponents.get(i).size()+":---");
            for (Node<DenseVector<Double>> n : nl) {
                System.out.print(n.id+":           "+ mapVideo.get(n.id).getTags());
            }
            System.out.println(" ");
        }
    }
}
