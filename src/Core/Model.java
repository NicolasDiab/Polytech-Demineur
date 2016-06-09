/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import java.util.Observable;

/**
 *
 * @author Nicolas
 */
public class Model extends Observable {
    Board board;
    String state;
    int score;
    
    public void clic(int row, int col) {
        boolean status = board.gameIsWon();
        if (status) {
            state = "Victory";
            
        } else if(board.squareIsMine(row, col)) {
            state = "Defeat";
        } else {
            state = "Running";
        }
        
        // notification de la vue, suite à la mise à jour des champs state et score
        setChanged();
        notifyObservers();
    }
    
    public String isWon() {
        return state;
    }
    
    public int getScore() {
        return score;
    }
}
