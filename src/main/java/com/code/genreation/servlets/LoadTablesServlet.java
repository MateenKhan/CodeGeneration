package com.code.genreation.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.code.generation.database.mysql.DatabaseUtilities;

/**
 * Servlet implementation class CodeGeneratorServlet
 */
@WebServlet("/load")
public class LoadTablesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		ConfigurationLoaderServlet.loadTablesList(getServletConfig());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(getTablesList());
        out.close();
	}
	
	public static String getTablesList(){
		Connection conn = null;
		try {
			conn = DatabaseUtilities.getReadConnection();
			ResultSet rs = conn.createStatement().executeQuery("show tables;");
			JSONArray tablesList = new JSONArray();
			while(rs.next()){
				tablesList.put(rs.getString(1));
			}
			System.out.println(tablesList);
			return tablesList.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabaseUtilities.closeConnection(conn);
		}
		return null;
	}
}
