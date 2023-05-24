package PiecesAndBoard;

import javax.swing.*;

public class Knight extends Piece{
    public Knight(boolean color) {
        super('n',color, color?
                new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\whiteKnight.png")
                : new ImageIcon("C:\\JavaProjects\\ChessProject\\src\\main\\resources\\blackKnight.png"));
    }
}
