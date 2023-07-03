package moves;

import PiecesAndBoard.*;

import javax.swing.*;
import java.awt.*;

public class PromotionWindow {
    public static Piece getChoiceFromWindow(boolean color){
        JOptionPane optionPane = new JOptionPane("Choose a piece!");
        ImageIcon ex = new ImageIcon();
        ImageIcon[] options = {PieceFactory.buildPiece('q', color).getPieceImage(),
                PieceFactory.buildPiece('r', color).getPieceImage(),
                PieceFactory.buildPiece('b', color).getPieceImage(),
                PieceFactory.buildPiece('n', color).getPieceImage(),
        };
        for (int i = 0; i < 4; i++) {
            options[i].setDescription(Integer.toString(i));
        }
        optionPane.setOptions(options);

        JDialog dialog = optionPane.createDialog(null, "Pawn Promotion");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

        int res = Integer.valueOf(((ImageIcon)optionPane.getValue()).getDescription());
        switch (res){
            case 0:
                return new Queen(color);
            case 1:
                return new Rook(color);
            case 3:
                return new Bishop(color);
            case 4:
                return new Knight(color);
            default:
                return null;
        }
    }
}
