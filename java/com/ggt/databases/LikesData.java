package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.JDBCDriver;

public class LikesData {

	public boolean deletePostLikes(String postId) {

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "DELETE FROM likes WHERE post_id = ?";

		PreparedStatement ppst = null;
		boolean isPostLikesdeleted = false;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			ppst.executeUpdate();
			isPostLikesdeleted = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isPostLikesdeleted = false;
			return isPostLikesdeleted;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isPostLikesdeleted;
	}

	public int getLikeCount(String postId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT COUNT(post_id) FROM likes WHERE post_id = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		int likeCount = 0;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				likeCount = rst.getInt("COUNT(post_id)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
					conn.close();
					ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return likeCount;
	}

	public boolean checkUserLikedPost(String postId, String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT like_id FROM likes WHERE post_id=? && user_id = ?";

		PreparedStatement ppst = null;
		ResultSet rst = null;
		boolean wasUserLikePost = false;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			ppst.setString(2, userId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				wasUserLikePost = true;
			}
			conn.close();
			ppst.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wasUserLikePost;
	}
}
