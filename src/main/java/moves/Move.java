package moves;

import PiecesAndBoard.Board;
import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

public class Move {
    private Square source; // where the piece is from
    private Square destination; // where the piece is going to go
    private boolean turn; // the side that makes that move
    private Piece pieceTaken; // save the piece that has been taken (if at all) as a result of this move
    private MoveType moveType; // the type of the move (Castle, En-Passent etc)
    public Move(Square source, Square destination, boolean turn){ // Constructor
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
    public MoveType getMoveType() { return moveType; }
    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public void makeMove() { // Physically making the move on the board
        if(moveType == MoveType.NORMAL || moveType == MoveType.DOUBLE_MOVE){
            makeNormalMove();
        }
        else if(moveType == MoveType.CASTLE){
            castle();
        }
        else{
            enPassent();
        }
    }
    private void makeNormalMove(){
        Piece save = source.getPieceOccupying(); // saving the piece on source square, because we need to save the
                                                 // piece for when we move it from the source square.

        if(destination.getPieceOccupying()!=null){ // if a piece has been captured as a result of the move, we need to
                                                   // save it, because if the move is illegal we will need to undo it.

            this.pieceTaken = destination.getPieceOccupying();
        }
        destination.setPieceOccupying(save); // transfer the piece from the source to the destination
        source.setPieceOccupying(null); // clearing the source square
    }
    private void castle(){
        makeNormalMove(); // that is for the movement of the king

        int xCorner = destination.getXOnBoard() > source.getXOnBoard()? 7:0; // getting the corner the rook is at.
        int xMovement = destination.getXOnBoard()>source.getXOnBoard()? 1:-1; // this is so we know where the rook has
                                                                              // to be in relation to the king.
        Move rookMovement = new Move(Board.board[xCorner][source.getYOnBoard()],
                                     Board.board[source.getXOnBoard()+xMovement][source.getYOnBoard()],
                                     turn);
        rookMovement.makeNormalMove();
    }
    private void enPassent(){
        makeNormalMove();
        Square enemyPawn = Board.board[destination.getXOnBoard()][source.getYOnBoard()];
        this.pieceTaken = enemyPawn.getPieceOccupying();
        enemyPawn.setPieceOccupying(null);
    }
    public void undoMove(){
        Piece save = destination.getPieceOccupying();
        source.setPieceOccupying(save);
        if(moveType == MoveType.NORMAL || moveType == MoveType.DOUBLE_MOVE){
            destination.setPieceOccupying(pieceTaken);
        }
        else if(moveType == MoveType.CASTLE){
            destination.setPieceOccupying(null);
            undoRookMotion();
        }
        else{
            destination.setPieceOccupying(null);
            Board.board[destination.getXOnBoard()][source.getYOnBoard()].setPieceOccupying(pieceTaken);
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
