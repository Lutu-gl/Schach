package view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import model.Chessboard;


/**
 * Controller only for handling the main menu
 * @since 1.0
 * @version 4.6
 */
public class Controller{
    @FXML
    public ImageView myImageView, pieceDesignImageView, fieldDesignImageView;
    public CheckBox checkBox1, checkBox2, checkBox3;
    public TextField time1, time2, inkrementWhite, inkrementBlack;
    public ChoiceBox<String> dropdown1, dropdown2;

    Image myImage2 = new Image(getClass().getResourceAsStream("Ebene6.png"));
    Image myImage = new Image(getClass().getResourceAsStream("Ebene7.png"));

    private ObservableList<String> dropdownList1 = FXCollections.observableArrayList("Weiß", "Schwarz", "Zufall");
    private ObservableList<String> dropdownList2 = FXCollections.observableArrayList("Local", "Host", "Join");

    /**
     * Initialize method which sets the default selected item in the dropdown lists
     */
    public void initialize() {
        dropdown1.setItems(dropdownList1);
        dropdown2.setItems(dropdownList2);
        dropdown1.getSelectionModel().select("Weiß");
        dropdown2.getSelectionModel().select("Local");
        pieceDesignImageView.setImage(new Image(Main.class.getResourceAsStream("/Neo_W_K.png")));
    }


    /**
     * CheckBox handler. Only one checkbox can be marked at the same time
     * @param e ActionEvent of clicked CheckBox
     */
    public void displayImage(ActionEvent e){
        CheckBox clickedBox = (CheckBox) e.getSource();
        //if (clickedBox.getText().equals("Spieler gegen Computer")) {
        System.out.println(clickedBox.getId());
        if (clickedBox.getId().equals("checkBox1")) {
            myImageView.setImage(myImage);
            checkBox2.setSelected(false);
            checkBox3.setSelected(false);
            checkBox1.setSelected(true);
        } else if (clickedBox.getId().equals("checkBox2")) {
            myImageView.setImage(myImage2);
            checkBox1.setSelected(false);
            checkBox3.setSelected(false);
            checkBox2.setSelected(true);
        } else {
            myImageView.setImage(myImage);
            checkBox2.setSelected(false);
            checkBox1.setSelected(false);
            checkBox3.setSelected(true);
        }
    }

    /**
     * Handler for start button. Checks if everything was selected correctly!
     */
    public void startGame() {
        // Falls kein Spielmodus ausgewählt ist
        if (!checkBox1.isSelected() && !checkBox2.isSelected() && !checkBox3.isSelected()) {
            System.out.println("Kein Spielmodus ausgewählt!");
            return;
        }
        // Wenn man einem Spiel joint, muss die Zeiteingabe nicht korrekt sein
        int intTime1 = 0, intTime2 = 0, intInkrement1 = 0, intInkrement2 = 0;
        if (!(checkBox2.isSelected() && dropdown2.getSelectionModel().getSelectedItem().equals("Join"))) {
            // Falls keine Zeit eingegeben wurde
            if (time1.getText().length() <= 0 || time2.getText().length() <= 0) {
                System.out.println("Keine Zeit eingegeben!");
                return;
            }

            if (inkrementWhite.getText().length() <= 0 || inkrementBlack.getText().length() <= 0) {
                System.out.println("Kein Inkrement eingegeben!");
            }

            try {
                intTime1 = Integer.parseInt(time1.getText());
                intTime2 = Integer.parseInt(time2.getText());
                intInkrement1 = Integer.parseInt(inkrementWhite.getText());
                intInkrement2 = Integer.parseInt(inkrementBlack.getText());
            } catch (NumberFormatException e) {
                System.out.println("String statt Zahl eingegeben!");
                return;
            }
        }
        System.out.println("Los geats!!!");
        int size = 8;
        // Set the size and the FEN of the logic chessboard
        Chessboard board = Chessboard.getInstance();

        // Check which gamemode has to start
        boolean whiteAi = true, blackAi = true;
        if (checkBox1.isSelected()) {
            System.out.println(dropdown1.getSelectionModel().getSelectedItem());
            if (dropdown1.getSelectionModel().getSelectedItem().equals("Weiß")) whiteAi = false;
            else if (dropdown1.getSelectionModel().getSelectedItem().equals("Schwarz")) blackAi = false;
            else {
                if (Math.round(Math.random()) == 1) whiteAi = false;
                else blackAi = false;
            }
        } else if (checkBox2.isSelected()) {
            if (dropdown2.getSelectionModel().getSelectedItem().equals("Local")) {
                whiteAi = false;
                blackAi = false;
            } else if (dropdown2.getSelectionModel().getSelectedItem().equals("Host")) {
                hostDialog();
            } else {
                joinDialog();
            }
        }

        //MainLaxe.saveParametersForGame(size, whiteAi, blackAi, intTime1, intTime2, 0, 0);
        MainLaxe.size = size;
        MainLaxe.whiteAi = whiteAi;
        MainLaxe.blackAi = blackAi;
        MainLaxe.timeWhite = intTime1;
        MainLaxe.timeBlack = intTime2;
        MainLaxe.inkrementWhite = intInkrement1;
        MainLaxe.inkrementBlack = intInkrement2;
        MainLaxe.invertBoard = !blackAi&&!checkBox2.isSelected();
        if (!checkBox2.isSelected() || (checkBox2.isSelected() && dropdown2.getSelectionModel().getSelectedItem().equals("Local")))
            MainLaxe.startGame();
    }

    public void handlePieceDesignMenu() {
        System.out.println("Clicked!");
        Dialog<ImageView> dialog = new Dialog<>();
        dialog.setTitle("Designmenu");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.setHeaderText("Wähle ein Design:");
        dialog.getDialogPane().getButtonTypes().add(type);
        PieceDesign[] designs = PieceDesign.values();
        int y = 20, x = 0;
        for (int i = 0; i < designs.length; i++) {
            ImageView imageView = new ImageView(new Image(Main.class.getResourceAsStream("/"+designs[i].getDesign()+"W_K.png")));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
            x = (x + 120) % 360;
            if ((i+1)%3==0)
                y += 120;
            dialog.getDialogPane().getChildren().add(imageView);
        }
        dialog.getDialogPane().setPrefHeight(600);
        dialog.getDialogPane().setPrefWidth(360);
        dialog.show();
    }

    /**
     * Changes to the host menu
     */
    private void hostDialog() {
        MainLaxe.changeScene(MainLaxe.hostScene);
    }

    /**
     * Changes to the join menu
     */
    private void joinDialog() {
        MainLaxe.changeScene(MainLaxe.joinScene);
    }
}