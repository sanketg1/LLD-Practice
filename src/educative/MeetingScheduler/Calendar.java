package educative.MeetingScheduler;
import java.util.*;
public class Calendar {
    private List<Meeting> meetings;
    public Calendar() {
        this.meetings = new ArrayList<>();
    }

    public void addMeeting(Meeting meeting) {
        if (!meetings.contains(meeting)) meetings.add(meeting);
    }

    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }
}
