package PiecesAndBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Square extends JButton{
    private boolean squareColor; // the color of the square
    private Piece pieceOccupying = null; // the piece that is currently on this square
    private int xOnBoard; // the x location of the square on the board (x between 0 and 8)
    private int yOnBoard; // the y location of the square on the board (y between 0 and 8)
    public static final int SQUARE_SIZE = 90; // the size of all squares
    public static final Color COLOR_OF_FIRST_SIDE = Color.lightGray; // the first color of all squares
    public static final Color COLOR_OF_SECOND_SIDE = Color.darkGray; // the second color of all squares
    public Square(boolean squareColor, int xOnBoard, int yOnBoard){
        this.squareColor = squareColor; // assigning
        this.setBackground(squareColor? COLOR_OF_FIRST_SIDE: COLOR_OF_SECOND_SIDE); // setting the square's color

        this.xOnBoard = xOnBoard; // assigning
        this.yOnBoard = yOnBoard; // assigning
        this.setBounds(xOnBoard*SQUARE_SIZE+30, yOnBoard*SQUARE_SIZE+30, SQUARE_SIZE,SQUARE_SIZE);
    }

    public boolean getSquareColor() {
        return squareColor;
    }
    public int getXOnBoard() {
        return xOnBoard;
    }

    public int getYOnBoard() {
        return yOnBoard;
    }

    public Piece getPieceOccupying() {
        return pieceOccupying;
    }

    public void setPieceOccupying(Piece pieceOccupying) {
        if (this.pieceOccupying == null) {
            if (pieceOccupying != null) {
                this.pieceOccupying = pieceOccupying;
                this.add(pieceOccupying);
            }
            return;
        }
        if (pieceOccupying == null) {
            this.remove(this.pieceOccupying);
            this.pieceOccupying = null;
            return;
        }

        if (pieceOccupying.getPieceColor() != this.pieceOccupying.getPieceColor()) {
            this.remove(this.pieceOccupying);
            this.pieceOccupying = pieceOccupying;
            this.add(pieceOccupying);
        }
    }
}
