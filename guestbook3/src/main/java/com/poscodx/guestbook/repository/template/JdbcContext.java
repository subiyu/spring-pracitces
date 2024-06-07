package com.poscodx.guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;

import com.poscodx.guestbook.vo.GuestBookVo;

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

	public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
		return executeQueryWithStatementStrategy(new StatementStrategy() {			
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		}, rowMapper);
	}
	
	public int execute(String sql) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {			
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		});
	}
	
	public int execute(String sql, Object[] parameters) {
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

	public <E> List<E> executeQueryWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
		List<E> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
		
			pstmt = statementStrategy.makeStatement(conn);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				E e = rowMapper.mapRow(rs, rs.getRow());
				result.add(e);
			}
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
