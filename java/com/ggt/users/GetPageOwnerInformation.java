package com.ggt.users;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggt.databases.FriendshipsData;
import com.ggt.databases.UsersData;

@WebServlet("/GetPageOwnerInformation")
@MultipartConfig()
public class GetPageOwnerInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		String thePageOwnerSession = request.getParameter("thePageOwnerSession");
		String theCurrentUserId = request.getParameter("userId");
		
		UsersData usersData = new UsersData();
		String pageOwnerId = usersData.getUserId(thePageOwnerSession);
		
		String pageOwnerName = usersData.getUserName(pageOwnerId);
		String pageOwnerAvatarURL = usersData.getUserAvatarSource(pageOwnerId);
		String currentUserSession = usersData.getUserSession(theCurrentUserId);
		
		FriendshipsData friendshipsData = new FriendshipsData();
		int friendshipStatusCode = friendshipsData.checkFriendshipStatus(theCurrentUserId, pageOwnerId);
		boolean isPageowner = false;
		
		if (thePageOwnerSession.equals(currentUserSession)) {
			isPageowner = true;
		}
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("pageOwnerName", pageOwnerName);
			jsonObj.put("pageOwnerAvatarURL", pageOwnerAvatarURL);
			jsonObj.put("isPageowner", isPageowner);
			jsonObj.put("friendshipStatusCode", friendshipStatusCode);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}
}
