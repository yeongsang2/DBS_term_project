import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection con = Connect.getConnection();
        System.out.println(con);
    }
}
