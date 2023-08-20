import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyUtils {
	private Connection connection;
	private Statement statement;
	private String schemaName;


	public Connection createConnection() throws SQLException {
		DriverManager.registerDriver(new org.h2.Driver());
		connection = DriverManager.getConnection("jdbc:h2:mem:test", "", "");
		return connection;
	}

	public void closeConnection() throws SQLException {
		connection.close();
	}

	public Statement createStatement() throws SQLException {
		statement = connection.createStatement();
		return statement;
	}

	public void closeStatement() throws SQLException {
		statement.close();
	}

	public void createSchema(String schemaName) throws SQLException {
		this.schemaName = schemaName;
		String sql = "CREATE SCHEMA " + schemaName;
		statement.executeUpdate(sql);
	}

	public void dropSchema() throws SQLException {
		String sql = "DROP SCHEMA " + this.schemaName;
		statement.executeUpdate(sql);

	}

	public void useSchema() throws SQLException {
		String sql = "USE " + this.schemaName;
		statement.executeUpdate(sql);
	}

	public void createTableRoles() throws SQLException {
		String sql = "CREATE TABLE Roles" +
				"(id INTEGER GENERATED ALWAYS AS IDENTITY," +
				"roleName VARCHAR(255) NOT NULL ," +
				"PRIMARY KEY(id))";
		statement.executeUpdate(sql);

	}

	public void createTableDirections() throws SQLException {
		String sql = "CREATE TABLE Directions" +
				"(id INTEGER GENERATED ALWAYS AS IDENTITY," +
				"directionName VARCHAR(255) NOT NULL," +
				"PRIMARY KEY(id))";
		statement.executeUpdate(sql);
	}

	public void createTableProjects() throws SQLException {
		String sql = "CREATE TABLE Projects" +
				"(id INTEGER GENERATED ALWAYS AS IDENTITY," +
				"projectName VARCHAR(255) NOT NULL," +
				"directionId INTEGER NOT NULL," +
				"PRIMARY KEY (id)," +
				"FOREIGN KEY (directionId) REFERENCES Directions(id))";
		statement.executeUpdate(sql);
	}

	public void createTableEmployee() throws SQLException {
		String sql = "CREATE TABLE Employee" +
				"(id INTEGER GENERATED ALWAYS AS IDENTITY," +
				"firstName VARCHAR(255) NOT NULL," +
				"roleId INTEGER NOT NULL," +
				"projectId INTEGER NOT NULL," +
				"PRIMARY KEY (id)," +
				"FOREIGN KEY (roleId) REFERENCES ROLES (id)," +
				"FOREIGN KEY (projectId) REFERENCES Projects (id))";
		statement.executeUpdate(sql);
	}

	public void dropTable(String tableName) throws SQLException {
		String sql = "DROP TABLE " + tableName;
		statement.executeUpdate(sql);
	}

	public void insertTableRoles(String roleName) throws SQLException {
		String sql = "INSERT INTO Roles(roleName) " +
				"VALUES ('" + roleName + "')";

		statement.executeUpdate(sql);
	}

	public void insertTableDirections(String directionName) throws SQLException {
		String sql = "INSERT INTO Directions(directionName)" +
				"VALUES('" + directionName + "')";
		statement.executeUpdate(sql);
	}

	public void insertTableProjects(String projectName, String directionName) throws SQLException {
		String sql = "INSERT INTO Projects(projectName,directionId)" +
				"Values('" + projectName + "'," + getDirectionId(directionName) + ")";

		statement.executeUpdate(sql);


	}

	public void insertTableEmployee(String firstName, String roleName, String projectName) throws SQLException {
		String sql = "INSERT INTO Employee(firstName,roleId,projectId)" +
				"Values('" + firstName + "'," + getRoleId(roleName) + "," + getProjectId(projectName) + ")";
		statement.executeUpdate(sql);
	}

	public int getRoleId(String roleName) throws SQLException {
		int id = 0;

		String sql = "SELECT Id FROM Roles WHERE roleName = ('" + roleName + "')";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			id = resultSet.getInt(1);
		}

		return id;
	}

	public int getDirectionId(String directionName) throws SQLException {
		int id = 0;
		String name = "";
		String sql = "SELECT id FROM Directions WHERE directionName = '" + directionName + "'";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			id = resultSet.getInt(1);
		}


		return id;
	}

	public int getProjectId(String projectName) throws SQLException {
		int id = 0;
		String sql = "SELECT id FROM Projects WHERE projectName = '" + projectName + "'";

		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			id = resultSet.getInt(1);
		}

		return id;
	}

	public int getEmployeeId(String firstName) throws SQLException {
		int id = 0;
		String sql = "SELECT id FROM Employee WHERE firstName = '" + firstName + "'";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			id = resultSet.getInt(1);
		}

		return id;
	}

	public List<String> getAllRoles() throws SQLException {
		List<String> result = new ArrayList<>();

		String sql = "SELECT roleName FROM Roles";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add((resultSet.getString("roleName")));
		}

		return result;
	}

	public List<String> getAllDirestion() throws SQLException {
		List<String> result = new ArrayList<>();

		String sql = "SELECT * FROM Directions";


		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add(resultSet.getString("directionName"));
		}

		return result;
	}

	public List<String> getAllProjects() throws SQLException {
		List<String> result = new ArrayList<>();

		String sql = "SELECT * FROM Projects";
		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add(resultSet.getString("projectName"));
		}

		return result;
	}

	public List<String> getAllEmployee() throws SQLException {
		List<String> result = new ArrayList<>();

		String sql = "SELECT * FROM Employee";

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add(resultSet.getString("firstName"));
		}
		return result;
	}


	public List<String> getAllDevelopers() throws SQLException {
		List<String> result = new ArrayList<>();
		String sql = "SELECT * FROM Employee WHERE roleId = " + getRoleId("Developer");
		ResultSet resultSet = statement.executeQuery(sql);
		while (resultSet.next()) {
			result.add(resultSet.getString("FirstName"));
		}

		return result;
	}

	public List<String> getAllJavaProjects() throws SQLException {
		List<String> result = new ArrayList<>();
		String sql = "SELECT * FROM Projects WHERE directionId = " + getDirectionId("Java");

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add(resultSet.getString("projectName"));
		}
		return result;
	}

	public List<String> getAllJavaDevelopers() throws SQLException {
		List<String> result = new ArrayList<>();

		String sql = "SELECT * FROM Employee WHERE roleId = 1 AND projectId IN(1,2) " ;

		ResultSet resultSet = statement.executeQuery(sql);

		while (resultSet.next()) {
			result.add(resultSet.getString("firstName"));
		}


		return result;
	}


}

