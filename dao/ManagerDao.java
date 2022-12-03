package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDao {

    private static Connection con;
    private static PreparedStatement pstmt;

    private ManagerDao() throws SQLException, ClassNotFoundException {
        con = Connect.getConnection();
    };

    public static ManagerDao getInstance() throws SQLException, ClassNotFoundException {
        ManagerDao managerDao = new ManagerDao();
        return managerDao;
    }

    //관리자 로그인
    public int login(String managerId) throws SQLException {

        pstmt = con.prepareStatement("select count(*) from manager where manager_id = '2020039111'");
//        pstmt.setString(1, managerId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    //특정 좌석 공석처리
    public void doEmpty(int locationId, int seatNumber) throws SQLException {

        String userId;
        int userSeatId;

//        String userId = threadMapHashMap.get(thread);
//        threadMapHashMap.remove(thread);

        // seat table use_in_number 감소
        pstmt = con.prepareStatement("update seat set use_in_number = use_in_number - 1 where location_id = ? and seat_no = ?");
        pstmt.setInt(1, locationId);
        pstmt.setInt(2, seatNumber);
        pstmt.executeUpdate();

        // user_seat 에서 user_id, user_seat_id 가져옴
        pstmt = con.prepareStatement("select user_id, user_seat_id from user_seat where location_id = ? and seat_no = ? and is_finished = ?");
        pstmt.setInt(1, locationId);
        pstmt.setInt(2, seatNumber);
        pstmt.setBoolean(3, false);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        userId = rs.getString(1);
        userSeatId = rs.getInt(2);

        // user_seat 변경
        pstmt = con.prepareStatement("update user_seat set is_finished = ? and is_seated = ? where user_seat_id = ?");
        pstmt.setBoolean(1, true);
        pstmt.setBoolean(2, false);
        pstmt.setInt(3, userSeatId);
        pstmt.executeUpdate();

        // user state 변경
        pstmt = con.prepareStatement("update user set state = ? where user_id = ?");
        pstmt.setBoolean(1, false);
        pstmt.setString(2, userId);
        pstmt.executeUpdate();

        // room 이용자수 변경 ,, - 1
        pstmt = con.prepareStatement("update room set use_seat_number = use_seat_number -1  where location_id = ?");
        pstmt.setInt(1, locationId);
        pstmt.executeUpdate();

    }

    //현재 도서관 사용중인 회원 보여줌
    public void showUseInUser() throws SQLException {
        pstmt = con.prepareStatement("select u.name, u.user_type, u.major, us.start_time, us.end_time, l.name, l.floor, l.sector, us.seat_no from user as u left join user_seat as us on u.user_id = us.user_id  left join location as l on us.location_id = l.location_id where us.is_finished = false;");
        ResultSet rs = pstmt.executeQuery();
        System.out.println("|  이름     |  유저타입  |      전공      |          시작시간       |         종료시간       |        좌석      ㅣ");
        while (rs.next()){
            System.out.println("|  " + rs.getString(1) + "   |  " + rs.getString(2) + "  |  " + rs.getString(3) + "  |  " + rs.getString(4) + "  |  "+ rs.getString(5) +"  | "+rs.getString(6)+" "+rs.getString(7)+" "+rs.getString(8)+" "+rs.getInt(9) + "번 | ");
        }

    }
}
