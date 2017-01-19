package com.java.kanke.utils.mysql;

import com.java.kanke.utils.PropertyUtil;
import com.java.kanke.utils.bean.Video;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBCommon {
 
	public static Logger logger = Logger.getLogger(DBCommon.class);
	
    private static DataSource ds_sqlserver;
    private static DataSource ds_oracle;
    private static DataSource ds_mysql;
//    private static LBHttpSolrServer solrServer;
    private static Connection conn;
 
    private static String driver_sqlserver = PropertyUtil.get("driver_sqlserver");
    private static String url_sqlserver = PropertyUtil.get("url_sqlserver");
    private static String username_sqlserver = PropertyUtil.get("username_sqlserver");
    private static String password_sqlserver = PropertyUtil.get("password_sqlserver");
 
    private static String driver_oracle = PropertyUtil.get("driver_oracle");
    private static String url_oracle = PropertyUtil.get("url_oracle");
    private static String username_oracle = PropertyUtil.get("username_oracle");
    private static String password_oracle = PropertyUtil.get("password_oracle");
 
    private static String driver_mysql = PropertyUtil.get("driver_mysql");
    private static String url_mysql = PropertyUtil.get("url_mysql");
    private static String username_mysql = PropertyUtil.get("username_mysql");
    private static String password_mysql = PropertyUtil.get("password_mysql");
 
    private static String url_solr = PropertyUtil.get("url_solr");
 
    private DBCommon() {
 
    }
 
    public synchronized static QueryRunner getQueryRunnerSqlserver() {
        if (ds_sqlserver == null) {
            BasicDataSource dbcpDataSource = new BasicDataSource();
            dbcpDataSource.setDriverClassName(driver_sqlserver);
            dbcpDataSource.setUrl(url_sqlserver);
            dbcpDataSource.setUsername(username_sqlserver);
            dbcpDataSource.setPassword(password_sqlserver);
            dbcpDataSource.setDefaultAutoCommit(true);
            dbcpDataSource.setMaxActive(100);
            dbcpDataSource.setMaxIdle( 30);
            dbcpDataSource.setMaxWait(500);
            DBCommon.ds_sqlserver = dbcpDataSource;
            System.out.println("Init dbcp...");
        }
        return new QueryRunner(DBCommon.ds_sqlserver);
    }
 
    public synchronized static QueryRunner getQueryRunnerOracle() {
        if (ds_oracle == null) {
            BasicDataSource dbcpDataSource = new BasicDataSource();
            dbcpDataSource.setDriverClassName(driver_oracle);
            dbcpDataSource.setUrl(url_oracle);
            dbcpDataSource.setUsername(username_oracle);
            dbcpDataSource.setPassword(password_oracle);
            dbcpDataSource.setDefaultAutoCommit(true);
            dbcpDataSource.setMaxActive(100);
            dbcpDataSource.setMaxIdle( 30);
            dbcpDataSource.setMaxWait(500);
            DBCommon.ds_oracle = dbcpDataSource;
            System.out.println("Init dbcp...");
        }
        return new QueryRunner(DBCommon.ds_oracle);
    }
 
    public synchronized static QueryRunner getQueryRunnerMysql() {
        if (ds_mysql == null) {
            BasicDataSource dbcpDataSource = new BasicDataSource();
            dbcpDataSource.setDriverClassName(driver_mysql);
            dbcpDataSource.setUrl(url_mysql);
            dbcpDataSource.setUsername(username_mysql);
            dbcpDataSource.setPassword(password_mysql);
            dbcpDataSource.setDefaultAutoCommit(true);
            dbcpDataSource.setMaxActive(100);
            dbcpDataSource.setMaxIdle( 30);
            dbcpDataSource.setMaxWait(500);
            DBCommon.ds_mysql = dbcpDataSource;
            System.out.println("Init dbcp...");
        }
        return new QueryRunner(DBCommon.ds_mysql);
    }

 
    public synchronized static Connection getConnClobOracle() {
        if (conn == null) {
            try {
                Class.forName(driver_oracle);
                DBCommon.conn = DriverManager.getConnection(url_oracle, username_oracle, password_oracle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }


    public static <T>List<T>  queryForBean(String sql, Object[] params,Class clazz){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        List<T> a = new ArrayList<T>();
        try {
             logger.info("sql--->"+sql);
             a= (List) runner.query(sql,params,new BeanListHandler<T>(clazz));   
        } catch (SQLException e) {
            e.printStackTrace();
        }
         
        return a ;
    }


    public static Video queryBean(String sql, Object[] params, Class clazz){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        Video v = new Video();
        try {
            logger.info("sql--->"+sql);
            v= runner.query(sql,params,new BeanHandler<Video>(clazz));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return v ;
    }

    public static Map<String,Object> queryMap(String sql, Object[] params){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            logger.info("sql--->"+sql);
            map =  runner.query(sql,params,new MapHandler())  ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map ;
    }

    public static Map<String, Map<String, Object>> queryMapList(String sql, Object[] params){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        Map<String, Map<String, Object>> rs = new HashMap<String, Map<String, Object>>() ;
        try {
            logger.info("sql--->"+sql);
             rs = runner.query(sql, new KeyedHandler<String>("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs ;
    }

    public static <T> Map<Integer, T> queryMapList(String sql, Object[] params,Class clazz){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        // new BeanMapHandler<Integer, Users>(Users.class,"id")
        Map<Integer, T> rs = new HashMap<Integer,T>();
        try {
            logger.info("sql--->"+sql);
            rs = runner.query(sql, new BeanMapHandler<Integer, T>(clazz,1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs ;
    }





    public static <T>List<T> queryColumnListHandler(String sql,String tags,Object[] params) throws SQLException{
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        logger.info("sql--->"+sql);
        List<T> list = (List<T>) runner.query(sql, new ColumnListHandler(tags),params);
        return list;
    }

    public static void saveBean(String sql ,Object[] params)throws SQLException{
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        int i=runner.update(sql, params);
    }
    public static Map<String,String> queryForMap(String sql, Object[] params){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        Map<String,String> a = new HashMap<String,String>();
        try {
            logger.info("sql--->"+sql);
            a= (Map) runner.query(sql,params,new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a ;
    }

    public static Map<String,List<String>> queryForMapList(String sql, Object[] params){
        QueryRunner runner = DBCommon.getQueryRunnerMysql();
        Map<String,List<String>> a = new HashMap<String,List<String>>();
        try {
            logger.info("sql--->"+sql);
            a= (Map) runner.query(sql,params, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a ;
    }


}