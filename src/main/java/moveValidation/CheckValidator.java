package moveValidation;

import PiecesAndBoard.Piece;
import PiecesAndBoard.Square;

public class CheckValidator {
    private Square kingPosition;
    private boolean turn;
    private Square[][] board;
    public CheckValidator(boolean turn, Square[][] board){
        this.turn = turn;
        this.board = board;
        Piece pieceOnSquare;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieceOnSquare = board[i][j].getPieceOccupying();
                if(pieceOnSquare != null && pieceOnSquare.getPieceType() == 'k'
                        && turn == pieceOnSquare.getPieceColor()){
                    kingPosition = board[i][j];
                    return;
                }
            }
        }
    }
    private boolean checkForChecksInDirection(int advanceX, int advanceY){
        char dangerPiece;
        if(advanceY == 0 || advanceX == 0){
            dangerPiece = 'r';
        }
        else{
            dangerPiece = 'b';
        }
        Square start;
        Piece pieceOnSquare;
        int currentX = kingPosition.getXOnBoard() + advanceX;
        int currentY = kingPosition.getYOnBoard() + advanceY;
        while(currentX>=0 && currentX<8 && currentY >= 0 && currentY<8){
            start = board[currentX][currentY];
            pieceOnSquare = start.getPieceOccupying();
            if(pieceOnSquare== null){
                currentX+= advanceX;
                currentY+=advanceY;
                continue;
            }
            if(pieceOnSquare.getPieceColor() == turn){
                return false;
            }
            if(pieceOnSquare.getPieceType() == dangerPiece || pieceOnSquare.getPieceType() == 'q'){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    private boolean isDiagonalAttack(){
        return  (checkForChecksInDirection(1,-1) ||
                checkForChecksInDirection(1,1) ||
                checkForChecksInDirection(-1,-1) ||
                checkForChecksInDirection(-1,1));
    }

    private boolean isLinedAttack() {
        return  (checkForChecksInDirection(1,0) ||
                checkForChecksInDirection(0,1) ||
                checkForChecksInDirection(-1,0) ||
                checkForChecksInDirection(0,-1));
    }

    private boolean isKnightInCoordinates(int x, int y){
        if(x<0 || x>7 || y<0 || y>7){
            return false;
        }
        if(board[x][y].getPieceOccupying() == null){
            return false;
        }
        if(board[x][y].getPieceOccupying().getPieceType() == 'n' &&
        board[x][y].getPieceOccupying().getPieceColor() != turn){
            return true;
        }
        return false;
    }
    private boolean isKnightCheck() {
        int xKing = kingPosition.getXOnBoard();
        int yKing = kingPosition.getYOnBoard();
        int[] oneOffset = {1,-1};
        int[] twoOffset = {2,-2};
        for(int i = 0; i<2; i++){
            for (int j = 0; j < 2; j++) {
                if(isKnightInCoordinates(xKing+oneOffset[i], yKing+twoOffset[j])){ // 1,2
                    return true;
                }
                if(isKnightInCoordinates(xKing+twoOffset[i], yKing+oneOffset[j])){ // 2,1
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPawnCheck(){
        int xKing = kingPosition.getXOnBoard();
        int yKing = kingPosition.getYOnBoard();
        Piece pieceOnSquare;
        if(turn && yKing >= 2){
            if(xKing != 7){
                pieceOnSquare = board[xKing+1][yKing-1].getPieceOccupying();
                if(pieceOnSquare != null && pieceOnSquare.getPieceType() == 'p' && !pieceOnSquare.getPieceColor()){
                    return true;
                }
            }
            if(xKing != 0){
                pieceOnSquare = board[xKing-1][yKing-1].getPieceOccupying();
                if(pieceOnSquare != null && pieceOnSquare.getPieceType() == 'p' && !pieceOnSquare.getPieceColor()){
                    return true;
                }
            }
        }
        else if(!turn && yKing <= 5){
            if(xKing != 7){
                pieceOnSquare = board[xKing-1][yKing+1].getPieceOccupying();
                if(pieceOnSquare != null && pieceOnSquare.getPieceType() == 'p' && pieceOnSquare.getPieceColor()){
                    return true;
                }
            }
            if(xKing != 0){
                pieceOnSquare = board[xKing+1][yKing+1].getPieceOccupying();
                if(pieceOnSquare != null && pieceOnSquare.getPieceType() == 'p' && pieceOnSquare.getPieceColor()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKingNearEnemyKing() {
        int[] directions = new int[]{1,0,-1};
        int xKing = kingPosition.getXOnBoard();
        int yKing = kingPosition.getYOnBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i==1 && j==1){
                    break;
                }
                xKing+=directions[i];
                yKing+=directions[j];
                if(xKing>7 || xKing<0 || yKing<0|| yKing>7){
                    break;
                }
                if(board[xKing][yKing].getPieceOccupying() != null &&
                board[xKing][yKing].getPieceOccupying().getPieceType() == 'k' ){
                    return true;
                }
                xKing = kingPosition.getXOnBoard();
                yKing = kingPosition.getYOnBoard();
            }
        }
        return false;
    }

    public boolean isInCheck(){
        if(isDiagonalAttack() || isLinedAttack() || isPawnCheck() || isKnightCheck() || isKingNearEnemyKing()){
            return true;
        }
        return false;
    }
}
