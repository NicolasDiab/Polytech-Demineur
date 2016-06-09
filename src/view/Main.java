package view;

import core.Board;
import core.Model;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.Shadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.util.Observer;
import java.util.Observable;

public class Main extends Application {
    // Ajouter la classe modèle
    core.Model m;
    Text affichage;
    int column;
    int row;
    public Board board;
    public String [] stringMat;

    @Override
    public void start(Stage primaryStage){
        int levelSize = 20;
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //primaryStage.setTitle("Démineur - Diab / Piat");
        //Scene scene = new Scene(root, 1000, 1000);

        m = new Model();

        // gestion du placement (permet de placer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane border = new BorderPane();

        // permet de placer les diffrents boutons dans une grille
        GridPane gPane = new GridPane();

        column = 0;
        row = 0;

        affichage = new Text("");
        affichage.setFont(Font.font ("Verdana", 20));
        affichage.setFill(Color.RED);
        border.setTop(affichage);

        // la vue observe les "update" du modèle, et réalise les mises à jour graphiques
        m.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                for(int x=0; x<levelSize;++x){
                    for(int y=0; y<levelSize; ++y){
                        if(board.getSquares()[x][y].isVisible()){
                            if(board.getSquares()[x][y].isMine()){
                                stringMat[levelSize*y + x] = "M";
                            }
                            else if(board.getSquares()[x][y].isFlag()){
                                stringMat[levelSize*y + x] = "F";
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
                    t.setFont(Font.font ("Verdana", 20));
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

                            m.clic(board,  finalI /20, column);
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

            // listen left clicks on affichage
            int finalI = i;
            t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    affichage.setText(affichage.getText() + t.getText());
                    m.clic(board,  finalI /20, column);
                    board = m.getBoard();
                }
            });
        }

        gPane.setGridLinesVisible(true);
        border.setCenter(gPane);
        Scene scene = new Scene(border, Color.GRAY);
        primaryStage.setTitle("Démineur Diab / Piat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
