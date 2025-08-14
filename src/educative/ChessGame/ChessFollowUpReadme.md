## 📋 Design Considerations

- **🏰 How would you handle special moves like castling, en passant, and promotion in your design?**

- **✅ How does your design ensure move validation and game state consistency?**

- **🌐 Can you explain how you would extend the design to support networked multiplayer?**

- **⏪ How would you implement undo/redo functionality for moves?**

- **🖥️ How do you manage the separation of concerns between the game logic and the UI?**

- **🏗️ What design patterns have you used and why?**





## ♟️ Special Moves Handling

- **🏰 Castling** – Validate king and rook positions, ensure no pieces in between, verify neither has moved before, and check that no square in the castling path is under attack.
- **⚡ En Passant** – Track the last double-step pawn move and allow capture only immediately after it, removing the captured pawn from the adjacent file.
- **🎖️ Promotion** – Detect when a pawn reaches the final rank and prompt for promotion to Queen, Rook, Bishop, or Knight.

---

## ✅ Move Validation & Game State Consistency

- **🛡️ Rule Enforcement** – All moves go through a centralized move validator that checks legality based on piece rules, turn order, and check/checkmate status.
- **📜 Immutable State Updates** – Maintain an immutable game state history to avoid corruption and simplify debugging.
- **🔄 Consistency Checks** – After each move, validate that the game state remains legal and synchronized between all players/clients.

---

## 🌐 Extending to Networked Multiplayer

- **🗄️ Client-Server Model** – Game logic resides on the server; clients send move requests and receive validated state updates.
- **📡 Real-Time Communication** – Use WebSockets for low-latency updates.
- **💾 Serialization** – Transmit game state in a compact format (e.g., JSON or FEN for chess positions) to keep messages lightweight.

---

## ⏪ Undo / Redo Functionality

- **🧾 Move History Stack** – Maintain two stacks: one for past moves (undo) and one for reverted moves (redo).
- **♻️ State Restoration** – Store board snapshots or apply inverse moves to revert state.
- **🧪 Safety Checks** – Ensure undo/redo maintains turn order and legality.

---

## 🖥️ Separation of Concerns (Game Logic vs UI)

- **🧠 Core Engine** – Handles rules, move generation, and validation without any UI dependencies.
- **🎨 UI Layer** – Renders board, pieces, and animations while making API calls to the core engine for interactions.
- **🔌 Communication** – Use event listeners or observer pattern to update UI based on engine events.

---

## 🏗️ Design Patterns Used

- **♻️ State Pattern** – Manage different game states (e.g., playing, paused, game over).
- **👀 Observer Pattern** – Notify UI or remote clients of changes in the game state.
- **🏭 Factory Pattern** – Create pieces dynamically based on type and color.
- **🛠️ Command Pattern** – Encapsulate moves, enabling undo/redo functionality.
- **📦 Singleton Pattern** – Ensure only one instance of the game manager exists.

---
