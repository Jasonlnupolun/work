package com.java.kanke.utils.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
 * @author Mr.hu 
 * @version crateTime：2013-10-30 下午5:41:30 
 * Class Explain:JedisUtil   
 */  
public class JedisUtil {   
      
     private Logger log = Logger.getLogger(this.getClass());    
     /**缓存生存时间 */  
     private final int expire = 60000;  
     private static JedisPool jedisPool = null;
     private JedisUtil() {
          
     }
     /*static {
            JedisPoolConfig config = new JedisPoolConfig();  
            config.setMaxIdle(JRedisPoolConfig.MAX_IDLE);
            config.setMaxWaitMillis(JRedisPoolConfig.MAX_WAIT);
            //redis如果设置了密码：  
            jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP,
                    JRedisPoolConfig.REDIS_PORT,
                    10000, JRedisPoolConfig.REDIS_PASSWORD);
       }*/

    public static JedisPool getPool() {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(1000);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(5);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(1000 * 100);
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP,
                    JRedisPoolConfig.REDIS_PORT,
                    10000, JRedisPoolConfig.REDIS_PASSWORD);
        }
        return jedisPool;
    }
       

      
     /** 
      * 从jedis连接池中获取获取jedis对象   
      * @return 
      */  
     public Jedis getJedis() {    
         return getPool().getResource();
    }  
       
       
     private static final JedisUtil jedisUtil = new JedisUtil();
       
   
    /** 
     * 获取JedisUtil实例 
     * @return 
     */  
    public static JedisUtil getInstance() {
        if(jedisUtil!=null){
            return new JedisUtil();
        }
        return jedisUtil;
    }  
  
    /** 
     * 回收jedis 
     * @param jedis 
     */  
    public void returnJedis(Jedis jedis) {  
        jedisPool.returnResource(jedis);  
    }   

    /** 
     * 设置过期时间 
     * @author ruan 2013-4-11 
     * @param key 
     * @param seconds 
     */  
    public void expire(String key, int seconds) {  
        if (seconds <= 0) {   
            return;  
        }  
        Jedis jedis = getJedis();  
        jedis.expire(key, seconds);  
        returnJedis(jedis);  
    }  
  
    /** 
     * 设置默认过期时间 
     * @author ruan 2013-4-11 
     * @param key 
     */  
    public void expire(String key) {  
        expire(key, expire);  
    }


}  