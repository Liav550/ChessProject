package PiecesAndBoard;

import moves.Move;
import moves.MoveValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JFrame implements MouseListener{
    public static final Square[][] board = new Square[8][8]; // representing the view of the board
    private Square sourceSquare = null; // this is for saving the source of the piece we want to move
    private boolean colorToPlay = true;
    public static final String STARTING_POSITION_WHITE_FRONT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    // public static final String STARTING_POSITION_BLACK_FRONT = "RNBKQBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbkqbnr";
    // in the future...

    public Board(){
        // setting up the frame's properties
        setTitle("Game on! ");
        setResizable(false);
        setSize(1000,800);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(0, 0, 0));
        setLocationRelativeTo(null);

        // building the squares
        boolean color;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                color = (i+j)%2 == 0; // the color of each square depends on the sum of the indexes divided by 2.
                board[i][j] = new Square(color, i,j);
                board[i][j].addMouseListener(this); // adding a mouse listener for each square
                add(board[i][j]); // adding the square to the board
            }
        }
        JButton clearButton = new JButton();
        clearButton.setName("Clear");
        clearButton.setText("Clear");
        clearButton.setBounds(800,400,130, 80);
        clearButton.setBackground(new Color(255, 224, 22));
        clearButton.addMouseListener(this);
        add(clearButton);

        setBoardByFEN(STARTING_POSITION_WHITE_FRONT); // setting the starting position by using FEN
        setVisible(true); // show the board on the screen
    }
    // This method loads the board according to a FEN string
    private void setBoardByFEN(String fen){
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
    private void clearBoard(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j].setPieceOccupying(null);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(((JButton)e.getSource()).getName() != null && ((JButton)e.getSource()).getName().equals("Clear")){
            dispose();
            Board b = new Board();
            return;
        }
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
            MoveValidator validator = new MoveValidator(new Move(sourceSquare,board[0][0],colorToPlay));
            validator.highlightPossibleMoves();
        }
        else{
            Square destinationSquare = (Square) e.getSource();
            Move move = new Move(sourceSquare,destinationSquare, colorToPlay);
            MoveValidator moveValidator = new MoveValidator(move);
            if(moveValidator.isLegalMove()){
                move.makeMove();
                System.out.println(move.getMoveType());
                updatePawnTurnsSinceDoubleMove();
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
    private void updatePawnTurnsSinceDoubleMove(){
        Piece pieceOnSquare;
        Pawn pawnOnSquare;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                pieceOnSquare = board[i][j].getPieceOccupying();
                if(pieceOnSquare == null){
                    continue;
                }
                if(pieceOnSquare.getPieceType() == 'p' && pieceOnSquare.getPieceColor() == colorToPlay){
                    pawnOnSquare = (Pawn) pieceOnSquare;
                    if(pawnOnSquare.hasDoubleMoved()){
                        pawnOnSquare.incrementTurnsSinceDoubleMove();
                    }
                }
            }
        }
    }
}
