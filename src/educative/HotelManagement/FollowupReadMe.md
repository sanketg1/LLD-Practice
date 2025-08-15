## 🏨 Follow-up Questions for SDE-2 Interview — Hotel Management System

---

### 📈 Scalability
- **How would you modify the system to support thousands of hotels and millions of bookings efficiently?**

### 🧵 Concurrency
- **How does the system handle concurrent booking requests for the same room?**

### 🛡️ Fault Tolerance
- **What happens if a payment transaction fails midway? How would you ensure data consistency?**

### 🧩 Extensibility
- **How would you add support for new payment methods or notification channels without major code changes?**

### 🔒 Security
- **How would you secure sensitive data such as payment information and personal guest details?**

### ⚡ Caching
- **Would you use caching for room availability? How would you keep it consistent?**

### 🔎 Search Optimization
- **How would you optimize the room search for large catalogs?**

---

## 🕳️ Missing Edge Cases in Current Flow

- **🔁 Overbooking** — No handling of race conditions leading to double booking of the same room.
- **💳 Partial Payment** — Handling partial or failed payments is not described.
- **❌ Cancellation & Refunds** — No flow for booking cancellations and refund processing.
- **🧽 Room Maintenance** — What if a room is under maintenance or becomes unavailable unexpectedly?
- **📪 Notification Failures** — What if SMS or email notifications fail to send?
- **👥 Multiple Guests per Booking** — Handling bookings for groups or multiple guests.
- **🌍 Time Zone Differences** — Managing bookings across different time zones for hotel branches.


## 1. Handling Overbooking and Concurrency Control

**Problem:**  
Multiple booking requests for the same room can be processed simultaneously, causing overbooking.

**Possible Solutions:**
1. Use **locking mechanisms** (e.g., pessimistic locking) on room records during booking to prevent concurrent modifications.
2. Implement **optimistic locking** with version checks to detect conflicts and retry booking.
3. Use a **transactional system** with ACID properties to ensure atomicity of booking operations.
4. Introduce a **booking queue** or **reservation hold system** to serialize booking requests.

**Design Consideration:**  
Ensure the booking status is updated atomically to **"Booked"** only after payment confirmation.
## 1. Handling Overbooking and Concurrency Control

### 🛠️ Design Suggestions:
- Add a **version** or **lock** attribute to the `Room` or `RoomBooking` class to implement **optimistic locking**.
- Use **database transactions** to wrap the booking process, including checking availability and updating booking status.
- Introduce a **BookingStatus** state machine to handle states like `PENDING`, `CONFIRMED`, and `CANCELLED`.
- Implement a **reservation hold period** where a room is temporarily locked for a user until payment is confirmed.

---

### 💻 Code Suggestions (Java-like pseudocode):

```java
class RoomBooking {
    private int version; // for optimistic locking
    private BookingStatus status;

    public synchronized boolean bookRoom() {
        if (status != BookingStatus.AVAILABLE) {
            return false; // room already booked or held
        }
        // Lock the room for booking
        status = BookingStatus.PENDING;
        // Proceed with payment and confirmation
        return true;
    }

    public synchronized void confirmBooking() {
        status = BookingStatus.CONFIRMED;
        version++;
    }

    public synchronized void cancelBooking() {
        status = BookingStatus.AVAILABLE;
        version++;
    }
}

// In the booking service layer:
public boolean attemptBooking(RoomBooking booking) {
    try {
        beginTransaction();
        if (!booking.bookRoom()) {
            rollbackTransaction();
            return false;
        }
        // Process payment
        if (paymentSuccessful()) {
            booking.confirmBooking();
            commitTransaction();
            return true;
        } else {
            booking.cancelBooking();
            rollbackTransaction();
            return false;
        }
    } catch (OptimisticLockException e) {
        rollbackTransaction();
        // Retry or notify user
        return false;
    }
}
📌 Key Points:

Use synchronized methods or database locks to avoid race conditions.

Wrap booking and payment in a single transaction to maintain consistency.

Use optimistic locking to detect concurrent updates and handle retries.