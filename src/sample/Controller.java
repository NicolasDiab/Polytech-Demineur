package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class Controller {
    public Label coucou;
    public void sayCoucou(ActionEvent actionEvent) {
        coucou.setText(" nouvelle montre");
    }
}
