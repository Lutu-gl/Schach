package model.pieces;

import model.Color;
import model.Field;
import model.Turn;

import java.util.ArrayList;

public class Queen extends Piece{
    public Queen(Color color, String name, Field field, int value) {
        super(color, name, field, value);
    }

    @Override
    public ArrayList<Field> getMoves(Turn t) {
        return null;
    }
}