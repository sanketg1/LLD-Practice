package educative.JigsawPuzzle;

import java.util.*;

// Enum for piece edges
enum Edge {
    INDENTATION,
    EXTRUSION,
    FLAT
}

class Side {
    private Edge edge;

    public Side(Edge edge) {
        this.edge = edge;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }
}

class Piece {
    private List<Side> sides = Arrays.asList(new Side[4]); // 4 sides

    public Piece(Side top, Side right, Side bottom, Side left) {
        sides.set(0, top);
        sides.set(1, right);
        sides.set(2, bottom);
        sides.set(3, left);
    }

    public List<Side> getSides() {
        return sides;
    }

    // Checks if this piece can be a corner (2 flat sides)
    public boolean checkCorner() {
        int flatCount = 0;
        for (Side side : sides) {
            if (side.getEdge() == Edge.FLAT) flatCount++;
        }
        return flatCount == 2;
    }

    // Checks if this piece is an edge (1 flat side)
    public boolean checkEdge() {
        int flatCount = 0;
        for (Side side : sides) {
            if (side.getEdge() == Edge.FLAT) flatCount++;
        }
        return flatCount == 1;
    }

    // Middle piece has no flat sides
    public boolean checkMiddle() {
        for (Side side : sides) {
            if (side.getEdge() == Edge.FLAT) return false;
        }
        return true;
    }

    // Utility: rotate piece (to try matching positions)
    public void rotateClockwise() {
        Collections.rotate(sides, 1);
    }
}

class Puzzle {
    private List<List<Piece>> board;
    private List<Piece> free; // represents the currently free pieces (yet to be inserted in board)

    // Default constructor for a puzzle (say n x n)
    public Puzzle(int size, List<Piece> freePieces) {
        board = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Piece> row = new ArrayList<>(Collections.nCopies(size, null));
            board.add(row);
        }
        this.free = freePieces;
    }

    public List<List<Piece>> getBoard() {
        return board;
    }

    public List<Piece> getFreePieces() {
        return free;
    }

    // Insert piece at given row and col
    public void insertPiece(Piece piece, int row, int column) {
        if (row < 0 || column < 0 || row >= board.size() || column >= board.size()) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (board.get(row).get(column) != null) {
            throw new IllegalStateException("Position already occupied");
        }
        board.get(row).set(column, piece);
        free.remove(piece);
    }

    // Puzzle is a singleton class
    private static Puzzle puzzle = null;

    private Puzzle() {}

    // Access the singleton instance
    public static Puzzle getInstance() {
        if (puzzle == null) {
            puzzle = new Puzzle();
        }
        return puzzle;
    }

    // For singleton: init with free pieces
    public static void initialize(int size, List<Piece> freePieces) {
        puzzle = new Puzzle(size, freePieces);
    }
}

class PuzzleSolver {
    // Try to match pieces into the board
    public Puzzle matchPieces(Puzzle puzzle) {
        List<List<Piece>> board = puzzle.getBoard();
        int size = board.size();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.get(r).get(c) == null) {
                    for (Piece piece : new ArrayList<>(puzzle.getFreePieces())) {
                        for (int rotation = 0; rotation < 4; rotation++) {
                            if (fits(puzzle, piece, r, c)) {
                                puzzle.insertPiece(piece, r, c);
                                break;
                            }
                            piece.rotateClockwise();
                        }
                        if (board.get(r).get(c) != null) break; // placed successfully
                    }
                }
            }
        }
        return puzzle;
    }

    // Check if piece fits at board[r][c]
    private boolean fits(Puzzle puzzle, Piece piece, int r, int c) {
        List<List<Piece>> board = puzzle.getBoard();
        int size = board.size();

        List<Side> sides = piece.getSides();

        // Top neighbor
        if (r > 0 && board.get(r - 1).get(c) != null) {
            Edge neighbor = board.get(r - 1).get(c).getSides().get(2).getEdge();
            if (!matchEdges(sides.get(0).getEdge(), neighbor)) return false;
        } else if (r == 0 && sides.get(0).getEdge() != Edge.FLAT) return false;

        // Left neighbor
        if (c > 0 && board.get(r).get(c - 1) != null) {
            Edge neighbor = board.get(r).get(c - 1).getSides().get(1).getEdge();
            if (!matchEdges(sides.get(3).getEdge(), neighbor)) return false;
        } else if (c == 0 && sides.get(3).getEdge() != Edge.FLAT) return false;

        // Right border
        if (c == size - 1 && sides.get(1).getEdge() != Edge.FLAT) return false;

        // Bottom border
        if (r == size - 1 && sides.get(2).getEdge() != Edge.FLAT) return false;

        return true;
    }

    // Edge matching rule
    private boolean matchEdges(Edge e1, Edge e2) {
        if (e1 == Edge.FLAT || e2 == Edge.FLAT) return e1 == e2;
        return (e1 == Edge.INDENTATION && e2 == Edge.EXTRUSION) ||
                (e1 == Edge.EXTRUSION && e2 == Edge.INDENTATION);
    }
}

// ---------- DEMO MAIN ----------
class Main {
    public static void main(String[] args) {
        // Example 2x2 puzzle
        Piece p1 = new Piece(new Side(Edge.FLAT), new Side(Edge.EXTRUSION), new Side(Edge.INDENTATION), new Side(Edge.FLAT)); // corner
        Piece p2 = new Piece(new Side(Edge.FLAT), new Side(Edge.FLAT), new Side(Edge.EXTRUSION), new Side(Edge.INDENTATION)); // corner
        Piece p3 = new Piece(new Side(Edge.INDENTATION), new Side(Edge.EXTRUSION), new Side(Edge.FLAT), new Side(Edge.FLAT)); // corner
        Piece p4 = new Piece(new Side(Edge.EXTRUSION), new Side(Edge.FLAT), new Side(Edge.FLAT), new Side(Edge.INDENTATION)); // corner

        List<Piece> freePieces = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        Puzzle.initialize(2, freePieces);
        Puzzle puzzle = Puzzle.getInstance();

        PuzzleSolver solver = new PuzzleSolver();
        solver.matchPieces(puzzle);

        System.out.println("Puzzle Solved:");
        for (List<Piece> row : puzzle.getBoard()) {
            for (Piece piece : row) {
                System.out.print((piece != null ? "P " : ". "));
            }
            System.out.println();
        }
    }
}
