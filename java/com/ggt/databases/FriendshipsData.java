package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.JDBCDriver;

public class FriendshipsData{

	public int checkFriendshipStatus(String currentUserId, String pageOwnerId){
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT status FROM friendships WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";

		PreparedStatement ppst = null;
		ResultSet rst = null;
		int friendshipStatusCode = -1;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, currentUserId);
			ppst.setString(2, pageOwnerId);
			ppst.setString(3, pageOwnerId);
			ppst.setString(4, currentUserId);
			rst = ppst.executeQuery();

			if (rst.next()) {
				friendshipStatusCode = rst.getInt("status");
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
		
		return friendshipStatusCode;
	}

	
	public ArrayList<String> getUserFriendsId(String userId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		
		String sql = "SELECT user1_id, user2_id FROM friendships WHERE user1_id = ? OR user2_id = ?";
		
		PreparedStatement ppst = null;
		ResultSet rst = null;
		ArrayList<String> arrayList = new ArrayList<>();
		String userIdOne = "";
		String userIdTwo = "";
		
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			ppst.setString(2, userId);
			rst = ppst.executeQuery();
			while(rst.next()) {
				userIdOne = rst.getString("user1_id");
				userIdTwo = rst.getString("user2_id");
				if(userIdOne.equals(userId)) {
					arrayList.add(userIdTwo);
				} else {
					arrayList.add(userIdOne);
				}
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
		
		return arrayList;
	}
	

}
