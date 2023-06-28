package com.ggt.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.JDBCDriver;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/AccountRegister")
@MultipartConfig()
public class AccountRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");

		/* 取得JDBC連線 */

		JDBCDriver jdbc = new JDBCDriver();
		Connection conn = jdbc.getConn();

		String sql = "INSERT INTO users(first_name, last_name, email, password, gender, phone_number, birth_date, user_session) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String gender = request.getParameter("gender");
		String phoneNumber = request.getParameter("phoneNumber");
		String birthDate = request.getParameter("birthDate");

		/* 設置sessionId */
		HttpSession session = request.getSession();
		String userSession = session.getId();
		boolean isRegisterSuccessful = true;

		PreparedStatement ppst = null;
		try {
			ppst = conn.prepareStatement(sql);
			ppst.setString(1, firstName);
			ppst.setString(2, lastName);
			ppst.setString(3, email);
			ppst.setString(4, password);
			ppst.setString(5, gender);
			ppst.setString(6, phoneNumber);
			ppst.setString(7, birthDate);
			ppst.setString(8, userSession);
			ppst.executeUpdate();

			FileReader filereader = null;
			BufferedReader bufferedReader = null;
			String savePath = null;
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
			try {
				filereader = new FileReader(
						request.getServletContext().getRealPath("/defaultfile") + "/personal-page.html");
				bufferedReader = new BufferedReader(filereader);
				savePath = request.getServletContext().getRealPath("/users/" + userSession);
				System.out.println(savePath);
				File fileSaveDir = new File(savePath);
				if (!fileSaveDir.exists()) {
					fileSaveDir.mkdirs();
				}

				File file = new File(savePath + File.separator + "personal-page.html");
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
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			conn.close();
			ppst.close();
		} catch (SQLException e) {
			isRegisterSuccessful = false;
			e.printStackTrace();
		}

		JSONObject jsonObj = new JSONObject();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			jsonObj.put("isRegisterSuccessful", isRegisterSuccessful);
			out.println(jsonObj.toString());
			out.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
