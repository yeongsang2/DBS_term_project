package MyThread;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class GoOutThread extends Thread{

    private String userId;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public GoOutThread(String userId){
        this.userId = userId;
    }

    @Override
    public void run() {

        int time = 1;
        try {
            while (true) {
                System.out.println("머고");
                if(time == 1800000){
                    System.out.println("공석처리");
                }
                Thread.sleep(1000);
                time++;
            }
        } catch (InterruptedException e) {
            System.out.println("thread 종료");
        }
        System.out.println("thread 종료");
    }
}
