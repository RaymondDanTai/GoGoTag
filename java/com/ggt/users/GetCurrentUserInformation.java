package com.ggt.users;

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

@WebServlet("/GetCurrentUserInformation")
@MultipartConfig()
public class GetCurrentUserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userSession = request.getParameter("userSession");
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT first_name, last_name, email, gender, phone_number, birth_date, user_avatar_source FROM users WHERE user_id = ? && user_session = ?";
		
		PreparedStatement ppst = null;
		ResultSet rst = null;
		
		JSONObject jsonObj = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			ppst.setString(2, userSession);
			rst = ppst.executeQuery();
			jsonObj = new JSONObject();
			if (rst.next()) {
				String lastName = rst.getString("last_name");
				jsonObj.put("lastName", lastName);
				String firstName = rst.getString("first_name");
				jsonObj.put("firstName", firstName);
				String email = rst.getString("email");
				jsonObj.put("email", email);
				String gender = rst.getString("gender");
				jsonObj.put("gender", gender);
				String phoneNumber = rst.getString("phone_number");
				jsonObj.put("phoneNumber", phoneNumber);
				String birthDate = rst.getString("birth_date");
				birthDate = birthDate.substring(0, 10);
				jsonObj.put("birthDate", birthDate);
				String userAvatarSource = rst.getString("user_avatar_source");
				jsonObj.put("userAvatarSource", userAvatarSource);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println(jsonObj.toString());
				out.close();
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
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
