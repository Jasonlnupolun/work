/**
 * 
 */
package com.java.kanke.utils.bean;

import java.io.Serializable;

/**
 * @author liuwanxiang
 *
 */
@SuppressWarnings("serial")
public class UserHistory implements Serializable {

	private String id;
	private String userid;
	private String videoid;
	private String kankeid;

	private String typename;
	private String title;
	private String addtime;
	private String tags;
	private String year ;

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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	private String region;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}


	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}




	//注意这里重写了equals方法
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}else {
			if(this.getClass() == obj.getClass()){
				UserHistory u = (UserHistory) obj;
				return (this.getId().equals(u.getId())) && (this.getUserid().equals(u.getUserid())) && (this.getVideoid().equals(u.getVideoid()));
			}else{
				return false;
			}
		}
	}

}
