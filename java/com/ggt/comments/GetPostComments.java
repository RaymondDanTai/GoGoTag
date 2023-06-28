package com.ggt.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.databases.UsersData;

import db.JDBCDriver;

@WebServlet("/GetPostComments")
@MultipartConfig()
public class GetPostComments extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 取得request資料
		request.setCharacterEncoding("UTF-8");
		String postId = request.getParameter("postId");
		String userId = request.getParameter("userId");

		// 連線資料庫
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		// 查詢資料並取出
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
				jsonObj.put("commentText", commentText);
				jsonObj.put("commenterId", commenterId);
				jsonObj.put("commenterAvatarSource", commenterAvatarSource);
				jsonObj.put("commenterName", commenterName);

				if (commenterId.equals(userId)) {
					isCommenter = true;
					jsonObj.put("isCommenter", isCommenter);
				} else {
					jsonObj.put("isCommenter", isCommenter);
				}

				jsonArray.put(jsonObj);
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonArray.toString());
			out.close();
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
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
