package com.java.kanke.utils.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

import java.util.List;
import java.util.Set;

//*******************************************Keys*******************************************//
    public class Keys {
        /** 
         * 清空所有key 
         */  
        public String flushAll() {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            String stata = jedis.flushAll();
            tool.returnJedis(jedis);
            return stata;  
        }  
  
        /** 
         * 更改key 
         * @param String oldkey 
         * @param String  newkey 
         * @return 状态码 
         * */  
        public String rename(String oldkey, String newkey) {   
            return rename(SafeEncoder.encode(oldkey),
                    SafeEncoder.encode(newkey));  
        }  
  
        /** 
         * 更改key,仅当新key不存在时才执行 
         * @param String oldkey 
         * @param String newkey  
         * @return 状态码 
         * */  
        public long renamenx(String oldkey, String newkey) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long status = jedis.renamenx(oldkey, newkey);
            tool.returnJedis(jedis);
            return status;  
        }  
  
        /** 
         * 更改key 
         * @param String oldkey 
         * @param String newkey 
         * @return 状态码 
         * */  
        public String rename(byte[] oldkey, byte[] newkey) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            String status = jedis.rename(oldkey, newkey);  
            tool.returnJedis(jedis);
            return status;  
        }  
  
        /** 
         * 设置key的过期时间，以秒为单位 
         * @param String key 
         * @param 时间,已秒为单位 
         * @return 影响的记录数 
         * */  
        public long expired(String key, int seconds) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long count = jedis.expire(key, seconds);  
            tool.returnJedis(jedis);
            return count;  
        }  
  
        /** 
         * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。 
         * @param String key 
         * @param 时间,已秒为单位 
         * @return 影响的记录数 
         * */  
        public long expireAt(String key, long timestamp) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long count = jedis.expireAt(key, timestamp);  
            tool.returnJedis(jedis);
            return count;  
        }  
  
        /** 
         * 查询key的过期时间 
         * @param String key 
         * @return 以秒为单位的时间表示 
         * */  
        public long ttl(String key) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis sjedis=tool.getJedis();
            long len = sjedis.ttl(key);  
            tool.returnJedis(sjedis);
            return len;  
        }  
  
        /** 
         * 取消对key过期时间的设置 
         * @param key 
         * @return 影响的记录数 
         * */  
        public long persist(String key) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long count = jedis.persist(key);  
            tool.returnJedis(jedis);
            return count;  
        }  
  
        /** 
         * 删除keys对应的记录,可以是多个key 
         * @param String  ... keys 
         * @return 删除的记录数 
         * */  
        public long del(String... keys) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long count = jedis.del(keys);  
            tool.returnJedis(jedis);
            return count;  
        }  
  
        /** 
         * 删除keys对应的记录,可以是多个key 
         * @param String .. keys 
         * @return 删除的记录数 
         * */  
        public long del(byte[]... keys) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            long count = jedis.del(keys);  
            tool.returnJedis(jedis);
            return count;  
        }  
  
        /** 
         * 判断key是否存在 
         * @param String key 
         * @return boolean 
         * */  
        public boolean exists(String key) {  
            //ShardedJedis sjedis = getShardedJedis();
            JedisUtil tool = JedisUtil.getInstance();
            Jedis sjedis=tool.getJedis();
            boolean exis = sjedis.exists(key);
            tool.returnJedis(sjedis);
            return exis;  
        }  
  
        /** 
         * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法 
         * @param String key 
         * @return List<String> 集合的全部记录 
         * **/  
        public List<String> sort(String key) {
            JedisUtil tool = JedisUtil.getInstance();
            //ShardedJedis sjedis = getShardedJedis();  
            Jedis sjedis=tool.getJedis();
            List<String> list = sjedis.sort(key);
            tool.returnJedis(sjedis);
            return list;  
        }  
  
        /** 
         * 对List,Set,SortSet进行排序或limit 
         * @param String key 
         * @param SortingParams parame 定义排序类型或limit的起止位置. 
         * @return List<String> 全部或部分记录 
         * **/  
        public List<String> sort(String key, SortingParams parame) {
            //ShardedJedis sjedis = getShardedJedis();
            JedisUtil tool = JedisUtil.getInstance();
            Jedis sjedis=tool.getJedis();
            List<String> list = sjedis.sort(key, parame);  
            tool.returnJedis(sjedis);
            return list;  
        }  
  
        /** 
         * 返回指定key存储的类型 
         * @param String key 
         * @return String string|list|set|zset|hash 
         * **/  
        public String type(String key) {
            JedisUtil tool = JedisUtil.getInstance();
            //ShardedJedis sjedis = getShardedJedis();   
            Jedis sjedis=tool.getJedis();
            String type = sjedis.type(key);   
            tool.returnJedis(sjedis);
            return type;  
        }  
  
        /** 
         * 查找所有匹配给定的模式的键 
         * @param String  key的表达式,*表示多个，？表示一个 
         * */  
        public Set<String> keys(String pattern) {
            JedisUtil tool = JedisUtil.getInstance();
            Jedis jedis = tool.getJedis();
            Set<String> set = jedis.keys(pattern);
            tool.returnJedis(jedis);
            return set;  
        }  
    }  