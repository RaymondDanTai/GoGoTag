package com.ggt.users;

import db.JDBCDriver;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.MultipartConfig;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/CheckLoginStatus")
@MultipartConfig()
public class CheckLoginStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* JDBC連線 */
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		/* SQL查詢比對 */
		String sql = "SELECT first_name, user_avatar_source FROM users WHERE user_session = ? AND user_id = ?";
		String userSession = request.getParameter("userSession");
		String userId = request.getParameter("userId");
		PreparedStatement pstm = null;
		ResultSet rst = null;
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, userSession);
			pstm.setString(2, userId);
			rst = pstm.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		/* 將資料轉為JSON格式 */
		String firstName = "";
		String userAvatarSource = "";
		boolean isLoggedIn = true;
		JSONObject jsonObject = new JSONObject();
		;
		try {
			if (rst.next()) {
				firstName = rst.getString("first_name");
				userAvatarSource = rst.getString("user_avatar_source");
			} else {
				isLoggedIn = false;
			}
			conn.close();
			pstm.close();
			rst.close();
		} catch (SQLException e) {
			e.printStackTrace();
			isLoggedIn = false;
		}

		try {
			jsonObject.put("firstName", firstName);
			jsonObject.put("userAvatarSource", userAvatarSource);
			jsonObject.put("isLoggedIn", isLoggedIn);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonObject.toString());
		out.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}