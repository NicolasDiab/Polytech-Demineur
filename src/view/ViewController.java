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
import javafx.stage.Stage;
import java.util.Observer;
import java.util.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ViewController extends Application {

    Model m;
    GridPane gPane;
    BorderPane root;
    
    Image imageSmillingSmiley = new Image("ressources/smileySouriant.png");
    Image imageCryingSmiley = new Image("ressources/smileyPleure.png");
    Image imageDrunkSmiley = new Image("ressources/smileyBourre.jpg");
    Image imageBlank = new Image("ressources/blank.png");
    Image imageExposed = new Image("ressources/exposed.png");
    Image imageFlag = new Image("ressources/flag.png");
    Image imageHitMine = new Image("ressources/hitmine.png");
    Image imageMine = new Image("ressources/mine.png");
    Image[] tabImageNumbers = new Image[8];
    Image imageWrongMine = new Image("ressources/wrongmine.png");
    ImageView ivSmiley;

    @Override
    public void start(Stage primaryStage) {
        
        tabImageNumbers[0] = new Image("ressources/number1.png");
        tabImageNumbers[1] = new Image("ressources/number2.png");
        tabImageNumbers[2] = new Image("ressources/number3.png");
        tabImageNumbers[3] = new Image("ressources/number4.png");
        tabImageNumbers[4] = new Image("ressources/number5.png");
        tabImageNumbers[5] = new Image("ressources/number6.png");
        tabImageNumbers[6] = new Image("ressources/number7.png");
        tabImageNumbers[7] = new Image("ressources/number8.png");
        
        int nbRow = 30;
        int nbCol = 30;
        int nbMines = nbRow * nbCol / 7;

        m = new Model(nbRow, nbCol, nbMines);

        root = new BorderPane();
        gPane = new GridPane();
        
        // Menu
        MenuBar menuBar = new MenuBar();
        root.setTop(menuBar);
        Menu menu = new Menu("Size");
        
        Slider sliderRow = new Slider(5, 30, 1);
        sliderRow.setShowTickMarks(true);
        sliderRow.setShowTickLabels(true);
        sliderRow.setMajorTickUnit(5f);
        sliderRow.setBlockIncrement(1f);
        
        Slider sliderCol = new Slider(5, 30, 1);
        sliderCol.setShowTickMarks(true);
        sliderCol.setShowTickLabels(true);
        sliderCol.setMajorTickUnit(5f);
        sliderCol.setBlockIncrement(1f);
        
        CustomMenuItem menuSliderRow = new CustomMenuItem(sliderRow);
        menuSliderRow.setHideOnClick(false);
        CustomMenuItem menuSliderCol = new CustomMenuItem(sliderCol);
        menuSliderCol.setHideOnClick(false);
        
        menu.getItems().add(new MenuItem("Number of rows"));
        menu.getItems().add(menuSliderRow);
        menu.getItems().add(new SeparatorMenuItem());
        menu.getItems().add(new MenuItem("Number of columns"));
        menu.getItems().add(menuSliderCol);
        
        menuBar.getMenus().add(menu);
        
        sliderRow.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    int newNbRow = new_val.intValue();
                    int nbCol = m.getColSize();
                    resetView(newNbRow, nbCol, newNbRow * nbCol / 7);
            }
        });
        
        sliderCol.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                    int newNbCol = new_val.intValue();
                    int nbRow = m.getRowSize();
                    resetView(nbRow, newNbCol, nbRow * newNbCol / 7);
            }
        });

        // Smiley
        StackPane p = new StackPane();
        ivSmiley = new ImageView(imageSmillingSmiley);
        p.getChildren().add(ivSmiley);
        p.prefHeight(20);
        StackPane.setAlignment(ivSmiley, Pos.CENTER);

        ivSmiley.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    resetView(m.getRowSize(), m.getColSize(), m.getNbMines());
                }
            }
        });
        root.setCenter(p);

        // grid
        updateView();

        m.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                updateView();
            }
        });

        gPane.setGridLinesVisible(true);
        root.setBottom(gPane);
        
        // Scene
        Scene scene = new Scene(root, Color.WHITE);
        primaryStage.setTitle("Démineur Diab / Piat");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateView() {
        for (int row=0; row<m.getRowSize(); row++) {
            for (int col=0; col<m.getColSize(); col++) {
                HBox hBox = new HBox(10);
                ImageView ivSquare = new ImageView(imageBlank);
                
                if (m.getBoard().getSquares()[row][col].isFlag()) {
                    ivSquare = new ImageView(imageFlag);
                }
                if (m.getBoard().getSquares()[row][col].isVisible()) {
                    if (m.getBoard().getSquares()[row][col].isMine()) {
                        ivSquare = new ImageView(imageMine);
                    } else {
                        int nbNeighbour = m.getBoard().getSquares()[row][col]
                                .getNbNeighbourMines();
                        if (nbNeighbour == 0) {
                            ivSquare = new ImageView(imageExposed);
                        } else {
                            ivSquare = new ImageView(tabImageNumbers[nbNeighbour - 1]);
                        }
                    }
                }
                
                hBox.getChildren().addAll(ivSquare);
                gPane.add(ivSquare, row, col);

                int finalRow = row;
                int finalCol = col;
                ivSquare.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            // RIGHT CLICK
                            m.rightClick(finalRow, finalCol);
                        } else if (event.getButton() == MouseButton.PRIMARY) {
                            // LEFT CLICK
                            m.leftClick(finalRow, finalCol);
                            if (m.getBoard().getSquares()[finalRow][finalCol].isMine()) {
                                ivSmiley.setImage(imageCryingSmiley);
                            }
                        }
                    }
                });
            }
        }
        if (m.getStatus().equals("VICTORY")) {
            ivSmiley.setImage(imageDrunkSmiley);
            
        }
    }
    
    public void resetView (int nbRow, int nbCol, int nbMines) {
        m.resetBoard(nbRow, nbCol, nbMines);
        ivSmiley.setImage(imageSmillingSmiley);

        gPane = new GridPane();
        gPane.setGridLinesVisible(true);
        root.setBottom(gPane);
        updateView();
    }

    public static void main(String[] args) {
        launch();
    }
}
