package educative.MeetingScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Meeting {
    private static  int nextId=1;
    private int id;
    private Map<User,RSVPStatus>participantStatus;
    private Interval interval;
    private MeetingRoom room;
    private String subject;

    public Meeting(List<User>participants, Interval interval, MeetingRoom room, String subject) {
        this.id =nextId++;
        this.participantStatus=new HashMap<>();

        for(User u: participants){
            this.participantStatus.put(u,RSVPStatus.PENDING);
        }

        this.interval = interval;
        this.room = room;
        this.subject = subject;
    }

    public void addParticipants(List<User> participants){
        for(User u:participants){
            if(!participantStatus.containsKey(u)){
                participantStatus.put(u,RSVPStatus.PENDING);
            }
        }
    }
    public void updateParticipantStatus(User user, RSVPStatus status) {
        if (participantStatus.containsKey(user)) {
            participantStatus.put(user, status);
        }
    }

    public List<User> getAcceptedParticipants() {
        List<User> accepted = new ArrayList<>();
        for (Map.Entry<User, RSVPStatus> entry : participantStatus.entrySet()) {
            if (entry.getValue() == RSVPStatus.ACCEPTED) accepted.add(entry.getKey());
        }
        return accepted;
    }

    public List<User> getPendingParticipants() {
        List<User> pending = new ArrayList<>();
        for (Map.Entry<User, RSVPStatus> entry : participantStatus.entrySet()) {
            if (entry.getValue() == RSVPStatus.PENDING) pending.add(entry.getKey());
        }
        return pending;
    }

    public List<User> getRejectedParticipants() {
        List<User> rejected = new ArrayList<>();
        for (Map.Entry<User, RSVPStatus> entry : participantStatus.entrySet()) {
            if (entry.getValue() == RSVPStatus.REJECTED) rejected.add(entry.getKey());
        }
        return rejected;
    }

    public Interval getInterval() { return interval; }
    public MeetingRoom getRoom() { return room; }
    public String getSubject() { return subject; }
}
