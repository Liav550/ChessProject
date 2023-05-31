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
    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Board(){
        board = new Square[8][8]; // initializing the body of the board

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
                color = (i+j)%2 == 0; // the color of each square depends on the sum of it's indexes divided by 2.
                board[i][j] = new Square(color, i,j);
                board[i][j].addMouseListener(this); // adding a mouse listener for each square
                add(board[i][j]); // adding the square to the board
            }
        }
        setBoardByFEN(STARTING_POSITION); // setting the starting position by using FEN
        setVisible(true); // show the board on the screen
    }


    // This method loads the board according to a FEN string
    public void setBoardByFEN(String fen){
        int len = fen.length(); // the length of the FEN string
        int currentX = 0; // the x dimension of the current square we are iterating through according to the FEN string
        int currentY = 0; // the y dimension of the current square we are iterating through according to the FEN string
        boolean color; // the color of the piece we will put on the square
        char currentChar; // the Character we are iterating through
        for (int i = 0; i < len; i++) {
            currentChar = fen.charAt(i); // load the current character
            if(Character.isDigit(currentChar)){ // if the character is a digit:
                currentX+= Character.getNumericValue(currentChar); // we go "currentX" to our right,
                                                                   // and go to the next piece
            }
            else if(Character.isLetter(currentChar)) { // if the character is a letter:
                color = Character.isUpperCase(currentChar); // getting the color of the piece by the type of the letter
                board[currentX][currentY].setPieceOccupying
                        (PieceFactory.buildPiece(Character.toLowerCase(currentChar),color)); // build the correct piece
                                                                                             // to the current square
                currentX++; // continue to the next file on the same row
            }
            else{ // if we reached here, it means we have the '/' sign, which means to skip to the next row
                currentY++; // skip the row
                currentX = 0; // start scanning from column 0
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
            MoveValidator validator = new MoveValidator(board, new Move(sourceSquare,board[0][0],colorToPlay));
            validator.highlightPossibleMoves();
        }
        else{
            Square destinationSquare = (Square) e.getSource();
            Move move = new Move(sourceSquare,destinationSquare, colorToPlay);
            MoveValidator moveValidator = new MoveValidator(board,move);
            if(moveValidator.isLegalMove()){
                move.makeMove();
                colorToPlay = !colorToPlay;
            }
            moveValidator.turnOffHighlight();
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
