package moveValidation;

import PiecesAndBoard.Knight;
import PiecesAndBoard.Pawn;
import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

import java.awt.*;
import java.util.ArrayList;

public class MoveValidator {
    private Square[][] board;
    private Move move;

    public static final Color COLOR_OF_POSSIBLE_MOVES = new Color(255, 245, 150);
    public MoveValidator(Square[][] board, Move move){
        this.board = board;
        this.move = move;
    }

    private boolean inBounds(int x, int y){
        return  x>=0 && x<=7 && y>=0 && y<=7;
    }

    private boolean isMoveInList(ArrayList<Move> moves){
        if(moves == null){
            return false;
        }
        for (Move m: moves) {
            if(move.equalTo(m)){
                return true;
            }
        }
        return false;
    }
    public boolean isAbleToMove(){
        Piece pieceOnFrom = move.getSource().getPieceOccupying();
        Piece pieceOnTo = move.getDestination().getPieceOccupying();
        if(pieceOnFrom.getPieceColor() != move.getTurn()){
            return false;
        }
        if(pieceOnTo != null && pieceOnTo.getPieceColor() == pieceOnFrom.getPieceColor()){
            return false;
        }
        // here we make the move, and check if it exposes a check on our king.
        // if it does, we will undo the move. returning false here is temporary!!!
        move.makeMove();
        CheckValidator checkValidator = new CheckValidator(move.getTurn(), board);
        if(checkValidator.isInCheck()){
            move.undoMove();
            return false;
        }
        move.undoMove();
        return true;
    }

    private ArrayList<Move> getPawnLegalMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        Square sourceSquare = move.getSource();
        int xSource = sourceSquare.getXOnBoard();
        int ySource = sourceSquare.getYOnBoard();
        int turnMul = move.getTurn()? -1: 1;
        Pawn pawnOnSource = (Pawn)sourceSquare.getPieceOccupying();
        if(board[xSource][ySource+ turnMul].getPieceOccupying() == null) {
            moves.add(new Move(sourceSquare, board[xSource][ySource+ turnMul], move.getTurn()));
            if(pawnOnSource.isInStartingPosition() && board[xSource][ySource+2*turnMul].getPieceOccupying() == null){
                Move doubleMove = new Move(sourceSquare, board[xSource][ySource+2*turnMul], move.getTurn());
                moves.add(doubleMove);
            }
        }
        if(xSource!=7){
            Piece diagonalPiece1 = board[xSource+1][ySource+ turnMul].getPieceOccupying();
            if(diagonalPiece1 != null && diagonalPiece1.getPieceColor() != move.getTurn()){
                moves.add(new Move(sourceSquare,board[xSource+1][ySource+ turnMul],move.getTurn()));
            }
        }
        if(xSource != 0){
            Piece diagonalPiece2 = board[xSource-1][ySource+ turnMul].getPieceOccupying();
            if(diagonalPiece2 != null && diagonalPiece2.getPieceColor() != move.getTurn()){
                moves.add(new Move(sourceSquare,board[xSource-1][ySource+ turnMul],move.getTurn()));
            }
        }
        pawnOnSource.setIsInStartingPosition(false);
        return moves;
    }
    private ArrayList<Move> getKnightLegalMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        int xSource = move.getSource().getXOnBoard();
        int ySource = move.getSource().getYOnBoard();
        int xCurrent;
        int yCurrent;
        int[] oneOffset = new int[]{1,-1};
        int[] twoOffset = new int[]{2,-2};
        Piece pieceInSquaresChecked;
        Piece pieceOnSource = move.getSource().getPieceOccupying();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                xCurrent = xSource+oneOffset[i];
                yCurrent = ySource+twoOffset[j];
                if(inBounds(xCurrent,yCurrent)){
                    pieceInSquaresChecked = board[xCurrent][yCurrent].getPieceOccupying();
                    if(pieceInSquaresChecked == null ||
                            pieceInSquaresChecked.getPieceColor() != pieceOnSource.getPieceColor()){
                        moves.add(new Move(move.getSource(), board[xCurrent][yCurrent],move.getTurn()));
                    }
                }
                xCurrent = xSource+twoOffset[i];
                yCurrent = ySource+oneOffset[j];
                if(inBounds(xCurrent,yCurrent)){
                    pieceInSquaresChecked = board[xCurrent][yCurrent].getPieceOccupying();
                    if(pieceInSquaresChecked == null ||
                            pieceInSquaresChecked.getPieceColor() != pieceOnSource.getPieceColor()){
                        moves.add(new Move(move.getSource(), board[xCurrent][yCurrent],move.getTurn()));
                    }
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> getLegalMovesInDirection(int advanceX, int advanceY){
        ArrayList<Move> moves = new ArrayList<>();
        Square start = move.getSource();
        int xCurrent = start.getXOnBoard()+advanceX;
        int yCurrent = start.getYOnBoard()+advanceY;
        while(inBounds(xCurrent,yCurrent)){
            start = board[xCurrent][yCurrent];
            if(start.getPieceOccupying() == null || start.getPieceOccupying().getPieceColor() != move.getTurn()){
                moves.add(new Move(move.getSource(),start,move.getTurn()));
                xCurrent+=advanceX;
                yCurrent+=advanceY;
            }
            else{
                return moves;
            }
        }
        return moves;
    }
    private ArrayList<Move> getBishopLegalMoves(){
        ArrayList<Move> movesTopLeft = getLegalMovesInDirection(-1,-1);
        ArrayList<Move> movesTopRight = getLegalMovesInDirection(1,-1);
        ArrayList<Move> movesBottomRight = getLegalMovesInDirection(1,1);
        ArrayList<Move> movesBottomLeft = getLegalMovesInDirection(-1,1);
        movesTopLeft.addAll(movesTopRight);
        movesTopLeft.addAll(movesBottomRight);
        movesTopLeft.addAll(movesBottomLeft);
        return movesTopLeft;
    }

    private ArrayList<Move> getRookLegalMoves(){
        ArrayList<Move> movesTop = getLegalMovesInDirection(0,-1);
        ArrayList<Move> movesBottom = getLegalMovesInDirection(0,1);
        ArrayList<Move> movesRight = getLegalMovesInDirection(1,0);
        ArrayList<Move> movesLeft = getLegalMovesInDirection(-1,0);
        movesTop.addAll(movesBottom);
        movesTop.addAll(movesRight);
        movesTop.addAll(movesLeft);
        return movesTop;
    }

    private ArrayList<Move> getQueenLegalMoves() {
        ArrayList<Move> bishopDirections = getBishopLegalMoves();
        ArrayList<Move> rookDirections = getRookLegalMoves();
        bishopDirections.addAll(rookDirections);
        return bishopDirections;
    }

    private ArrayList<Move> getListOfPossibleMoves(char pieceType){
        ArrayList<Move> movesForPiece=null;
        if(move.getSource().getPieceOccupying().getPieceType() == 'p'){
            movesForPiece = getPawnLegalMoves();
        }
        else if(move.getSource().getPieceOccupying().getPieceType() == 'n'){
            movesForPiece = getKnightLegalMoves();
        }
        else if(move.getSource().getPieceOccupying().getPieceType() == 'b'){
            movesForPiece = getBishopLegalMoves();
        }
        else if(move.getSource().getPieceOccupying().getPieceType() == 'r'){
            movesForPiece = getRookLegalMoves();
        }
        else{
            movesForPiece = getQueenLegalMoves();
        }
        return movesForPiece;
    }
    public boolean isLegalMove(){
        if(!isAbleToMove()){
            return false;
        }
        boolean ret;
        char pieceType = move.getSource().getPieceOccupying().getPieceType();
        ArrayList<Move> movesForPiece = getListOfPossibleMoves(pieceType);
        ret= isMoveInList(movesForPiece);
        return ret;
    }
}
