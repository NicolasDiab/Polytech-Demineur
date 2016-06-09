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
            row = rn.nextInt(10); // int between 0 and 9
            column = rn.nextInt(10);
            } while(squares[row][column].isMine());
            Square square = squares[row][column];
            square.setMine(true);
            
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
        int i = row == 0 ? row : row+1;
        int maxI = row == this.rows ? row : row+1;
        int j = column == 0 ? column : row+1;
        int maxJ = column == this.columns ? column : column+1;
        for(; i<maxI; ++i) {
            for(; j<maxJ; ++i) {
                if (i!=row && j!=column) {
                    list.add(squares[row-1][column-1]);
                }
            }
        }
        
        return list;
    }
    
    public int clic(int row, int column) {
        Square square = squares[row][column];
        // if players clics on a mine, return -1, game over
        if (square.isMine()) {
            return -1;
        }
        // if players clics on an already visible square, nothing happens
        if (square.isVisible()) {
            return 0;
        }
        square.setVisible(true);
        
        discoverNeighbours(row, column);
        if (gameIsWon()) return 1;
        
        return 0;
    }
    
    // recursive method - turns neighbour squares visible
    // stop recursity if neighbour is a mine or if square is visible (marked)
    public void discoverNeighbours(int row, int column) {
        List<Square> neighbours = getNeighbourSquares(row, column);
        for(Square neighbour : neighbours) {
            if(!neighbour.isMine()) {
                // has a mine nerby
                if (neighbour.getNbNeighbourMines() > 0) {
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
            for(int j=0; j<columns; ++i) {
                Square square = squares[i][j];
                if(!square.isVisible() && !square.isMine()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public boolean squareIsMine(int row, int col) {
        if(squares[row][col].isMine()) {
            return true;
        }
        return false;
    }
}