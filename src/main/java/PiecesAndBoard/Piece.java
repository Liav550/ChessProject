package PiecesAndBoard;

import javax.swing.*;
import java.awt.*;

public abstract class Piece extends JLabel{
    private char pieceType;
    private boolean pieceColor;
    private ImageIcon pieceImage;
    public Piece(char pieceType, boolean pieceColor, ImageIcon pieceImage){
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.pieceImage = pieceImage;
        this.setIcon(pieceImage);
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
    }

    public boolean getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(boolean pieceColor) {
        this.pieceColor = pieceColor;
    }

    public char getPieceType() {
        return pieceType;
    }

    public void setPieceType(char pieceType) {
        this.pieceType = pieceType;
    }

    public ImageIcon getPieceIcon() {
        return pieceImage;
    }
}
