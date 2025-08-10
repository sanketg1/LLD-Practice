package educative.MeetingScheduler;

import java.util.*;

public class MeetingScheduler {
    private User organizer;
    private Calendar calendar;
    private List<MeetingRoom> rooms;

    public MeetingScheduler(User organizer, List<MeetingRoom> rooms) {
        this.organizer = organizer;
        this.calendar = organizer.getCalendar();
        this.rooms = rooms;
    }

    public Meeting scheduleMeeting(List<User> users, Interval interval, String subject) {
        MeetingRoom room = checkRoomsAvailability(users.size(), interval);
        if (room == null) {
            System.out.println("✗ No room available for the interval.");
            return null;
        }
        bookRoom(room, interval);

        Meeting meeting = new Meeting(users, interval, room, subject);

        // Add to all calendars
        for (User user : users) {
            user.getCalendar().addMeeting(meeting);
        }
        organizer.getCalendar().addMeeting(meeting);

        // Send invite notifications
        Notification notification = new Notification(1, "Meeting Invitation: " + subject, new Date());
        for (User user : users) {
            notification.sendInvite(user, meeting);
            // Simulate user response
            RSVPStatus resp = new Random().nextBoolean() ? RSVPStatus.ACCEPTED : RSVPStatus.REJECTED;
            user.respondInvitation(meeting, resp);
        }
        System.out.println("✔ Meeting scheduled successfully!\n");
        return meeting;
    }

    public boolean cancelMeeting(Meeting meeting) {
        releaseRoom(meeting.getRoom(), meeting.getInterval());
        for (User user : meeting.getAcceptedParticipants()) {
            user.getCalendar().removeMeeting(meeting);
            Notification notification = new Notification(2, "Meeting Cancelled: " + meeting.getSubject(), new Date());
            notification.sendCancelNotification(user, meeting);
        }
        System.out.println("✔ Meeting cancelled!\n");
        return true;
    }

    public MeetingRoom checkRoomsAvailability(int capacity, Interval interval) {
        for (MeetingRoom room : rooms) {
            if (room.isAvailableFor(interval, capacity)) return room;
        }
        return null;
    }

    public boolean bookRoom(MeetingRoom room, Interval interval) {
        room.bookInterval(interval);
        return true;
    }

    public boolean releaseRoom(MeetingRoom room, Interval interval) {
        room.releaseInterval(interval);
        return true;
    }
}
