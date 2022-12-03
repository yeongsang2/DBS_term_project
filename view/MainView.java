package view;

import com.sun.tools.javac.Main;
import dao.ManagerDao;
import dao.SeatDao;
import dao.UserDao;
import domain.Location;
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
    private static SeatDao seatDao;

    private MainView() throws SQLException, ClassNotFoundException {
        userDao = UserDao.getInstance();
        seatDao = SeatDao.getInstance();
    }
    public static MainView getMainView() throws SQLException, ClassNotFoundException {
        MainView mainView = new MainView();
        return mainView;
    }
    public void showMainView() throws IOException, SQLException, InterruptedException, ClassNotFoundException {

        /**
         * 1. 회원가입
         * 2. 좌석예약
         * 3. 외출
         * 4. 외출복귀
         * 5. 연장
         * 6. 관리자 모드
         * 7. 사용종료
         * 8. 좌석사용이력
         * 9. 나의자리
         * 10. 시스템종료
         */
        while(true) {
            System.out.println("--------------------------------------");
            System.out.println(" 1. 회원가입 2. 좌석예약 3. 외출      ");
            System.out.println(" 4. 외출복귀 5. 좌석연장 6. 사용종료    ");
            System.out.println(" 7. 관리자모드 8. 좌석사용이력 9. 나의자리 ");
            System.out.println(" 10. 시스템 종료 ");
            System.out.println("---------------------------------------");

            //
            int c = Integer.parseInt(br.readLine());
            switch (c) {
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
                    comeBack();
                    break;
                case 5:
                    extendSeat();
                    break;
                case 6:
                    endUse();
                    break;
                case 7:
                    manageMode();
                    break;
                case 8:
                    useHistory();
                    break;
                case 9:
                    showMySeat();
                    break;
                case 10:
                    exit(0);
                    break;
            }
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
    private void reserve() throws IOException, SQLException {

        /**
         * 회원 인증
         */
        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();

        if(userDao.login(userId) == 1){

            if(seatDao.isUsed(userId)){
                System.out.println("이미 사용중입니다.");
                return;
            }

            /**
             * 좌석 선택
             * 좌석보여주기, 좌석 선택하기
             */
            System.out.println("조망형 / 스마트 , A / B, 1층 / 2층");
            System.out.println("2층은 학생만 사용가능합니다. ");
            st= new StringTokenizer(br.readLine());
            int locationId = seatDao.showSeat(new Location(st.nextToken(), st.nextToken(), st.nextToken()));

            //해당 열람실을 사용할수 있는 user_type인지 체크
            if(!userDao.userTypeCheck(userId, locationId)){
                System.out.println("해당 열람실은 학생만 이용할 수 있습니다. 1층 열람실을 이용해주세요");
                return;
            }

            //좌석 선택
            System.out.println("선택할 좌석을 입력해주세요");
            int seatNumber = Integer.parseInt(br.readLine());
            seatDao.reserveSeat(userId,seatNumber, locationId);


        }else {
            System.out.println("존재하지 않는 회원 번호 입니다.");
        }

    }

    private void goOut() throws IOException, SQLException, InterruptedException {
        /**
         * 외출 30분이내에 복귀 하지 않을시 좌석 공석 처리됨
         */
         System.out.println("회원번호를 입력해주세요");
         String userId = br.readLine();
         seatDao.goOutTemp(userId);


    }

    private void comeBack() throws IOException, SQLException { //복귀

        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();
        seatDao.comeBack(userId);
    }

    private void extendSeat() throws IOException, SQLException {
        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();
        seatDao.extendSeat(userId);
    }

    private void endUse() throws IOException, SQLException {

        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();
        seatDao.endUse(userId);
    }

    // 관리자모드
    private void manageMode() throws IOException, SQLException, ClassNotFoundException {

        /**
         *  - 공석처리 하기
         *  - 이용중인 사용자 리스트 보기
         */
        System.out.println("관리자 번호를 입력해주세요");
        String managerId = br.readLine();
        ManagerDao managerDao = ManagerDao.getInstance();
        if(managerDao.login(managerId) == 1 ){
            System.out.println("1. 공석처리 ,2. 현재 사용중인 사람들 보기");
            int c = Integer.parseInt(br.readLine());
            switch (c){
                case 1:
                    //좌석 선택
                    System.out.println("조망형 / 스마트 , A / B, 1층 / 2층");
                    st= new StringTokenizer(br.readLine());
                    int locationId = seatDao.showSeat(new Location(st.nextToken(), st.nextToken(), st.nextToken()));

                    //공석 처리
                    System.out.println("공석 처리할 좌석을 입력해주세요");
                    int seatNumber = Integer.parseInt(br.readLine());
                    managerDao.doEmpty(locationId, seatNumber);
                case 2:
                    // 사용중인 사람들 나열
                    managerDao.showUseInUser();

            }
        }else {
            System.out.println("등록되지 않은 관리자 입니다.");
        }
    }

    //좌석 사용 이력
    private void useHistory() throws IOException, SQLException {

        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();
        if(userDao.login(userId) == 1 ){
            userDao.showUseHistory(userId);
        }else{
            System.out.println("존재하지 않는 회원번호 입니다.");
        }
    }
    private void showMySeat() throws IOException, SQLException {

        System.out.println("회원번호를 입력해주세요");
        String userId = br.readLine();
        userDao.showMySeat(userId);

    }
}
