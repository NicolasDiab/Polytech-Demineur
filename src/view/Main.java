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
            public void update(Observable o, Object arg) {;
               if (m.isWon() == "Victory") {
                    affichage.setText(" Victoire !");
                } else if (m.isWon() == "Defeat"){
                    affichage.setText("Défaite !");
                }
                else{
                    affichage.setText("La partie est en cours ... ");
                }
            }
        });
        // on efface affichage lors du clic
        affichage.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                affichage.setText("");
            }
        });


        Board board = new Board(levelSize,levelSize,15);

        String [] stringMat = new String[(levelSize*levelSize)];
        for (int x = 0; x<levelSize; ++x){
            for (int y=0;y<levelSize; ++y){
                stringMat[levelSize*y + x] = " ";
            }
        }


        // création des bouton et placement dans la grille
        for (int i=0; i<levelSize*levelSize; ++i) {
            String s = stringMat[i];
            final Text t = new Text(s);
            t.setWrappingWidth(30);
            t.setFont(Font.font ("Verdana", 20));
            t.setTextAlignment(TextAlignment.CENTER);

            gPane.add(t, column++, row);

            if (column > levelSize-1) {
                column = 0;
                row++;
            }

            // un controleur (EventHandler) par bouton écoute et met à jour le champ affichage
            int finalI = i;
            t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    affichage.setText(affichage.getText() + t.getText());
                    m.clic(board,  finalI /20, column);

                }
            });
        }

/*
        // un controleur écoute le bouton "=" et déclenche l'appel du modèle
        t.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                m.calc(affichage.getText());
            }
        });*/

        gPane.setGridLinesVisible(true);
        border.setCenter(gPane);
        Scene scene = new Scene(border, Color.GRAY);
        primaryStage.setTitle("Démineur Diab / Piat");
        primaryStage.setScene(scene);
        primaryStage.show();


/*        for (int i = 0; i < levelSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / levelSize);
            mainGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < levelSize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / levelSize);
            mainGridPane.getRowConstraints().add(rowConst);
        }*/

        //MainGridPane.addRow(1);
/*        Button myButton = new Button();
        myButton.setText("YOLO");
        //mainGridPane.add(myButton,0,0);
        for(int i=0; i<levelSize; ++i){
            mainGridPane.add(new Button(), 0, 0);
        }*/
        //Launcher launcher = new Launcher(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
