package cn.study.dao.impl;

import cn.study.dao.ProvinceDao;
import cn.study.domain.Province;
import cn.study.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProvinceDaoImpl implements ProvinceDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Province> findAll() {
        String sql = "select * from province";
        List<Province> provinces = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Province>(Province.class));
        return provinces;
    }
}
