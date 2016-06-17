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
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class ViewController extends Application {

    Model m;
    Text statusDisplay;
    int column;
    int row;
    public Board board;
    public String[] stringMat;
    GridPane gPane;
    BorderPane border;

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
        Image image = new Image("ressources/smileySouriant.png");
        ImageView iv = new ImageView(image);
        p.getChildren().add(iv);
        StackPane.setAlignment(iv, Pos.CENTER);

        p.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    board = new Board(levelSize, levelSize, nbMines);
                    stringMat = new String[(levelSize * levelSize)];
                    for (int x = 0; x < levelSize; ++x) {
                        for (int y = 0; y < levelSize; ++y) {
                            stringMat[levelSize * y + x] = " ";
                        }
                    }
                    statusDisplay.setText("<RUNNING>"); 
                    Image image = new Image("ressources/smileySouriant.png");
                    iv.setImage(image);
                    
                    gPane = new GridPane();
                    gPane.setGridLinesVisible(true);
                    border.setBottom(gPane);
                    updateView(gPane, iv, levelSize);
                }
            }
        });
        border.setTop(p);

        column = 0;
        row = 0;
        statusDisplay = new Text("");
        statusDisplay.setFont(Font.font("Arial", 20));
        statusDisplay.setFill(Color.RED);
        border.setCenter(statusDisplay);

        board = new Board(levelSize, levelSize, nbMines);

        stringMat = new String[(levelSize * levelSize)];
        for (int x = 0; x < levelSize; ++x) {
            for (int y = 0; y < levelSize; ++y) {
                stringMat[levelSize * y + x] = " ";
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
        for (int x = 0; x < levelSize; ++x) {
            for (int y = 0; y < levelSize; ++y) {
                if (board.getSquares()[x][y].isFlag()) {
                    stringMat[levelSize * y + x] = "F";
                }
                if (board.getSquares()[x][y].isVisible()) {
                    if (board.getSquares()[x][y].isMine()) {
                        stringMat[levelSize * y + x] = "M";
                    } else {
                        stringMat[levelSize * y + x] = board.getSquares()[x][y].getNbNeighbourMines() + "";
                    }
                }
            }
        }
        column = 0;
        row = 0;
        // Reconstruction du BOARD
        for (int i = 0; i < levelSize * levelSize; ++i) {
            final Text t = new Text(stringMat[i]);
            t.setWrappingWidth(30);
            t.setFont(Font.font("Arial", 20));
            t.setTextAlignment(TextAlignment.CENTER);

            switch (stringMat[i]) {
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

            gPane.add(t, row, column++);

            if (column > levelSize - 1) {
                column = 0;
                row++;
            }

            int finalI = i;
            t.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Double div = (double) finalI / (double) levelSize;
                    int intPart = div.intValue();
                    // on multiplie pour éviter des erreurs d'arrondi liées à Java
                    // on gère 3 décimales
                    int decPart = (int) ((div * 1000 - (intPart * 1000)) / 50);
                    if (event.getButton() == MouseButton.SECONDARY) {
                        stringMat[finalI] = " ";
                        m.rightClick(board, decPart, intPart);
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        m.leftClick(board, decPart, intPart);
                    }
                    board = m.getBoard();
                    if (stringMat[finalI].equals("M")) {
                        Image image = new Image("ressources/smileyPleure.png");
                        iv.setImage(image);
                    }
                }
            });
        }
        if (!statusDisplay.getText().equals("<DEFEAT !>")) {
           statusDisplay.setText(m.isWon()); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
