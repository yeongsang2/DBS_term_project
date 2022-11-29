package dao;

import MyThread.GoOutThread;
import domain.Location;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class SeatDao {

    private static Connection con;
    private static PreparedStatement pstmt;
    private static HashMap<Thread, String> threadMapHashMap = new HashMap<>();

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

         // seat table 변경
         pstmt = con.prepareStatement("update seat set use_in_number = use_in_number +1  where location_id = ? and seat_no = ?");
         pstmt.setInt(1,locationId);
         pstmt.setInt(2,seatNumber);
         pstmt.executeUpdate();

    }

    public Boolean checkUse(String userId) throws SQLException {

        String query = "select state from user where user_id = ?";
        pstmt = con.prepareStatement(query);
        pstmt.setString(1, userId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getBoolean(1);

    }

    public void doEmptySeat(Thread thread) throws SQLException {

        int locationId;
        int seatNumber;
        String userId = threadMapHashMap.get(thread);

        threadMapHashMap.remove(thread);

        // user_seat 이용해서 location_id, seatNumber 조회
        pstmt = con.prepareStatement("select location_id, seat_no from user_seat where user_id = ? and is_finished = ?");
        pstmt.setString(1, userId);
        pstmt.setBoolean(2, false);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        locationId = rs.getInt(1);
        seatNumber = rs.getInt(2);

        // seat table use_in_number 감소
        pstmt = con.prepareStatement("update seat set use_in_number = use_in_number - 1 where location_id = ? and seat_no = ?");
        pstmt.setInt(1, locationId);
        pstmt.setInt(2, seatNumber);

        // user_seat 변경
        pstmt = con.prepareStatement("update user_seat set is_finished = ? and is_seated = ? where user_id = ?");
        pstmt.setBoolean(1, true);
        pstmt.setBoolean(2, false);
        pstmt.setString(3,userId);
        pstmt.executeUpdate();

        // user state 변경
        pstmt = con.prepareStatement("update user set state = ? where user_id = ?");
        pstmt.setBoolean(1, false);
        pstmt.setString(2, userId);
        pstmt.executeUpdate();
        System.out.println("공석처리");

        // room 이용자수 변경 ,, - 1
        pstmt = con.prepareStatement("update room set use_seat_number = use_seat_number -1  where location_id = ?");
        pstmt.setInt(1, locationId);
        pstmt.executeUpdate();
    }

    public void goOutTemp(String userId) throws SQLException, InterruptedException {

        // 이용중인지 check
        if(checkUse(userId)){

            //user_seat is_seated 상태 update
            pstmt = con.prepareStatement("update user_seat set is_seated = ? where user_id = ? and is_finished = ? ");
            pstmt.setBoolean(1, false);
            pstmt.setString(2, userId);
            pstmt.setBoolean(3, false);
            pstmt.executeUpdate();
            System.out.println("외출 완료 되었습니다.");

            // 30분 후 복귀 안할시 공석처리됨
            Thread thread = new Thread() {
                @Override
                public void run() {
                    int time = 1;
                    try {
                        while (true) {
                            if(time == 3600){
                                doEmptySeat(this);
                            }
                            Thread.sleep(1000);
                            time++;
                        }
                    } catch (InterruptedException e) {
                        System.out.println("thread 종료");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } // run ()
            };
            thread.start();
            threadMapHashMap.put(thread, userId); // thread hashMap 에 추가

        }else {
            System.out.println("사용중이 아닙니다");
        }
    }
}
