package json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/5.
 */
public class TestMap {
    public static void main(String[] args) {
        //列表/array 数据
        String str="[{'id': '1','code': 'bj','name': '北京','map': '39.90403, 116.40752599999996'}, {'id': '2','code': 'sz','name': '深圳','map': '22.543099, 114.05786799999998'}, {'id': '9','code': 'sh','name': '上海','map': '31.230393,121.473704'}, {'id': '10','code': 'gz','name': '广州','map': '23.129163,113.26443500000005'}]";
        Gson gson=new Gson();
//        List<City> rs=new ArrayList<City>();
//        Type type = new TypeToken<ArrayList<City>>() {}.getType();
//        rs=gson.fromJson(str, type);
//        for(City o:rs){
//            System.out.println(o.name);
//        }

        //map数据
        String jsonStr= "{\"动画 奇幻\":\"film_1522418;film_1069;film_761634;film_81752;film_1526528;film_73163;film_73243;film_1522107;film_1369213\",\"都市 言情\":\"tv_47157;tv_2760;tv_32575;tv_51665;tv_52644;tv_49675;tv_36074;tv_53163;tv_46799\"}";
        Type typ = new TypeToken<java.util.LinkedHashMap<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(jsonStr, typ);
        System.out.println(map);
        String rep = "{}";
        System.out.print(rep.split("\\{|\\}").length);
    }
}
