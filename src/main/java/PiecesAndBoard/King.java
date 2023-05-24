package PiecesAndBoard;

import javax.swing.*;

public class King extends Piece{
    private boolean hasMoved;
    public King(boolean color) {
        super('k',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whiteKing.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackKing.png"));
        hasMoved = false;
    }

    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }

    public boolean HasMoved() {
        return hasMoved;
    }
}
