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
