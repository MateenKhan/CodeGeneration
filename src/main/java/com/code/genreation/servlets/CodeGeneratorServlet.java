package com.code.genreation.servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.code.generation.database.mysql.DatabaseUtilities;

/**
 * Servlet implementation class CodeGeneratorServlet
 */
@WebServlet("/code")
public class CodeGeneratorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(CodeGeneratorServlet.class);

	private static final String sql = "SELECT `COLUMN_NAME`,`COLUMN_TYPE` FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`=? AND `TABLE_NAME`=?;";
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			processRequest(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String table = request.getParameter("table");
			String database = request.getParameter("database");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String filename = "home.jsp";
			String filepath = "e:\\";
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			FileInputStream fileInputStream = new FileInputStream(filepath + filename);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
			out.close();
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

	private JSONObject getTableDetails(String tableName,String dbName){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if(StringUtils.isEmpty(tableName)){
				return null;
			}
			Connection conn = DatabaseUtilities.getReadConnection();
			if(StringUtils.isEmpty(dbName)){
				dbName = "qount";
			}
			ps = conn.prepareStatement("select * from `"+dbName+"`.`"+tableName+"`;");
			ps.setString(1, dbName);
			ps.setString(2, tableName);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			rsmd.getColumnCount();
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return null;
	}
}
