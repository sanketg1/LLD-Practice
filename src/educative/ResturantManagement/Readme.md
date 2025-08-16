# Follow-up Questions for SDE-2 Interview on Restaurant Management System

## 1. Scalability
**Question:** How would you modify the current design to support multiple branches of the restaurant with shared inventory and centralized order management?  
**Answer:**
- Introduce a **centralized inventory service** accessible by all branches.
- Use **branch-level microservices** for local operations (reservations, orders).
- Implement a **message broker (e.g., Kafka, RabbitMQ)** for synchronization between branches.
- Support **horizontal scaling** with load balancers and distributed databases.

---

## 2. Concurrency
**Question:** How does the system handle concurrent reservations or orders for the same table or menu item?  
**Answer:**
- Use **pessimistic locking** on table records during reservation.
- Use **optimistic locking** with version numbers for order updates.
- Queue booking/reservation requests for **serialized processing**.
- Ensure **atomic updates** on table status and order records.

---

## 3. Extensibility
**Question:** How would you add support for special discounts, loyalty programs, or dynamic pricing?  
**Answer:**
- Add a **promotion/discount service** decoupled from core ordering.
- Use **strategy pattern** to apply discounts dynamically.
- Extend the pricing engine with **rules-based configurations** (e.g., loyalty tiers, time-based pricing).
- Maintain **audit logs** for discounts applied.

---

## 4. Failure Handling
**Question:** What happens if a payment transaction fails midway? How is the system state maintained?  
**Answer:**
- Use **transactional workflows** with rollback support.
- Maintain an **order state machine**: Pending → Paid → Failed.
- On failure:
    - Mark payment as **Failed**.
    - Free up reserved table if not yet served.
    - Notify customer with retry option.
- Use **idempotent payment APIs** to avoid duplicate charges.

---

## 5. Data Consistency
**Question:** How do you ensure data consistency between orders, payments, and table status in a distributed environment?  
**Answer:**
- Use **distributed transactions** or **sagas pattern**.
- Apply **event-driven architecture** with reliable message queues.
- Ensure **eventual consistency** with compensating actions (e.g., rollback table if payment fails).
- Regularly **reconcile data** across services.

---

## 6. Security
**Question:** How is sensitive data like customer information and payment details protected?  
**Answer:**
- Encrypt sensitive data using **AES-256** for storage and **TLS** for transmission.
- Use **PCI-DSS compliant** payment gateways.
- Apply **role-based access control (RBAC)** for staff accounts.
- Tokenize payment info instead of storing raw card details.
- Regular **security audits** and monitoring.

---

# Missing Edge Cases in Current Flow

1. **Overbooking**
    - Introduce table-level locks or queue system.
    - Validate overlapping reservations before confirming.

2. **Partial Order Updates**
    - Support **order modification workflows** (add/remove items).
    - Maintain **audit trails** for order changes.

3. **Payment Failures**
    - Implement **retry logic**.
    - Cancel unpaid orders automatically after timeout.
    - Restore table availability if payment fails.

4. **No-show Reservations**
    - Add **auto-cancel reservations** after grace period.
    - Introduce **deposit system** to reduce no-shows.

5. **Menu Item Availability**
    - Check **real-time inventory** before confirming order.
    - Auto-update menu availability across branches.

6. **Concurrent Access**
    - Use **database-level concurrency control**.
    - Apply **optimistic/pessimistic locks** on shared records.
    - Ensure **transaction isolation levels** (e.g., SERIALIZABLE) where necessary.

---
