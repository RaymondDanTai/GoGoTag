package com.ggt.comments;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.databases.CommentsData;

@WebServlet("/GetCommentCount")
@MultipartConfig()
public class GetCommentCount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		request.setCharacterEncoding("UTF-8");

		String commentedPostId = request.getParameter("commentedPostId");

		CommentsData commentsData = new CommentsData();
		int commentCount = commentsData.getCommentCount(commentedPostId);

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("commentCount", commentCount);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
