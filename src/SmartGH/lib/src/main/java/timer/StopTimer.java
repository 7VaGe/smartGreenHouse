package timer;

import java.util.Timer;
import java.util.TimerTask;

public class StopTimer {
    Timer timer;

    public StopTimer(int seconds) {
        timer = new Timer();
        timer.schedule(new StopTask(), seconds * 1000);
    }

    public static void main(String[] args) {
        new StopTimer(5);
        System.out.println("DISTRIBUTION | START");
    }

    class StopTask extends TimerTask {
        public void run() {
            System.out.println("DISTRIBUTION | OUTOFTIME");
            timer.cancel();
        }
    }
}