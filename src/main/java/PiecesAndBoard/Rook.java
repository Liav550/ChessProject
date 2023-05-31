package PiecesAndBoard;

import javax.swing.*;

public class Rook extends Piece{
    private boolean hasMoved;
    public Rook(boolean color) {
        super('r',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whiteRook.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackRook.png"));
        hasMoved = false;
    }

    public void setHasMoved(boolean hasMoved){
        this.hasMoved = hasMoved;
    }

    public boolean getHasMoved() {
        return hasMoved;
    }
}
