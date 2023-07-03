package PiecesAndBoard;

import javax.swing.*;

public class Pawn extends Piece{
    private boolean isInStartingPosition;
    private int turnsSinceDoubleMove;
    private boolean hasDoubleMoved;
    public Pawn(boolean color) {
        super('p',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whitePawn.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackPawn.png"));
        isInStartingPosition = true;
    }

    public boolean isInStartingPosition() {
        return isInStartingPosition;
    }
    public void setIsInStartingPosition(boolean inStartingPosition) {
        isInStartingPosition = inStartingPosition;
    }
    public int getTurnsSinceDoubleMove(){
        return turnsSinceDoubleMove;
    }
    public void incrementTurnsSinceDoubleMove(){
        turnsSinceDoubleMove++;
    }
    public boolean hasDoubleMoved(){
        return hasDoubleMoved;
    }
    public void setHasDoubleMoved(boolean hasDoubleMoved){
        this.hasDoubleMoved = hasDoubleMoved;
    }
}
