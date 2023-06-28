package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.JDBCDriver;

public class CommentsData {

	public boolean deletePostComments(String postId) {

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "DELETE FROM comments WHERE post_id = ?";

		PreparedStatement ppst = null;
		boolean isPostCommentsdeleted = false;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			ppst.executeUpdate();
			isPostCommentsdeleted = true;
		} catch (SQLException e) {
			e.printStackTrace();
			isPostCommentsdeleted = false;
			return isPostCommentsdeleted;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isPostCommentsdeleted;
	}

	public int getCommentCount(String postId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT COUNT(post_id) FROM comments WHERE post_id = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		int commentCount = 0;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				commentCount = rst.getInt("COUNT(post_id)");
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
		return commentCount;
	}

	public JSONArray getPostComments(String postId, String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT comment_id, comment_text, commenter_id FROM comments WHERE post_id = ? ORDER BY created_at LIMIT 10";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		String commentId = "";
		String commentText = "";
		String commenterId = "";
		String commenterAvatarSource = "";
		String commenterName = "";
		boolean isCommenter = false;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		try {
			System.out.println("輸入查詢內容");
			System.out.println("postId是" + postId);
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();
			while (rst.next()) {
				jsonObj = new JSONObject();
				commentId = rst.getString("comment_id");
				commentText = rst.getString("comment_text");
				commenterId = rst.getString("commenter_id");
				UsersData usersDatamaintenace = new UsersData();
				commenterAvatarSource = usersDatamaintenace.getUserAvatarSource(commenterId);
				commenterName = usersDatamaintenace.getUserName(commenterId);
				jsonObj.put("commentId", commentId);
				System.out.println(commentId);
				jsonObj.put("commentText", commentText);
				System.out.println(commentText);
				jsonObj.put("commenterId", commenterId);
				System.out.println(commenterId);
				jsonObj.put("commenterAvatarSource", commenterAvatarSource);
				System.out.println(commenterAvatarSource);
				jsonObj.put("commenterName", commenterName);
				System.out.println(commenterName);
				if (commenterId.equals(userId)) {
					isCommenter = true;
					jsonObj.put("isCommenter", isCommenter);
				} else {
					jsonObj.put("isCommenter", isCommenter);
				}
				jsonArray.put(jsonObj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
		return jsonArray;
	}
}
