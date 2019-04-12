package Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class CollectFourDB {
	String arr = null; // We will use this string to execute queries

	private final String userName = "root"; // enter user name for DB connection
	private final String password = "cancan563"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "collectfour"; // the name of the database file

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		conn = DriverManager.getConnection("jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName, connectionProps);
		return conn;
	}
	
	public String Login(String username,String password) {
		// Connect to MySQL
		Connection conn = null;
		try {
			conn = this.getConnection();
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
		}
		try {
			PreparedStatement st = conn.prepareStatement("SELECT * " + "FROM collectfourtable "+ "WHERE username LIKE "+ "'%"+username+"%'"+" AND password LIKE "+"'%"+password+"%'");
			// We write a query to get the data from the table.
			ResultSet rs = st.executeQuery();

			arr = null;
			while (rs.next()) {
				String x = rs.getString("username");
				arr = x.replace("\n", ",");
			}
			if(!arr.equals(username)){
				System.out.println("wrong username and/or password entered. Please try again.");
				return "fail";
			}
			return "success";
		}
		catch (SQLException e) {
			System.out.println("ERROR: Could not connect the table");
			e.printStackTrace();
			return ("failed to get a username from table");
		}

	}
}
