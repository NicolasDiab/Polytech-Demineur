package view;

import core.Board;
import core.Model;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import java.util.Observer;
import java.util.Observable;
import core.Model;

public class Main extends Application {
    Model m;
    Text affichage;
    int column;
    int row;
    public Board board;
    public String [] stringMat;

    @Override
    public void start(Stage primaryStage){
        int levelSize = 20;
        m = new Model();

        // gestion du placement (permet de placer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = new GridPane();
        column = 0;
        row = 0;
        affichage = new Text("");
        affichage.setFont(Font.font ("Arial", 20));
        affichage.setFill(Color.RED);
        border.setTop(affichage);

        // la vue observe les "update" du modèle, et réalise les mises à jour graphiques
        m.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                for(int x=0; x<levelSize;++x){
                    for(int y=0; y<levelSize; ++y){
                        if(board.getSquares()[x][y].isFlag()){
                            stringMat[levelSize*y + x] = "F";
                        }
                        if(board.getSquares()[x][y].isVisible()){
                            if(board.getSquares()[x][y].isMine()){
                                stringMat[levelSize*y + x] = "M";
                            }
                            else{
                                stringMat[levelSize*y + x] = board.getSquares()[x][y].getNbNeighbourMines() + "";
                            }
                        }
                    }
                }
                column = 0;
                row = 0;
                // Reconstruction du BOARD
                for (int i=0; i<levelSize*levelSize; ++i) {
                    final Text t = new Text(stringMat[i]);
                    t.setWrappingWidth(30);
                    t.setFont(Font.font ("Arial", 20));
                    t.setTextAlignment(TextAlignment.CENTER);

                    gPane.add(t, row, column++);

                    if (column > levelSize-1) {
                        column = 0;
                        row++;
                    }

                    int finalI = i;
                    t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Double div = (double)finalI/(double)levelSize;
                            int intPart = div.intValue();
                            int decPart = (int)((div*100 - ( intPart * 100)) /5);
                            if(event.getButton() == MouseButton.SECONDARY){
                                m.rightClic(board,  decPart, intPart);
                            }
                            else{
                                m.clic(board,  decPart, intPart);
                            }
                            board = m.getBoard();
                        }
                    });
                }
                affichage.setText(m.isWon());
            }
        });

        board = new Board(levelSize,levelSize,15);

        stringMat = new String[(levelSize*levelSize)];
        for (int x = 0; x<levelSize; ++x){
            for (int y=0;y<levelSize; ++y){
                stringMat[levelSize*y + x] = " ";
            }
        }


        // create buttons
        for (int i=0; i<levelSize*levelSize; ++i) {
            String s = stringMat[i];
            final Text t = new Text(s);
            t.setWrappingWidth(30);
            t.setFont(Font.font ("Verdana", 20));
            t.setTextAlignment(TextAlignment.CENTER);

            gPane.add(t, row, column++);

            if (column > levelSize-1) {
                column = 0;
                row++;
            }

            // listen left and right clicks on affichage
            int finalI = i;
            t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Double div = (double)finalI/(double)levelSize;
                    int intPart = div.intValue();
                    int decPart = (int)((div*100 - ( intPart * 100)) /5);
                    if(event.getButton() == MouseButton.SECONDARY){
                        m.rightClic(board,  decPart, intPart);
                    }
                    else{
                        m.clic(board,  decPart, intPart);
                    }
                    board = m.getBoard();
                }
            });
        }

        gPane.setGridLinesVisible(true);
        border.setCenter(gPane);
        Scene scene = new Scene(border, Color.WHITE);
        primaryStage.setTitle("Démineur Diab / Piat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}