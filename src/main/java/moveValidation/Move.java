package moveValidation;

import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

import java.awt.*;

public class Move {
    private static final Color MOVE_HIGHLIGHT_COLOR = new Color(71, 192, 217);
    private Square source;
    private Square destination;
    private boolean turn;
    private Piece pieceTaken;

    public Move(Square from, Square to, boolean turn){
        this.source = from;
        this.destination = to;
        this.turn = turn;
    }

    public Square getSource() {
        return source;
    }

    public void setFrom(Square from) {
        this.source = from;
    }

    public Square getDestination() {
        return destination;
    }
    public void setTo(Square to) {
        this.destination = to;
    }
    public boolean getTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void makeMove(){
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
