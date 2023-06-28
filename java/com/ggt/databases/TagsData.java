package com.ggt.databases;

import java.sql.Connection;
import db.JDBCDriver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;;

public class TagsData {

	public void getUsedNum(int tagId) {
		/* JDBC連線 */
		JDBCDriver getConn = new JDBCDriver();
		Connection conn = getConn.getConn();

		/* 設置SQL語法 */
		String sql = "SELECT used_num FROM tags WHERE tags_id = ?";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		String usedNum = "";
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setInt(1, tagId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				usedNum = rst.getString("used_num");
			} else {
				System.out.println("未找到符合的標籤編號");
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
	}

	public void updateUsedNum(int tagId) {
		/* JDBC連線 */
		JDBCDriver getConn = new JDBCDriver();
		Connection conn = getConn.getConn();

		/* 設置SQL語法 */
		String sql = "UPDATE used_num SET used_num = (SELECT COUNT(*) FROM posts WHERE posts.tags_id = ?)";

		PreparedStatement ppst = null;

		/* 代入資料 */
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setInt(1, tagId);
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

	public void updateUsedNum(String tagId) {
		/* JDBC連線 */
		JDBCDriver getConn = new JDBCDriver();
		Connection conn = getConn.getConn();

		/* 設置SQL語法 */
		String sql = "UPDATE tags SET used_num = (SELECT COUNT(*) FROM posts WHERE posts.tags_id = ?)";

		PreparedStatement ppst = null;

		/* 代入資料 */
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, tagId);
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

	public String getTagName(String tagId) {
	    JDBCDriver getConn = new JDBCDriver();
	    Connection conn = getConn.getConn();

	    String sql = "SELECT tag_name FROM tags WHERE tags_id = ?";
	    
	    PreparedStatement ppst = null;
	    ResultSet rst = null;
	    String tagName = "";

	    try {
	        ppst = conn.prepareStatement(sql);
	        ppst.setString(1, tagId);
	        rst = ppst.executeQuery();

	        if (rst.next()) {
	            tagName = rst.getString("tag_name");
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

	    return tagName;
	}

}
