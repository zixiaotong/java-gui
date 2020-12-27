package shanglei.saolei.program;

import java.util.TimerTask;

public class UpdateTimeTask extends TimerTask {

    private MineFrame mf;
    private int second;

    public UpdateTimeTask(MineFrame mf) {
        super();
        this.mf = mf;
        this.second = 0;
    }

    public int getSecond() {
        return second;
    }

    @Override
    public void run() {
        second++;
        mf.setTimeUsed(second);
    }

}
