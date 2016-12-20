package com.java.kanke.utils.bean;

/**
 * Created by Administrator on 2016/11/8.
 */
public class UserHistoryRecord {

    private String videoid ;  // 观看影片的id

    private String starttime ;   // 观看的开始时间

    private String endtime ;     // 观看的结束时间

    private String  channelid ;  // 观看的频道

    private String tags ;       // 影视的标签

    private String region ;     // 地区

    private String year ;       // 年代

    private Integer ofweek;      // 周几

    private Integer ofhour;      // 24 小时的几点

    public Integer getOfweek() {
        return ofweek;
    }

    public void setOfweek(Integer ofweek) {
        this.ofweek = ofweek;
    }

    public Integer getOfhour() {
        return ofhour;
    }

    public void setOfhour(Integer ofhour) {
        this.ofhour = ofhour;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
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

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }


}
