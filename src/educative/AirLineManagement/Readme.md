# Follow-up Questions for SDE-2 Interview on Airline Management System

## 📈 Design Scalability
- How would you modify the current design to handle thousands of concurrent flight bookings efficiently?
    - Consider horizontal scaling, sharding, caching, CQRS, and eventual consistency where appropriate.

## 🧯 Fault Tolerance
- How does the system handle payment failures or partial failures during booking?
    - Think saga pattern, outbox/inbox, idempotent operations, and compensating transactions.

## 🔒 Concurrency Control
- How would you prevent race conditions when multiple customers try to book the same seat simultaneously?
    - Discuss pessimistic vs optimistic locking, versioning, distributed locks, and unique constraints.

## 🧩 Extensibility
- How would you extend the system to support multi-leg flights or code-sharing between airlines?
    - Consider itinerary modeling, partner APIs, normalization vs denormalization, and pricing engines.

## 🔐 Security
- What measures would you implement to secure sensitive data like payment information and user credentials?
    - Cover encryption at rest/in transit, tokenization, PCI-DSS, OAuth2/OIDC, and secrets management.

## ✅ Data Consistency
- How would you ensure data consistency across distributed components, especially for flight seat availability?
    - Explore two-phase commit vs sagas, read-your-writes, and anti-entropy reconciliation.

---

# Missing Edge Cases in Current Flow

- **Overbooking**: Handling cases where more bookings are made than available seats.
- **Cancellation and Refunds**: Flow for customers canceling reservations and processing refunds.
- **Flight Delays and Rescheduling**: Managing changes in flight schedules and notifying passengers.
- **Partial Payment Failures**: What happens if payment is interrupted after seat assignment?
- **Seat Upgrades or Changes**: Allowing customers to change or upgrade seats after booking.
- **Multiple Payment Methods**: Supporting split payments or alternative payment options.
- **Notification Failures**: Handling cases where email or SMS notifications fail to send.
- **Crew Unavailability**: Managing crew scheduling conflicts or last-minute unavailability.

---

## ✅ Optional Checklist Format (for your prep)

- [ ] Scalability strategy (load, caching, CQRS)
- [ ] Concurrency control on seat inventory
- [ ] Saga/compensation for booking/payment
- [ ] Multi-leg & code-share modeling
- [ ] Security controls (PCI, OAuth2/OIDC)
- [ ] Consistency model & data integrity
- [ ] Overbooking policy
- [ ] Cancellation/refund workflows
- [ ] Delay/rescheduling notifications
- [ ] Partial payment handling
- [ ] Seat upgrade/change flow
- [ ] Split/alt payment support
- [ ] Notification retries & DLQ
- [ ] Crew scheduling edge cases
# Design Improvement 1: Handling High Concurrency in Flight Bookings

## Problem
Multiple customers may try to book the same seat at the same time, leading to race conditions and inconsistent seat assignments.

## Improvement
Implement concurrency control using locking mechanisms or atomic transactions to ensure that seat assignment is thread-safe.

## Code Concept (Java-like pseudocode)

```java
class FlightSeat {
    private Seat seat;
    private boolean isBooked = false;

    // synchronized method to ensure atomic seat booking
    public synchronized boolean bookSeat() {
        if (!isBooked) {
            isBooked = true;
            return true;
        }
        return false; // seat already booked
    }
}

// Usage in booking flow
boolean success = flightSeat.bookSeat();
if (success) {
    // proceed with reservation
} else {
    // inform customer seat is unavailable
}
Explanation

The synchronized keyword ensures only one thread can execute the bookSeat method at a time.

Once a seat is marked as booked, any other customer trying to book it will get false.

This prevents double booking in high concurrency scenarios.

## Design Improvement 2: Handling Payment Failures and Partial Failures

**Problem:**  
Payment might fail after seat assignment, leading to inconsistent booking states.

**Improvement:**  
Implement a transactional approach where seat booking and payment are part of a single atomic operation. Use a `PaymentStatus` enum and rollback seat assignment if payment fails.

### Code Concept (Java-like pseudocode):

```java
enum PaymentStatus { PENDING, SUCCESS, FAILED }

class FlightReservation {
    private FlightSeat flightSeat;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    public boolean bookAndPay(Payment payment) {
        synchronized(flightSeat) {
            if (!flightSeat.bookSeat()) {
                return false; // seat already booked
            }
        }
        
        paymentStatus = payment.process();
        if (paymentStatus == PaymentStatus.FAILED) {
            flightSeat.releaseSeat(); // rollback seat booking
            return false;
        }
        return true;
    }
}
Explanation:
This ensures that if payment fails, the seat booking is reversed to keep the system consistent.

## Design Improvement 3: Preventing Overbooking

**Problem:**  
The system currently does not handle the case where more bookings are made than available seats, leading to overbooking.

**Improvement:**  
Maintain a centralized seat availability count and validate availability before confirming any booking. Use atomic operations or database transactions to ensure accurate seat count updates.

### Code Concept (Java-like pseudocode):

```java
class FlightInstance {
    private int totalSeats;
    private int bookedSeats = 0;

    public synchronized boolean bookSeat() {
        if (bookedSeats < totalSeats) {
            bookedSeats++;
            return true;
        }
        return false; // no seats available
    }

    public synchronized void cancelSeat() {
        if (bookedSeats > 0) {
            bookedSeats--;
        }
    }
}

// Booking flow
if (flightInstance.bookSeat()) {
    // proceed with reservation and payment
} else {
    // inform customer no seats available
}
Explanation:
This ensures the system never books more seats than available, preventing overbooking issues.

## Design Improvement 4: Handling Flight Cancellations and Refunds

**Problem:**  
The current system lacks a flow for customers to cancel reservations and receive refunds.

**Improvement:**  
Add cancellation methods in `FlightReservation` and `Payment` classes. On cancellation, update seat availability and process refund based on payment status.

**Code Concept (Java-like pseudocode):**
```java
class FlightReservation {
    private FlightSeat flightSeat;
    private Payment payment;
    private boolean isCancelled = false;

    public synchronized boolean cancelReservation() {
        if (isCancelled) {
            return false; // already cancelled
        }
        isCancelled = true;
        flightSeat.releaseSeat(); // free up the seat
        payment.processRefund();  // refund the payment
        return true;
    }
}

class Payment {
    private PaymentStatus status;

    public void processRefund() {
        if (status == PaymentStatus.SUCCESS) {
            // logic to refund the amount
            status = PaymentStatus.REFUNDED;
        }
    }
}

### Design Improvement 5: Managing Flight Delays and Rescheduling  

**Problem:**  
The system currently does not handle flight delays or rescheduling, which affects passengers and crew.  

**Improvement:**  
Introduce methods to update flight schedules and notify affected passengers and crew.  
Maintain a `status` attribute in `FlightInstance` to represent states like **Scheduled**, **Delayed**, or **Cancelled**.  

**Code Concept (Java-like pseudocode):**  

```java
enum FlightStatus { SCHEDULED, DELAYED, CANCELLED }

class FlightInstance {
    private FlightStatus status = FlightStatus.SCHEDULED;
    private List<Notification> notifications;

    public synchronized void updateStatus(FlightStatus newStatus) {
        this.status = newStatus;
        notifyPassengersAndCrew();
    }

    private void notifyPassengersAndCrew() {
        for (Notification notification : notifications) {
            notification.send();
        }
    }
}
Explanation:
This design allows the system to:

Update flight statuses dynamically.

Notify all passengers and crew when a delay or cancellation occurs.

Keep the system consistent and resilient during disruptions.

### Design Improvement 6: Supporting Seat Upgrades and Changes
Problem: The current system does not allow customers to change or upgrade their seats after booking.

Improvement: Implement methods to allow seat change requests, validate availability of the new seat, and update the reservation accordingly. Handle any additional payment or refund if the new seat has a different price.

Code Concept (Java-like pseudocode):

class FlightReservation {
    private FlightSeat currentSeat;
    private Payment payment;

    public synchronized boolean changeSeat(FlightSeat newSeat) {
        if (newSeat.bookSeat()) { // try to book the new seat
            currentSeat.releaseSeat(); // release old seat
            currentSeat = newSeat;
            // Optionally handle payment difference here
            return true;
        }
        return false; // new seat not available
    }
}
This design enables customers to modify their seat assignments safely.

Design Improvement 7: Handling Notification Failures
Problem: Notifications (email or SMS) might fail to send, leaving passengers or crew uninformed about important updates.

Improvement: Implement retry mechanisms and fallback notification channels. Maintain notification status and log failures for manual intervention if needed.

Code Concept (Java-like pseudocode):

abstract class Notification {
    protected NotificationStatus status = NotificationStatus.PENDING;

    public void sendWithRetry() {
        int retries = 3;
        while (retries > 0) {
            if (send()) {
                status = NotificationStatus.SENT;
                return;
            } else {
                retries--;
                // wait or log retry attempt
            }
        }
        status = NotificationStatus.FAILED;
        logFailure();
        // Optionally trigger fallback notification
    }

    protected abstract boolean send();

    private void logFailure() {
        // Log failure details for manual follow-up
    }
}

class EmailNotification extends Notification {
    @Override
    protected boolean send() {
        // send email logic
        return true; // or false if failed
    }
}
This ensures the system attempts to deliver notifications reliably and handles failures gracefully.

### Design Improvement 7: Handling Notification Failures
**Problem:** Notifications (email or SMS) might fail to send, leaving passengers or crew uninformed about important updates.  

**Improvement:** Implement retry mechanisms and fallback notification channels. Maintain notification status and log failures for manual intervention if needed.  

**Code Concept (Java-like pseudocode):**

```java
abstract class Notification {
    protected NotificationStatus status = NotificationStatus.PENDING;

    public void sendWithRetry() {
        int retries = 3;
        while (retries > 0) {
            if (send()) {
                status = NotificationStatus.SENT;
                return;
            } else {
                retries--;
                // wait or log retry attempt
            }
        }
        status = NotificationStatus.FAILED;
        logFailure();
        // Optionally trigger fallback notification
    }

    protected abstract boolean send();

    private void logFailure() {
        // Log failure details for manual follow-up
    }
}

class EmailNotification extends Notification {
    @Override
    protected boolean send() {
        // send email logic
        return true; // or false if failed
    }
}
This ensures the system attempts to deliver notifications reliably and handles failures gracefully.

## Design Improvement 8: Managing Crew Unavailability and Scheduling Conflicts

**Problem:**  
The system currently does not handle cases where crew members become unavailable last minute or have scheduling conflicts.

**Improvement:**  
Implement crew availability tracking and conflict detection when assigning crew to flight instances. Provide methods to reassign crew and notify affected parties.

### Code Concept (Java-like pseudocode):

```java
class Crew {
    private List<FlightInstance> assignedFlights;
    private boolean isAvailable = true;

    public boolean isAvailableFor(FlightInstance flightInstance) {
        if (!isAvailable) return false;
        for (FlightInstance fi : assignedFlights) {
            if (fi.conflictsWith(flightInstance)) {
                return false; // scheduling conflict
            }
        }
        return true;
    }

    public void markUnavailable() {
        isAvailable = false;
        notifyAdminForReassignment();
    }

    private void notifyAdminForReassignment() {
        // logic to notify admin or system for reassignment
    }
}

class Admin {
    public boolean assignCrewToFlight(Crew crew, FlightInstance flightInstance) {
        if (crew.isAvailableFor(flightInstance)) {
            flightInstance.addCrew(crew);
            crew.assignedFlights.add(flightInstance);
            return true;
        }
        return false; // cannot assign due to unavailability or conflict
    }
}

---

## 📌 Final Summary

The Airline Management System design we built focuses on **modularity, scalability, and real-world practicality**.  
Through progressive design improvements, we addressed core challenges like:

- ✅ Flight scheduling, ticket booking, and seat management.  
- ✅ Payment handling and notifications.  
- ✅ Baggage, loyalty programs, and meal preferences.  
- ✅ Crew assignment, availability, and scheduling conflict resolution.  
- ✅ Extensibility for future needs like real-time flight tracking or dynamic pricing.  

### Key Design Principles Applied:
- **Encapsulation & Abstraction** → Clean separation of concerns (Flights, Passengers, Crew, Payments, etc.).  
- **Inheritance & Polymorphism** → Specialized behaviors for entities like different `Order` or `Payment` types.  
- **Composition & Associations** → Rich relationships (e.g., Flight ↔ Crew, Passenger ↔ Booking).  
- **Flexibility & Extensibility** → Ability to integrate new modules (meal plans, notifications, reassignments).  
