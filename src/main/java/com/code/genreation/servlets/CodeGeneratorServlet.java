package com.code.genreation.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.code.generation.database.mysql.DatabaseUtilities;
import com.code.genreation.Controller;
import com.code.genreation.ControllerImpl;
import com.code.genreation.Dao;
import com.code.genreation.DaoImpl;
import com.code.genreation.Pojo;
import com.code.genreation.SqlQuerys;
import com.code.genreation.UiComponent;
import com.code.genreation.UiForm;
import com.code.genreation.UiModel;
import com.code.genreation.UiService;
import com.code.genreation.common.Utilities;

/**
 * Servlet implementation class CodeGeneratorServlet
 */
@WebServlet("/code")
public class CodeGeneratorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(CodeGeneratorServlet.class);

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
		List<File> files = null;
		ZipOutputStream zos = null;
		BufferedInputStream fif = null;
		try {
			JSONObject requestObj = new JSONObject();
			String tableName = request.getParameter("table");
			String dbName = request.getParameter("database");
			String pk = request.getParameter("pk");
			if (!StringUtils.isEmpty(pk)) {
				requestObj.put("pk", pk);
			}
			JSONArray fields = getTableDetails(pk, tableName, dbName, requestObj);
			if (fields != null && fields.length() != 0) {
				requestObj.put("fields", fields);
				requestObj.put("name", tableName);
			} else {
				throw new Exception("unable to fetch fields for the table:" + tableName);
			}
			System.out.println(requestObj);
			boolean createPojo = Boolean.parseBoolean(request.getParameter("pojo"));
			boolean createDao = Boolean.parseBoolean(request.getParameter("dao"));
			boolean createDaoImpl = Boolean.parseBoolean(request.getParameter("daoImpl"));
			boolean createQuerys = Boolean.parseBoolean(request.getParameter("querys"));
			boolean createController = Boolean.parseBoolean(request.getParameter("controller"));
			boolean createcontrollerImpl = Boolean.parseBoolean(request.getParameter("controllerImpl"));
			files = new ArrayList<File>();

			File pojo = null, dao = null, daoImpl = null, sqlQuerys = null, controller = null, controllerImpl = null;
			if (createPojo)
				pojo = Pojo.createPojo(requestObj);
			if (createDao)
				dao = Dao.createDao(requestObj);
			if (createDaoImpl)
				daoImpl = DaoImpl.createDaoImpl(requestObj);
			if (createQuerys)
				sqlQuerys = SqlQuerys.createSqlQuerys(requestObj);
			if (createController)
				controller = Controller.createController(requestObj);
			if (createcontrollerImpl)
				controllerImpl = ControllerImpl.createControllerImpl(requestObj);
			File uiComponent = UiComponent.createUiComponent(requestObj);
			File uiForm = UiForm.createUiForm(requestObj);
			File uiModel = UiModel.createUiModel(requestObj);
			File uiService = UiService.createUiService(requestObj);
			// Set the content type based to zip
			response.setContentType("Content-type: application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=code.zip");

			// List of files to be downloaded
			files.add(pojo);
			files.add(dao);
			files.add(daoImpl);
			files.add(sqlQuerys);
			files.add(controller);
			files.add(controllerImpl);
			files.add(uiComponent);
			files.add(uiForm);
			files.add(uiModel);
			files.add(uiService);
//			FileOutputStream out = new FileOutputStream("C:/Users/MateenAhmed/Downloads/temp/1.zip");
			ServletOutputStream out = response.getOutputStream();
			zos = new ZipOutputStream(new BufferedOutputStream(out));
			for (File file : files) {
				System.out.println("Adding file " + file.getName());
				zos.putNextEntry(new ZipEntry(file.getName()));
				// Get the file
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					// If the file does not exists, write an error entry instead
					// of file contents
					zos.write(("ERROR: Could not find file " + file.getName()).getBytes());
					zos.closeEntry();
					System.out.println("Could not find file " + file.getAbsolutePath());
					continue;
				}
				fif = new BufferedInputStream(fis);
				// Write the contents of the file
				int data = 0;
				while ((data = fif.read()) != -1) {
					zos.write(data);
				}
				fif.close();
				zos.closeEntry();
				System.out.println("Finished adding file " + file.getName());
			}
			zos.close();
		} catch (WebApplicationException e) {
			LOGGER.error("error creating code", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("error creating code", e);
		} finally {
			Utilities.deleteFilesAsync(files);
			Utilities.closeZipOutputStream(zos);
			Utilities.closeBufferedInputStream(fif);
		}
	}

	private JSONArray getTableDetails(String pk, String tableName, String dbName, JSONObject requestObj) {
		if (StringUtils.isEmpty(tableName)) {
			return null;
		}
		if (requestObj == null) {
			return null;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet pkResultSet = null;
		Connection conn = null;
		try {
			conn = DatabaseUtilities.getReadConnection();
			ps = conn.prepareStatement("select * from `" + dbName + "`.`" + tableName + "` limit 1;");
			rs = ps.executeQuery();
			if (StringUtils.isEmpty(pk)) {
				DatabaseMetaData dm = conn.getMetaData();
				pkResultSet = dm.getExportedKeys("", "qount", tableName);
				if (pkResultSet.next()) {
					pk = pkResultSet.getString("PKCOLUMN_NAME");
				}
				if (StringUtils.isEmpty(pk)) {
					throw new Exception("single column pk is mandatory in the selected table");
				}
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			JSONArray result = new JSONArray();
			boolean isPkFound = false;
			for (int i = 1; i <= columnCount; i++) {
				JSONObject obj = new JSONObject();
				String columnName = rsmd.getColumnName(i);
				obj.put("name", columnName);
				int columnType = rsmd.getColumnType(i);
				obj.put("type", Utilities.getJavaTypeFromSql(columnType));
				if (!isPkFound && columnName.equals(pk)) {
					requestObj.put("pk", columnName);
					requestObj.put("pkType", Utilities.getJavaTypeFromSql(columnType));
					isPkFound = true;
				}
				result.put(obj);
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			DatabaseUtilities.closeConnection(conn);
			DatabaseUtilities.closeStatement(ps);
			DatabaseUtilities.closeResultSet(pkResultSet);
			DatabaseUtilities.closeResultSet(rs);
		}
		return null;
	}
}
