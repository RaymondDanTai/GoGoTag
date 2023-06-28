package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCDriver {
	private static void loadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("載入JDBC驅動程式發生錯誤");
			e.printStackTrace();
		}
	}

	public Connection getConn() {
		loadDriver();
		String url = "jdbc:mysql://localhost:8888/social_media";
		String user = "root";
		String password = "1234";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("連線資料庫發生錯誤");
			e.printStackTrace();
		}
		return conn;
	}
}
