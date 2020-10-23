package cn.study.jedis.test;

import cn.study.jedis.util.JedisPoolUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.*;

/**
 * jedis测试类
 */
public class JedisTest {
    @Test
    public void test1(){
        //1获取连接
        Jedis jedis = new Jedis("localhost",6379);
        //2调用对应的方法操作
        jedis.set("username","zhangsan");
        //关闭连接
        jedis.close();
    }

    /**
     * String数据结构操作
     */
    @Test
    public void test2(){
        //1获取连接
        Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379
        //2调用对应的方法操作
        jedis.set("username","zhangsan");

        String username = jedis.get("username");
        System.out.println(username);

        //可以使用setex()方法存储可以指定过期时间的 key value
        jedis.setex("activecode",20,"hehe");//将activecode：hehe键值对存入redis，并且20秒后自动删除该键值对
        //关闭连接
        jedis.close();
    }

    /**
     * hash数据结构操作
     */
    @Test
    public void test3(){
        //1获取连接
        Jedis jedis = new Jedis("localhost",6379);
        //2调用对应的方法操作
        //设置
        jedis.hset("user","name","lisi");
        jedis.hset("user","age","23");
        jedis.hset("user","gender","male");

        //获取
        String name = jedis.hget("user","name");
        System.out.println(name);

        //获取hash的所有map中的数据
        Map<String, String> user = jedis.hgetAll("user");
/*        Set<String> keyset = user.keySet();
        for(String key:keyset){
            String value = user.get(key);
            System.out.println(key + ":" + value);
        }*/
/*        Set<Map.Entry<String, String>> entries = user.entrySet();
        for(Map.Entry<String, String> entry:  entries){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
        }*/

/*        for(String key : user.keySet()){
            System.out.println(key);
        }
        for(String value : user.values()){
            System.out.println(value);
        }*/
        Iterator<Map.Entry<String, String>> iterator = user.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            System.out.println(key+":"+value);
        }

        //关闭连接
        jedis.close();
    }

    /**
     * List数据结构操作
     */
    @Test
    public void test4(){
        //1获取连接
        Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379
        //2调用对应的方法操作
        jedis.lpush("mylist","a","b","c");//从左边存            c b a
        jedis.rpush("mylist","a","b","c");//从右边存            c b a a b c

        List<String> mylist = jedis.lrange("mylist", 0, -1);//以 -1 表示列表的最后一个元素，-2 表示列表的倒数第二个元素，以此类推。
        for(String s:mylist){
            System.out.println(s);
        }
        String element1 = jedis.lpop("mylist");
        String element2 = jedis.rpop("mylist");
        System.out.println(element1 + "---" + element2);

        //关闭连接
        jedis.close();
    }

    @Test
    public void testArr(){
        int[] arr = {1,2,3};
        System.out.println(arr);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test(){
        //1获取连接
        Jedis jedis = new Jedis();
        //2调用对应的方法操作
        //即 ltrim key start end 中的start要比end大即可，数值且都为正数,可以移除所有的元素
        jedis.ltrim("mylist",1,0);
        //关闭连接
        jedis.close();
    }

    /**
     * set数据结构操作    和List的区别主要是不能重复元素，存相同的会覆盖
     */
    @Test
    public void test5(){
        //1获取连接
        Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379
        //2调用对应的方法操作
        jedis.sadd("myset","java","php","c++");
        Set<String> myset = jedis.smembers("myset");
        System.out.println(myset);
        //关闭连接
        jedis.close();
    }

    /**
     * sorted set数据结构操作    和set的区别主要是存在一个分数，可以排序，也不能重复，重复会覆盖分数
     */
    @Test
    public void test6(){
        //1获取连接
        Jedis jedis = new Jedis();//如果使用空参构造，默认值 "localhost",6379
        //2调用对应的方法操作
        jedis.zadd("mysort",1,"亚瑟");
        jedis.zadd("mysort",30,"后裔");
        jedis.zadd("mysort",55,"孙悟空");

        Set<String> mysort = jedis.zrange("mysort", 0, -1);
        Set<Tuple> mysort1 = jedis.zrangeWithScores("mysort", 0, -1);
        System.out.println(mysort);
        System.out.println(mysort1);

        //关闭连接
        jedis.close();
    }

    /**
     * jedis连接池的使用
     */
    @Test
    public void test7(){
        //创建一个连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);

        //创建连接池
        //JedisPool jedisPool = new JedisPool();
        JedisPool jedisPool = new JedisPool(config,"127.0.0.1",6379);
        //获取连接
        Jedis jedis = jedisPool.getResource();
        //使用
        jedis.set("hehe","5555555");
        //关闭,归还到连接池中
        jedis.close();
    }
    /**
     * jedis连接池工具类的使用
     */
    @Test
    public void test8(){

        //获取连接
        Jedis jedis = JedisPoolUtils.getdJedis();
        //使用
        jedis.set("hehe","aaaaaaa");
        //关闭,归还到连接池中
        jedis.close();
    }
}
