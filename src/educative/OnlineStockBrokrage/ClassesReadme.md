# Online Stock Brokerage System

## Classes

- **Account (abstract)**
- **Admin** (extends Account)
- **Member** (extends Account)
- **Watchlist**
- **Stock**
- **StockInventory** (implements Search interface)
- **StockExchange**
- **StockPosition**
- **StockLot**
- **Order (abstract)**
    - MarketOrder (extends Order)
    - LimitOrder (extends Order)
    - StopLossOrder (extends Order)
    - StopLimitOrder (extends Order)
- **OrderPart**
- **DepositMoney**
- **WithdrawMoney**
- **TransferMoney (abstract)**
    - ElectronicBank (extends TransferMoney)
    - Wire (extends TransferMoney)
    - Check (extends TransferMoney)
- **Notification (abstract)**
    - SmsNotification (extends Notification)
    - EmailNotification (extends Notification)

---

## Relationships

### Association

- **One-way:**
    - StockInventory → Watchlist, StockExchange
    - Order → Stock, StockExchange, StockLot
    - Account → Order, DepositMoney, WithdrawMoney
    - StockPosition → Order

- **Two-way:**
    - Notification ↔ Order
    - Watchlist ↔ Account
    - StockPosition ↔ Account

### Composition

- Order **composed of** OrderPart
- StockInventory **composed of** Stock
- StockPosition **composed of** StockLot

### Inheritance

- Admin and Member **extend** Account
- MarketOrder, LimitOrder, StopLossOrder, StopLimitOrder **extend** Order
- ElectronicBank, Wire, Check **extend** TransferMoney
- SmsNotification and EmailNotification **extend** Notification
- StockInventory **implements** Search interface

---

## Explanation

- The **Account** class is abstract and represents user accounts, extended by **Admin** and **Member**.
- **Member** can perform stock operations like search, order placement, and managing watchlists.
- **Admin** manages user accounts (blocking, unblocking, resetting passwords).
- **StockInventory** keeps track of stocks and implements search functionality.
- **Order** and its subclasses represent different types of stock orders.
- **OrderPart** represents parts of an order (price, quantity, execution date).
- **TransferMoney** and its subclasses represent different payment methods.
- **Notification** and its subclasses handle sending notifications via SMS or email.
- The system uses various **associations** to model interactions, **compositions** to represent whole-part relationships, and **inheritance** for specialization.  
