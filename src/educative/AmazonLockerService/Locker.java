package educative.AmazonLockerService;

import java.sql.SQLOutput;

public class Locker {
    private String lockerId;
    private LockerSize lockerSize;
    private String locationId;
    private LockerState lockerState;
    private LockerPackage currentPackage;

    public Locker(String lockerId, LockerSize lockerSize, String locationId) {
        this.lockerId = lockerId;
        this.lockerSize = lockerSize;
        this.locationId = locationId;
        this.lockerState = LockerState.AVAILABLE;
        this.currentPackage = null;
    }

    public String getLockerId() {
        return lockerId;
    }

    public LockerSize getLockerSize() {
        return lockerSize;
    }

    public String getLocationId() {
        return locationId;
    }

    public LockerState getLockerState() {
        return lockerState;
    }

    public LockerPackage getCurrentPackage() {
        return currentPackage;
    }
    public boolean addPackage(LockerPackage pkg){
        if(lockerState !=LockerState.AVAILABLE){
            System.out.println("locker" + pkg.getPackageId()+ "added to locker" +lockerId);
            return false;
        }
        this.currentPackage =pkg;
        lockerState = LockerState.BOOKED;
        System.out.println("package "+ pkg.getPackageId()+ "added to locker"+ lockerId);
        return true;
    }

    public boolean removePackage(String code){
        if(lockerState != LockerState.BOOKED || currentPackage == null){
            System.out.println("Failed to verify code for the package in locker" + lockerId);
            return false;
        }

        System.out.println("Package" + currentPackage.getPackageId() + "removed from locker" + lockerId);
        this.currentPackage = null;
        lockerState = LockerState.AVAILABLE;
        return true;
    }
}
