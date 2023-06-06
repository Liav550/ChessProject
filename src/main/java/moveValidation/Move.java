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
    private MoveType moveType;
    public Move(Square source, Square destination, boolean turn){
        this.source = source;
        this.destination = destination;
        this.turn = turn;
        this.moveType = MoveType.NORMAL;
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

    public MoveType getMoveType() {
        return moveType;
    }
    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public void makeMove() {
        if(moveType == MoveType.NORMAL){
            makeNormalMove();
        }
        else if(moveType == MoveType.CASTLE){
            castle();
        }
    }
    private void makeNormalMove(){
        Piece save = source.getPieceOccupying();
        if(destination.getPieceOccupying()!=null){
            this.pieceTaken = destination.getPieceOccupying();
        }
        destination.setPieceOccupying(save);
        source.setPieceOccupying(null);
    }
    private void castle(){
        // king movement
        makeNormalMove();

        // rook movement
        int xCorner = destination.getXOnBoard() > source.getXOnBoard()? 7:0;
        int xMovement = destination.getXOnBoard()>source.getXOnBoard()?1:-1;
        Move rookMovement = new Move(Board.board[xCorner][source.getYOnBoard()],
                                     Board.board[source.getXOnBoard()+xMovement][source.getYOnBoard()],
                                     turn);
        rookMovement.makeNormalMove();
    }
    private void enPassent(){

    }
    public void undoMove(){
        Piece save = destination.getPieceOccupying();
        source.setPieceOccupying(save);
        destination.setPieceOccupying(pieceTaken);
        if(moveType == MoveType.CASTLE){
            undoRookMotion();
        }
    }

    private void undoRookMotion(){
        int directionToCorner = destination.getXOnBoard()>source.getXOnBoard()? 7:0;
        int directionOfMovement = destination.getXOnBoard()>source.getXOnBoard()? 1:-1;
        Move returnRook = new Move(Board.board[source.getXOnBoard()+directionOfMovement][source.getYOnBoard()],
                                   Board.board[directionToCorner][source.getYOnBoard()],
                                   turn);
        returnRook.makeNormalMove();
    }
    public boolean equalTo(Move other){
        return (this.source == other.source && this.destination == other.destination);
    }
}
