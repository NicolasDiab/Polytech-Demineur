/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.Observable;

/**
 *
 * @author Nicolas
 */
public class Model extends Observable {
    String state;
    int score;
    Board board;
    
    public void clic(Board board, int row, int col) {
        board.removeFlag(row, col);
        System.out.println("Clic gauche");
        this.board = board;
        boolean status = board.gameIsWon();
        if (status) {
            state = "<VICTORY !>";
        } else if(board.squareIsMine(row, col)) {
            board.allMinesVisible();
            state = "<DEFEAT !>";
            board.clic(row, col);
        } else {
            state = "<Running>";
            board.clic(row, col);
        }
        
        setChanged();
        notifyObservers();
    }
    
    public void rightClic(Board board, int row, int col) {
        System.out.println("Clic droit");
        this.board = board;
        Square[][] squares = board.getSquares();
        if (squares[row][col].isFlag()) {
            board.removeFlag(row, col);
        } else {
            board.putFlag(row, col);
        }
        
        setChanged();
        notifyObservers();
    }
    
    public String isWon() {
        return state;
    }
    
    public int getScore() {
        return score;
    }

    public Board getBoard() { return board; }
}
