import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; //드라이버
    private static final String DB_URL = "jdbc:mysql://192.168.47.3:4567/library_management"; //접속할 DB 서버
    private static final String USER_NAME = "yeongsang2"; //DB에 접속할 사용자 이름을 상수로 정의
    private static final String PASSWORD = "041102"; //사용자의 비밀번호를 상수로 정의
    private static Connection con;

    public static Connection getConnection() throws SQLException, ClassNotFoundException { //db연결

        Class.forName(JDBC_DRIVER);
        con= DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        return con;
    }

    public static void close() throws SQLException{
        if(con != null){
            if( !con.isClosed()){
                con.close();
            }
        }
    }

}
