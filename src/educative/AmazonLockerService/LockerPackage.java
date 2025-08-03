package educative.AmazonLockerService;

import java.sql.SQLOutput;
import java.util.Date;

public class LockerPackage extends Package {
    private int codeValidDays;
    private String lockerId;
    private String code;
    private Date packageDeliveryTime;
    private String deliveryPersonId;

    public LockerPackage(String packageId, double packageSize, Order order, int codeValidDays, String lockerId, String code, Date packageDeliveryTime, String deliveryPersonId) {
        super(packageId, packageSize, order);
        this.codeValidDays = codeValidDays;
        this.lockerId = lockerId;
        this.code = code;
        this.packageDeliveryTime = packageDeliveryTime;
        this.deliveryPersonId = deliveryPersonId;
    }

    public int getCodeValidDays() {
        return codeValidDays;
    }

    public String getLockerId() {
        return lockerId;
    }

    public String getCode() {
        return code;
    }

    public Date getPackageDeliveryTime() {
        return packageDeliveryTime;
    }

    public String getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public boolean isValidCode(){
        Date now = new Date();
        long diffInMillis =  now.getTime() - packageDeliveryTime.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
        return diffInDays <= codeValidDays;
    }

    public boolean verifyCode(String code){
        if(!this.code.equals(code)){
            return false;
        }

        if(!isValidCode()){
            System.out.println("Code expired for package: "+ getPackageId());
            return false;
        }

        System.out.println("Code verified for package: " + getPackageId());
        return true;
    }
}
