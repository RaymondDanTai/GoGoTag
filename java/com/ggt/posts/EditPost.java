package com.ggt.posts;

import java.io.File;
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

import com.ggt.databases.LocationsData;
import com.ggt.databases.TagsData;
import com.ggt.dataprocessing.ImageDataProcessing;

import db.JDBCDriver;

@WebServlet("/EditPost")
@MultipartConfig()
public class EditPost extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");

		String postId = request.getParameter("postId");
		String tagId = request.getParameter("tagId");
		String postText = request.getParameter("postText");
		String locationName = request.getParameter("locationName");
		LocationsData locationData = new LocationsData();
		String locationId = locationData.getLocationId(locationName);

		Part imgData = request.getPart("postImageData");
		TagsData tagsData = new TagsData();
		String tagName = tagsData.getTagName(tagId);
		String dirPath = request.getServletContext().getRealPath("/" + tagName);
		File fileSaveDir = new File(dirPath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		ImageDataProcessing imageDataProcessing = new ImageDataProcessing();
		String postImgURL = imageDataProcessing.getPostImgURL(dirPath, tagName, imgData, postId);

		/* 取得資料庫連線 */
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		/* 設置資料庫語法 */
		String sql = "UPDATE posts SET tags_id = ?, location_id = ?, post_text = ?, post_img = ? WHERE post_id = ?";

		/* 設置來自前端的值 */
		PreparedStatement ppst = null;
		boolean isEditPostSuccessful = true;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, tagId);
			ppst.setString(2, locationId);
			ppst.setString(3, postText);
			ppst.setString(4, postImgURL);
			ppst.setString(5, postId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			isEditPostSuccessful = false;
			e.printStackTrace();
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
			jsonObj.put("isEditPostSuccessful", isEditPostSuccessful);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
