package com.ggt.users;

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
import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.dataprocessing.ImageDataProcessing;

import db.JDBCDriver;

@WebServlet("/UpdateUserAvatarURL")
@MultipartConfig()
public class UpdateUserAvatarURL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String userId = request.getParameter("userId");
		String userSession = request.getParameter("userSession");
		Part imgData = request.getPart("userAvatarData");
		ImageDataProcessing imageDataProcessing = new ImageDataProcessing();
		/* 取得檔案儲存資料夾路徑 */
		String dirPath = request.getServletContext().getRealPath("/users/" + userSession);
		String userAvatarURL = imageDataProcessing.getUserAvatarURL(dirPath, imgData, userSession);
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "UPDATE users SET user_avatar_source = ? WHERE user_id = ? && user_session = ?";
		PreparedStatement ppst = null;
		boolean isUserAvatarUpdated = true;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userAvatarURL);
			ppst.setString(2, userId);
			ppst.setString(3, userSession);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isUserAvatarUpdated = false;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ppst != null) {
					ppst.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("isUserAvatarUpdated", isUserAvatarUpdated);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}
}
