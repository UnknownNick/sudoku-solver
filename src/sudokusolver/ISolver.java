/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 * Interface containing methods for finding concrete numbers and declining 
 * impossibles in sudoku. Class implementing this presents one way of finding
 * new numbers.
 * @author JH
 */
public interface ISolver {
    
    /**
     * Checks all possible numbers in represented part and removes these ones 
     * whitch are not possible in each NumberBox.
     */
    public void check();
    
    /**
     * Finds concrete number in part of sudoku represented by implementing 
     * class.
     * @param num
     * @return Position of the found number or null if it's not there.
     */
    //SudokuProvider.Position checkContainment(int num);
    
    /**
     * Checks possible numbers and if there is only one remaining matches it as
     * final.
     */
    void checkPossibles();
}
