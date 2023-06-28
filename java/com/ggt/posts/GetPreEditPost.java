package com.ggt.posts;

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

import db.JDBCDriver;

import com.ggt.databases.*;

@WebServlet("/GetPreEditPost")
@MultipartConfig()
public class GetPreEditPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		String postId = request.getParameter("postId");

		/* 取得資料庫連線 */
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		/* 設置資料庫語法 */
		String sql = "SELECT post_text, post_img, tags_id, poster_id, location_id FROM posts WHERE post_id = ?";

		/* 設置來自前端的值 */
		PreparedStatement ppst = null;
		ResultSet rst = null;
		boolean isGetPreEditPostSuccess = true;
		JSONObject jsonObj = null;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postId);
			rst = ppst.executeQuery();

			if (rst.next()) {
				String postText = rst.getString("post_Text");
				String postImg = rst.getString("post_img");
				String tagsId = rst.getString("tags_id");
				String posterId = rst.getString("poster_id");
				String locationId = rst.getString("location_id");
				jsonObj = new JSONObject();

				jsonObj.put("postId", postId);
				jsonObj.put("postImg", postImg);
				jsonObj.put("postText", postText);
				jsonObj.put("tagsId", tagsId);

				PostsData postsData = new PostsData();
				String posterName = postsData.getPosterName(posterId);
				String posterAvatar = postsData.getPosterAvatarURL(posterId);
				jsonObj.put("posterName", posterName);
				jsonObj.put("posterAvatar", posterAvatar);

				LocationsData locationData = new LocationsData();
				String locationName = locationData.getLocationName(locationId);
				jsonObj.put("locationName", locationName);
			}
		} catch (SQLException e) {
			isGetPreEditPostSuccess = false;
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

		try {
			jsonObj.put("isGetPreEditPostSuccess", isGetPreEditPostSuccess);
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
