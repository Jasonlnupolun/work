package kmeans;

import org.encog.ml.data.basic.BasicMLData;

/**
 * Created by Administrator on 2016/12/29.
 */
public class KpointBean extends BasicMLData {
    private String id ;
    public KpointBean(double[] d,String id ) {
        super(d);
        this.id = id ;
    }
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id ;
    }
}
