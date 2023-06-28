package com.ggt.posts;

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

import com.ggt.databases.CommentsData;
import com.ggt.databases.LikesData;

import db.JDBCDriver;

@WebServlet("/DeletePost")
@MultipartConfig()
public class DeletePost extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String postId = request.getParameter("postId");

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		CommentsData commentsDataMaintenance = new CommentsData();
		boolean isPostCommentsdeleted = commentsDataMaintenance.deletePostComments(postId);

		LikesData likesDataMaintenance = new LikesData();
		boolean isPostLikesdeleted = likesDataMaintenance.deletePostLikes(postId);

		boolean isPostDeleted = true;

		if (isPostCommentsdeleted && isPostLikesdeleted) {

			String sql = "DELETE FROM posts WHERE post_id = ?";

			PreparedStatement ppst = null;
			try {
				ppst = conn.prepareStatement(sql);
				ppst.setString(1, postId);
				ppst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				isPostDeleted = false;
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
			jsonObj.put("isPostDeleted", isPostDeleted);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonObj.toString());
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
