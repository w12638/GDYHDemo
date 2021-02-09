package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class DBUtil {
	private static String url =  "jdbc:sqlite:D:\\用户目录\\下载\\sqlite-dll-win64-x64-3340100\\app.db";
	//增删改sql
	public static  void operationSql(String sql) throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(url);
		Statement stmt = ((java.sql.Connection) c).createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
        c.close();
	}

	//查询sql 返回String
	public static  String selectSql(String sql,String data) throws ClassNotFoundException, SQLException{
		String res = "";
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(url);
		Statement stmt = ((java.sql.Connection) c).createStatement();
		ResultSet rs = stmt.executeQuery( sql);
		rs.getString(data);
		rs.close();
		stmt.close();
        c.close();
        return res;
	}
	//查询sql返回List
	public static  List selectSql(String sql) throws ClassNotFoundException, SQLException{
		List list = new ArrayList();
		Class.forName("org.sqlite.JDBC");
		Connection c = DriverManager.getConnection(url);
		Statement stmt = ((java.sql.Connection) c).createStatement();
		ResultSet rs = stmt.executeQuery( sql);
        list = convertList(rs);
		rs.close();
		stmt.close();
        c.close();
        return list;
	}
	//ResultSet转换为List的方法
	private static List convertList(ResultSet rs) throws SQLException {
		List list = new ArrayList();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount(); //Map rowData;
		while (rs.next()) { //rowData = new HashMap(columnCount);
		Map rowData = new HashMap();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		} 
		return list;
	}
}
