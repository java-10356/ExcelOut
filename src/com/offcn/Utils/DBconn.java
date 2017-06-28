package com.offcn.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconn {
	private static Connection conn = null;

	// 获取一个数据库连接
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			String dbUrl = "jdbc:mysql://127.0.0.1:3306/ssm2";
			conn = DriverManager.getConnection(dbUrl, "root", "773231");
			// System.out.println("========数据库连接成功========");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("========数据库连接失败========");
			return null;
		}
		return conn;
	}
}
