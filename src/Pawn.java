import java.util.HashSet;

public class Pawn extends Piece {

    private String color;
    private int value = 1;
    private ChessPieceType chessPieceType = ChessPieceType.PAWN;
    private int moveCount;


    public Pawn(String c) {
        color = c; //black,white
        moveCount=0;
    }

    @Override
    public HashSet<Position> getAvailableMoves(Board board, int row, int col) {
        HashSet<Position> availablePositions = new HashSet<>();
        Position position;
        int direction; //Up = -1. Down = 1.

        if(color=="black") {
            direction=1;
        }
        else {
            direction=-1;
        }

        position= new Position(row+direction, col);
        if(board.getSpace(position)!=null) {
            if(legalMove(board, position) && board.getSpace(position).getPiece()==null) {
                availablePositions.add(position);
            }
        }


        for(int i=-1; i<=1; i+=2) {
            position= new Position(row+direction, col+i);
            if(legalMove(board, position) && board.getSpace(position).getPiece()!=null) {
                if(!board.getSpace(position).getPiece().getColor().equals(color)){
                    availablePositions.add(position);
                }
            }
        }

        if(moveCount==0){
            position= new Position(row+(direction*2), col);
            if(legalMove(board, position) && board.getSpace(position).getPiece()==null) {
                availablePositions.add(position);
            }
        }

        return availablePositions;
    }



    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public ChessPieceType getType() {
        return chessPieceType;
    }

    @Override
    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }
}
