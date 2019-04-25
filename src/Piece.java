import java.io.Serializable;
import java.util.HashSet;

/**
 * All chess pieces implement this interface.
 * Provides methods to make their usage more consistent across the board.
 */
public abstract class Piece implements Serializable {

    /**
     * Used to identify what kind of piece it is (for displaying the piece, for example)
     * Possibly could be replaced with HashMap of String values, or a set of instanceof calls.
     */
    public enum ChessPieceType {
        PAWN, BISHOP, KING, QUEEN, ROOK, KNIGHT
    }

    /**
     * Returns the set of all possible, legal Positions that a piece at the given row and col can go to.
     *
     * @param row row of the piece in the instance of Board
     * @param col column of the piece in the instance of Board
     * @return set of Positions that the piece is able to move to
     */
    public abstract HashSet<Position> getAvailableMoves(Board board, int row, int col);

    HashSet<Position> getAvailableMoves(Board board, Position position) {
        return getAvailableMoves(board, position.row, position.col);
    }

    HashSet<Position> getAvailableDiagonalMoves(Board board, int row, int col) {
        HashSet<Position> availablePositions = new HashSet<>();

        probeByDirectionVector(board, row, col, new Position(1, 1), availablePositions);

        probeByDirectionVector(board, row, col, new Position(-1, 1), availablePositions);

        probeByDirectionVector(board, row, col, new Position(1, -1), availablePositions);

        probeByDirectionVector(board, row, col, new Position(-1, -1), availablePositions);

        return availablePositions;
    }

    HashSet<Position> getAvailableHorizontalVerticalMoves(Board board, int row, int col) {
        HashSet<Position> availablePositions = new HashSet<>();

        probeByDirectionVector(board, row, col, Position.i, availablePositions);

        probeByDirectionVector(board, row, col, Position.i.scalarMult(-1), availablePositions);

        probeByDirectionVector(board, row, col, Position.j, availablePositions);

        probeByDirectionVector(board, row, col, Position.j.scalarMult(-1), availablePositions);

        return availablePositions;
    }

    /**
     * Uses a 2x1 direction vector (an instance of Position class) and probes in that direction by scaling in that
     * direction by scalar multiples of the vector.
     */
    private void probeByDirectionVector(Board board, int row, int col, Position directionVector, HashSet<Position> availablePositions) {
        probeByDirectionVector(board, row, col, directionVector, availablePositions, board.getRows(), board.getCols());
    }

    void probeByDirectionVector(Board board, int row, int col, Position directionVector,
                                HashSet<Position> availablePositions, int maxRowScale, int maxColScale) {
        boolean encounteredIllegalMove = false;
        for (int i = 1, j = 1; 0 <= row + (directionVector.row * i) && row + (directionVector.row * i) < board.getRows()
                && 0 <= col + (directionVector.col * j) && col + (directionVector.col * j) < board.getCols() &&
                i <= maxRowScale && j <= maxColScale && !encounteredIllegalMove; i++, j++) {

            Position newPosition = new Position(row + (directionVector.row * i), col + (directionVector.col * j));

            if (isASpace(board, newPosition)) {
                if (hasAPiece(board, newPosition)) {
                    encounteredIllegalMove = true;
                    if (colorsAreDifferent(board, newPosition)) {
                        availablePositions.add(newPosition);
                    }
                }
            } else {
                encounteredIllegalMove = true;
            }
//            encounteredIllegalMove = !legalMove(board, newPosition);

            if (!encounteredIllegalMove) {
                availablePositions.add(newPosition);
            }
        }
    }

    /**
     * Tells the piece to attempt to move from a[currentRow][currentCol] to a[newRow][newCol].
     *
     * @param board   instance of the board
     * @param pBefore position where the piece currently is located.
     * @param pAfter  position where the piece currently is location.
     * @return A Status indicating whether the move was successful, or not.
     */
    Status move(Board board, Position pBefore, Position pAfter) {
        if (getAvailableMoves(board, pBefore).contains(pAfter)) {
            board.getSpace(pAfter).setPiece(this);
            board.getSpace(pBefore).setPiece(null);

            return Status.SuccessfulMove(getType(), pBefore, pAfter);
        } else {
            return Status.FailedMove();
        }
    }

    /**
     * The AI is expected to make decisions based on the idea that different pieces are of different worth.
     * The value that this method returns determines the Piece's worth to the AI.
     */
    @SuppressWarnings("unused")
    public abstract int getValue();

    /**
     * Pieces are associated with a color (in a standard game, black and white). This returns the color associated with
     * this Piece.
     *
     * @return String representing color.
     */
    public abstract String getColor();

    /**
     * Identifies what kind of piece this is (King, Queen, Knight, etc.)
     *
     * @return type as a ChessPieceType (from Piece.ChessPieceType enum)
     */
    public abstract ChessPieceType getType();

    /**
     * @return the number of times this specific piece has moved.
     */
    public abstract int getMoveCount();

    public abstract void setMoveCount(int moveCount);

    /**
     * Helper method which indicates whether the given space is withing the bounds of the board and does not contain a
     * piece of the same color as this one.
     *
     * @param board    "Board" in which to perform this check
     * @param position Position of the space upon which to perform this check
     * @return true if this (Piece) is able to be moved to this location. Else false.
     */
    boolean legalMove(Board board, Position position) {
        if (board.positionIsWithinBounds(position) && isASpace(board, position)) {
            return (!hasAPiece(board, position) || colorsAreDifferent(board, position));
        }

        return false;
    }

    static boolean isASpace(Board b, Position p) {
        return b.getSpace(p) != null;
    }

    static boolean hasAPiece(Board b, Position p) {
        return b.getSpace(p).getPiece() != null;
    }

    private boolean colorsAreDifferent(Board board, Position position) {
        return !getColor().equals(board.getSpace(position).getPiece().getColor());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece p = (Piece) o;
        return getType() == p.getType()
                && getColor().equals(p.getColor());
    }
}
