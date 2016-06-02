package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by GregoirePiat on 02/06/16.
 */
public class Launcher {

    @FXML
    private GridPane mainGridPane;
    @FXML
    private MenuBar mainMenuBar;
    @FXML
    private BorderPane borderPane;

    public Launcher(Stage primaryStage) throws Exception{
        int levelSize = 20;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Démineur - Diab / Piat");
        Scene scene = new Scene(root, 1000, 1000);

        for (int i = 0; i < levelSize; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / levelSize);
            mainGridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < levelSize; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / levelSize);
            mainGridPane.getRowConstraints().add(rowConst);
        }

        //MainGridPane.addRow(1);
/*        Button myButton = new Button();
        myButton.setText("YOLO");
        //mainGridPane.add(myButton,0,0);
        for(int i=0; i<levelSize; ++i){
            mainGridPane.add(new Button(), 0, 0);
        }*/

        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
