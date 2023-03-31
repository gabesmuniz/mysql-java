package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {
		
		//DELETAR DADOS
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = DB.getConnection();
			
			st = conn.prepareStatement(
					"DELETE FROM department "
					+ "WHERE "
					+ "Id = ?");
			
			st.setInt(1, 2);//5 removido, 2 para lançar exceção forçada.
			
			int rowsAffected = st.executeUpdate();
		
			System.out.println("Done! Rows Affected: " + rowsAffected);
		
		} catch (SQLException except) {
			//lançando exceção personalizada.
			throw new DbIntegrityException(except.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
