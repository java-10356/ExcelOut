package com.offcn.DaoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.offcn.Utils.DbcpUtil;

public class StudentDaoImpl {

	private static Connection conn = DbcpUtil.getConnection();

	private static Statement stmt = null;

	public static void insert(List<HashMap<String, String>> list) {
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			String comn = "";
			String what = "";
			// 遍历单个学生 这个map集合
			HashMap<String, String> map = list.get(i);
			for (HashMap.Entry<String, String> entry : map.entrySet()) {
				comn += entry.getKey() + ",";
				what += "'" + entry.getValue() + "',";
			}

			String sql = "insert into studenttwo(" + comn.substring(0, comn.length() - 1) + ")" + " value("
					+ what.substring(0, what.length() - 1) + ")";

			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//查出数据库student表所有信息
	public static List getAll() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = DbcpUtil.getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		int count = 0;
		try {
			stmt = conn.createStatement();
			String sql = "select  * from student";
			rs = stmt.executeQuery(sql);
			rsmd = rs.getMetaData();// 字段名称
			count = rsmd.getColumnCount();// 得对象得字段值
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < count; i++) {
					String key = rsmd.getColumnLabel(i + 1);
					Object value = rs.getObject(key);
					map.put(key, value);
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

}
