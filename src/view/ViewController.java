package view;

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
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ViewController extends Application {

    Model m;
    Text statusDisplay;
    String[][] stringMat;
    GridPane gPane;
    BorderPane border;
    Image imageSmillingSmiley = new Image("ressources/smileySouriant.png");
    Image imageCryingSmiley = new Image("ressources/smileyPleure.png");
    Image imageDrunkSmiley = new Image("ressources/smileyBourre.jpg");

    @Override
    public void start(Stage primaryStage) {
        int levelSize = 20;
        int nbMines = 40;

        m = new Model();

        border = new BorderPane();
        gPane = new GridPane();

        // Smiley
        StackPane p = new StackPane();
        p.setPrefSize(15, 15);
        ImageView iv = new ImageView(imageSmillingSmiley);
        p.getChildren().add(iv);
        StackPane.setAlignment(iv, Pos.CENTER);

        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    m.resetBoard(levelSize, levelSize, nbMines);
                    stringMat = new String[levelSize][levelSize];
                    for (int x = 0; x < levelSize; ++x) {
                        for (int y = 0; y < levelSize; ++y) {
                            stringMat[x][y] = " ";
                        }
                    }
                    statusDisplay.setText("<RUNNING>");
                    iv.setImage(imageSmillingSmiley);
                    
                    gPane = new GridPane();
                    gPane.setGridLinesVisible(true);
                    border.setBottom(gPane);
                    updateView(gPane, iv, levelSize);
                }
            }
        });
        border.setTop(p);

        statusDisplay = new Text("");
        statusDisplay.setFont(Font.font("Arial", 20));
        statusDisplay.setFill(Color.RED);
        border.setCenter(statusDisplay);

        m.resetBoard(levelSize, levelSize, nbMines);

        stringMat = new String[levelSize][levelSize];
        for (int x = 0; x < levelSize; ++x) {
            for (int y = 0; y < levelSize; ++y) {
                stringMat[x][y] = " ";
            }
        }

        updateView(gPane, iv, levelSize);

        m.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updateView(gPane, iv, levelSize);
            }
        });

        gPane.setGridLinesVisible(true);
        border.setBottom(gPane);
        Scene scene = new Scene(border, Color.WHITE);
        primaryStage.setTitle("Démineur Diab / Piat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateView(GridPane gPane, ImageView iv, int levelSize) {
        for (int row=0; row<levelSize; row++) {
            for (int col=0; col<levelSize; col++) {
                if (m.getBoard().getSquares()[row][col].isFlag()) {
                    
                    
                    
                    stringMat[row][col] = "F";
                }
                if (m.getBoard().getSquares()[row][col].isVisible()) {
                    if (m.getBoard().getSquares()[row][col].isMine()) {
                        stringMat[row][col] = "M";
                    } else {
                        stringMat[row][col] = m.getBoard().getSquares()[row][col]
                                .getNbNeighbourMines() + "";
                    }
                }
                
                final Text t = new Text(stringMat[row][col]);
                t.setWrappingWidth(30);
                t.setFont(Font.font("Arial", 20));
                t.setTextAlignment(TextAlignment.CENTER);
                
                switch (stringMat[row][col]) {
                    case "0":
                        t.setFill(Color.WHEAT);
                        break;
                    case "1":
                        t.setFill(Color.BLUE);
                        break;
                    case "2":
                        t.setFill(Color.GREEN);
                        break;
                    case "3":
                        t.setFill(Color.RED);
                        break;
                    case "4":
                        t.setFill(Color.BLUEVIOLET);
                        break;
                    case "5":
                        t.setFill(Color.FIREBRICK);
                        break;
                    case "6":
                        t.setFill(Color.AQUAMARINE);
                        break;
                    case "7":
                        t.setFill(Color.BLACK);
                        break;
                    case "8":
                        t.setFill(Color.GRAY);
                        break;
                    case "F":
                        t.setFill(Color.ORANGE);
                        break;
                    case "M":
                        t.setFill(Color.CHOCOLATE);
                        break;
                }

                /*HBox hBox = new HBox(10);
                Image mine = new Image("ressources/mine.png");
                ImageView ivSquare = new ImageView(mine);
                hBox.getChildren().addAll(ivSquare);*/
                
                gPane.add(t, row, col);

                int finalRow = row;
                int finalCol = col;
                t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            stringMat[finalRow][finalCol] = " ";
                            m.rightClick(finalRow, finalCol);
                        } else if (event.getButton() == MouseButton.PRIMARY) {
                            m.leftClick(finalRow, finalCol);
                            if (stringMat[finalRow][finalCol].equals("F")) {
                                stringMat[finalRow][finalCol] = " ";
                            } else if (stringMat[finalRow][finalCol].equals(" ")) {
                                stringMat[finalRow][finalCol] = "F";
                            }
                        }
                        if (stringMat[finalRow][finalCol].equals("M")) {
                            iv.setImage(imageCryingSmiley);
                        }
                    }
                });
            
                if (!statusDisplay.getText().equals("<DEFEAT !>")) {
                   statusDisplay.setText(m.getStatus()); 
                }
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
