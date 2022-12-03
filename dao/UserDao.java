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

    // 좌석 사용이력
    public void showUseHistory(String userId) throws SQLException {

        int cnt =1;
        pstmt = con.prepareStatement("select u.name, us.start_time, us.end_time, l.name, l.floor, l.sector, us.seat_no from user as u left join user_seat as us on u.user_id = us.user_id  left join location as l on us.location_id = l.location_id where us.user_id = ? order by us.start_time;");
        pstmt.setString(1,userId);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            if(cnt==1){
                System.out.println(rs.getString(1) + "님의 좌석 사용이력");
                System.out.println("  |          시작시간       |         종료시간       |        좌석      ㅣ");
            }
            System.out.println(cnt + " |  " + rs.getString(2) + "  |  "+ rs.getString(3) +"  | "+rs.getString(4)+" "+rs.getString(5)+" "+rs.getString(6)+" "+rs.getInt(7) + "번 | ");
            cnt++;
        }

    }
}
