package Dbscan;

/**
 * Created by Administrator on 2016/12/29.
 */
public class MyPoint {
    private double x ;
    private double y ;
    private boolean isVisited ;    //定义点是否被访问过


    public MyPoint() {
        super();
    }

    public MyPoint(boolean isVisited) {
        super();
        this.isVisited = isVisited;
    }

    public MyPoint(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public MyPoint(double x, double y,boolean isVisited) {
        super();
        this.x = x;
        this.y = y;
        this.isVisited = isVisited;
    }

    public boolean getIsVisited() {
        return isVisited;
    }

    public void setIsVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "("+x+","+y+")";
    }

    public static void main(String[] args) {
        System.out.println(Math.sqrt((296-298)*(296-298)+(444-447)*(444-447)));

    }
}
