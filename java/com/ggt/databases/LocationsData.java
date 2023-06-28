package com.ggt.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBCDriver;

public class LocationsData {

	public void updateAllCheckinNum() {
		/* JDBC連線 */
		JDBCDriver getConn = new JDBCDriver();
		Connection conn = getConn.getConn();

		/* 設置SQL語法 */
		String sql = "UPDATE location SET checkin_num = (SELECT COUNT(*) FROM posts WHERE posts.location_id = location.location_id)";

		/* 資料更新 */
		PreparedStatement ppst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
	}

	public String getLocationName(String locationId) {
	    JDBCDriver getConn = new JDBCDriver();
	    Connection conn = getConn.getConn();

	    String sql = "SELECT location_name FROM location WHERE location_id = ?";
	    
	    PreparedStatement ppst = null;
	    ResultSet rst = null;
	    String locationName = "";

	    try {
	        ppst = conn.prepareStatement(sql);
	        ppst.setString(1, locationId);
	        rst = ppst.executeQuery();

	        if (rst.next()) {
	            locationName = rst.getString("location_name");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rst != null) {
	                rst.close();
	            }
	            if (ppst != null) {
	                ppst.close();
	            }
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return locationName;
	}

	public void ckeckLocationRepeat(String locationName) throws SQLException {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT location_name FROM location WHERE location_name = ?";
		PreparedStatement ppst = conn.prepareStatement(sql);
		ppst.setString(1, locationName);
		ResultSet rst = ppst.executeQuery();
		if (rst.next()) {
		} else {
			addLocation(locationName);
		}

		conn.close();
		ppst.close();
		rst.close();
	}

	public void addLocation(String locationName) throws SQLException {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "INSERT INTO location (location_name) VALUES (?)";
		PreparedStatement ppst = conn.prepareStatement(sql);
		ppst.setString(1, locationName);
		ppst.executeUpdate();
		conn.close();
		ppst.close();
	}

	public String getLocationId(String locationName) {
		try {
			ckeckLocationRepeat(locationName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT location_id FROM location WHERE location_name = ?";
		PreparedStatement ppst;
		ResultSet rst;
		String locationId = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, locationName);
			rst = ppst.executeQuery();
			if (rst.next()) {
				locationId = rst.getString("location_id");
			} else {
				locationId = "0";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();

			}
		}
		return locationId;
	}
}
