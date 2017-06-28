package com.offcn.Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DbcpUtil {
	// 声明一个连接池对象
		private static DataSource ds = null;

		static {
			Properties p = new Properties();
			try {
				// 获取资源
				InputStream is = DbcpUtil.class.getClassLoader().getResourceAsStream("dbcp.properties");
				p.load(is);
				ds = BasicDataSourceFactory.createDataSource(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 获取连接
		public static Connection getConnection() {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException("创建连接失败" + e);
			}
			return conn;
		}

}

