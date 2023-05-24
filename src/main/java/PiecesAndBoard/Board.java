package PiecesAndBoard;

import moveValidation.Move;
import moveValidation.MoveValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener{
    private Square[][] board; // representing the view of the board
    private Square sourceSquare = null; // this is for saving the source of the piece we want to move
    private boolean colorToPlay = true;

    public Board(){
        board = new Square[8][8]; // initializing

        // setting up the frame's properties
        setTitle("Game on! ");
        setResizable(false);
        setSize(800,800);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(44, 35, 35));
        setLocationRelativeTo(null);

        // building the squares
        boolean color;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                color = (i+j)%2 == 0;
                board[i][j] = new Square(color, i,j);
                board[i][j].addMouseListener(this);
                add(board[i][j]);
            }
        }
        setBoardByFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        setVisible(true);
    }
    public void setBoardByFEN(String fen){
        int len = fen.length();
        int currentX = 0;
        int currentY = 0;
        boolean color;
        char currentChar;
        for (int i = 0; i < len; i++) {
            if(currentY <= 7){
                currentChar = fen.charAt(i);
                if(Character.isDigit(currentChar)){
                    currentX+= Character.getNumericValue(currentChar);
                }
                else if(Character.isLetter(currentChar)) {
                    color = Character.isUpperCase(currentChar);
                    board[currentX][currentY].setPieceOccupying
                            (PieceFactory.buildPiece(Character.toLowerCase(currentChar),color));
                    currentX++;
                }
                else{
                    currentY++;
                    currentX = 0;
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(sourceSquare == null){
            sourceSquare = (Square) e.getSource();
            if(sourceSquare.getPieceOccupying()==null){
                sourceSquare = null;
                return;
            }
            else if(sourceSquare.getPieceOccupying().getPieceColor() != colorToPlay){
                sourceSquare = null;
                return;
            }
        }
        else{
            Square destinationSquare = (Square) e.getSource();
            Move move = new Move(sourceSquare,destinationSquare, colorToPlay);
            MoveValidator moveValidator = new MoveValidator(board,move);
            if(moveValidator.isLegalMove()){
                move.makeMove();
                colorToPlay = !colorToPlay;
            }
            sourceSquare = null;
            revalidate();
            repaint();

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
