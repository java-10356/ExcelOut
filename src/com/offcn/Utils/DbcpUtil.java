package com.offcn.Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DbcpUtil {
	// ����һ�����ӳض���
		private static DataSource ds = null;

		static {
			Properties p = new Properties();
			try {
				// ��ȡ��Դ
				InputStream is = DbcpUtil.class.getClassLoader().getResourceAsStream("dbcp.properties");
				p.load(is);
				ds = BasicDataSourceFactory.createDataSource(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// ��ȡ����
		public static Connection getConnection() {
			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException("��������ʧ��" + e);
			}
			return conn;
		}

}

