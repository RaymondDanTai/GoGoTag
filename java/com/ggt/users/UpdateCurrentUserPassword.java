package com.ggt.users;

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

import com.ggt.databases.UsersData;

import db.JDBCDriver;

@WebServlet("/UpdateCurrentUserPassword")
@MultipartConfig()
public class UpdateCurrentUserPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userSession = request.getParameter("userSession");
		String originalUserPassword = request.getParameter("originalUserPassword");
		String newUserPassword = request.getParameter("newUserPassword");

		UsersData usersData = new UsersData();
		boolean isPasswordCorrect = usersData.compareUserPassword(userId, userSession, originalUserPassword);
		JSONObject jsonObj = new JSONObject();
		
		if (isPasswordCorrect) {
			JDBCDriver jdbc = new JDBCDriver();
			Connection conn = jdbc.getConn();

			String sql = "UPDATE users SET password = ? WHERE user_id = ? && user_session = ?";

			PreparedStatement ppst = null;
			boolean isUserPasswordUpdated = true;

			try {
				ppst = conn.prepareStatement(sql);
				ppst.setString(1, newUserPassword);
				ppst.setString(2, userId);
				ppst.setString(3, userSession);
				ppst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				isUserPasswordUpdated = false;
			} finally {
				try {
						conn.close();
						ppst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				jsonObj.put("isPasswordCorrect", isPasswordCorrect);
				jsonObj.put("isUserPasswordUpdated", isUserPasswordUpdated);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println(jsonObj.toString());
				out.close();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				jsonObj.put("isPasswordCorrect", isPasswordCorrect);
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
