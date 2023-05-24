package PiecesAndBoard;

import javax.swing.*;

public class Queen extends Piece{
    public Queen(boolean color) {
        super('q',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whiteQueen.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackQueen.png"));
    }
}
