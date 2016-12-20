package com.java.kanke.utils.redis;

import com.java.kanke.utils.PropertyUtil;

/**
 * Created by Administrator on 2016/11/8.
 */
public class JRedisPoolConfig {

    public final static Long  MAX_ACTIVE = Long.parseLong(PropertyUtil.get("max_active"));

    public final static Integer  MAX_IDLE = Integer.parseInt(PropertyUtil.get("max_idle"));

    public final static Long  MAX_WAIT = Long.parseLong(PropertyUtil.get("max_wait"));

    public final static String  REDIS_IP = PropertyUtil.get("redis_ip");

    public final static Integer  REDIS_PORT = Integer.parseInt(PropertyUtil.get("redis_host"));

    public final static String  REDIS_PASSWORD = PropertyUtil.get("redis_pwd");
}
