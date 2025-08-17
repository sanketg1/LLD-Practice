# ✈️ Airline Management System – Classes & Relations

## **Classes**

### 🔹 Core Entities
- **Account**  
  Represents user accounts for Admin, Crew, FrontDeskOfficer, and Customer.

- **Person (abstract)**  
  Base class for all people interacting with the system.
    - **Admin**: Manages flights and crew assignments.
    - **Crew**: Operates flights.
    - **FrontDeskOfficer**: Assists customers with bookings.
    - **Customer**: Books tickets and makes payments.

- **Passenger**  
  Represents a traveler in the system. *(Specialized type of Person for booking flights)*

---

### 🔹 Flight & Seats
- **Seat**  
  Represents a physical seat in an aircraft.

- **FlightSeat**  
  Represents a seat assigned to a specific **FlightInstance**.

- **Flight**  
  Defines details about a scheduled route (e.g., Flight No, Source, Destination).

- **FlightInstance**  
  Represents a specific occurrence of a **Flight**, assigned to an aircraft with **FlightSeats**.

---

### 🔹 Reservations & Itinerary
- **Itinerary**  
  Tracks the travel plan of a **Customer**, containing multiple **FlightReservations**.

- **FlightReservation**  
  Represents a booking made by a **Passenger** for one or more flights.

---

### 🔹 Payments & Notifications
- **Payment (abstract)**  
  Handles payment processing.
    - **Cash**
    - **CreditCard**

- **Notification (abstract)**  
  Sends booking/payment notifications.
    - **SMSNotification**
    - **EmailNotification**

---

### 🔹 Supporting Classes
- **SearchCatalog**  
  Manages **FlightInstances** and implements search functionality.

- **Airport**  
  Represents an airport.

- **Aircraft**  
  Represents an aircraft with a set of seats.

- **Airline (Singleton)**  
  Represents the airline company managing flights and aircraft.

---

## **Relations & Explanations**

1. **Person ↔ Account**  
   Each `Person` has an associated `Account`.

2. **Passenger extends Person**  
   `Passenger` is a specialized `Person`.

3. **Flight ↔ FlightInstance**  
   A `FlightInstance` represents a scheduled occurrence of a `Flight`.

4. **FlightInstance ↔ FlightSeat**  
   A `FlightInstance` is composed of multiple `FlightSeats`.

5. **Seat ↔ FlightSeat**  
   Each `FlightSeat` is mapped to a physical `Seat`.

6. **Itinerary ↔ FlightReservation**  
   An `Itinerary` contains one or more `FlightReservations`.

7. **FlightReservation ↔ Passenger**  
   Each reservation is made by a `Passenger`.

8. **Payment ↔ FlightReservation**  
   A `Payment` is associated with a reservation.

9. **Notification ↔ Payment**  
   Notifications are sent when a payment succeeds/fails.

10. **SearchCatalog ↔ FlightInstance**  
    `SearchCatalog` allows customers to search for available `FlightInstances`.

11. **Airline ↔ Aircraft / Flights**  
    The airline manages aircraft and flight schedules.

12. **Admin (Person) ↔ Flights & Crew**  
    Admin manages flight creation and crew assignment.

---

## **Design Highlights**
- **Inheritance**:
    - `Person` → `Admin`, `Crew`, `FrontDeskOfficer`, `Customer`, `Passenger`
    - `Payment` → `Cash`, `CreditCard`
    - `Notification` → `SMSNotification`, `EmailNotification`

- **Composition**:
    - `FlightInstance` **has** `FlightSeats`
    - `Itinerary` **contains** `FlightReservations`

- **Design Pattern**:
    - **Singleton** → `Airline`  

![Airline Management UML](AirLineManagement.png)
