import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.JDBCDriver;

public class FriendshipsDataMaintenance {
	protected int getFriendAvatar(String pageOwnerId) {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();
		String sql = "SELECT  FROM friendships WHERE (user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)";
		PreparedStatement ppst = null;
		ResultSet rst = null;
		int friendshipStatusCode = -1;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, pageOwnerId);
			rst = ppst.executeQuery();
			if (rst.next()) {
				friendshipStatusCode = rst.getInt("status");
			}
		} catch (SQLException e) {
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
		return friendshipStatusCode;
	}
}
