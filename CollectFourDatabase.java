package Game;

import java.sql.*;
import java.util.*;;

public class CollectFourDatabase {
	Scanner input = new Scanner(System.in);

	private final String userName = "root"; // enter user name for DB connection
	private final String password = "root"; // enter password for DB connection
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "collectfour"; // the name of the database file
	private String username;
	private String userPassword;

	public Connection getConnection() {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);
		Statement s = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/collectfour?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Turkey");
			s = conn.createStatement();
			System.out.println("Please enter your user name: ");
			username = input.nextLine();
			System.out.println("Please enter your password: ");
			userPassword = input.nextLine();

			String sql = "INSERT INTO registration " + "(username,password) " + "VALUES ('" + username + "','"
					+ userPassword + "')";
			s.execute(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return conn;
	}

}
