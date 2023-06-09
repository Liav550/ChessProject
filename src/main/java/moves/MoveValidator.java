package moves;

import PiecesAndBoard.*;

import java.awt.*;
import java.util.ArrayList;

public class MoveValidator {
    private Move move;

    public MoveValidator(Move move) {
        this.move = move;
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    private boolean isMoveInList(ArrayList<Move> moves) {
        if (moves == null) {
            return false;
        }
        boolean ret= false;
        for (Move m : moves) {
            if (move.equalTo(m)) {
                if (m.getMoveType() == MoveType.CASTLE) {
                    move.setMoveType(MoveType.CASTLE);
                }
                else if (m.getMoveType() == MoveType.EN_PASSENT) {
                    move.setMoveType(MoveType.EN_PASSENT);
                }
                else if(m.getMoveType() == MoveType.DOUBLE_MOVE){
                    move.setMoveType(MoveType.DOUBLE_MOVE);
                    ((Pawn)move.getSource().getPieceOccupying()).setHasDoubleMoved(true);
                }
                ret = true;
                break;
            }
        }
        return ret;
    }

    public boolean isAbleToMove() {
        Piece pieceOnFrom = move.getSource().getPieceOccupying();
        Piece pieceOnTo = move.getDestination().getPieceOccupying();
        if (pieceOnFrom.getPieceColor() != move.getTurn()) {
            return false;
        }
        if (pieceOnTo != null && pieceOnTo.getPieceColor() == pieceOnFrom.getPieceColor()) {
            return false;
        }
        // here we make the move, and check if it exposes a check on our king.
        // if it does, we will undo the move.
        move.makeMove();
        CheckValidator checkValidator = new CheckValidator(move.getTurn());
        if (checkValidator.isInCheck()) {
            move.undoMove();
            return false;
        }
        move.undoMove();
        return true;
    }

    private ArrayList<Move> deleteIfIllegal(ArrayList<Move> moves) {
        if (moves == null) {
            return null;
        }
        MoveValidator validatePossibleMoves;
        ArrayList<Move> ret = new ArrayList<>();
        for (Move m : moves) {
            validatePossibleMoves = new MoveValidator(m);
            if (validatePossibleMoves.isAbleToMove()) {
                ret.add(m);
            }
        }
        return ret;
    }

    private boolean isEnPassentLegal(int direction){
        int perspective = move.getTurn()? -1: 1;
        if(!inBounds(move.getSource().getXOnBoard()+direction, move.getSource().getYOnBoard()+perspective)){
            return false;
        }
        Square squareOfEnemyPawn = Board.board[move.getSource().getXOnBoard() + direction][move.getSource().getYOnBoard()];
        if(squareOfEnemyPawn.getPieceOccupying() == null){
            return false;
        }
        if(squareOfEnemyPawn.getPieceOccupying().getPieceColor() == move.getTurn() ||
           squareOfEnemyPawn.getPieceOccupying().getPieceType() != 'p'){
            return false;
        }
        Pawn enemyPawn = (Pawn) squareOfEnemyPawn.getPieceOccupying();
        if(enemyPawn.getTurnsSinceDoubleMove() != 1){
            return false;
        }

        Square takeSpot = Board.board[move.getSource().getXOnBoard() + direction][move.getSource().getYOnBoard()+perspective];
        if(takeSpot.getPieceOccupying() != null){
            return false;
        }
        return true;
    }
    private ArrayList<Move> getPawnLegalMoves() {
        ArrayList<Move> moves = new ArrayList<>();
        Square sourceSquare = move.getSource();
        int xSource = sourceSquare.getXOnBoard();
        int ySource = sourceSquare.getYOnBoard();
        int turnMul = move.getTurn() ? -1 : 1;
        Move moveToAdd;
        Pawn pawnOnSource = (Pawn) sourceSquare.getPieceOccupying();
        if (Board.board[xSource][ySource + turnMul].getPieceOccupying() == null) {
            moveToAdd = new Move(sourceSquare, Board.board[xSource][ySource + turnMul], move.getTurn());
            moves.add(moveToAdd);
            if (pawnOnSource.isInStartingPosition() && Board.board[xSource][ySource + 2 * turnMul].getPieceOccupying() == null) {
                moveToAdd = new Move(sourceSquare, Board.board[xSource][ySource + 2 * turnMul], move.getTurn());
                moveToAdd.setMoveType(MoveType.DOUBLE_MOVE);
                moves.add(moveToAdd);
            }
        }
        if (xSource != 7) {
            Piece diagonalPiece1 = Board.board[xSource + 1][ySource + turnMul].getPieceOccupying();
            if (diagonalPiece1 != null && diagonalPiece1.getPieceColor() != move.getTurn()) {
                moveToAdd = new Move(sourceSquare, Board.board[xSource + 1][ySource + turnMul], move.getTurn());
                moves.add(moveToAdd);
            }
        }
        if (xSource != 0) {
            Piece diagonalPiece2 = Board.board[xSource - 1][ySource + turnMul].getPieceOccupying();
            if (diagonalPiece2 != null && diagonalPiece2.getPieceColor() != move.getTurn()) {
                moveToAdd = new Move(sourceSquare, Board.board[xSource - 1][ySource + turnMul], move.getTurn());
                moves.add(moveToAdd);
            }
        }
        if(isEnPassentLegal(1)){
            moveToAdd = new Move(move.getSource(),
                                 Board.board[move.getSource().getXOnBoard() + 1][move.getSource().getYOnBoard()+turnMul],
                    move.getTurn());
            moveToAdd.setMoveType(MoveType.EN_PASSENT);
            moves.add(moveToAdd);
        }
        else if(isEnPassentLegal(-1)){
            moveToAdd = new Move(move.getSource(),
                    Board.board[move.getSource().getXOnBoard() - 1][move.getSource().getYOnBoard()+turnMul],
                    move.getTurn());
            moveToAdd.setMoveType(MoveType.EN_PASSENT);
            moves.add(moveToAdd);
        }
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
                    pieceInSquaresChecked = Board.board[xCurrent][yCurrent].getPieceOccupying();
                    if(pieceInSquaresChecked == null ||
                            pieceInSquaresChecked.getPieceColor() != pieceOnSource.getPieceColor()){
                        moves.add(new Move(move.getSource(), Board.board[xCurrent][yCurrent],move.getTurn()));
                    }
                }
                xCurrent = xSource+twoOffset[i];
                yCurrent = ySource+oneOffset[j];
                if(inBounds(xCurrent,yCurrent)){
                    pieceInSquaresChecked = Board.board[xCurrent][yCurrent].getPieceOccupying();
                    if(pieceInSquaresChecked == null ||
                            pieceInSquaresChecked.getPieceColor() != pieceOnSource.getPieceColor()){
                        moves.add(new Move(move.getSource(), Board.board[xCurrent][yCurrent],move.getTurn()));
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
            start = Board.board[xCurrent][yCurrent];
            if(start.getPieceOccupying() == null){
                moves.add(new Move(move.getSource(),start,move.getTurn()));
                xCurrent+=advanceX;
                yCurrent+=advanceY;
            }
            else{
                if(start.getPieceOccupying().getPieceColor() != move.getTurn()){
                    moves.add(new Move(move.getSource(),start,move.getTurn()));
                }
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

    private boolean isCastleLegal(int direction){
        CheckValidator checkValidator = new CheckValidator(move.getTurn());
        if(checkValidator.isInCheck()){
            return false;
        }
        if(((King)move.getSource().getPieceOccupying()).getHasMoved()){
            return false;
        }
        int xSource = move.getSource().getXOnBoard();
        int ySource = move.getSource().getYOnBoard();
        if(Board.board[xSource+direction][ySource].getPieceOccupying() != null ||
                Board.board[xSource+2*direction][ySource].getPieceOccupying() != null){
            return false;
        }
        Move oneStepToDirection = new Move(move.getSource(), Board.board[xSource+direction][ySource], move.getTurn());
        oneStepToDirection.makeMove();
        checkValidator = new CheckValidator(move.getTurn());
        if(checkValidator.isInCheck()){
            oneStepToDirection.undoMove();
            return false;
        }
        oneStepToDirection.undoMove();
        int xCorner = direction==1? 7: 0;
        Piece pieceOnCorner = Board.board[xCorner][ySource].getPieceOccupying();
        if(pieceOnCorner == null){
            return false;
        }
        if(pieceOnCorner.getPieceType() != 'r'){
            return false;
        }
        if(pieceOnCorner.getPieceColor() != move.getTurn()){
            return false;
        }
        Rook rookOnCorner = (Rook) pieceOnCorner;
        if(rookOnCorner.getHasMoved()){
            return false;
        }
        return true;
    }

    private ArrayList<Move> getKingLegalMoves(){
        ArrayList<Move> moves = new ArrayList<>();
        int[] directions = new int[]{1,0,-1};
        Square squareChecked;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i==1 && j==1){
                    continue;
                }
                if(inBounds(move.getSource().getXOnBoard()+ directions[i],
                            move.getSource().getYOnBoard()+directions[j])){
                    squareChecked = Board.board[move.getSource().getXOnBoard()+ directions[i]]
                                         [move.getSource().getYOnBoard()+directions[j]];
                    if(squareChecked.getPieceOccupying() != null &&
                            squareChecked.getPieceOccupying().getPieceColor() == move.getTurn()){
                        continue;
                    }
                    moves.add(new Move(move.getSource(),squareChecked,move.getTurn()));
                }
            }
        }
        Move castle;

        if(isCastleLegal(1)){
            castle = new Move(move.getSource(),
                              Board.board[move.getSource().getXOnBoard()+2][move.getSource().getYOnBoard()],
                              move.getTurn());
            castle.setMoveType(MoveType.CASTLE);
            moves.add(castle);
        }
        if(isCastleLegal(-1)){
            castle = new Move(move.getSource(),
                    Board.board[move.getSource().getXOnBoard()-2][move.getSource().getYOnBoard()],
                    move.getTurn());
            castle.setMoveType(MoveType.CASTLE);
            moves.add(castle);
        }
        return moves;
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
        else if(move.getSource().getPieceOccupying().getPieceType() == 'k'){
            movesForPiece = getKingLegalMoves();
        }
        else{
            movesForPiece = getQueenLegalMoves();
        }
        movesForPiece = deleteIfIllegal(movesForPiece);
        isMoveInList(movesForPiece);
        return movesForPiece;
    }
    public boolean isLegalMove() {
        boolean ret;
        char pieceType = move.getSource().getPieceOccupying().getPieceType();
        ArrayList<Move> movesForPiece = getListOfPossibleMoves(pieceType);
        ret = isMoveInList(movesForPiece);
        Piece pieceOnSquare = move.getSource().getPieceOccupying();
        if (ret && pieceType == 'p') {
            ((Pawn)pieceOnSquare).setIsInStartingPosition(false);
        }
        else if(ret && pieceType == 'k'){
            ((King)pieceOnSquare).setHasMoved(true);
        }
        else if(ret && pieceType == 'r'){
            ((Rook)pieceOnSquare).setHasMoved(true);
        }
        return ret;
    }

    public void highlightPossibleMoves(){
        ArrayList<Move> movesToHighlight = getListOfPossibleMoves(move.getSource().getPieceOccupying().getPieceType());
        for(Move m: movesToHighlight){
            m.getDestination().setBackground(Square.COLOR_OF_HIGHLIGHT);
        }
    }

    public void turnOffHighlight(){
        Color originalColor;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                originalColor = Board.board[i][j].getSquareColor()? Square.COLOR_OF_FIRST_SIDE: Square.COLOR_OF_SECOND_SIDE;
                Board.board[i][j].setBackground(originalColor);
            }
        }
    }
}
