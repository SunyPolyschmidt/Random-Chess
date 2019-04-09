import java.util.Random;

public class SwitchPieceEffect extends Effect {

    private EffectType effectType = EffectType.SwitchPiece;

    public void doEffect(Space s){
        if(s.getPiece().getType()!=Piece.ChessPieceType.KING){
            String color = s.getPiece().getColor();
            Random randomNum = new Random();
            Piece newPiece;
            int randInt = randomNum.nextInt(5);
            switch (randInt){
                case 0:
                    newPiece = new Pawn(color);
                    break;
                case 1:
                    newPiece = new Rook(color);
                    break;
                case 2:
                    newPiece = new Knight(color);
                    break;
                case 3:
                    newPiece = new Bishop(color);
                    break;
                case 4:
                    newPiece = new Queen(color);
                    break;
                default:
                    newPiece = s.getPiece();
                    break;
            }
            s.setPiece(newPiece);
        }

    }

    public EffectType getType(){
        return effectType;
    }
}
