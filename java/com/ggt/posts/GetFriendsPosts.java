package com.ggt.posts;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.*;
import db.JDBCDriver;
import com.ggt.databases.*;
import com.ggt.dataprocessing.*;

@WebServlet("/GetFriendsPosts")
@MultipartConfig()
public class GetFriendsPosts extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		/* 取得資料庫連線 */
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		/* 取得前端資料 */
		String userId = request.getParameter("userId");
		String postTimeBaseline = request.getParameter("postTimeBaseline");
		String sqlSelect = request.getParameter("sqlSelect");
		Timestamp timestamp = Timestamp.valueOf(postTimeBaseline);
		
		String sql = "";
		if(sqlSelect.equals("0")) {
			sql = "SELECT post_id, post_text, post_img, updated_at, like_count, tags_id, poster_id, location_id FROM posts WHERE  (poster_id IN (SELECT user1_id FROM friendships WHERE user2_id = ?) OR poster_id IN (SELECT user2_id FROM friendships WHERE user1_id = ?)) AND updated_at <= ? ORDER BY updated_at DESC LIMIT 5";
		} else {
			sql = "SELECT post_id, post_text, post_img, updated_at, like_count, tags_id, poster_id, location_id FROM posts WHERE  (poster_id IN (SELECT user1_id FROM friendships WHERE user2_id = ?) OR poster_id IN (SELECT user2_id FROM friendships WHERE user1_id = ?)) AND updated_at < ? ORDER BY updated_at DESC LIMIT 5";
		}

		/* 代入資料 */
		PreparedStatement ppst = null;

		String postId = null;

		String posterId = null;
		String posterAvatarURL = null;
		String posterName = null;

		String updatedAt = null;
		String updatedDate = null;

		String tagId = null;
		String tagName = null;

		String locationId = null;
		String locationName = null;

		String postText = null;
		String postImgURL = null;

		int likeCount = 0;
		int commentCount = 0;

		String checkPoster = "1";

		ResultSet rst = null;

		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, userId);
			ppst.setString(2, userId);
			ppst.setTimestamp(3, timestamp);
			rst = ppst.executeQuery();
			JSONObject jsonObj;
			JSONArray jsonAry = new JSONArray();

			while (rst.next()) {
				jsonObj = new JSONObject();

				postId = rst.getString("post_id");
				jsonObj.put("postId", postId);

				/* 取得貼文者資料 */
				posterId = rst.getString("poster_id");
				jsonObj.put("posterId", posterId);
				UsersData usersData = new UsersData();
				String posterSession = usersData.getUserSession(posterId);
				jsonObj.put("posterSession", posterSession);
				PostsData postsData = new PostsData();
				posterAvatarURL = postsData.getPosterAvatarURL(posterId);
				jsonObj.put("posterAvatarURL", posterAvatarURL);
				posterName = postsData.getPosterName(posterId);

				jsonObj.put("posterName", posterName);

				/* 取得更新日期 */
				updatedAt = rst.getString("updated_at");
				jsonObj.put("updatedAt", updatedAt);
				TimesDataProcessing timesData = new TimesDataProcessing();
				updatedDate = timesData.getUpdatedDate(updatedAt);
				jsonObj.put("updatedDate", updatedDate);

				/* 取得標籤名稱 */
				tagId = rst.getString("tags_id");
				TagsData tagsData = new TagsData();
				tagName = tagsData.getTagName(tagId);
				jsonObj.put("tagName", tagName);

				/* 取得打卡地點 */
				locationId = rst.getString("location_id");
				LocationsData locationData = new LocationsData();
				locationName = locationData.getLocationName(locationId);
				jsonObj.put("locationName", locationName);

				/* 取得貼文內容 */
				postText = rst.getString("post_text");
				jsonObj.put("postText", postText);

				postImgURL = rst.getString("post_img");
				jsonObj.put("postImgURL", postImgURL);

				/* 取得點讚次數 */
				LikesData likesDataMaintenance = new LikesData();
				likeCount = likesDataMaintenance.getLikeCount(postId);
				jsonObj.put("likeCount", likeCount);

				/* 比對該貼文使用者是否有點讚 */
				LikesData likesData = new LikesData();
				boolean isUserLiked = likesData.checkUserLikedPost(postId, userId);
				jsonObj.put("isUserLiked", isUserLiked);

				/* 取得留言次數 */
				CommentsData commentsData = new CommentsData();
				commentCount = commentsData.getCommentCount(postId);
				jsonObj.put("commentCount", commentCount);

				/* 確認使用者是否與poster一樣 */
				if (posterId.equals(userId)) {
					checkPoster = "1";
				} else {
					checkPoster = "0";
				}
				jsonObj.put("checkPoster", checkPoster);

				jsonAry.put(jsonObj);
			}
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print(jsonAry.toString());
			out.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
}
