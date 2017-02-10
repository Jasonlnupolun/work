package jedis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Set;

/**
 * Created by Administrator on 2017/2/10.
 */
public class RedisTest {

    public static Logger logger = Logger.getLogger(RedisTest.class);
    public static void test(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        config.setTestOnBorrow(true);
        JedisPool pool = new JedisPool(config, "serverIp", 8080, 2000, "passwrod");

        Jedis jedis = null;
        boolean broken = false;
        if (pool != null) {
            try {
                jedis = pool.getResource();
                Set<String> keys = jedis.keys("*");
            }
            catch (JedisException e) {
                broken = handleJedisException(e);
                throw e;
            }
            finally {
                closeResource(pool, jedis, broken);
            }
            pool.destroy();
        }

    }

    private static boolean handleJedisException(JedisException jedisException) {
        if (jedisException instanceof JedisConnectionException) {
            logger.error("Redis connection lost.", jedisException);
        }
        else if (jedisException instanceof JedisDataException) {
            if ((jedisException.getMessage() != null) && (jedisException
                    .getMessage().indexOf("READONLY") != -1)) {
                logger.error("Redis connection are read-only slave.",
                        jedisException);
            }
            else {
                return false;
            }
        }
        else {
            logger.error("Jedis exception happen.", jedisException);
        }
        return true;
    }

    /**
     * Return jedis connection to the pool, call different return methods
     * depends on the conectionBroken status.
     */
    protected static void closeResource(JedisPool pool, Jedis jedis,
                                        boolean conectionBroken) {
        try {
            if (conectionBroken) {
                pool.returnBrokenResource(jedis);
            }
            else {
                pool.returnResource(jedis);
            }
        }
        catch (Exception e) {
            logger.error("return back jedis failed, will fore close the jedis.",
                    e);
            destroyJedis(jedis);
        }
    }

    public static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                }
                catch (Exception e) {
                }
                jedis.disconnect();
            }
            catch (Exception e) {
            }
        }
    }
}
