package com.ggt.friendships;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.databases.FriendshipsData;
import com.ggt.databases.UsersData;

@WebServlet("/GetCurrentUserFriends")
@MultipartConfig()
public class GetCurrentUserFriends extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		String currentUserId = request.getParameter("currentUserId");
		
		FriendshipsData friendshipsData = new FriendshipsData();
		ArrayList<String> friendsId = friendshipsData.getUserFriendsId(currentUserId);
		
		UsersData usersData = null;
		String friendId = "";
		String userName = "";
		String userAvatarURL = "";
		String friendSession = "";
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		for(int i = 0; i < friendsId.size(); i++) {
			usersData = new UsersData();
			jsonObj = new JSONObject();
			friendId = friendsId.get(i);
			userName = usersData.getUserName(friendsId.get(i));
			userAvatarURL = usersData.getUserAvatarSource(friendsId.get(i));
			friendSession = usersData.getUserSession(friendsId.get(i));
			try {
				jsonObj.put("friendId", friendId);
				jsonObj.put("userName", userName);
				jsonObj.put("userAvatarURL", userAvatarURL);
				jsonObj.put("friendSession", friendSession);
				jsonArray.put(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jsonArray.toString());
		out.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}
}
