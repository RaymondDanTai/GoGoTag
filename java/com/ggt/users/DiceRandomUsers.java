package com.ggt.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.databases.UsersData;

import db.JDBCDriver;

@WebServlet("/DiceRandomUsers")
@MultipartConfig()
public class DiceRandomUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");

		/* 取得資料庫連線 */
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT user_id FROM users WHERE user_id <> ?";

		PreparedStatement ppst = null;
		ResultSet rst = null;
		ArrayList<String> userIdArray = new ArrayList<String>();
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		String usersId = "";
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			rst = ppst.executeQuery();
			while (rst.next()) {
				usersId = rst.getString("user_id");
				userIdArray.add(usersId);
			}
			Random random = new Random();
			int[] randomNum = new int[6];
			for (int i = 0; i < randomNum.length; i++) {
				int randomNumber = random.nextInt(userIdArray.size());
				randomNum[i] = Integer.parseInt(userIdArray.get(randomNumber));
				userIdArray.remove(randomNumber);
			}
			
			
			for(int i = 0; i < randomNum.length; i++) {
				jsonObj = new JSONObject();
				UsersData usersData = new UsersData();
				String userAvatarURL = usersData.getUserAvatarSource(String.valueOf(randomNum[i]));
				String userSession = usersData.getUserSession(String.valueOf(randomNum[i]));
				jsonObj.put("userSession", userSession);
				jsonObj.put("userAvatarURL", userAvatarURL);
				jsonArray.put(jsonObj);
				System.out.println(randomNum[i]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				rst.close();
				ppst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.close();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
