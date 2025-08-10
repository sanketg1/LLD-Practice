package educative.MeetingScheduler;

import java.util.*;

public class MeetingRoom {
    private int id;
    private int capacity;
    private List<Interval> bookedIntervals;

    public MeetingRoom(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.bookedIntervals = new ArrayList<>();
    }

    public boolean isAvailableFor(Interval interval, int requestCapacity) {
        if (requestCapacity > capacity) return false;
        for (Interval iv : bookedIntervals) {
            if (iv.overlaps(interval)) return false;
        }
        return true;
    }

    public void bookInterval(Interval interval) {
        bookedIntervals.add(interval);
    }

    public void releaseInterval(Interval interval) {
        bookedIntervals.removeIf(iv -> iv.getStartTime().equals(interval.getStartTime())
                && iv.getEndTime().equals(interval.getEndTime()));
    }

    public int getId() { return id; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return "Room " + id + " (cap: " + capacity + ")";
    }
}