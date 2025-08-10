package educative.MeetingScheduler;

import java.util.Date;

public class Notification {
    private int notificationId;
    private String content;
    private Date creationDate;

    public Notification(int notificationId, String content, Date creationDate) {
        this.notificationId = notificationId;
        this.content = content;
        this.creationDate = creationDate;
    }

    public void sendInvite(User user, Meeting meeting) {
        System.out.println("  - Notification sent to " + user.getName() + " for meeting: " + meeting.getSubject());
    }

    public void sendCancelNotification(User user, Meeting meeting) {
        System.out.println("  - Cancellation sent to " + user.getName() + " for meeting: " + meeting.getSubject());
    }
}