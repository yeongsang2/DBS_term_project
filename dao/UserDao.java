package dao;

import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static Connection con;
    private static PreparedStatement pstmt;

    private UserDao() throws SQLException, ClassNotFoundException {
        con = Connect.getConnection();
    }

    public static UserDao getInstance() throws SQLException, ClassNotFoundException {
        UserDao userDao = new UserDao();
        return userDao;
    }

    public void signUpUser(User user) throws SQLException {

        String insertQuery = "insert into user(user_id, name, user_type, state, major) values(?, ?, ?, ?, ?)";
        pstmt = con.prepareStatement(insertQuery);
        pstmt.setString(1, user.getUserId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getUserType().toString());
        pstmt.setBoolean(4, user.isState());
        pstmt.setString(5, user.getMajor());
        pstmt.executeUpdate();

    }


    /**
        return 값 1이면 존재, 0이면 없는 회원
     */
    public int login(String userId) throws SQLException {
        String query = "select count(*) from user where user_id = ?";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();

        rs.next();
        return rs.getInt(1);
    }
}
