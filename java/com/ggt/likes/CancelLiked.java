package com.ggt.likes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import db.JDBCDriver;

@WebServlet("/CancelLiked")
@MultipartConfig()
public class CancelLiked extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		request.setCharacterEncoding("UTF-8");
		String postId = request.getParameter("postId");
		String userId = request.getParameter("userId");
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		
		String sql = "DELETE FROM likes WHERE post_id=? && user_id = ?";
		
		PreparedStatement ppst = null;
		boolean islikeCanceled = true;
		JSONObject jsonObj = new JSONObject();
		
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			ppst.setString(2, userId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			islikeCanceled = false;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			jsonObj.put("islikeCanceled", islikeCanceled);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

}
