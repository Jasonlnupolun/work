package com.java.kanke.utils.bean;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ChannelInfo {
    /**
     * 影片id
     */
    private String vid ;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    /**
     * 频道id
     */
    private String id ;

    /**
     * 开始时间
     */
    private String starttime ;

    /**
     * 结束时间
     */
    private String endtime ;

    /**
     * 影片类型
     */
    private String typename ;


}
