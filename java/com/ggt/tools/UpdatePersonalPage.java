package com.ggt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.JDBCDriver;

public class UpdatePersonalPage {
	public void main(String[] args) throws IOException, SQLException {
		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "SELECT user_session FROM users";

		PreparedStatement ppst = null;
		ResultSet rst = null;
		ArrayList<String> userSessionArray = new ArrayList<String>();

		ppst = conn.prepareStatement(sql);
		rst = ppst.executeQuery();
		while (rst.next()) {
			userSessionArray.add(rst.getString("user_session"));
		}

		FileReader filereader = null;
		BufferedReader bufferedReader = null;
		String savePath = null;
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;

		for (int i = 0; i < userSessionArray.size(); i++) {
			try {
				filereader = new FileReader("/SocialMedia/src/main/webapp/defaultfile/defaultfile/personal-page.html");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bufferedReader = new BufferedReader(filereader);
			savePath = "/SocialMedia/src/main/webapp/users/" + userSessionArray.get(i);
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdirs();
				File file = new File(savePath + File.separator + "peronal-page.html");
				if (!file.exists()) {
					file.createNewFile();
				}
				fileWriter = new FileWriter(file);
				bufferedWriter = new BufferedWriter(fileWriter);
				String bufferedReaderLine;
				while ((bufferedReaderLine = bufferedReader.readLine()) != null) {
					bufferedWriter.write(bufferedReaderLine);
					bufferedWriter.newLine();
				}
				bufferedWriter.flush();
			}

		}
	}
}
