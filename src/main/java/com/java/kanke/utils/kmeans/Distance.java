package com.java.kanke.utils.kmeans;

public class Distance {
		int dest;
        int source;
        double dist;

        public int getDest() {  
            return dest;  
        }  
      
        public void setDest(int dest) {  
            this.dest = dest;  
        }  
      
        public int getSource() {  
            return source;  
        }  
      
        public void setSource(int source) {  
            this.source = source;  
        }  
      
        public double getDist() {  
            return dist;  
        }  
      
        public void setDist(double dist) {  
            this.dist = dist;  
        }  

        public Distance(int dest, int source, double dist) {  
            this.dest = dest;  
            this.source = source;  
            this.dist = dist;  
        }  
        public Distance() {
        }  
}
