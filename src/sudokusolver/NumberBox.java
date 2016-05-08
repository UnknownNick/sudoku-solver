/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;
import java.util.*;
/**
 * Represents one box where number should be written, has methods for marking
 * possible numbers, returning, adding and removing them. Can be marked as 
 * finaly matched.
 * @author JH
 */
public class NumberBox {
    /**
     * Greatest possible number.
     */
    protected int maxDigit;
    
    /**
     * The only final number presented by this NumberBox, if not found yet,
     * equals 0.
     */
    protected int finalNumber = 0;
    
    /**
     * Marks if the final number was already set.
     */
    protected boolean found = false;
    
    /**
     * Set of all numbers which can be in this box, implicitly set with all 
     * numbers less or equal maxDigit.
     */
    protected Set<Integer> possible;
    
    /**
     * Position of this NumberBox.
     */
    protected SudokuProvider.Position pos;
    
    /**
     * Creates a new NumberBox with specified coordinates, greatest possible number
     * in this box is 9.
     * @param x X position
     * @param y Y position
     */
    public NumberBox(int x, int y) {
        this.pos = new SudokuProvider.Position(x, y, SudokuProvider.PosType.BOX_POS);
        this.maxDigit = 9;
        int[] digits = {1,2,3,4,5,6,7,8,9};
        possible = Collections.synchronizedSet(new HashSet(9));
        for(int i : digits) {
            possible.add(i);
        }
    }
    
    /**
     * Creates a new NumberBox with specified coordinates, greatest possible number
     * in this box is 9.
     * @param pos position of this box
     */
    public NumberBox(SudokuProvider.Position pos) {
        this.pos = pos;
        this.maxDigit = 9;
        int[] digits = {1,2,3,4,5,6,7,8,9};
        possible = Collections.synchronizedSet(new HashSet(9));
        for(int i : digits) {
            possible.add(i);
        }
    }
    
    /**
     * Checks if specified number can be in this box.
     * @param quarry number which is being checked
     * @return true if parameter is possible here
     */
    public boolean isPossible(int quarry) {
        return possible.contains(quarry);
    }
    
    /**
     * Adds number to set of possibilities.
     * @param num number being added
     * @return false if parameter is out of range or is already present
     */
    public boolean addPossibleNumber(int num) {
        if(num > 0 && num <= maxDigit)
            return possible.add(num);
        else return false;
    }
    
    /**
     * Removes number from set of possibilities.
     * @param num number being removed
     * @return true if number was succesfuly removed
     */
    public boolean removePossibleNumber(int num) {
        return possible.remove(num);
    }
    
    /**
     * Sets this box finally to specified number.
     * @param num final number
     * @return true if succesed
     */
    public boolean setFinally(int num) {
        if(num > 0 && num <= maxDigit)
            return (this.found() ? false : finalNumber == (finalNumber = num));
        else return false;
    }
    
    /**
     * Returns number set to this box.
     * @return final number or 0 if is not set yet
     */
    public int getNum() {
        return finalNumber;
    }
    
    /**
     * Checks if this box was already finally set.
     * @return true if number was already found
     */
    public boolean found() {
        return (found = !(finalNumber == 0));
    }
    
    /**
     * Returns all possibilities.
     * @return array of all integers possible in this box
     */
    public Integer[] getPossibleNums() {
        return possible.toArray(new Integer[0]);
    }
    
    /**
     * @return position of this NumberBox
     */
    public SudokuProvider.Position getPosition() {
        return this.pos;
    }
}
