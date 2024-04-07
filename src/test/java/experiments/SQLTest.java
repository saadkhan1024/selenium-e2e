package experiments;

import java.sql.*;

public class SQLTest {
    String dbDriver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
    String dbURL = "jdbc:sqlserver://locahost:1433/dbname";

    public void connectToTable() throws SQLException, ClassNotFoundException {
        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbURL, "username", "password");
        Statement st = con.createStatement();
        String query = "SELECT * FROM customers";
        ResultSet rs = st.executeQuery(query);

        while(rs.next()) {
            String customerID = rs.getString("c_id");
        }
    }
}