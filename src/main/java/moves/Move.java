package moves;

import PiecesAndBoard.Board;
import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

public class Move {
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
    public MoveType getMoveType() { return moveType; }
    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    // this function physically executes a move on the board.
    // the way the move is executed depends on the move's type. that's why
    // the if statements are for.
    public void makeMove() {
        if(moveType == MoveType.NORMAL || moveType == MoveType.DOUBLE_MOVE){
            makeNormalMove();
        }
        else if(moveType == MoveType.CASTLE){
            castle();
        }
        else if(moveType == MoveType.EN_PASSENT) {
            enPassent();
        }
    }

    public boolean isPromotingMove(){
        if(turn && destination.getYOnBoard()== 0){
            return true;
        }
        if(!turn && destination.getYOnBoard() == 7){
            return true;
        }
        return false;
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
        makeNormalMove();
        int xCorner = destination.getXOnBoard() > source.getXOnBoard()? 7:0;
        int xMovement = destination.getXOnBoard()>source.getXOnBoard()? 1:-1;
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
