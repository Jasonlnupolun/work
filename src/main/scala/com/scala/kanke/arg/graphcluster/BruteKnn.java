package com.scala.kanke.arg.graphcluster;

import breeze.linalg.DenseVector;
import com.java.kanke.utils.bean.Video;
import com.java.kanke.utils.es.SaveData;
import com.java.kanke.utils.mysql.DBCommon;
import com.scala.kanke.arg.knn.Distance;
import com.scala.kanke.arg.knn.FeatureBean;
import com.scala.kanke.arg.knn.Vectoriza;
import com.scala.kanke.common.Constant;
import com.scala.kanke.common.TagConfigClass;
import info.debatty.java.graphs.Graph;
import info.debatty.java.graphs.Neighbor;
import info.debatty.java.graphs.NeighborList;
import info.debatty.java.graphs.Node;
import info.debatty.java.graphs.build.Brute;

import java.util.*;

/**
 * Created by Administrator on 2017/1/10.
 */
public class BruteKnn {

    // 查询数据库中的数据特征向量话
    public static Map<String,Video> mapVideo = new HashMap<String,Video>();
    public static List<FeatureBean> initVector(){
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
        List<FeatureBean> featureBeens = initVector();
        ArrayList<Node> nodes = new ArrayList<Node>(featureBeens.size());
        for (int i = 0; i < featureBeens.size(); i++) {
            FeatureBean f = featureBeens.get(i);
            nodes.add(new Node(f.getKankeid()+"&&"+f.getTagsString(), f.getTags()));
        }
        Brute builder = new Brute<DenseVector<Double>>();
        builder.setSimilarity(new Distance());
        Graph<DenseVector<Double>> graph = builder.computeGraph(nodes);
        int label = 0 ;
        for (Node n : nodes) {
            NeighborList nl = graph.get(n);
            List<KmResults> kmList = new ArrayList<KmResults>() ;

            if(nl!=null){
                Iterator<Neighbor> it = nl.iterator();
                KmResults k = new KmResults();
                k.setId(n.id.split("&&")[0]);
                k.setTags(n.id.split("&&")[1]);
                k.setRelateid(label+"");
                kmList.add(k);
                if(it!=null){
                    while(it.hasNext()) {
                        Neighbor ele = it.next();
                        if(ele.similarity>0.6){
                            KmResults rlatek = new KmResults();
                            rlatek.setId(ele.node.id.split("&&")[0]);
                            rlatek.setTags(ele.node.id.split("&&")[1]);
                            rlatek.setRelateid(label+"");
                            kmList.add(rlatek);
                            it.remove();
                            graph.fastRemove(ele.node);
                        }
                    }
                }
            }
            label++;
            SaveData.saveData(kmList,"jsyx","kmeans");
        }
    }

}
