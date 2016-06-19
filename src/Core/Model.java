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
    
    public Model(int rowSize, int colSize, int nbMines) {
        super();
        this.state = "NEW GAME";
        this.board = new Board(rowSize, colSize, nbMines);
        this.score = 0;
        
        setChanged();
        notifyObservers();
    }

    public void leftClick(int row, int col) {
        if (!state.equals("DEFEAT") && !state.equals("VICTORY")) {
            board.removeFlag(row, col);
            boolean status = board.gameIsWon();
            if (status) {
                state = "VICTORY";
            } else if (board.squareIsMine(row, col)) {
                board.allMinesVisible();
                state = "DEFEAT";
                board.clic(row, col);
            } else {
                state = "RUNNING";
                board.clic(row, col);
            }
        }

        setChanged();
        notifyObservers();
    }

    public void rightClick(int row, int col) {
        Square[][] squares = board.getSquares();
        if (squares[row][col].isFlag()) {
            board.removeFlag(row, col);
        } else {
            board.putFlag(row, col);
        }

        setChanged();
        notifyObservers();
    }
    
    public void resetBoard(int rowSize, int colSize, int nbMines) {
        state =  "NEW GAME";
        board = new Board(rowSize, colSize, nbMines);
        
        setChanged();
        notifyObservers();
    }

    public String getStatus() {
        return state;
    }

    public int getScore() {
        return score;
    }

    public Board getBoard() {
        return board;
    }
    
    public int getRowSize() {
        return board.getRows();
    }
    
    public int getColSize() {
        return board.getColumns();
    }
    
    public int getNbMines() {
        return board.getMines();
    }
}
