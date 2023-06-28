package com.ggt.comments;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import db.JDBCDriver;

@WebServlet("/AddPostComment")
@MultipartConfig()
public class AddPostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 設置request資料的編碼
		request.setCharacterEncoding("UTF-8");
		String commentText = request.getParameter("commentText");
		String commentedPostId = request.getParameter("commentedPostId");
		String commenterId = request.getParameter("commenterId");

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "INSERT INTO comments ( comment_text, post_id, commenter_id) VALUES( ?, ?, ?)";

		PreparedStatement ppst = null;
		boolean isCommentSuccessful = true;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, commentText);
			ppst.setString(2, commentedPostId);
			ppst.setString(3, commenterId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isCommentSuccessful = false;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("isCommentSuccessful", isCommentSuccessful);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
