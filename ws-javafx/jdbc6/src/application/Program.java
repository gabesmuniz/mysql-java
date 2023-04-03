package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping;

import db.DB;
import db.DbException;

//PROTEGENDO PARA EXECUTAR TUDO OU NADA (ATOMICIDADE DO BANCO)
public class Program {

	public static void main(String[] args) {
		
		Connection conn = null;
		Statement st = null;
		
		try {
			conn = DB.getConnection();
			
			//Início proteção
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			
			/*
			// erro forçado.
			int x = 1;
			if (x < 2) {
				throw new SQLException("Fake Error");
			}
			*/
			
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");

			conn.commit();
			//Fim proteção.
			
			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);

		} catch (SQLException except) {
			//Caso tenha estourado alguma exceção:
			try {
				//Rollback
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + except.getMessage());
			} catch (SQLException except1) {
				//Caso ocorra erro na tentativa de rollback:
				throw new DbException("Error trying to rollback! Caused by: " + except1.getMessage());
			}
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
