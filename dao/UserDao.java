package dao;

import domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {

    private static Connection con;
    private static PreparedStatement pstmt;

    private UserDao() throws SQLException, ClassNotFoundException {
        con = Connect.getConnection();
    }

    public static UserDao getUserDao() throws SQLException, ClassNotFoundException {
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


}
