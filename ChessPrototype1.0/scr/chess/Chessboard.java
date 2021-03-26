package chess;

import gameLogic.Color;
import gameLogic.Gamestate;
import gameLogic.Move;
import gameLogic.Turn;
import gameLogic.GamestateObserver;
import gameLogic.Observer;
import javafx.geometry.Pos;
import pieces.Pawn;
import pieces.*;
import pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * The actual Board containing a list of FieldLabels, the Constructor initializes it with 64 labels
 * aswell as a list of all pieces
 */
public class Chessboard extends GridPane {

    /*** A two Dimensional Array containing all Squares(FieldLabels)*/
    private FieldLabel[][] labels = new FieldLabel[8][8];
    /*** A Arraylist containing all Pieces*/
    private ArrayList<Piece> b_pieces = new ArrayList<>();
    private ArrayList<Piece> w_pieces = new ArrayList<>();

    /*** Variable for the gamestate. For example if White/Black Wins or Stalemate etc.*/
    Gamestate gamestate = Gamestate.PLAYING;
    /*** Halfmove counter*/
    private int turn=0;
    /*** Fullmove counter*/
    private int fullturn=0;
    /*** 50 Rule move counter */
    private int ruleCounter=0;
    /*** previous fen for the threefold repetition for White*/
    private ArrayList<String> playedFens = new ArrayList<>();
    /*** counter for threefold repetition */
    private int threefoldCounter=0;
    /*** A reference to Blacks King*/
    private King b_King;
    /*** A reference to Whites King*/
    private King w_King;
    /*** Pawn that can be enPassant*/
    private Pawn enPassantable;
    /*** observers*/
    ArrayList<Observer> observers = new ArrayList<>();

    /**
     * Constructor initializing the Board with 64 Fieldlabels
     */
    public Chessboard(){
        register(new GamestateObserver(this));

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;");
                label.setId(""+ (char)(97 + i) + (8-j));
                label.setText(""+ (char)(97 + i) + (8-j));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setAlignment(Pos.CENTER);
                label.setBoard(this);
                labels[i][j]=label;
                this.add(label, i, j);
            }
        Move.board = this;
    }

    /**
     * Method to toggle the Coordinates of the FieldLabels
     * @param show boolean true or false
     */
    public void doShowTextLabels(boolean show){
        if(show) {
            FieldLabel label;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++){
                    label = labels[i][j];
                    label.setText(""+ (char)(97 + i) + (8-j));
                }
        }else{
            for (FieldLabel[] fieldLabel : labels) {
                for (FieldLabel fieldLabel1 : fieldLabel) {
                    fieldLabel1.setText("");
                }
            }
        }
    }


    /**
     * Gets called after every Halfmove
     * More functionality will be added
     */
    public void endTurn(Move move){
        fullturn = (turn / 2) + (Turn.getColorToMove() == Color.BLACK ? 1 : 1);
        turn++;
        ruleCounter++;

        //Three fold repetition.
        String fen = getBoardAsFen();

        int spaceCounter=0;
        for (int i = fen.length()-1; i >= 0; i--) {
            if(fen.charAt(i) == ' '){
                spaceCounter++;
            }
            if(spaceCounter >= 2){
                fen = fen.substring(0, i);
                break;
            }
        }

        playedFens.add(fen);
        //Maybe this can be done somehow faster
        for(String f : playedFens){
            if(fen.equals(f)){
                threefoldCounter++;
            }
        }
        if(threefoldCounter >= 3){
            //System.out.println("Treefold applies. Anyone can claim a DRAW!");
            gamestate = Gamestate.PLAYERCANCLAIMDRAW;
        }else{
            threefoldCounter=0;
        }


        //50 move rule
        if(move.getMovingPiece() instanceof Pawn) ruleCounter = 0;
        if(move.getEatenPiece() != null) ruleCounter = 0;

        if(ruleCounter >= 100){
            //System.out.println("50 move rule -> DRAW");
            gamestate = Gamestate.DRAW;
        }

        //System.out.println("turn: " + turn + " fullturn: " +fullturn);
        //System.out.println(getBoardAsFen());
        //setBoardByFen(getBoardAsFen());
        notifyObserver();
    }

    /**
     * Getter for Turn
     * @return returns int number of halfmoves since start of game
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Getter for fullturn
     * @return returns int number of full moves since start of game
     */
    public int getFullturn() {
        return fullturn;
    }

    /**
     * Setter for fullturn
     * @param fullturn int number of full moves since start of game
     */
    public void setFullturn(int fullturn) {
        this.fullturn = fullturn;
    }
    /**
     * Getter for 50 move rule
     * @return returns int number of moves before the 50 move rule applys (half moves)
     */
    public int getRuleCounter() {
        return ruleCounter;
    }
    /**
     * Setter for rule counter
     * @param ruleCounter int number of moves before the 50 move rule applys (half moves)
     */
    public void setRuleCounter(int ruleCounter) {
        this.ruleCounter = ruleCounter;
    }

    /**
     * Setter for Turn
     * @param turn int number of halfmoves since start of game
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }
    /**
     * @param fen Add a fen to the Arraylist playedFens to check threefold repetition
     */
    public void addFen(String fen){
        int spaceCounter=0;
        for (int i = fen.length()-1; i >= 0; i--) {
            if(fen.charAt(i) == ' '){
                spaceCounter++;
            }
            if(spaceCounter >= 2){
                fen = fen.substring(0, i);
                break;
            }
        }

        playedFens.add(fen);
    }

    /**
     * Add Method for ArrayList<Piece> pieces
     * @param p Piece that has been added to the Board
     */
    public void addPiece(Piece p){
        if(p.getColor() == Color.BLACK)
            b_pieces.add(p);
        else
            w_pieces.add(p);
    }

    /**
     * Remove Method for ArrayList<Piece> pieces
     * @param p Piece that needs to be removed
     */
    public boolean removePiece(Piece p){


        if(p == null) return false;
        if(p.getColor() == Color.BLACK)
            return b_pieces.remove(p);
        else
            return w_pieces.remove(p);
    }

    /**
     * Getter for Two Dimensional Array of all FieldLabels
     * @return FieldLabels[8][8]
     */
    public FieldLabel[][] getLabels() {
        return labels;
    }

    /**
     * Getter for ArrayList<Piece> pieces
     * @return ArrayList<Piece>
     */
    public ArrayList<Piece> getB_pieces() {
        return b_pieces;
    }
    public ArrayList<Piece> getW_pieces() {
        return w_pieces;
    }

    /**
     * Sets Board to a Position by a Fen String
     * @param fen Fend String to set the Board by
     */
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
    public void setBoardByFen(String fen){
        /*
        String fenBoard = fen.substring(0, fen.indexOf(' ')).replace("/","");
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","");//.replace(" ","");

        //System.out.println(fenBoard);
        //System.out.println(fenInfo);
        ArrayList<String> rows = new ArrayList<>();
        ArrayList<String> currentRows = new ArrayList<>();
        char c;
        String currentFen = getBoardAsFen();

        if(currentFen != null) {
            //System.out.println(fen);
            //System.out.println(currentFen);
            //System.out.println(getBoardAsFen()); Also die Idee: Die ganzen / / bei getBoardAsFen durchgehen. Schauen wo dort was andersch isch. Wenn was andersch isch des ändern
            if (currentFen.equals(fen)) {
                return;
            }
            String string = "";

            for (int i = 0; i < fen.length(); i++) {
                c = fen.charAt(i);
                if (c == '/') {
                    rows.add(string);
                    string = "";
                } else {
                    string = string + c;
                }
                if (c == ' '){
                    rows.add(string);
                    break;
                }
            }
            string = "";

            for (int i = 0; i < currentFen.length(); i++) {
                c = currentFen.charAt(i);
                if (c == ' '){
                    currentRows.add(string);
                    break;
                }
                if (c == '/') {
                    currentRows.add(string);
                    string = "";
                } else {
                    string = string + c;
                }

            }
           // System.out.println(rows);
            //System.out.println(currentRows);

            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                String currentrow = currentRows.get(i);

                if(!row.equals(currentrow)){
                    //System.out.println("Bei row: " + i + " fen: " + row + " currentfen " + currentrow);
                    //In this row make everything new :)
                    int x=0;
                    int y=i;

                    for (int j = 0; j < row.length(); j++, x++) {
                        c = row.charAt(j);

                        Piece piece;
                        switch(c){
                            //Black pieces
                            case 'p':
                                piece =  new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), labels[x][y], Color.BLACK, "Black Pawn");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'n':
                                piece = new Knight(new ImageView(new Image("Imgs\\B_Knight.png")), labels[x][y], Color.BLACK, "Black Knight");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'b':
                                piece = new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), labels[x][y], Color.BLACK, "Black Bishop");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'r':
                                piece =new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[x][y], Color.BLACK, "Black Rook");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'q':
                                piece = new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), labels[x][y], Color.BLACK, "Black Queen");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'k':
                                piece = new King(new ImageView(new Image("Imgs\\B_King.png")), labels[x][y], Color.BLACK, "Black King");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                b_King = (King)piece;
                                break;
                            //White pieces
                            case 'P':
                                piece = new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), labels[x][y], Color.WHITE, "White Pawn");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'N':
                                piece = new Knight(new ImageView(new Image("Imgs\\W_Knight.png")), labels[x][y], Color.WHITE, "White Knight");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'B':
                                piece = new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), labels[x][y], Color.WHITE, "White Bishop");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'R':
                                piece = new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[x][y], Color.WHITE, "White Rook");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'Q':
                                piece = new Queen(new ImageView(new Image("Imgs\\W_Queen.png")), labels[x][y], Color.WHITE, "White Queen");
                                getLabels()[x][y].setPiece(piece);
                                this.addPiece(piece);
                                break;
                            case 'K':
                                piece = new King(new ImageView(new Image("Imgs\\W_King.png")), labels[x][y], Color.WHITE, "White King");
                                getLabels()[x][y].setPiece(piece);
                                w_King = (King)piece;
                                this.addPiece(piece);
                                break;
                            //nl
                            case '/': continue;
                        }
                        //1=49  &&  8=56
                        if(c >= 49 && c <= 56){
                            //System.out.println("x="+x);
                            int number = Integer.parseInt(c+"");

                            for (int k = 0; k < number; k++) {
                                labels[x+k][y].removePiece();
                            }
                            x += (number-1);
                        }
                    }
                }
            }
        } else {
            int posString = 0;


            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (posString >= fenBoard.length()) break;
                    c = fenBoard.charAt(posString++);
                    Piece piece;
                    switch (c) {
                        //Black pieces
                        case 'p':
                            piece = new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), labels[j][i], Color.BLACK, "Black Pawn");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'n':
                            piece = new Knight(new ImageView(new Image("Imgs\\B_Knight.png")), labels[j][i], Color.BLACK, "Black Knight");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'b':
                            piece = new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), labels[j][i], Color.BLACK, "Black Bishop");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'r':
                            piece = new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[j][i], Color.BLACK, "Black Rook");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'q':
                            piece = new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), labels[j][i], Color.BLACK, "Black Queen");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'k':
                            piece = new King(new ImageView(new Image("Imgs\\B_King.png")), labels[j][i], Color.BLACK, "Black King");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            b_King = (King) piece;
                            break;
                        //White pieces
                        case 'P':
                            piece = new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), labels[j][i], Color.WHITE, "White Pawn");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'N':
                            piece = new Knight(new ImageView(new Image("Imgs\\W_Knight.png")), labels[j][i], Color.WHITE, "White Knight");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'B':
                            piece = new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), labels[j][i], Color.WHITE, "White Bishop");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'R':
                            piece = new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[j][i], Color.WHITE, "White Rook");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'Q':
                            piece = new Queen(new ImageView(new Image("Imgs\\W_Queen.png")), labels[j][i], Color.WHITE, "White Queen");
                            getLabels()[j][i].setPiece(piece);
                            this.addPiece(piece);
                            break;
                        case 'K':
                            piece = new King(new ImageView(new Image("Imgs\\W_King.png")), labels[j][i], Color.WHITE, "White King");
                            getLabels()[j][i].setPiece(piece);
                            w_King = (King) piece;
                            this.addPiece(piece);
                            break;
                        //nl
                        case '/':
                            continue;
                    }
                    //blank space
                    if ((c > 49) && (c <= 57)) {
                        j += (c - 49);
                    }
                }
            }
        }
        int turnIndex = 0;

        for (int j = 0; j < fenInfo.length(); j++) {
            c = fenInfo.charAt(j);

            switch(c){
                case 'w': Turn.setColorToMove(Color.WHITE); break; //System.out.println("Whites Turn"); break;//Weiß am zug
                case 'b': Turn.setColorToMove(Color.BLACK); break;  //System.out.println("Blacks Turn"); break; //Schwarz am Zug
                case 'K': getW_King().setCanCastleKing(true); break; //System.out.println("w king");
                case 'Q':  getW_King().setCanCastleQueen(true); break; //System.out.println("w queen");
                case 'k':  getB_King().setCanCastleKing(true); break; //System.out.println("b king");
                case 'q': getB_King().setCanCastleQueen(true); break; // System.out.println("b queen");

            } //1 = 49   -> 9 = 57   -=45
            //en Passant
            if((c >= 97) && (c<=105) && fenInfo.charAt(j+1) != ' '){
                int x=c-97;
                int y = (Turn.getColorToMove() != Color.WHITE ? 5 : 2 );
                int moveDirect = (Turn.getColorToMove() != Color.WHITE ? -1 : +1);

                //System.out.println("En Passant bei: " + c + fenInfo.charAt(j+1) ); //enPassant ist möglich bei dem feld
                FieldLabel label = getLabelByCoordinates(x,y);

                //System.out.println("label das enpassant .." + label);

                FieldLabel label2 = getLabelByCoordinates(x+1,y+moveDirect);
                //System.out.println(label2);

                if(label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()){
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }
                label2 = getLabelByCoordinates(x-1,y+moveDirect);
                //System.out.println(label2);
                if(label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()){
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }

                //Enpassant remove
                enPassantable = null;
                turnIndex = j+3;
            }
            if(c == 45){    //-
                turnIndex = j+2;
            }
        }
        String number="";
        for (int i = turnIndex; i < fenInfo.length(); i++) {
            if(fenInfo.charAt(i) == ' '){
                ruleCounter = Integer.parseInt(number);
                number = "";
                i++;
            }
            number = number + fenInfo.charAt(i);
        }
        fullturn = Integer.parseInt(number);

        turn = fullturn*2  + (Turn.getColorToMove() == Color.BLACK ? -1 : 0);

        //System.out.println("turn: " + turn + " fullturn " + fullturn + " rule " + ruleCounter);
       // System.out.println("Jetzt der Fen nach undo move " + getBoardAsFen());

         */

        String fenBoard = fen.substring(0, fen.indexOf(' ')).replace("/","");
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","");//.replace(" ","");

        //System.out.println(fenBoard);
        //System.out.println(fenInfo);


        int posString=0;
        char c;
        int turnIndex=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(posString >= fenBoard.length()) break;
                c = fenBoard.charAt(posString++);
                Piece piece;
                switch(c){
                    //Black pieces
                    case 'p':
                        piece =  new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), labels[j][i], Color.BLACK, "Black Pawn");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'n':
                        piece = new Knight(new ImageView(new Image("Imgs\\B_Knight.png")), labels[j][i], Color.BLACK, "Black Knight");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'b':
                        piece = new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), labels[j][i], Color.BLACK, "Black Bishop");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'r':
                        piece =new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[j][i], Color.BLACK, "Black Rook");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'q':
                        piece = new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), labels[j][i], Color.BLACK, "Black Queen");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'k':
                        piece = new King(new ImageView(new Image("Imgs\\B_King.png")), labels[j][i], Color.BLACK, "Black King");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        b_King = (King)piece;
                        break;
                    //White pieces
                    case 'P':
                        piece = new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), labels[j][i], Color.WHITE, "White Pawn");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'N':
                        piece = new Knight(new ImageView(new Image("Imgs\\W_Knight.png")), labels[j][i], Color.WHITE, "White Knight");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'B':
                        piece = new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), labels[j][i], Color.WHITE, "White Bishop");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'R':
                        piece = new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[j][i], Color.WHITE, "White Rook");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'Q':
                        piece = new Queen(new ImageView(new Image("Imgs\\W_Queen.png")), labels[j][i], Color.WHITE, "White Queen");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'K':
                        piece = new King(new ImageView(new Image("Imgs\\W_King.png")), labels[j][i], Color.WHITE, "White King");
                        getLabels()[j][i].setPiece(piece);
                        w_King = (King)piece;
                        this.addPiece(piece);
                        break;
                    //nl
                    case '/': continue;
                }
                //blank space
                if((c > 49) && (c<=57)){
                    j+=(c-49);
                }


            }
        }

        for (int j = 0; j < fenInfo.length(); j++) {
            c = fenInfo.charAt(j);

            switch(c){
                case 'w': Turn.setColorToMove(Color.WHITE); break; //System.out.println("Whites Turn"); break;//Weiß am zug
                case 'b': Turn.setColorToMove(Color.BLACK); break;  //System.out.println("Blacks Turn"); break; //Schwarz am Zug
                case 'K': getW_King().setCanCastleKing(true); break; //System.out.println("w king");
                case 'Q':  getW_King().setCanCastleQueen(true); break; //System.out.println("w queen");
                case 'k':  getB_King().setCanCastleKing(true); break; //System.out.println("b king");
                case 'q': getB_King().setCanCastleQueen(true); break; // System.out.println("b queen");

            } //1 = 49   -> 9 = 57   -=45
            //en Passant
            if((c >= 97) && (c<=105) && fenInfo.charAt(j+1) != ' '){
                int x=c-97;
                int y = (Turn.getColorToMove() != Color.WHITE ? 5 : 2 );
                int moveDirect = (Turn.getColorToMove() != Color.WHITE ? -1 : +1);

                //System.out.println("En Passant bei: " + c + fenInfo.charAt(j+1) ); //enPassant ist möglich bei dem feld
                FieldLabel label = getLabelByCoordinates(x,y);

                //System.out.println("label das enpassant .." + label);

                FieldLabel label2 = getLabelByCoordinates(x+1,y+moveDirect);
                //System.out.println(label2);

                if(label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()){
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }
                label2 = getLabelByCoordinates(x-1,y+moveDirect);
                //System.out.println(label2);
                if(label2 != null && label2.hasPiece() && label2.getPiece() instanceof Pawn && label2.getPiece().getColor() == Turn.getColorToMove()){
                    ((Pawn) label2.getPiece()).setEnpassantLabel(label);
                }

                //Enpassant remove
                enPassantable = null;
                turnIndex = j+3;
            }
            if(c == 45){    //-
                turnIndex = j+2;
            }
        }
        String number="";
        for (int i = turnIndex; i < fenInfo.length(); i++) {
            if(fenInfo.charAt(i) == ' '){
                ruleCounter = Integer.parseInt(number);
                number = "";
                i++;
            }
            number = number + fenInfo.charAt(i);
        }
        fullturn = Integer.parseInt(number);

        turn = fullturn*2  + (Turn.getColorToMove() == Color.BLACK ? -1 : 0);

        //System.out.println("turn: " + turn + " fullturn " + fullturn + " rule " + ruleCounter);

    }

    public void clearAll(){
        for (FieldLabel[] e : labels) {
            for (FieldLabel h : e) {
                if(h.hasPiece())

                    h.removePiece();
            }
        }
        b_pieces.removeAll(b_pieces);
        w_pieces.removeAll(w_pieces);

    }


    public ArrayList<Piece> getPiecesByColor(Color c){
        return c == Color.BLACK ? b_pieces : w_pieces;
    }

    /**
     * Returns the King of either Black or White
     * @param c Enum Color of King
     * @return King Object
     */
    public King getKing(Color c){
        for (Piece e: getPiecesByColor(c))
        {
            if(e.getName().contains("King")&&e.getColor()==c)
                return (King)e;
        }
        return null;
    }

    /**
     * Returns the Fen String for the Board in its current state
     * @return String Fen
     * !!!Not finished!!!
     */
    public String getBoardAsFen(){
        if(getW_King() == null || getB_King() == null){
            return null;
        }

        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            int emptyLabels = 0;
            for (int j = 0; j < 8; j++)
            {
                FieldLabel current = getLabelByCoordinates(j,i);

                if(current.getPiece()!=null){
                    if(emptyLabels != 0){
                        fen.append(emptyLabels);
                        emptyLabels = 0;
                    }
                    String name = current.getPiece().getClass().getSimpleName();
                    if(name.equals("Knight"))
                        fen.append((current.getPiece().getColor() == Color.WHITE ? "N":"n"));
                    else
                        fen.append((current.getPiece().getColor() == Color.WHITE ? name.charAt(0):Character.toLowerCase(name.charAt(0))));
                }else
                    emptyLabels++;

            }
            if(emptyLabels != 0)
                fen.append(emptyLabels);
            if(i != 7)
                fen.append('/');
        }
        fen.append(Turn.getColorToMove() == Color.WHITE ? " w " : " b ");
        if(getW_King().isCanCastleKing()) fen.append("K");
        if(getW_King().isCanCastleQueen()) fen.append("Q");
        if(getB_King().isCanCastleKing()) fen.append("k");
        if(getB_King().isCanCastleQueen()) fen.append("q");


        if(enPassantable == null){
            fen.append(" -");      //Hier ACHTUNG!! da wäre en passant
        }else{
            FieldLabel left = getLabelByCoordinates(enPassantable.getFieldLabel().getX()-1, enPassantable.getFieldLabel().getY());
            FieldLabel right = getLabelByCoordinates(enPassantable.getFieldLabel().getX()+1, enPassantable.getFieldLabel().getY());

            if((left != null && left.hasPiece() && left.getPiece() instanceof Pawn && left.getPiece().getColor() != enPassantable.getColor())
                    || right != null && right.hasPiece() && right.getPiece() instanceof Pawn && right.getPiece().getColor() != enPassantable.getColor()) {
                fen.append(" " + ((char) (enPassantable.getFieldLabel().getX() + 97)) + (Turn.getColorToMove() == Color.WHITE ? "6" : "3"));
            }else{
                fen.append(" -");
            }
        }



        fen.append(" " + ruleCounter + " " + fullturn);


        return fen.toString();
    }


    public void getEnpassantLabel(){

    }



    public void register(Observer o) {
        observers.add(o);
    }

    public void unregister(Observer o) {
        observers.remove(o);
    }

    public void notifyObserver(){
        for(Observer o : observers){
            o.update();
        }
    }
    /**
     * Turns Coordinates into the Fieldlabel
     * can Return Null if x or y are invalid
     * @param x x-coordinate of FieldLabel
     * @param y y-Coordinate of FieldLabel
     * @return FieldLabel object or Null
     */
    public FieldLabel getLabelByCoordinates(int x, int y) {
        try { return labels[x][y]; } catch (ArrayIndexOutOfBoundsException ignored){
            //System.out.println(x + " " +y);
        }
        return null;
    }

    /**
     * Getter for Black King
     * @return King Object of Black
     */
    public King getB_King() {
        return b_King;
    }

    /**
     * Getter for White King
     * @return King Object of white
     */
    public King getW_King() {
        return w_King;
    }
    /**
     * Getter for gamestate
     * @return the gamestate
     */
    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Setter for gamestate
     * @param gamestate the current gamestate
     */
    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
        //System.out.println("Current state: " + gamestate);
    }

    public Pawn getEnPassantable() {
        return enPassantable;
    }

    public void setEnPassantable(Pawn enPassantable) {
        this.enPassantable = enPassantable;
    }
}