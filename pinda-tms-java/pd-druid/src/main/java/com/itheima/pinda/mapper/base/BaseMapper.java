package com.itheima.pinda.mapper.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseMapper {

    @Autowired
    private DataSource dataSource;

    public List<Map> list(String sql) {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            List<Map> resultList = new ArrayList();
            while (rs.next()) {
                Map map = new HashMap();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    map.put(columnName, rs.getObject(columnName));
                }
                resultList.add(map);
            }

            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭连接异常：", e);
            }
        }
        return null;
    }

    public Map listFirst(String sql) {
        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();
            rs = st.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rs.next()) {
                Map map = new HashMap();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    String columnName = rsmd.getColumnName(i + 1);
                    map.put(columnName, rs.getObject(columnName));
                }
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("关闭连接异常：", e);
            }
        }
        return null;
    }
}
