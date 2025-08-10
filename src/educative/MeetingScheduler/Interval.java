package educative.MeetingScheduler;

import java.util.Date;

public class Interval {
    private Date startTime;
    private Date endTime;

    public Interval(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Date getStartTime() {
        return startTime;
    }
    public boolean overlaps(Interval other){
        return !(endTime.before(other.startTime)||startTime.after(other.endTime));
    }
}
