package com.java.kanke.utils;


import java.io.*;
import java.util.Properties;


public class PropertyUtil {
	private static Properties p = null;
	private static String path;
	
	static{
		path=System.getProperty("user.dir")+File.separator+"conf"+File.separator+"application.properties";
	}
	public static String get(String key){
		try{
			p = new Properties();
			File file = new File(path);
			FileInputStream fin = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fin,"utf-8"); 
			p.load(isr);
		}catch(Exception e){
			e.printStackTrace();
		}	
		String value = p.getProperty(key);
		return value;
	}
	
	public static void set(String key, String value){
		Properties prop = new Properties();
		try{
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();
			OutputStream fos = new FileOutputStream(path);
			prop.setProperty(key, value);
			prop.store(fos, "Update '" + key + "' value");
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		PropertyUtil propertyUtil = new PropertyUtil();
		System.out.println(get("charset"));
		System.out.println(get("url"));
	}
}