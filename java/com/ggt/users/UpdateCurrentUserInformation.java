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

import db.JDBCDriver;

@WebServlet("/UpdateCurrentUserInformation")
@MultipartConfig()
public class UpdateCurrentUserInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userSession = request.getParameter("userSession");
		String lastname = request.getParameter("lastname");
		String firstname = request.getParameter("firstname");
		String gender = request.getParameter("gender");
		String phoneNumber = request.getParameter("phoneNumber");
		String birthDate = request.getParameter("birthDate");
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "UPDATE users SET last_name = ?, first_name = ?, gender = ?, phone_number = ?, birth_date = ? WHERE user_id = ? && user_session = ?";
		PreparedStatement ppst = null;
		boolean isUserInformationUpdated = true;
		JSONObject jsonObj = new JSONObject();
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, lastname);
			ppst.setString(2, firstname);
			ppst.setString(3, gender);
			ppst.setString(4, phoneNumber);
			ppst.setString(5, birthDate);
			ppst.setString(6, userId);
			ppst.setString(7, userSession);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isUserInformationUpdated = false;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			jsonObj.put("isUserInformationUpdated", isUserInformationUpdated);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
