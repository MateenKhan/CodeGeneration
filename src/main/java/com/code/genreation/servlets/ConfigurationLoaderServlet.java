package com.code.genreation.servlets;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.json.JSONArray;

import com.code.generation.database.mysql.DatabaseUtilities;
import com.code.generation.database.mysql.MySQLManager;
import com.code.genreation.common.Log4jLoder;


/**
 * 
 * @author Mateen
 * @version 1.0
 *
 */
public class ConfigurationLoaderServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	/**
	 * this method is called while the server startup or on deploying of the
	 * application to the server
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		Log4jLoder.getLog4jLoder().initilializeLogging();
		try {
			Class.forName(MySQLManager.class.getName());
			getTablesList(config);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void getTablesList(ServletConfig config){
		try {
			Connection conn = DatabaseUtilities.getReadConnection();
			ResultSet rs = conn.createStatement().executeQuery("show tables;");
			JSONArray tablesList = new JSONArray();
			while(rs.next()){
				tablesList.put(rs.getString(1));
			}
			System.out.println(tablesList);
			config.getServletContext().setAttribute("tables", tablesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}