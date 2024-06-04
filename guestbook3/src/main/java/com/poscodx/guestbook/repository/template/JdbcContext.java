package com.poscodx.guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

//getConnection + 쿼리 세팅
public class JdbcContext {
	private DataSource dataSource;
	
	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
//	public <T> T executeQueryForObject(String sql) {
//		return null;
//	}
//	
//	public <T> List<T> executeQueryForObject(String sql, Object[] parameter) {
//		return null;
//	}
	
	public int executeUpdate(String sql) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {			
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		});
	}
	
	public int executeUpdate(String sql, Object[] parameters) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {			
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				for(int i=0; i<parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]); 	//index가 1부터 시작함
				}
				
				return pstmt;
			}
		});
	}
	
	public int executeUpdateWithStatementStrategy(StatementStrategy statementStrategy) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			pstmt = statementStrategy.makeStatement(conn);
			result = pstmt.executeUpdate();
		} catch(SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

}
