package com.java.kanke.utils.kmeans;

import java.util.ArrayList;
import java.util.Random;

public class Kmeans2 {

	//FIELDS
	private double[][] data; 	//data to cluster
	private int numClusters; 	//number of cluster
	private double[][] clusterCenters;	//cluster centers
	private int dataSize;	// size of data (number of patterns)
	private int dataDim;	//dimension of each pattern
	private ArrayList<double[]>[] clusters;	//calculated cluster
	private double[] clusterVars;	//cluster variances
	private double epsilon;

	//CONSTRUCTORS
	public Kmeans2(double[][] data, int numClusters, double[][] clusterCenters ){
		dataSize = data.length;
		dataDim = data[0].length;
		this.data = data;
		this.numClusters = numClusters;
		this.clusterCenters = clusterCenters;
		//initialization of the clusters
		clusters = new ArrayList[numClusters];
		for(int i = 0; i < clusterCenters.length; i++){
			clusters[i] = new ArrayList<double[]>();
		}

		clusterVars = new double[numClusters];
		epsilon = 0.1;

	}

	//initial cluster centers are generated randomly
	public Kmeans2(double[][] data, int numClusters){
		this(data, numClusters, true);
	}

	public Kmeans2(double[][] data, int numClusters, boolean randomizeCenters) {
		dataSize = data.length;
		dataDim = data[0].length;
		this.data = data;
		this.clusterCenters = new double[numClusters][dataDim];
		//initialization of the clusters
		clusters = new ArrayList[numClusters];
		for(int i = 0; i < clusterCenters.length; i++){
			clusters[i] = new ArrayList<double[]>();
		}

		clusterVars = new double[numClusters];
		epsilon = 0.1;
		if(randomizeCenters){
			randomizeCenters(numClusters, data);
		}
	}

	private void randomizeCenters(int numClusters, double[][] data) {
		Random r = new Random();
		int[] check = new int[numClusters];
		for(int i = 0; i < numClusters; i++){
			int rand = r.nextInt(dataSize); //random chooser of a pattern
			if(check[i] == 0){
				this.clusterCenters[i] = data[rand].clone();
				check[i] = 1;
			}else{
				i--;
			}	
		}
	}

	private void calculateClusterCenters(){
		for(int i = 0; i < numClusters; i++){ //for each cluster it computes the center
			int clustSize = clusters[i].size();
			for(int k = 0; k < dataDim; k++){
				double sum = 0d;
				for(int j = 0; j < clustSize; j++){
					double[] elem = clusters[i].get(j);
					sum += elem[k];
				}
				clusterCenters[i][k] = sum / clustSize;
			}
		}
	}

	private void calculateClusterVars(){
		for(int i = 0; i < numClusters; i++){ //for each cluster it computes the center
			int clustSize = clusters[i].size();
			double sum = 0d;
			for (int j = 0; j < clustSize; j++) {
				double[] elem = clusters[i].get(j);
				for (int k = 0; k < dataDim; k++) {
					Math.pow( (Double)elem[k] - getClusterCenters()[i][k], 2);
				}
			}
			clusterVars[i] = sum / clustSize;
		}
	}

	public double[][] getClusterCenters() {
		return clusterCenters;
	}

	public double getTotalVar(){
		double total = 0d;

		for(double var : clusterVars)
			total += var;

		return total;
	}

	public double[] getClusterVars(){
		return clusterVars;
	}

	public ArrayList<double[]>[] getClusters(){
		return clusters;
	}

	//assign data to closest cluster center
	private void assignData(){
		for(int k=0;k<numClusters;k++){
			clusters[k].clear();
		}

		for(int i=0; i<dataSize; i++){

			int clust = 0;
			double dist = Double.MAX_VALUE;
			double newdist = 0;

			for(int j=0; j<numClusters; j++)
			{
				newdist = distToCenter( data[i], j );
				if( newdist <= dist )
				{
					clust = j;
					dist = newdist;
				}
			}

			clusters[clust].add(data[i]);
		}


	}

	private double distToCenter(double[] datum, int j) {
		double sum = 0d;
        for(int i=0;i < dataDim; i++){
            sum += Math.pow(( datum[i] - getClusterCenters()[j][i] ), 2);
        }

        return Math.sqrt(sum);
	}

	 public void calculateClusters()
	    {
	 
	         double var1 = Double.MAX_VALUE;
	         double var2;
	         double delta;
	 
	        do{
	              calculateClusterCenters();
	              assignData();
	              calculateClusterVars();
	              var2 = getTotalVar();
	              if (Double.isNaN(var2)){   // if this happens, there must be some empty clusters
	                 delta = Double.MAX_VALUE;
	                 randomizeCenters(numClusters, data);
	                 assignData();
	                 calculateClusterCenters();
	                 calculateClusterVars();
	             }
	             else{
	                 delta = Math.abs(var1 - var2);
	                 var1 = var2;
	             }
	 
	        }while(delta > epsilon);  //stop criteria
	    }
	 
	    public void setEpsilon(double epsilon)
	    {
	        if(epsilon > 0)
	        {
	            this.epsilon = epsilon;
	        }
	    }


}
