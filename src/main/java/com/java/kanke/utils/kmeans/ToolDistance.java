package com.java.kanke.utils.kmeans;

import java.util.ArrayList;


public class ToolDistance {
    public static double juli(DataBean g1, DataBean g2) {
        double result = Math.sqrt(StrictMath.pow(g1.getXaxis() - g2.getXaxis(), 2)
                + StrictMath.pow(g1.getYaxis() - g2.getYaxis(), 2));  
               return result;  
    }  
    public static double getDistanceByptop(DataBean usualpoint,DataBean center,ArrayList<DistanceBean> disBean){
    	//DistanceBean bean=new DistanceBean();
    	ArrayList<DistanceBean> disBean1=new ArrayList<DistanceBean>();
    	//disBean1=disbean;
    	//for(DistanceBean bean:disBean)
    	return 0;
    }
}
