package model.pieces;

import model.Chessboard;
import model.Color;
import model.Field;

import java.util.ArrayList;

public class Bishop extends Piece{
    public Bishop(Color color, String name, Field field, int value, char shortName) {
        super(color, name, field, value, shortName);
    }

    private Chessboard chessboard;
    private ArrayList<Field> availableMoves;

    @Override
    public ArrayList<Field> getMoves() {
        availableMoves = new ArrayList<>();
        chessboard = Chessboard.getInstance();
        int line = field.getLine(), column = field.getColumn();
        // Go to left and down
        boolean[] continueSearches = new boolean[]{true, true, true, true};
        for (int gap = 1; gap < chessboard.getFields().length; gap++) {
            if (continueSearches[0])
                continueSearches[0] = evaluate(line+gap, column+gap);
            if (continueSearches[1])
                continueSearches[1] = evaluate(line+gap, column-gap);
            if (continueSearches[2])
                continueSearches[2] = evaluate(line-gap, column+gap);
            if (continueSearches[3])
                continueSearches[3] = evaluate(line-gap, column-gap);
        }
        return availableMoves;
    }

    private boolean evaluate(int line, int column) {
        if (line >= chessboard.getFields().length || column >= chessboard.getFields().length || column < 0 || line < 0) return false;
        Field nextField = chessboard.getFields()[line][column];
        // Schauen, ob das Feld überhaupt existiert
        if (nextField.isExists()) {
            // Schauen ob sich ein Piece darauf befindet
            if (nextField.hasPiece()) {
                // Bei gegnerischer Figur kann man schlagen
                if (!nextField.getPiece().getColor().equals(chessboard.getCurrentTurn()))
                    availableMoves.add(nextField);
                // Die Suche wird abgebrochen, da ein Bishop nicht über Figuren laufen kann
                return false;
            }
            // Wenn kein Piece auf dem Feld ist wird es hinzugefügt
            else {
                availableMoves.add(nextField);
                return true;
            }
        }
        // Falls das Feld nicht existiert wird abgebrochen
        else {
            return false;
        }
    }
}
