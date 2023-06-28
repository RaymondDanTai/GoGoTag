package com.ggt.likes;

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

import org.json.JSONException;
import org.json.JSONObject;

import db.JDBCDriver;

@WebServlet("/CheckUserLikedPost")
@MultipartConfig()
public class CheckUserLikedPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("UTF-8");
		String postId = request.getParameter("postId");
		String userId = request.getParameter("userId");
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		
		String sql = "SELECT like_id FROM likes WHERE user_id = ? && post_id = ?";
		
		PreparedStatement ppst = null;
		ResultSet rst = null;
		boolean wasUserLikePost = false;
		JSONObject jsonObj = new JSONObject();
		
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			ppst.setString(2, postId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				wasUserLikePost = true;
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
		
		try {
			jsonObj.put("wasUserLikePost", wasUserLikePost);
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
