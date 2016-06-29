package com.searchengine.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Dao {

	/**
	 * Static initial block When instance this class loading the driver
	 */
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The getConnetion method of SavaToDatabase This method get a Connection on
	 * database
	 * 
	 * @return conn Return a connection
	 */
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost/searchengine?"
							+ "user=root&password=root");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * The getStatement method of SaveToDatabase This method get a stat on conn
	 * 
	 * @param conn
	 * @return stat Return a statement
	 */
	public Statement getStatement(Connection conn) {
		Statement stat = null;
		try {
			stat = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stat;
	}
	
	/**
	 *获取所有连接
	 * 
	 * @param stat
	 * @return
	 */
	public ResultSet getResultSetAll(Statement stat) {
		ResultSet rs = null;
		String sql = "SELECT * FROM link";
		try {
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet getResultSetMD5(Statement stat,String link_md5) {
		ResultSet rs = null;
		String sql = "SELECT * FROM link where link_md5 = \'"+link_md5+"\'";
		try {
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * The saveData method of SaveToDatabase This method execute a sql stat
	 * 
	 * @param stat
	 * @param str
	 */
	public void SavaData(Statement stat, String str) {
		try {
			stat.executeUpdate(str);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The closeAll method of SaveToDatabase This method colse the connection
	 * and statement than can not closed
	 * 
	 * @param conn
	 * @param stat
	 */
	public void closeAll(Connection conn, Statement stat,ResultSet rs) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
