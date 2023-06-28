package com.ggt.likes;

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

@WebServlet("/ThumbLike")
@MultipartConfig()
public class ThumbLike extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		String postId = request.getParameter("postId");
		String userId = request.getParameter("userId");
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		
		String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
		
		PreparedStatement ppst = null;
		boolean isLikeThumbed = true;
		JSONObject jsonObj = new JSONObject();
		
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			ppst.setString(2, userId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isLikeThumbed = false;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			jsonObj.put("isLikeThumbed", isLikeThumbed);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

}
