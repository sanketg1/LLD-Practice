package educative.MeetingScheduler;

import java.util.Date;

public class Driver {
    public static void main(String[] args) {
        // Set up rooms
        MeetingRoom roomA = new MeetingRoom(1, 4);
        MeetingRoom roomB = new MeetingRoom(2, 8);
        java.util.List<MeetingRoom> rooms = java.util.Arrays.asList(roomA, roomB);

        // Set up users
        User alice = new User("Alice", "alice@email.com");
        User bob = new User("Bob", "bob@email.com");
        User charlie = new User("Charlie", "charlie@email.com");
        java.util.List<User> participants = java.util.Arrays.asList(alice, bob, charlie);

        // Set up scheduler
        MeetingScheduler scheduler = new MeetingScheduler(alice, rooms);

        // Scenario 1: All accept
        header("Scenario 1: Schedule a Meeting (Random Accept/Reject)");
        arrow("Scheduling meeting \"Design Review\" for Alice, Bob, Charlie...");
        Interval interval1 = getInterval(2025, java.util.Calendar.JULY, 10, 10, 0, 11, 0);
        Meeting meeting1 = scheduler.scheduleMeeting(participants, interval1, "Design Review");

        // Scenario 2: Schedule another meeting (possibly some will reject)
        header("Scenario 2: Schedule Another Meeting (Random Accept/Reject)");
        arrow("Scheduling meeting \"Sprint Planning\" for Alice, Bob, Charlie...");
        Interval interval2 = getInterval(2025, java.util.Calendar.JULY, 10, 12, 0, 13, 0);
        Meeting meeting2 = scheduler.scheduleMeeting(participants, interval2, "Sprint Planning");

        // Scenario 3: Cancel a meeting
        header("Scenario 3: Cancel Meeting");
        arrow("Cancelling meeting \"Design Review\"...");
        if (meeting1 != null) {
            scheduler.cancelMeeting(meeting1);
        }
    }

    // Formatting helpers
    private static void header(String title) {
        System.out.println("\n==============================");
        System.out.println("▶ " + title);
        System.out.println("==============================\n");
    }
    private static void arrow(String msg) { System.out.println("→ " + msg); }

    // Utility function for creating an interval
    private static Interval getInterval(int year, int month, int day, int startHour, int startMin, int endHour, int endMin) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month, day, startHour, startMin, 0);
        Date start = cal.getTime();
        cal.set(year, month, day, endHour, endMin, 0);
        Date end = cal.getTime();
        return new Interval(start, end);
    }
}
