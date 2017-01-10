package com.java.kanke.utils.kmeans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtil {
	public static DataBean find(String pointid){
		try {
			String xaxis=null;
			String yaxis=null;
			
			Document document=XmlParse.getDocument();
			NodeList list=document.getElementsByTagName("point");
			for(int i=0;i<list.getLength();i++){
				Element elist=(Element)list.item(i);
				if(elist.getAttribute("pointid").equals(pointid)){
					String idcard=elist.getAttribute("X-axis");
					String examid0=elist.getAttribute("Y-axis");
					NodeList children=elist.getChildNodes();
					for(int j=0;j<children.getLength();j++){
						if((children.item(j).getTextContent().getClass().equals(String.class))&&((children.item(j).getTextContent())!=null)){
							Node e=children.item(j);
							if(e.getNodeName().equals("X-axis")){
								xaxis=e.getTextContent();
							}else if(e.getNodeName().equals("Y-axis")){
								yaxis=e.getTextContent();
							}
						}
					}
					double xaxis_double=Double.parseDouble(xaxis);
					double yaxis_double=Double.parseDouble(yaxis);
					DataBean point=new DataBean();
					point.setPointid(pointid);
					point.setXaxis(xaxis_double);
					point.setYaxis(yaxis_double);
					return point;
					}
			}
			return null;
		} catch (Exception e) {
			 throw new RuntimeException(e);
			}
		
	}
}
