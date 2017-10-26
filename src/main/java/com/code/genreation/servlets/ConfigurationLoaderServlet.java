package com.code.genreation.servlets;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.json.JSONArray;

import com.code.generation.database.mysql.DatabaseUtilities;
import com.code.generation.database.mysql.LocalMysqlManager;
import com.code.generation.database.mysql.MySQLManager;
import com.code.genreation.common.Log4jLoder;
import com.code.genreation.common.PropertyManager;


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
			if(PropertyManager.getProperty("mysql.connect.to.local").equals("true")){
				Class.forName(LocalMysqlManager.class.getName());
			}else{
				Class.forName(MySQLManager.class.getName());
			}
			loadTablesList(config);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadTablesList(ServletConfig config){
		Connection conn = null;
		try {
			conn = DatabaseUtilities.getReadConnection();
			ResultSet rs = conn.createStatement().executeQuery("show tables;");
			JSONArray tablesList = new JSONArray();
			while(rs.next()){
				tablesList.put(rs.getString(1));
			}
			System.out.println(tablesList);
			config.getServletContext().setAttribute("tables", tablesList);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DatabaseUtilities.closeConnection(conn);
		}
	}

}