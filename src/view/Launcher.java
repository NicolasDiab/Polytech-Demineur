package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    @FXML
    private Label lblScore;

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
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
