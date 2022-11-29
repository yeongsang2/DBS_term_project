package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BulkLoading {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection con = Connect.getConnection();
        String query = "insert into seat(seat_no, location_id, is_full, available_number, seat_type) values (?,?,?,?,?)";

        PreparedStatement pstmt = con.prepareStatement(query);

        for(int i=1; i<31; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,2);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,1);
            pstmt.setInt(5,1);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<31; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,3);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,1);
            pstmt.setInt(5,1);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<31; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,4);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,1);
            pstmt.setInt(5,1);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<51; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,5);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,2);
            pstmt.setInt(5,2);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<51; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,6);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,2);
            pstmt.setInt(5,2);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<51; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,7);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,2);
            pstmt.setInt(5,2);
            int rs = pstmt.executeUpdate();
        }
        for(int i=1; i<51; i++){
            pstmt.setInt(1,i);
            pstmt.setInt(2,8);
            pstmt.setBoolean(3,false);
            pstmt.setInt(4,2);
            pstmt.setInt(5,2);
            int rs = pstmt.executeUpdate();
        }
        Connect.close();
        System.out.println("finish");
    }
}
