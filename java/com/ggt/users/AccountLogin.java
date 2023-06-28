package com.ggt.users;

import db.JDBCDriver;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/Login")
@MultipartConfig()
public class AccountLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/* 進行JDBC連線 */
		JDBCDriver getConn = new JDBCDriver();
		Connection conn = getConn.getConn();

		/* 比對帳號密碼 */
		String sql = "SELECT user_id, user_session FROM users WHERE email=? AND password=?";
		String loginAccount = request.getParameter("loginAccount");
		System.out.println(loginAccount);
		String loginPassword = request.getParameter("loginPassword");
		System.out.println(loginPassword);
		PreparedStatement ppst = null;
		ResultSet rst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, loginAccount);
			ppst.setString(2, loginPassword);
			rst = ppst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int userId = 0;
		String userSession = null;
		boolean isLoginSuccessful = true;
		try {
			if (rst.next()) {
				userId = rst.getInt("user_id");
				userSession = rst.getString("user_session");
			} else {
				isLoginSuccessful = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			isLoginSuccessful = false;
		} finally {
			try {
				conn.close();
				ppst.close();
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		/* 回傳內容 */
		JSONObject jsonResponse = new JSONObject();
		try {
			jsonResponse.put("userId", userId);
			jsonResponse.put("userSession", userSession);
			jsonResponse.put("isLoginSuccessful", isLoginSuccessful);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse.toString());
		out.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
