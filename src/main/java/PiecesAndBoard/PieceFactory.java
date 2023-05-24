package PiecesAndBoard;

public class PieceFactory {
    public static Piece buildPiece(char pieceType, boolean pieceColor){
        if(pieceType == 'p'){
            return new Pawn(pieceColor);
        }
        if(pieceType == 'r'){
            return new Rook(pieceColor);
        }
        if(pieceType == 'n'){
            return new Knight(pieceColor);
        }
        if(pieceType == 'b'){
            return new Bishop(pieceColor);
        }
        if(pieceType == 'q'){
            return new Queen(pieceColor);
        }
        if(pieceType == 'k'){
            return new King(pieceColor);
        }

        System.out.println("Piece "+ pieceType + " is not recognized");
        return null;
    }
}
