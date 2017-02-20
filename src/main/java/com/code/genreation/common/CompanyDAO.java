package com.code.genreation.common;
import java.sql.Connection;
import java.util.List;
public interface CompanyDAO {

	Company get(Connection conn, Company company);

	List<Company> getAll(Connection conn);

	Company delete(Connection conn, Company company);

	Company create(Connection conn, Company company);

	Company update(Connection conn, Company company);


}