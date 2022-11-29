package dao;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import domain.Location;

import java.sql.*;
import java.time.LocalDateTime;

public class SeatDao {

    private static Connection con;
    private static PreparedStatement pstmt;

    private SeatDao() throws SQLException, ClassNotFoundException {
        con = Connect.getConnection();
    }

    public static SeatDao getInstance() throws SQLException, ClassNotFoundException {
        SeatDao seatDao = new SeatDao();
        return seatDao;
    }


    public int showSeat(Location location) throws SQLException {

        int locationId = 0;
        int cnt = 1;
        String query = "select seat_no, s.location_id, use_in_number, seat_type from seat as s where s.location_id = ( select l.location_id from location as l where name = ? and sector = ? and floor = ?)";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, location.getName());
        pstmt.setString(2, location.getSector());
        pstmt.setString(3, location.getFloor());
        ResultSet rs = pstmt.executeQuery();
        System.out.println("---------------------------------------------");
        while (rs.next()){
            if(cnt == 1 ){
                locationId = rs.getInt(2);
            }
            System.out.print(String.format("%02d", rs.getInt(1)) + "-"+rs.getInt(3) + "/" + rs.getInt(4) + " | ");
            if(cnt == 5){
                System.out.println("");
                cnt = 0;
            }
            cnt++;
        }

        System.out.println("---------------------------------------------");
//        Connect.close();
        return locationId;
    }
    public void reserveSeat(String userId, int seatNumber, int locationId) throws SQLException {

        /**
         * user_seat 에 데이터 삽입,,
         * seat 이용중인 사람 증가
         * user state 변경
         * ..
         * 시간되면 thread
         */
         // user_seat record 생성
         pstmt = con.prepareStatement("insert into user_seat(start_time, end_time, extend_number, is_seated, is_finished, seat_no, location_id, user_id) VALUES (?,?,?,?,?,?,?,?)");
         pstmt.setString(1, String.valueOf(LocalDateTime.now()));
         pstmt.setString(2, String.valueOf(LocalDateTime.now().plusHours(4)));
         pstmt.setInt(3, 0);
         pstmt.setBoolean(4, true);
         pstmt.setBoolean(5, false);
         pstmt.setInt(6,seatNumber);
         pstmt.setInt(7,locationId);
         pstmt.setString(8,userId);
         pstmt.executeUpdate();

         // user state 변경
         pstmt = con.prepareStatement("update user set state = ? where user_id = ?");
         pstmt.setBoolean(1, true);
         pstmt.setString(2,userId);
         pstmt.executeUpdate();

         // room 이용자수 변경 ,, +1
         pstmt = con.prepareStatement("update room set use_seat_number = use_seat_number +1  where location_id = ?");
         pstmt.setInt(1, locationId);
         pstmt.executeUpdate();

    }
}
