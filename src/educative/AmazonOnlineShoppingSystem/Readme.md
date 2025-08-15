## 🛒 Follow-up Questions for SDE-2 — Amazon Online Shopping System

---

### 📈 Scalability
- **How would you modify the current design to handle millions of users and products efficiently?**

### 🧵 Concurrency
- **How does the system handle concurrent updates to the shopping cart or product inventory?**

### 🛡️ Fault Tolerance
- **What happens if payment processing fails midway? How does the system ensure consistency?**

### 🧩 Extensibility
- **How would you add support for multiple payment methods or shipment providers?**

### 🔒 Security
- **How is sensitive data like credit card information protected in the design?**

### ⚡ Caching
- **How would you implement caching to improve search performance?**

### 🔗 Data Consistency
- **How do you ensure the order status and shipment status remain synchronized?**

---

## 🕳️ Missing Edge Cases in the Current Flow

- **📦 Inventory Depletion** — Handling stock when multiple users try to buy the same product simultaneously.
- **💳 Payment Validity** — Managing expired or invalid payment methods during checkout.
- **📤 Partial Shipments** — Supporting partial fulfillment when some items are out of stock.
- **❌ Cancellations & Refunds** — Handling order cancellations and timely refunds.
- **👤 Guest Sessions** — Managing guest carts and persistence across devices.
- **🌐 Network Failures** — Handling failures during payment authorization or shipment creation.
- **🏷️ Address Validation** — Validating addresses and handling failed deliveries.

## 🛡️ Design Improvement: Handling Inventory Stock Depletion

**Problem:**  
When multiple users try to buy the same product at the same time, the system must prevent **overselling** and maintain inventory accuracy.

---

### 💡 Proposed Solution

1. **Atomic Stock Update**
    - Use *atomic decrement* operations or synchronized methods to ensure that stock changes happen safely.
    - Database-level transaction isolation (e.g., `SELECT ... FOR UPDATE`) to avoid race conditions.
    - In distributed environments, consider **distributed locks** (e.g., Redis Redlock, Zookeeper).

2. **Stock Reservation**
    - Temporarily **reserve** inventory when the user adds items to the cart.
    - Release reservation if the checkout is not completed within a defined timeout (e.g., 15 minutes).
    - Reduces last-minute “out of stock” disappointments at payment step.

3. **Fail-Fast Out-of-Stock Handling**
    - Immediately inform the user if requested quantity is unavailable.
    - Offer alternative products or adjust quantity suggestions.

---

### 🧩 Pseudo-code Example (Atomic Stock Update)

```java
class Product {
    private int stockCount;

    // Atomic method to reduce stock
    public synchronized boolean reduceStock(int quantity) {
        if (stockCount >= quantity) {
            stockCount -= quantity;
            return true;
        }
        return false; // Not enough stock
    }

    public synchronized void increaseStock(int quantity) {
        stockCount += quantity;
    }
}

// Usage during order placement
boolean success = product.reduceStock(orderQuantity);
if (!success) {
    // Handle out-of-stock scenario (notify user, suggest alternatives)
}
⚖️ Trade-offs

Synchronized methods
✅ Simple to implement in single-server setups.
❌ Not scalable for multi-node deployments — needs distributed locking or DB-level control.

Database Transactions
✅ Works in multi-node setups with ACID guarantees.
❌ Can cause contention under high load.

Distributed Locks
✅ Prevent overselling in distributed microservice architectures.
❌ Requires careful timeout/release handling to avoid deadlocks.

🚀 Scaling Approaches

Use inventory service as a single source of truth for stock updates.

Employ event-driven stock decrements with compensation logic for failed payments.

Implement read-write separation for inventory queries to reduce database load.
Use caching for frequently accessed product stock levels to reduce database hits.
---
## 💳 Design Improvement: Managing Expired or Invalid Payment Methods

**Problem:**  
Orders can fail if a payment method is **expired** or **invalid**, leading to poor user experience and lost sales.

---

### 💡 Proposed Solution

1. **Pre-Validation of Payment Methods**
   - Validate payment method **before** initiating the payment gateway call.
   - Store and regularly check `expiryDate` for cards and other time-sensitive payment methods.

2. **Payment Method Status Tracking**
   - Maintain a `PaymentStatus` enum (e.g., `VALID`, `EXPIRED`, `INVALID`, `FAILED`).
   - Store payment method metadata securely (PCI DSS compliance for cards).

3. **User Feedback and Recovery**
   - Prompt users to **update or replace** expired payment methods.
   - Offer an option to **choose an alternative payment method** at checkout.

4. **Retry and Fallback**
   - Implement retry mechanisms for transient failures.
   - Fallback to stored alternative payment methods.

---

### 🧩 Pseudo-code Example (Payment Validation)

```java
abstract class Payment {
    protected PaymentStatus status;
    protected Date expiryDate; // For cards or similar

    public boolean validatePaymentMethod() {
        Date currentDate = new Date();
        if (expiryDate != null && expiryDate.before(currentDate)) {
            status = PaymentStatus.FAILED;
            return false; // Payment method expired
        }
        // Additional validation logic (e.g., card number checksum, account validity)
        return true;
    }

    public abstract boolean processPayment(double amount);
}

class CreditCard extends Payment {
    @Override
    public boolean processPayment(double amount) {
        if (!validatePaymentMethod()) {
            System.out.println("Payment failed: Invalid or expired credit card.");
            return false;
        }
        // Process payment with gateway
        status = PaymentStatus.SUCCESS;
        return true;
    }
}

// Usage during order payment
boolean paymentSuccess = creditCard.processPayment(orderAmount);
if (!paymentSuccess) {
    // Prompt user to update payment method or choose alternative
}
⚖️ Trade-offs

Early validation
✅ Improves UX by catching invalid methods before processing.
❌ Requires extra metadata storage (expiry date, validation rules).

Gateway-side validation only
✅ Simplifies implementation.
❌ Increases failed transaction rate due to last-minute rejections.

🚀 Scaling Considerations

Implement scheduled background jobs to scan for expired payment methods and notify users proactively.

Use tokenized payment methods from PCI-compliant providers instead of storing raw details.

For recurring payments, trigger pre-billing validation before due date to avoid service disruption.
---
## 📦 Design Improvement: Handling Partial Shipments
## 📦 Design Improvement: Supporting Partial Shipments

**Problem:**  
When certain items in an order are **out of stock** or **delayed**, the system must be able to ship available items first while keeping the customer informed about the remaining ones.

---

### 💡 Proposed Solution

1. **Order Splitting**
   - Break a single order into **multiple shipments** based on item availability.

2. **Independent Shipment Tracking**
   - Each shipment gets its **own tracking number**, carrier info, and status.

3. **Customer Notifications**
   - Inform customers about:
     - Which items have shipped
     - Which are pending/backordered
     - Expected delivery dates

4. **Order Status Enhancement**
   - Introduce `PARTIALLY_SHIPPED` status for orders with split shipments.

---

### 🧩 Pseudo-code Example (Partial Shipment Handling)

```java
class Order {
    private List<OrderItem> items;
    private List<Shipment> shipments = new ArrayList<>();
    private OrderStatus status;

    public void createShipments() {
        List<OrderItem> readyToShip = new ArrayList<>();
        List<OrderItem> backOrdered = new ArrayList<>();

        for (OrderItem item : items) {
            if (item.getProduct().getStockCount() >= item.getQuantity()) {
                readyToShip.add(item);
            } else {
                backOrdered.add(item);
            }
        }

        if (!readyToShip.isEmpty()) {
            Shipment shipment = new Shipment(readyToShip);
            shipments.add(shipment);
            shipment.prepareShipment(); // Reserve stock, generate label, etc.
        }

        if (!backOrdered.isEmpty()) {
            // Notify customer about delay
            System.out.println("Some items are backordered and will ship later.");
        }

        updateOrderStatus(readyToShip, backOrdered);
    }

    private void updateOrderStatus(List<OrderItem> ready, List<OrderItem> backOrdered) {
        if (backOrdered.isEmpty()) {
            status = OrderStatus.SHIPPED;
        } else if (!ready.isEmpty()) {
            status = OrderStatus.PARTIALLY_SHIPPED;
        } else {
            status = OrderStatus.PENDING;
        }
    }
}
⚖️ Trade-offs

Advantages
✅ Faster delivery for available items
✅ Increased customer satisfaction
✅ Better stock utilization

Disadvantages
❌ Increased shipping costs (multiple packages)
❌ More complex order and inventory management

🚀 Scaling Considerations

Use event-driven architecture:

OrderCreated → triggers stock check service

ShipmentCreated → triggers notification and tracking updates

Allow multiple warehouse support — items may ship from different locations.

Integrate with carrier APIs for real-time tracking.
Implement a dashboard for customer service to manage partial shipments and customer inquiries.
---
## ❌ Design Improvement: Handling Order Cancellations and Refunds
## ❌ Design Improvement: Handling Order Cancellations and Refunds

**Problem:**  
Customers may want to cancel orders after placing them. The system must:
- Prevent cancellation if the order is already shipped or delivered.
- Process refunds.
- Restock items.
- Notify the customer.

---

### 💡 Proposed Solution

1. **Cancellation Rules**
   - Allow cancellation **only if the order has not been shipped**.
   - Prevent cancellation for shipped or delivered orders.

2. **Refund Processing**
   - Initiate refunds based on the **original payment method**.
   - Support full and partial refunds (if only part of the order is canceled).

3. **Inventory Update**
   - Restock items immediately after cancellation to make them available for new orders.

4. **Customer Notification**
   - Inform customers about cancellation confirmation and refund status.

---

### 🧩 Pseudo-code Example

```java
class Order {
    private OrderStatus status;
    private List<OrderItem> items;
    private Payment payment;

    public boolean cancelOrder() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            System.out.println("Order cannot be cancelled as it is already shipped or delivered.");
            return false;
        }

        status = OrderStatus.CANCELLED;
        refundPayment();
        restockItems();
        notifyCustomer();
        return true;
    }

    private void refundPayment() {
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            payment.processRefund();
            System.out.println("Refund processed successfully.");
        }
    }

    private void restockItems() {
        for (OrderItem item : items) {
            item.getProduct().increaseStock(item.getQuantity());
        }
    }

    private void notifyCustomer() {
        Notification notification = new Notification();
        notification.send("Your order has been cancelled and refund is processed.");
    }
}

abstract class Payment {
    protected PaymentStatus status;

    public PaymentStatus getStatus() {
        return status;
    }

    public abstract void processRefund();
}

class CreditCardPayment extends Payment {
    @Override
    public void processRefund() {
        // Logic to process refund via credit card API
        status = PaymentStatus.REFUNDED;
    }
}
⚖️ Trade-offs

Advantages
✅ Prevents invalid cancellations
✅ Automates refund and inventory updates
✅ Improves customer satisfaction

Disadvantages
❌ Adds complexity to payment integration
❌ Requires careful concurrency handling for stock updates

🚀 Scaling Considerations

Use event-driven workflow:
OrderCancelled → triggers refund service, inventory update, and notification service.

Support partial cancellations for multi-item orders.

Implement asynchronous refunds for payment providers that have long processing times.
Use a centralized cancellation service to handle complex business rules and maintain consistency across microservices.
Consider using a message queue (e.g., Kafka) to handle cancellation events and ensure reliability.
Implement a dashboard for customer service to manage cancellations and refunds.
---
## 👤 Design Improvement: Managing Guest Sessions
## 🛒 Design Improvement: Managing Guest User Sessions and Cart Persistence

**Problem:**  
Guest users may add items to their cart but lose them if they:  
- Switch devices  
- Close or refresh the browser  
- Remain inactive for too long  

This can cause frustration and lost sales.

---

### 💡 Proposed Solution

1. **Client-Side Persistence**
   - Use **cookies** or **localStorage** to store cart items locally.
   - Items are restored when the guest returns to the site on the same device.

2. **Server-Side Temporary Storage**
   - Store cart data against a **unique guest session ID** in the server.
   - Allows cart recovery even after device change if session is restored via a shared link or account linking.

3. **Cart Merge on Authentication**
   - When the guest logs in or registers, merge their guest cart with the authenticated user's existing cart.

---

### 🧩 Pseudo-code Example

```java
class ShoppingCart {
    private Map<Product, Integer> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void mergeCart(ShoppingCart guestCart) {
        for (Map.Entry<Product, Integer> entry : guestCart.items.entrySet()) {
            this.addItem(entry.getKey(), entry.getValue());
        }
    }
}

class GuestUser {
    private String sessionId;
    private ShoppingCart cart;

    public GuestUser(String sessionId) {
        this.sessionId = sessionId;
        this.cart = new ShoppingCart();
    }

    public ShoppingCart getCart() {
        return cart;
    }
}

class AuthenticatedUser extends Customer {
    private ShoppingCart cart;

    public void login(GuestUser guest) {
        this.cart.mergeCart(guest.getCart());
        // Optionally clear guest cart or expire guest session
    }
}
⚖️ Trade-offs

Advantages ✅

Preserves cart data across sessions.

Improves conversion rate by reducing cart abandonment.

Seamless experience when upgrading from guest to registered user.

Disadvantages ❌

Requires extra storage for guest carts.

Needs security measures for session hijacking prevention.

🚀 Scaling Considerations

Use Redis or other in-memory stores for fast guest cart lookups.

Implement cart expiration policies (e.g., auto-expire after 30 days).

Secure cookies with HTTPOnly and SameSite flags.

Limit cart size to prevent abuse.

🔐 Security Considerations

Encrypt server-side stored guest cart data.

Regenerate session IDs after login to avoid session fixation attacks.

Validate cart data before merging to prevent malicious item injection.
---
## 🌐 Design Improvement: Handling Network Failures During Payment or Shipment Creation
## 🌐 Design Improvement: Handling Network Failures During Payment or Shipment Creation

**Problem:**  
Network failures during payment processing or shipment creation can lead to:  
- Inconsistent order state (e.g., payment processed but shipment not created).  
- Duplicate charges or duplicate shipments when retrying without safeguards.  
- Poor user experience if no retry or recovery is attempted.  

---

### 💡 Proposed Solution

1. **Retry with Exponential Backoff**  
   - Automatically retry failed network calls for transient errors (e.g., timeout, temporary service unavailability).
   - Use **exponential backoff** to avoid overwhelming external services.

2. **Idempotent Operations**  
   - Ensure payment and shipment APIs are idempotent using unique transaction or shipment IDs.
   - Repeated calls with the same ID should not cause duplicate processing.

3. **Transaction Logging / State Machine**  
   - Track each stage (Payment Initiated → Payment Success → Shipment Created).
   - On failure, resume from the last successful state instead of starting over.

4. **User Notification and Recovery**  
   - Inform the customer if a payment or shipment fails.
   - Allow manual retry or cancellation.

---

### 🧩 Pseudo-code Example

```java
class Payment {
    private String transactionId;
    private PaymentStatus status;

    public boolean processPayment(double amount) {
        if (status == PaymentStatus.SUCCESS) {
            // Idempotent: already processed
            return true;
        }

        int retryCount = 0;
        while (retryCount < 3) {
            try {
                // Attempt payment via external API
                status = PaymentStatus.SUCCESS;
                return true;
            } catch (NetworkException e) {
                retryCount++;
                try {
                    Thread.sleep((long) Math.pow(2, retryCount) * 1000); // Exponential backoff
                } catch (InterruptedException ignored) {}
            }
        }

        status = PaymentStatus.FAILED;
        return false;
    }
}

class Shipment {
    private String shipmentId;
    private ShipmentStatus status;

    public boolean createShipment() {
        if (status == ShipmentStatus.CREATED) {
            // Idempotent: already created
            return true;
        }

        try {
            // Call shipment service API
            status = ShipmentStatus.CREATED;
            return true;
        } catch (NetworkException e) {
            status = ShipmentStatus.FAILED;
            return false;
        }
    }
}
⚖️ Trade-offs

Advantages ✅

Prevents duplicate payments or shipments.

Improves resilience to transient network issues.

Allows safe retry without breaking consistency.

Disadvantages ❌

Requires API design changes to support idempotency keys.

Increases complexity of state management.

🚀 Scaling Considerations

Use a distributed transaction log (e.g., Kafka, DynamoDB) to store operation states across services.

Implement dead-letter queues for failed operations requiring manual intervention.

Design APIs with idempotency keys and safe retry mechanisms by default.

🔐 Security Considerations

Encrypt stored transaction IDs.

Sign requests to prevent replay attacks when using idempotency keys.

Ensure retries cannot be exploited for repeated unauthorized shipments or charges.
---
## 🏷️ Design Improvement: Address Validation and Failed Deliveries
## 📦 Design Improvement: Validating Address Correctness and Handling Failed Deliveries

**Problem:**  
Incorrect shipping addresses or failed deliveries can lead to:  
- Delayed shipments  
- Increased operational costs  
- Poor customer experience  

---

### 💡 Proposed Solution

1. **Early Address Validation**  
   - Validate addresses at order placement using:
     - Regex rules for postal codes, city names, etc.
     - External address validation APIs (e.g., Google Maps API, USPS API).
   - Prompt users to confirm addresses before finalizing the order.

2. **Delivery Attempt Tracking**  
   - Maintain a counter for delivery attempts per shipment.
   - Retry deliveries up to a configurable maximum.

3. **Failed Delivery Handling**  
   - Notify customers immediately when delivery fails.  
   - Allow customers to:
     - Update delivery address.
     - Reschedule the delivery.

4. **Data-Driven Improvements**  
   - Log failed delivery reasons to optimize address validation rules.  

---

### 🧩 Pseudo-code Example

```java
class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;

    public boolean validate() {
        if (street == null || city == null || postalCode == null || country == null) {
            return false;
        }
        // Regex or external API validation could go here
        return true;
    }
}

class Shipment {
    private Address deliveryAddress;
    private int deliveryAttempts = 0;
    private final int maxDeliveryAttempts = 3;
    private ShipmentStatus status;

    public boolean attemptDelivery() {
        if (!deliveryAddress.validate()) {
            System.out.println("Invalid delivery address.");
            status = ShipmentStatus.FAILED;
            return false;
        }

        boolean success = performDelivery();

        if (success) {
            status = ShipmentStatus.DELIVERED;
            return true;
        } else {
            deliveryAttempts++;
            if (deliveryAttempts >= maxDeliveryAttempts) {
                status = ShipmentStatus.FAILED;
                notifyCustomerFailedDelivery();
            } else {
                status = ShipmentStatus.OUT_FOR_DELIVERY;
                // Schedule retry
            }
            return false;
        }
    }

    private boolean performDelivery() {
        // Integrate with courier API
        return false; // simulate failure
    }

    private void notifyCustomerFailedDelivery() {
        Notification
⚖️ Trade-offs

Advantages ✅

Catches invalid addresses early.

Reduces operational cost from failed deliveries.

Improves customer trust with proactive communication.

Disadvantages ❌

Address validation APIs may have latency or cost.

Over-restrictive regex rules may reject valid addresses.

🚀 Scaling Considerations

Use a queue-based retry system (e.g., Amazon SQS + Lambda) to handle failed deliveries asynchronously.

Integrate with multiple courier APIs for better delivery coverage.

Maintain a failed-delivery dashboard for operations teams.

🔐 Security Considerations

Encrypt address data at rest and in transit.

Limit address storage to necessary fields to comply with privacy laws (GDPR, CCPA).
Ensure address validation APIs are secure and do not expose sensitive user data.

---
## ⭐ Design Improvement: Implementing User Reviews and Ratings

**Problem:**  
While product reviews are mentioned in the system, there is no detailed design for:
- Handling **user-generated reviews**.
- Maintaining **average ratings** efficiently.
- **Moderating** inappropriate content.
- Preventing **duplicate reviews** from the same user.

---

### 💡 Proposed Solution

1. **User Permissions**
   - Only **authenticated** users can post reviews.
   - Prevent multiple reviews from the same user on the same product.
   - Allow update/delete operations by the same user.

2. **Ratings Management**
   - Maintain **average rating** per product for quick display.
   - Update the average when reviews are added, updated, or deleted.

3. **Moderation**
   - Introduce a review status: `PENDING`, `APPROVED`, `REJECTED`.
   - Admin dashboard for moderating flagged reviews.
   - Auto-flag reviews with profanity or spam content using NLP checks.

4. **Scalability**
   - Store reviews in a separate **reviews table** (e.g., `product_reviews`) with product ID index.
   - Use a **cache layer** (e.g., Redis) for average ratings to avoid recalculating on each page load.

---

### 🧩 Pseudo-code Example

```java
class Product {
    private List<ProductReview> reviews = new ArrayList<>();
    private double averageRating;

    public boolean addReview(ProductReview review) {
        for (ProductReview r : reviews) {
            if (r.getUserId().equals(review.getUserId())) {
                System.out.println("User has already reviewed this product.");
                return false;
            }
        }
        reviews.add(review);
        updateAverageRating();
        return true;
    }

    private void updateAverageRating() {
        int totalRating = 0;
        for (ProductReview r : reviews) {
            totalRating += r.getRating();
        }
        averageRating = reviews.isEmpty() ? 0 : (double) totalRating / reviews.size();
    }

    public double getAverageRating() {
        return averageRating;
    }
}

class ProductReview {
    private String userId;
    private String reviewText;
    private int rating; // 1 to 5
    private ReviewStatus status;

    public ProductReview(String userId, String reviewText, int rating) {
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.status = ReviewStatus.PENDING; // Default status
    }

    public String getUserId() { return userId; }
    public int getRating() { return rating; }
}
## 🎯 Design Improvement: Implementing Discounts and Wishlist Features

---

### **1️⃣ Discounts**

**Problem:**  
The platform needs to support **promotional offers** such as percentage discounts, fixed amount reductions, or buy-one-get-one deals to improve conversions and sales.

---

#### 💡 Proposed Solution
- **Discount Types**:  
  - Percentage Off (e.g., 10% discount)  
  - Fixed Amount Off (e.g., $20 off)  
  - Buy-One-Get-One (BOGO) or similar promotional offers
- **Application Levels**:  
  - **Product-level** discounts (e.g., “20% off this laptop”)  
  - **Cart-level** discounts (e.g., “$50 off orders above $500”)
- **Validation Rules**:  
  - Expiry date check  
  - Minimum purchase amount  
  - Product/category restrictions  

---

#### 🧩 Pseudo-code Example
```java
abstract class Discount {
    protected Date expiryDate;

    public boolean isValid() {
        return expiryDate == null || expiryDate.after(new Date());
    }

    public abstract double applyDiscount(double price);
}

class PercentageDiscount extends Discount {
    private double percentage;

    public PercentageDiscount(double percentage, Date expiryDate) {
        this.percentage = percentage;
        this.expiryDate = expiryDate;
    }

    @Override
    public double applyDiscount(double price) {
        if (!isValid()) return price;
        return price - (price * (percentage / 100));
    }
}

class ShoppingCart {
    private List<CartItem> items;
    private Discount discount;

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        if (discount != null) {
            total = discount.applyDiscount(total);
        }
        return total;
    }

    public void applyDiscount(Discount discount) {
        if (discount.isValid()) {
            this.discount = discount;
        }
    }
}
⚖️ Trade-offs

✅ Attracts more customers
✅ Increases average order value
❌ Adds complexity in validation logic
❌ Requires fraud-prevention checks for abuse

🚀 Scaling Considerations

Maintain discount rules in DB with caching for quick retrieval.

Use rules engine for complex promotional conditions.

Track discount usage for analytics.

2️⃣ Wishlist

Problem:
Customers need a way to save products for later without adding them to the cart, and receive availability or price drop notifications.

💡 Proposed Solution

Wishlist Operations:

Add/remove products

Persist wishlist for authenticated users across devices

Notify users on price changes or stock updates

Storage:

Separate wishlist table linked to user_id

Optionally store in cookies/local storage for guests

🧩 Pseudo-code Example
class Wishlist {
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public Set<Product> getProducts() {
        return products;
    }
}

class Customer {
    private Wishlist wishlist = new Wishlist();

    public Wishlist getWishlist() {
        return wishlist;
    }
}

⚖️ Trade-offs

✅ Improves user engagement and retention
✅ Enables targeted marketing (e.g., "Your saved item is now 20% off!")
❌ Additional storage and complexity for syncing across sessions

🚀 Scaling Considerations

Store wishlist in NoSQL DB for quick retrieval.

Push event notifications to users on relevant changes.

Implement bulk wishlist APIs for performance.