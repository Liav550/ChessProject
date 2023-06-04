package moveValidation;

import PiecesAndBoard.Board;
import PiecesAndBoard.Pawn;
import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

import java.awt.*;

public class Move {
    private static final Color MOVE_HIGHLIGHT_COLOR = new Color(71, 192, 217);
    private Square source;
    private Square destination;
    private boolean turn;
    private Piece pieceTaken;
    public Move(Square source, Square destination, boolean turn){
        this.source = source;
        this.destination = destination;
        this.turn = turn;
    }
    public Square getSource() {
        return source;
    }
    public Square getDestination() {
        return destination;
    }
    public boolean getTurn() {
        return turn;
    }

    public void makeMove() {
        Piece save = source.getPieceOccupying();
        this.pieceTaken = destination.getPieceOccupying();
        destination.setPieceOccupying(save);
        source.setPieceOccupying(null);
    }
    public void undoMove(){
        Piece save = destination.getPieceOccupying();
        source.setPieceOccupying(save);
        destination.setPieceOccupying(pieceTaken);
    }
    public boolean equalTo(Move other){
        return (this.source == other.source && this.destination == other.destination);
    }
}
