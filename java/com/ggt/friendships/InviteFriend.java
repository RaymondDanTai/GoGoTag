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

import com.ggt.databases.FriendshipsData;
import com.ggt.databases.UsersData;

import db.JDBCDriver;

@WebServlet("/InviteFriend")
@MultipartConfig()
public class InviteFriend extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		String currentUserId = request.getParameter("currentUserId");
		
		String pageOwnerSession = request.getParameter("pageOwnerSession");
		UsersData usersData = new UsersData();
		String pageOwnerId = usersData.getUserId(pageOwnerSession);
		
		FriendshipsData friendshipsData = new FriendshipsData();
		int friendshipStatusCode = friendshipsData.checkFriendshipStatus(currentUserId, pageOwnerId);
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		
		String sql = "";
		PreparedStatement ppst = null;
		boolean isFriendInvited = true;
		
		if (friendshipStatusCode == -1) {
			sql = "INSERT INTO friendships (user1_id, user2_id, status) VALUES(?, ?, 1)";
			try {
				ppst = conn.prepareStatement(sql);
				ppst.setString(1, currentUserId);
				ppst.setString(2, pageOwnerId);
				ppst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				isFriendInvited = false;
			} finally {
				try {
					conn.close();
					ppst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			sql = "UPDATE friendships SET status = 1 WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
			try {
				ppst = conn.prepareStatement(sql);
				ppst.setString(1, currentUserId);
				ppst.setString(2, pageOwnerId);
				ppst.setString(3, pageOwnerId);
				ppst.setString(4, currentUserId);
				ppst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				isFriendInvited = false;
			} finally {
				try {
					conn.close();
					ppst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("isFriendInvited", isFriendInvited);
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
