/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 *
 * @author Nicolas
 */
public class Square {
    private boolean mine;
    
    private boolean visible;
    private boolean flag;
    private int nbNeighbourMines;
    private int row;
    private int column;
    
    public Square(int row, int column) {
        this(false, row, column);
    }
    
    public Square(boolean mine, int row, int column) {
        this.row = row;
        this.column = column;
        this.mine = mine;
        this.flag = false;
        this.visible = false;
        this.nbNeighbourMines = 0;
    }

    /**
     * @return the mine
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * @param mine the mine to set
     */
    public void setMine(boolean mine) {
        this.mine = mine;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the flag
     */
    public boolean isFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * @return the nbNeighbour
     */
    public int getNbNeighbourMines() {
        return nbNeighbourMines;
    }

    /**
     * @param nbNeighbourMines the nbNeighbourMines to set
     */
    public void setNbNeighbourMines(int nbNeighbourMines) {
        this.nbNeighbourMines = nbNeighbourMines;
    }

    public void incrementeNbNeighbourMines() {
        this.nbNeighbourMines += 1;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }
    
    
}
