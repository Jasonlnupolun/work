package com.java.kanke.utils.json;

import com.java.kanke.utils.bean.UserHistory;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2016/11/16.
 */
public class JsonToBean {
    public static UserHistory toBean(String json){
        JSONObject obj = new JSONObject().fromObject(json);//将json字符串转换为json对象
        return (UserHistory)JSONObject.toBean(obj,UserHistory.class);//将建json对象转换为Person对象
    }

}
