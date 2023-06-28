package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBCDriver;

public class PostsData {

	public String getPosterName(String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT last_name, first_name FROM users WHERE users.user_id = ?";
		String posterName = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				posterName += rst.getString("last_name");
				posterName += rst.getString("first_name");
			} else {
				posterName = "未知的使用者";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		return posterName;
	}

	public String getPosterAvatarURL(String posterId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT user_avatar_source FROM users WHERE users.user_id = ?";
		String posterAvatarURL = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, posterId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				posterAvatarURL = rst.getString("user_avatar_source");
			} else {
				posterAvatarURL = "未知的使用者";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		return posterAvatarURL;
	}

	public String getPostText(String postId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT last_name,first_name FROM users WHERE users.user_id = ?";
		String posterName = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				posterName += rst.getString("last_name");
				posterName += rst.getString("first_name");
			} else {
				posterName = "未知的使用者";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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

		return posterName;
	}

	public String getPosterId(String postId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT poster_id FROM posts WHERE post_id = ?";
		String posterId = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				posterId = rst.getString("poster_id");
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

		return posterId;
	}
	
	public String getPostId(String posterId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT post_id FROM posts WHERE poster_id = ?";
		String postId = "";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, posterId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				postId = rst.getString("post_id");
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

		return postId;
	}
}
