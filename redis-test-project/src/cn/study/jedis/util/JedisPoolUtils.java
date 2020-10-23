package cn.study.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *      jedis连接池工具类
 *          加载配置文件，配置连接池的参数
 *          提供获取连接的方法
 */
public class JedisPoolUtils {
    private static JedisPool jedisPool;

    static {
        //读取配置文件
        InputStream is = JedisPoolUtils.class.getClassLoader().getResourceAsStream("jedis.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取数据，设置到jedisPoolConfig中
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));

        String host = properties.getProperty("host");
        int port = Integer.parseInt(properties.getProperty("port"));

        jedisPool = new JedisPool(jedisPoolConfig,host,port);
    }

    /**
     * 获取连接的方法
     */
    public static Jedis getdJedis(){
        return jedisPool.getResource();
    }
}
