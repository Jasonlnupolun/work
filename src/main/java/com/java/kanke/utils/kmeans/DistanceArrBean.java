package com.java.kanke.utils.kmeans;

public class DistanceArrBean {
	private DataBean usualpoint=null;//����ĳһ����
	private DataBean cterpoint=null;//��������
	private Double[] distancetoKcenter;//�����ľ�������
	public DataBean getUsualpoint() {
		return usualpoint;
	}
	public void setUsualpoint(DataBean usualpoint) {
		this.usualpoint = usualpoint;
	}
	public DataBean getCterpoint() {
		return cterpoint;
	}
	public void setCterpoint(DataBean cterpoint) {
		this.cterpoint = cterpoint;
	}
	public Double[] getDistancetoKcenter() {
		return distancetoKcenter;
	}
	public void setDistancetoKcenter(Double[] distancetoKcenter) {
		this.distancetoKcenter = distancetoKcenter;
	}
}
