package educative.MeetingScheduler;

import java.util.List;

public class User {
    private String name;
    private String email;
    private Calendar calendar;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.calendar=new Calendar();
    }
    public void respondInvitation(Meeting meeting,RSVPStatus response){
        meeting.updateParticipantStatus(this,response);
        System.out.println("  · " + name + " responded: " + response);
        if(response == RSVPStatus.REJECTED){
            calendar.removeMeeting(meeting);
        } else if (response == RSVPStatus.ACCEPTED) {
            calendar.addMeeting(meeting);
        }
    }

    public List<Meeting> viewMeeting(){
        return calendar.getMeetings();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
