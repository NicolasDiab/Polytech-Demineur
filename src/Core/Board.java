/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.*;

/**
 *
 * @author Nicolas
 */
public class Board {
    private final Square[][] squares;
    private final int rows;
    private final int columns;
    private final int mines;
    
    public Board(int rows, int columns, int nbMine) {
        this.rows = rows;
        this.columns = columns;
        this.mines = nbMine;
        
        squares = new Square[this.rows][this.columns];
        
        // add squares
        addSquares();
        
        // add random mines
        addMines();
    }
    
    // Constructor method. Add squares to the map
    private void addSquares() {
        for(int i=0; i<rows; ++i) {
            for(int j=0; j<columns; ++j) {
                Square square = new Square(i, j);
                squares[i][j] = square;
            }
        }
    }
    
    // Constructor method. Add mines to the squares
    private void addMines() {
        // add "mines" random mines to the board
        Random rn = new Random();
        for(int i=0; i<mines; ++i) {
            int row;
            int column;
            do {
            row = rn.nextInt(rows); // int between 0 and rows-1 / columns-1
            column = rn.nextInt(columns);
            } while(squares[row][column].isMine());
            squares[row][column].setMine(true);
            
            // for each neighbour incrementes the number of neighbour mines
            List<Square> neighbours = getNeighbourSquares(row, column);
            for(Square neighbour : neighbours) {
                neighbour.incrementeNbNeighbourMines();
            }
        }
    }
    
    // return a square's neighbours in a list of squares
    private List<Square> getNeighbourSquares(int row, int column) {
        ArrayList<Square> list = new ArrayList<>();
        
        // loop for 3*3 squares around the middle one
        // if side board is reached don't loop in
        int i = (row == 0) ? row : row-1;
        int maxI = (row == this.rows-1) ? row : row+1;
        int retainJ = (column == 0) ? column : column-1;
        int j = retainJ;
        int maxJ = (column == this.columns-1) ? column : column+1;
        for(; i<=maxI; i++) {
            for(; j<=maxJ; j++) {
                if (i!=column || j!=row) {
                    list.add(squares[i][j]);
                }
            }
            j = retainJ;
        }
        
        return list;
    }
    
    public int clic(int row, int column) {
        Square square = squares[row][column];
        // if players clics on an already visible square, nothing happens
        if (square.isVisible()) {
            return 0;
        }
        
        // if players clics on a mine, return -1, game over
        if (square.isMine()) {
            square.setVisible(true);
            allMinesVisible();
            return -1;
        }
        
        if (square.getNbNeighbourMines() == 0) {
            discoverNeighbours(row, column);
        }
        /*if (gameIsWon()) {
            square.setVisible(true);
            return 1;
        }*/
        
        square.setVisible(true);
        return 0;
    }
    
    // recursive method - turns neighbour squares visible
    // stop recursity if neighbour is a mine or if square is visible (marked)
    public void discoverNeighbours(int row, int column) {
        squares[row][column].setVisible(true);
        
        List<Square> neighbours = getNeighbourSquares(row, column);
        for(Square neighbour : neighbours) {
            if(!neighbour.isMine() && !neighbour.isVisible()) {
                
                if (neighbour.getNbNeighbourMines() > 0) {
                    // has a mine nerby
                    // stop recursivity
                    neighbour.setVisible(true);
                }
                else { // recursivity
                    neighbour.setVisible(true);
                    discoverNeighbours(neighbour.getRow(), neighbour.getColumn());
                }
            }
        }
    }
    
    public boolean gameIsWon() {
        for(int i=0; i<rows; ++i) {
            for(int j=0; j<columns; ++j) {
                Square square = squares[i][j];
                if(!square.isVisible() && !square.isMine()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean squareIsMine(int row, int col) {
        return squares[row][col].isMine();
    }

    public Square[][] getSquares(){
        return squares;
    }
    
    public void putFlag(int row, int col) {
        squares[row][col].setFlag(true);
    }
    
    public void removeFlag(int row, int col) {
        squares[row][col].setFlag(false);
    }
    
    public void allMinesVisible() {
        for(int i=0; i<rows; ++i) {
            for(int j=0; j<columns; ++j) {
                if (squares[i][j].isMine()) {
                    squares[i][j].setVisible(true);
                }
            }
        }
    }
}