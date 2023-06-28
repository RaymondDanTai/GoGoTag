package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBCDriver;

public class UsersData {

	public String getUserName(String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT last_name, first_name FROM users WHERE users.user_id = ?";
		String lastName = "";
		String firstName = "";
		String fullName = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				lastName = rst.getString("last_name");
				firstName = rst.getString("first_name");
				fullName = lastName + firstName;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				ppst.close();
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}

		return fullName;
	}

	public String getUserAvatarSource(String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT user_avatar_source FROM users WHERE users.user_id = ?";
		String userAvatarSource = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				userAvatarSource = rst.getString("user_avatar_source");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				ppst.close();
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}

		return userAvatarSource;
	}

	public boolean compareUserPassword(String userId, String userSession, String originalUserPassword) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT password FROM users WHERE user_id = ? && user_session = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		boolean isPasswordCorrect = false;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			ppst.setString(2, userSession);
			rst = ppst.executeQuery();
			String password = "";
			if (rst.next()) {
				password = rst.getString("password");
				if (password.equals(originalUserPassword))
					isPasswordCorrect = true;
			}
			conn.close();
			ppst.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isPasswordCorrect;
	}

	public String getUserSession(String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT user_session FROM users WHERE user_id = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		String userSession = "";
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				userSession = rst.getString("user_session");
			}
			conn.close();
			ppst.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userSession;
	}

	public String getUserId(String userSession) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT user_id FROM users WHERE user_session = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		String userId = "";
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userSession);
			rst = ppst.executeQuery();
			if (rst.next()) {
				userId = rst.getString("user_id");
			}
			conn.close();
			ppst.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userId;
	}
}
