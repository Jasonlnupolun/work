package com.java.kanke.utils.kmeans;

/**
 * Created by Administrator on 2016/12/29.
 */
public class Test2 {

    public static void main(String[] args) {
        int K = 25;
        String[] dataPaths = new String[]{"breast-cancer.data", "segment.data","housing.data", "meta.data"};
        for (String path : dataPaths) {
            DataSet dataset = new DataSet(path);
            Evaluation eva = new Evaluation(dataset, "Kmeans");
            eva.evaluateClustering(K);
            // print mean and standard deviation of accuracy
            System.out.println("Dataset:" + path + ", distance:" + eva.getClusterDist());
        }
    }
}
