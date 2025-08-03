package educative.AmazonLockerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LockerLocation {
    private String name ;
    private List<Locker> lockers;
    private double latitude ;
    private double longitude ;
    private Date openTime;
    private Date closeTime;

    public LockerLocation(String name,  double latitude, double longitude, Date openTime, Date closeTime) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.lockers =  new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public List<Locker> getLockers() {
        return lockers;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void addLocker(Locker locker){
        lockers.add(locker);
    }
}
