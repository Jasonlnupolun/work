package com.java.kanke.utils.bean;


/**
 * Created by Administrator on 2016/11/10.
 */
public class Video {
    public String id;             //表id
    public String kankeid;        // kankeid
    public String channelid ;     //频道id
    public String videoid;           //对方影视库的id
    public String tags;           //影片标签
    public String region;        //地区
    public long playcount;       // 播放量
    public String year;         // 影片年份
    public String videotype ;   //影片类型
    public double weightValue;   // 相似度
    private String title;        //名称
    private String addtime;      //添加时间
    private String userid;       // 用在用户的历史id
    private String score ;      // 得分

    private Long monthcount;    // 月播放量

    private Long weekcount;     // 周播放量

    public Long getMonthcount() {
        return monthcount;
    }

    public void setMonthcount(Long monthcount) {
        this.monthcount = monthcount;
    }

    public Long getWeekcount() {
        return weekcount;
    }

    public void setWeekcount(Long weekcount) {
        this.weekcount = weekcount;
    }


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }



    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
    public String getVideoid() {
        return videoid;
    }
    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
    public String getKankeid() {
        return kankeid;
    }
    public void setKankeid(String kankeid) {
        this.kankeid = kankeid;
    }
    public double getWeightValue() {
        return weightValue;
    }
    public void setWeightValue(double weightValue) {
        this.weightValue = weightValue;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public long getPlaycount() {
        return playcount;
    }
    public String getChannelid() {
        return channelid;
    }
    public String getVideotype() {
        return videotype;
    }
    public void setVideotype(String videotype) {
        this.videotype = videotype;
    }
    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }
    public void setPlaycount(long playcount) {
        this.playcount = playcount;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

}
