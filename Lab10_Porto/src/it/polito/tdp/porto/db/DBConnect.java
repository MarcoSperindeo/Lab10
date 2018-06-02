package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnect {

	private static String jdbcURL = "jdbc:mysql://localhost/porto2015?user=root";
	private static DataSource ds;

	public static Connection getConnection() {

		if (ds == null) {
			// crea il DataSource
			try {
				ds = DataSources.pooledDataSource(
						DataSources.unpooledDataSource(jdbcURL));
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		try {
			Connection c = ds.getConnection();
			return c;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}