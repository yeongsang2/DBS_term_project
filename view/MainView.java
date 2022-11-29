package view;

import com.sun.tools.javac.Main;
import dao.UserDao;
import domain.User;
import domain.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.StringTokenizer;

import static java.lang.System.exit;

public class MainView {


    private static  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static StringTokenizer st;
    private static UserDao userDao;
    private MainView() throws SQLException, ClassNotFoundException {
        userDao = UserDao.getUserDao();
    }
    public static MainView getMainView() throws SQLException, ClassNotFoundException {
        MainView mainView = new MainView();
        return mainView;
    }
    public void showMainView() throws IOException, SQLException {

        /**
         * 1. 회원가입
         * 2. 좌석예약
         * 3. 외출
         * 4. 외출복귀
         * 5. 연장
         * 6. 종료
         */
        System.out.println("----------------------------------");
        System.out.println(" 1. 회원가입 2. 좌석예약 3. 외출      ");
        System.out.println(" 4. 외출복귀 5. 좌석연장 6. 관리자모드 ");
        System.out.println(" 7. 사용종료 8. 시스템 종료          ");
        System.out.println("----------------------------------");

        //
        int c = Integer.parseInt(br.readLine());
        switch (c){
            case 1:
                signUp();
                break;
            case 2:
                reserve();
                break;
            case 3:
                goOut();
                break;
            case 4:
                goBack();
                break;
            case 5:
                extendSeat();
                break;

            case 6:
                manageMode();
                break;

            case 7:
                endUse();
                break;
            case 8:
                exit(0);
                break;
        }
    }

    public void signUp() throws IOException, SQLException {
        System.out.println(" 학생이면 1, 일반인이면 2번을 눌러주세요");
        int t = Integer.parseInt(br.readLine());
        switch (t){
            case 1:
                System.out.println("학번 이름 전공을 적어주세요");
                st = new StringTokenizer(br.readLine());
                User userA = new User(st.nextToken(), st.nextToken(), UserType.student, st.nextToken(), false);
                userDao.signUpUser(userA);
                break;
            case 2:
                System.out.println("회원번호 이름을 적어주세요");
                st = new StringTokenizer(br.readLine());
                User userB = new User(st.nextToken(), st.nextToken(), UserType.general, null, false);
                userDao.signUpUser(userB);
                break;
        }
    }
    private void reserve() {

    }

    private void goOut() {

    }

    private void goBack() {

    }

    private void extendSeat() {

    }

    private void manageMode() {


    }

    private void endUse() {
    }


}
