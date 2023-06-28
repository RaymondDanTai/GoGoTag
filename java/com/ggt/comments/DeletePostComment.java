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

@WebServlet("/DeletePostComment")
@MultipartConfig()
public class DeletePostComment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		String commentId = request.getParameter("commentId");

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "DELETE FROM comments WHERE comment_id = ?";
		PreparedStatement ppst = null;
		boolean isPostCommentdeleted = true;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, commentId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isPostCommentdeleted = false;
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
			jsonObj.put("isPostCommentdeleted", isPostCommentdeleted);
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
