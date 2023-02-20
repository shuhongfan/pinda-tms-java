import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在Java程序中操作Druid
 */
public class DruidTest {
    public static void main(String[] args) throws Exception{
        Class.forName("org.apache.calcite.avatica.remote.Driver");
        Connection connection = DriverManager.getConnection("jdbc:avatica:remote:url=http://39.99.34.207:8888/druid/v2/sql/avatica/");
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            rs = st.executeQuery("select * from ip_msg");
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
            System.out.println("==="+resultList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
            }
        }
    }
}
