package com.java.kanke.utils.es;

import com.java.kanke.utils.sql4es.EsUtilsSql;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.query.SqlElasticSearchRequestBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggreQuery {
	public static Map<String,List<Object>> groupByTime(String time1,String time2) throws Exception {
		Map<String ,List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> label = new ArrayList<Object>();
		List<Object> num = new ArrayList<Object>();
		System.out.println(String.format("SELECT count(*),url FROM log/docs WHERE logtime BETWEEN %c AND '%%s'  group by url   limit 30", time1,time2));
		Aggregations result = query(String.format("SELECT count(*),url FROM log/docs WHERE logtime BETWEEN '2016-04-28' AND '2016-04-29'  group by url   limit 30", time1,time2));
		Terms gender = result.get("url");
		for(Terms.Bucket bucket : gender.getBuckets()) {
			String key = bucket.getKey().toString();
			long count = ((ValueCount) bucket.getAggregations().get("COUNT(*)")).getValue();
			if(key.endsWith(".json")) {
				label.add(key);
				num.add(count);
			}
//			else {
//				throw new Exception(String.format("Unexpected key. expected: m OR f. found: %s", key));
//			}
		}
		map.put("label", label);
		map.put("num",num);
		
		return map;
	}
	
	
	public static Map<String,List<Object>> groupByTest(String index,String filed) throws Exception {
		Map<String ,List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> label = new ArrayList<Object>();
		List<Object> num = new ArrayList<Object>();
		Aggregations result = query(String.format("SELECT COUNT(*) FROM %s  GROUP BY %s", index,filed));
		Terms gender = result.get(filed);
		for(Terms.Bucket bucket : gender.getBuckets()) {
			String key = bucket.getKey().toString();
			long count = ((ValueCount) bucket.getAggregations().get("COUNT(*)")).getValue();
			if(key.endsWith(".json")) {
				label.add(key);
				num.add(count);
			}
//			else {
//				throw new Exception(String.format("Unexpected key. expected: m OR f. found: %s", key));
//			}
		}
		map.put("label", label);
		map.put("num",num);
		
		return map;
	}
	
	
	// 根据时间段统计
	public static Map<String,List<Object>> groupByRangeDate(String starttime,String endtime) throws Exception {
		
		
		Map<String ,List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> label = new ArrayList<Object>();
		List<Object> num = new ArrayList<Object>();
		String sql = "select count(*) from log/docs   where logtime BETWEEN ''{0}'' AND ''{1}''  group by date_histogram(field= ''logtime'',''interval''=''2H'',''format''=''yyyy-MM-dd-HH'' ,''alias''=''logtime'')";
		System.out.println(MessageFormat.format(sql,starttime,endtime ));
		Aggregations result =  query(MessageFormat.format(sql,starttime,endtime ));
		System.out.println(result.get("logtime"));
		
		Histogram gender = result.get("logtime");
		System.out.println(gender);
		int i =0 ;
		for(Bucket bucket : gender.getBuckets()) {
		
			String key = bucket.getKey().toString();
			long count = ((ValueCount) bucket.getAggregations().get("COUNT(*)")).getValue();
			if(key!=null) {
				label.add("从"+i+"到"+(i = i+2 )+"时");
				num.add(count);
			}
			else {
				throw new Exception(String.format("Unexpected key. expected: m OR f. found: %s", key));
			}
		}
		map.put("label", label);
		map.put("num",num);
		
		return map;
	}
	
	
	// 根据时间 天段统计
	public static Map<String,List<Object>> groupByRangeDayDate(String starttime,String endtime ) throws Exception {
		
		
		Map<String ,List<Object>> map = new HashMap<String, List<Object>>();
		List<Object> label = new ArrayList<Object>();
		List<Object> num = new ArrayList<Object>();
		System.out.println("select count(*) from  log/docs  WHERE logtime BETWEEN '"+starttime+"' AND '"+endtime+"'   group by date_histogram(field= 'logtime','interval'='1d','format'='yyyy-MM-dd' ,'alias'='logtime')");
		Aggregations result =  query(String.format("select count(*) from  log/docs  WHERE logtime BETWEEN '"+starttime+"' AND '"+endtime+"'   group by date_histogram(field= 'logtime','interval'='1d','format'='yyyy-MM-dd' ,'alias'='logtime')"));
		System.out.println(result.get("logtime"));
		
		Histogram gender = result.get("logtime");
		System.out.println(gender);
		int i =0 ;
		for(Bucket bucket : gender.getBuckets()) {
		
			String key = bucket.getKey().toString();
			long count = ((ValueCount) bucket.getAggregations().get("COUNT(*)")).getValue();
			if(key!=null) {
				
				label.add(key.substring(0, 10));
				num.add(count);
			}
			else {
				throw new Exception(String.format("Unexpected key. expected: m OR f. found: %s", key));
			}
		}
		map.put("label", label);
		map.put("num",num);
		
		return map;
	}
	
	
	//根据时间区间进行过滤统计

		public static Map<String,List<Object>> groupByFilterDate(String time1,String time2) throws Exception {
			
			
			Map<String ,List<Object>> map = new HashMap<String, List<Object>>();
			List<Object> label = new ArrayList<Object>();
			List<Object> num = new ArrayList<Object>();
			String sql = "SELECT count(*),url FROM log/docs WHERE logtime BETWEEN ''{0}'' AND ''{1}''  group by url ";
			Aggregations result =  query(MessageFormat.format(sql,time1,time2 ));
			System.out.println(result.get("url"));
			
			Terms gender = result.get("url");
			for(Terms.Bucket bucket : gender.getBuckets()) {
				String key = bucket.getKey().toString();
				long count = ((ValueCount) bucket.getAggregations().get("COUNT(*)")).getValue();
				if(key.endsWith(".json")) {
					label.add(key);
					num.add(count);
				}
//				else {
//					throw new Exception(String.format("Unexpected key. expected: m OR f. found: %s", key));
//				}
			}
			map.put("label", label);
			map.put("num",num);
			
			return map;
			
		}
		
	
	private static Aggregations query(String query) throws Exception {
        SqlElasticSearchRequestBuilder select = getSearchRequestBuilder(query);
		return ((SearchResponse)select.get()).getAggregations();
	}
	
	 private static SqlElasticSearchRequestBuilder getSearchRequestBuilder(String query) throws Exception {
	        SearchDao searchDao = new EsUtilsSql().getSearchDao();
	        return (SqlElasticSearchRequestBuilder) searchDao.explain(query).explain();
	 }
}
