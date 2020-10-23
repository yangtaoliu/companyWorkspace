package cn.study.service.impl;

import cn.study.dao.ProvinceDao;
import cn.study.dao.impl.ProvinceDaoImpl;
import cn.study.domain.Province;
import cn.study.jedis.util.JedisPoolUtils;
import cn.study.service.ProvinceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ProvinceServiceImpl implements ProvinceService {
    private ProvinceDao provinceDao = new ProvinceDaoImpl();

    @Override
    public List<Province> findAll() {
        return provinceDao.findAll();
    }

    /**
     * 使用redis缓存
     * @return
     */
    @Override
    public String findAllJson() {
        //从redis查询数据
        Jedis jedis = JedisPoolUtils.getdJedis();
        String provinceJson = jedis.get("province");
        if(provinceJson==null ||"".equals(provinceJson)){
            //redis中没有数据
            System.out.println("redis中无数据，查询数据库！");
            //从数据库中查询
            List<Province> all = provinceDao.findAll();
            //将数据序列化成json
            ObjectMapper mapper = new ObjectMapper();
            try {
                provinceJson = mapper.writeValueAsString(all);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //存入redis
            jedis.set("province", provinceJson);
            //归还连接，最好使用finally方式关闭连接
            jedis.close();
        }else{
            System.out.println("redis中有数据，查询缓存！");
        }
        return provinceJson;
    }
}
