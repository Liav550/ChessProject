package PiecesAndBoard;

import javax.swing.*;

public class Pawn extends Piece{
    private boolean isInStartingPosition;
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
}
