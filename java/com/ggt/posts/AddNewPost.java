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
import com.ggt.databases.PostsData;
import com.ggt.databases.TagsData;
import com.ggt.dataprocessing.ImageDataProcessing;

import db.JDBCDriver;

@WebServlet("/AddNewPost")
@MultipartConfig()
public class AddNewPost extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		request.setCharacterEncoding("UTF-8");
		String postText = request.getParameter("postText");
		
		String posterId = request.getParameter("posterId");
		PostsData postsData = new PostsData();
		String postId = postsData.getPostId(posterId);
		
		String tagId = request.getParameter("tagId");
		
		String locationName = request.getParameter("locationName");
		LocationsData locationsData = new LocationsData();
		String locationId = locationsData.getLocationId(locationName);
		
		Part postImageData = request.getPart("postImageData");
		TagsData tagsData = new TagsData();
		String tagName = tagsData.getTagName(tagId);
		String dirPath = request.getServletContext().getRealPath("/" + tagName);
		File fileSaveDir = new File(dirPath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		ImageDataProcessing imageDataProcessing = new ImageDataProcessing();
		String postImageURL = imageDataProcessing.getPostImgURL(dirPath, tagName, postImageData, postId);
	//	String compressedPostImageURL = imageDataProcessing.getCompressedPostImgURL(dirPath, tagName, postImageData, postId);
		
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "INSERT INTO posts (post_text, post_img, tags_id, poster_id, location_id) VALUES (?, ?, ?, ?, ?)";
		
		PreparedStatement ppst = null;
		JSONObject jsonObj = new JSONObject();
		boolean isPostUpdated = true;
		
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, postText);
			ppst.setString(2, postImageURL);
	//		ppst.setString(2, compressedPostImageURL);
			ppst.setString(3, tagId);
			ppst.setString(4, posterId);
			ppst.setString(5, locationId);
			ppst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			isPostUpdated = false;
		} finally {
			try {
				conn.close();
				ppst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			jsonObj.put("isPostUpdated", isPostUpdated);
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
