package PiecesAndBoard;

import javax.swing.*;

public class Bishop extends Piece{
    public Bishop(boolean color) {
        super('b',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whiteBishop.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackBishop.png"));
    }
}
