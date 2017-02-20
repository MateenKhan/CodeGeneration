package com.code.genreation.servlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

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
	}

	

}