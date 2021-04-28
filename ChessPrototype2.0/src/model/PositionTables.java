package model;

import model.pieces.*;

public class PositionTables {

    private static int[][] pawnMapWhite = new int[][]{
            {100,  100,  100,  100,  100,  100,  100,  100},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static int[][] pawnMapWhiteEndgame = new int[][]{
            {100,  100,  100,  100,  100,  100,  100,  100},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {25, 25, 25, 25, 25, 25, 25, 25},
            {20,  20, 20, 25, 25, 20, 20,  20},
            {10,  10,  10, 20, 20, 10, 10,  10},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {-2, -2, -2,-20,-20, -2, -2,  -2},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };


    private static int[][] pawnMapBlack = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {100,  100,  100,  100,  100,  100,  100,  100},
    };

    private static int[][] pawnMapBlackEndgame = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {-2, -2, -2,-20,-20, -2, -2,  -2},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {10,  10,  10, 20, 20, 10, 10,  10},
            {20,  20, 20, 25, 25, 20, 20,  20},
            {25, 25, 25, 25, 25, 25, 25, 25},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {100,  100,  100,  100,  100,  100,  100,  100},
    };


    private static int[][] knightMap = new int[][]{
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 15, 15, 15, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 15, 15, 15,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
    };
    private static int[][] bishopMapWhite = new int[][] {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  9,  0,  0,  0,  0,  9,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20},
    };
    private static int[][] bishopMapBlack = new int[][] {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  9,  0,  0,  0,  0,  9,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20},
    };
    private static int[][] rookMapWhite = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-15, -5,  0,  0, -5, -15,-20}
    };
    private static int[][] rookMapBlack = new int[][] {
            {-20,-15, -5,  0,  0, -5, -15,-20},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };
    private static int[][] queenMap = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            {-5,  0,  5,  5,  5,  5,  0, -5},
            {0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };
    private static int[][] kingMapMidWhite = new int[][] {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {20, 30, 20,  0,  0, 0, 30, 20}
    };
    private static int[][] kingMapMidBlack = new int[][] {
            {20, 30, 10,  0,  0, 10, 30, 20},
            {20, 20,  0,  0,  0,  0, 20, 20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30}
    };
    private static int[][] kingMapEndWhite = new int[][] {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}
    };
    private static int[][] kingMapEndBlack = new int[][] {
            {-50,-30,-30,-30,-30,-30,-30,-50},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-50,-40,-30,-20,-20,-30,-40,-50}
    };


    public static double getValue(Piece p) {
        double v =  getMapForPiece(p)[p.getField().getLine()][p.getField().getColumn()];
        return v/100;
    }
    
    private static int[][] getMapForPiece(Piece p) {
        int[][] map = null;
        if (p instanceof Pawn)
            if(Chessboard.getInstance().isEndGame())
                map = p.getColor().equals(Color.WHITE)?pawnMapWhiteEndgame:pawnMapBlackEndgame;
            else
                map = p.getColor().equals(Color.WHITE)?pawnMapWhite:pawnMapBlack;
        else if (p instanceof Queen)
            map = queenMap;
        else if (p instanceof Knight)
            map = knightMap;
        else if (p instanceof Bishop)
            map = p.getColor().equals(Color.WHITE)?bishopMapWhite:bishopMapBlack;
        else if (p instanceof Rook)
            map = p.getColor().equals(Color.WHITE)?rookMapWhite:rookMapBlack;
        else if (Chessboard.getInstance().isEndGame())
            map = p.getColor().equals(Color.WHITE)?kingMapEndWhite:kingMapEndBlack;
        else
            map = p.getColor().equals(Color.WHITE)?kingMapMidWhite:kingMapMidBlack;
        return map;
    }

}
