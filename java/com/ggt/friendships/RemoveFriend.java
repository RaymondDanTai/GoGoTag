package com.ggt.friendships;

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

import com.ggt.databases.UsersData;

import db.JDBCDriver;

@WebServlet("/RemoveFriend")
@MultipartConfig()
public class RemoveFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		String currentUserId = request.getParameter("currentUserId");

		String pageOwnerSession = request.getParameter("pageOwnerSession");
		UsersData usersData = new UsersData();
		String pageOwnerId = usersData.getUserId(pageOwnerSession);

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "DELETE FROM friendships WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
		PreparedStatement ppst = null;
		boolean isFriendRemoved = true;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, currentUserId);
			ppst.setString(2, pageOwnerId);
			ppst.setString(3, pageOwnerId);
			ppst.setString(4, currentUserId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isFriendRemoved = false;
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
			jsonObj.put("isFriendRemoved", isFriendRemoved);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
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
