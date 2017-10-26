package com.code.generation.database.mysql;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import com.code.genreation.common.PropertyManager;

public class LocalMysqlManager {

	private static final Logger LOGGER = Logger.getLogger(LocalMysqlManager.class);
	
	private static final LocalMysqlManager LOCAL_MYSQL_MANAGER = new LocalMysqlManager();
	
	private LocalMysqlManager(){
		
	}
	
	private DataSource dataSource = createDataSource();
	
	private DataSource createDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		String databaseName = PropertyManager.getProperty("local.dbname");
		dataSource.setUrl(PropertyManager.getProperty("local.url")+databaseName);
		dataSource.setUsername(PropertyManager.getProperty("local.username"));
		dataSource.setPassword(PropertyManager.getProperty("local.password"));
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setMaxOpenPreparedStatements(100);
		dataSource.setInitialSize(5);
		dataSource.setMaxTotal(20);
		dataSource.setMaxIdle(15);
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if(conn!=null){
				System.out.println("connection creation success");
			}
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			DatabaseUtilities.closeConnection(conn);
		}
		return dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public static LocalMysqlManager getInstance(){
		return LOCAL_MYSQL_MANAGER;
	}
}
