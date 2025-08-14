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

### 💡 Handling Special Moves in Code

- **Castling**:  
  - Track whether the king and relevant rooks have moved (e.g., with boolean flags in the `King` and `Rook` classes or in the game state).
  - When validating a castling move, ensure neither piece has moved, the squares between them are empty, and the king does not pass through or end up in check.
  - Update both king and rook positions in a single atomic move.

  ```java
  // Example: Castling logic in ChessMoveController.java
  if (move.isCastlingMove()) {
      King king = (King) move.getPiece();
      if (!king.hasMoved() && !rook.hasMoved() && board.isPathClear(king, rook) && !board.isKingInCheckPath(king, rook)) {
          board.movePiece(king, castlingTargetBox);
          board.movePiece(rook, rookTargetBox);
          king.setHasMoved(true);
          rook.setHasMoved(true);
      }
  }
  ```

- **En Passant**:  
  - Record the last double-step pawn move (e.g., store the file and rank of the pawn eligible for en passant in the game state).
  - Allow en passant capture only on the immediate next move.
  - When performing en passant, remove the captured pawn from the board even though it is not on the destination square.

  ```java
  // Example: En Passant logic in ChessMoveController.java
  if (move.isEnPassant()) {
      Pawn pawn = (Pawn) move.getPiece();
      int direction = pawn.isWhite() ? -1 : 1;
      Box capturedPawnBox = board.getBox(move.getTo().getRow() + direction, move.getTo().getCol());
      board.removePiece(capturedPawnBox);
      board.movePiece(pawn, move.getTo());
  }
  // Track last double-step pawn move
  if (move.getPiece() instanceof Pawn && Math.abs(move.getFrom().getRow() - move.getTo().getRow()) == 2) {
      board.setEnPassantTarget(move.getTo());
  }
  ```

- **Promotion**:  
  - Detect when a pawn reaches the final rank.
  - Prompt the player (via UI or input) to select a promotion piece (Queen, Rook, Bishop, Knight).
  - Replace the pawn with the chosen piece in the board state.

  ```java
  // Example: Promotion logic in ChessMoveController.java
  if (move.getPiece() instanceof Pawn && (move.getTo().getRow() == 0 || move.getTo().getRow() == 7)) {
      // Assume player.choosePromotionPiece() returns a Piece instance (e.g., new Queen(pawn.isWhite()))
      Piece promotedPiece = player.choosePromotionPiece();
      board.setPiece(move.getTo(), promotedPiece);
  }
  ```

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
