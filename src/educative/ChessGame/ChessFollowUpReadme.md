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

To ensure robust move validation and maintain game state consistency, my design follows these principles (referencing actual code):

1. **Centralized Move Validation**: All moves are validated through the `ChessMoveController.validateMove` method, which delegates to each piece's `canMove` method for rule enforcement.

   ```java
   // ChessMoveController.java
   public boolean validateMove(Piece piece, Chessboard board, Box start, Box end) {
       return piece != null && piece.canMove(board, start, end);
   }
   ```

2. **Rule Enforcement**: Each `Piece` subclass (e.g., `King`, `Rook`, `Pawn`) implements its own `canMove` method, encapsulating movement rules. The `Chessboard.isPathBlocked` method is used for sliding pieces.

3. **Game State Consistency**: The `Chessboard` class manages the board state with a 2D array of `Box` objects, each holding a reference to a `Piece` or null. The board can be reset to a valid initial state with `resetBoard()`.

4. **Turn Management & King Safety**: (Recommended for SDE 2 robustness) Ensure only the correct player's pieces can move on their turn, and after each move, check that the player's own king is not in check.

5. **Move History & Atomic Updates**: (Recommended) Maintain a move history for undo/redo and debugging. Apply moves atomically to prevent partial updates.

**Example: Move Validation in Game Loop**

```java
// In your game loop or controller
if (controller.validateMove(piece, board, start, end) && isCorrectTurn(piece)) {
    // Optionally: simulate move, check king safety
    // Apply move, update history, switch turn
}
```

This approach ensures that all chess rules are enforced and the game state remains consistent after every move, matching the expectations for a robust, production-quality chess engine.

---

## 🌐 Extending to Networked Multiplayer

To extend this design for networked multiplayer, I would:

1. **Introduce a Server-Client Architecture:**
   - Create a `GameServer` class to host the game logic and maintain the authoritative game state (e.g., Chessboard, Player turns, Move history).
   - Each remote player runs a `GameClient` that connects to the server.

2. **Define a Communication Protocol:**
   - Use sockets (e.g., Java Sockets, WebSockets) for real-time communication.
   - Define message types for moves, game state updates, chat, etc.
   - Serialize moves and game state (e.g., using JSON or a custom protocol).

3. **Integrate Move Validation:**
   - The server receives move requests from clients.
   - The server uses `ChessMoveController.validateMove` to validate moves before applying them.
   - Only valid moves update the game state; invalid moves are rejected with an error message.

4. **Synchronize Game State:**
   - After each valid move, the server broadcasts the updated game state to all clients.
   - Clients update their local view based on server messages.

5. **Handle Disconnections and Edge Cases:**
   - Implement reconnection logic, timeouts, and game resumption.

**Example: Server-side Move Handling Outline**

```java
// GameServer.java (outline)
public class GameServer {
    private Chessboard board;
    private ChessMoveController controller;
    // ... player management, networking setup ...

    public void onMoveReceived(Move move, Player player) {
        if (controller.validateMove(move.getPiece(), board, move.getStart(), move.getEnd()) && player.isTurn()) {
            // Apply move, update state, switch turn
            // Broadcast new state to all clients
        } else {
            // Send error to client
        }
    }
}
```

This approach ensures that the server is the single source of truth, all moves are validated centrally, and all clients remain in sync, providing a robust and fair multiplayer experience.

---

## ⏪ Undo / Redo Functionality

To implement undo/redo functionality for moves:

1. **Maintain Two Stacks:**
   - Use an `undoStack` to store executed moves.
   - Use a `redoStack` to store moves that have been undone.

2. **On Move Execution:**
   - Push the move onto the `undoStack`.
   - Clear the `redoStack` (since redo is only valid after an undo).

3. **Undo Operation:**
   - Pop the last move from the `undoStack`.
   - Reverse the move (restore the previous board state and piece positions).
   - Push the move onto the `redoStack`.

4. **Redo Operation:**
   - Pop the last move from the `redoStack`.
   - Re-apply the move to the board.
   - Push the move back onto the `undoStack`.

5. **State Restoration:**
   - Each move should store enough information to reverse itself (e.g., start/end positions, captured piece, promotion info).

**Example: Undo/Redo Logic Outline**

```java
// In ChessGame.java or ChessMoveController.java
private Stack<Move> undoStack = new Stack<>();
private Stack<Move> redoStack = new Stack<>();

public void makeMove(Move move) {
    // ...validate and apply move...
    undoStack.push(move);
    redoStack.clear();
}

public void undo() {
    if (!undoStack.isEmpty()) {
        Move lastMove = undoStack.pop();
        lastMove.undo(board); // Implement undo logic in Move
        redoStack.push(lastMove);
    }
}

public void redo() {
    if (!redoStack.isEmpty()) {
        Move move = redoStack.pop();
        move.apply(board); // Implement apply logic in Move
        undoStack.push(move);
    }
}
```

This approach ensures that undo/redo operations are efficient, reliable, and maintain game state consistency, as expected in a production-quality chess engine.

---

## 🖥️ Separation of Concerns (Game Logic vs UI)

To ensure a clean separation of concerns between the game logic and the UI, my design follows these principles (as reflected in your code):

1. **Layered Architecture:**
   - The core game logic (rules, move validation, board state) is encapsulated in classes like `Chessboard`, `Piece`, `Move`, and `ChessMoveController`.
   - The UI and presentation logic are handled separately in `ChessGameView`.

2. **Responsibilities:**
   - **Game Logic Layer:**
     - Handles all chess rules, move validation, and state changes.
     - Classes: `Chessboard`, `Piece` (and its subclasses), `Move`, `ChessMoveController`, etc.
   - **UI Layer:**
     - Responsible for displaying the board and moves to the user.
     - Class: `ChessGameView` (e.g., `showBoard(Chessboard board)`, `showMove(Move move)`).

3. **Loose Coupling:**
   - The UI layer calls methods on the game logic to get the current state (e.g., `board.printBoard()`), but the game logic does not depend on or call UI code.
   - This ensures that the core engine can be tested or reused independently of the UI.

4. **Example from Your Code:**

```java
// ChessGameView.java
public class ChessGameView {
    public void showBoard(Chessboard board) {
        board.printBoard();
    }
    public void showMove(Move move) {
        System.out.println(move);
    }
}
```

5. **Benefits:**
   - This separation makes your codebase easier to maintain, test, and extend (e.g., swapping out the console UI for a GUI or web interface without changing the game logic).

---

## 🏗️ Design Patterns Used

1. **State Pattern**
   - **Where:** Used to manage different game states (e.g., playing, paused, game over).
   - **Why:** Allows the game to transition cleanly between states, encapsulating state-specific behavior and making the codebase easier to extend and maintain.

2. **Observer Pattern**
   - **Where:** Used to notify the UI or remote clients of changes in the game state.
   - **Why:** Decouples the game logic from the UI, so the UI can react to changes (like moves or game over) without the core logic knowing about the UI implementation.

3. **Factory Pattern**
   - **Where:** Used for creating chess pieces dynamically based on type and color (e.g., in `Chessboard.resetBoard()`).
   - **Why:** Centralizes piece creation, making it easier to manage, extend, or modify how pieces are instantiated.

4. **Command Pattern**
   - **Where:** Each move is encapsulated as a `Move` object, supporting undo/redo functionality.
   - **Why:** Allows moves to be executed, undone, and redone in a uniform way, and supports features like move history and replay.

5. **Singleton Pattern**
   - **Where:** Used to ensure only one instance of the game manager exists (e.g., a `ChessGame` or `GameManager` class).
   - **Why:** Prevents conflicting game states and ensures a single source of truth for the game session.

---

---
## 🔑 Why Use UUID for IDs in the Chess Game Design?

- **🌍 Uniqueness Across Systems**  
  UUIDs (Universally Unique Identifiers) guarantee uniqueness without a central authority, making them ideal if the game state or moves might be shared or synchronized across distributed systems or multiple clients.

- **🛡️ Collision Avoidance**  
  Unlike simple integer IDs, UUIDs minimize the risk of collisions — especially important when multiple game instances or players generate IDs independently.

- **📈 Scalability**  
  UUIDs scale well as the system grows, such as when adding networked multiplayer or persistent game storage.

- **🔒 Security**  
  UUIDs are harder to guess or predict compared to sequential IDs, which can help prevent certain types of exploits or unauthorized actions.
 
