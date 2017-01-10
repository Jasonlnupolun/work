package com.java.kanke.utils.kmeans;

import java.util.ArrayList;
public class Cluster {
	private int center;
    private ArrayList<DataBean> ofCluster = new ArrayList<DataBean>();
	public int getCenter() {
		return center;
	}
	public void setCenter(int center) {
		this.center = center;
	}
	public ArrayList<DataBean> getOfCluster() {
		return ofCluster;
	}
	public void setOfCluster(ArrayList<DataBean> ofCluster) {
		this.ofCluster = ofCluster;
	}
	public void addDatabean(DataBean databean) {  
        if (!(this.ofCluster.contains(databean)))  
            this.ofCluster.add(databean);  
    }  

}
