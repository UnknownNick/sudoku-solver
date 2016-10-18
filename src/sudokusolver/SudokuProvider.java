/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 * Class which provides data infrastructure for other modules.
 * @author JH
 */
public class SudokuProvider implements Cloneable {
    
    /**
     * Greatest number used in this sudoku session.
     */
    protected final int digit;
    
    /**
     * Greatest possible digit useable by this provider, is used because some
     * data structures were difficult to write them changeable dynamically.
     */
    protected final int max = 9;
    
    /**
     * Low level container of found numbers.
     */
    protected int[] numbers;
    
    /**
     * Numbers specified at start of session.
     */
    protected int[] firstNumbers;
    
    /**
     * In progress numbers container with additional data used during
     * calculation.
     */
    protected NumberBox[] num;
    
    /**
     * Creates new provider with specified maximal number.
     * @param dg maximum possible number
     */
    public SudokuProvider(int dg) {
        if(dg < 1 || dg >= max) dg = 9;
        digit = dg;
        numbers = new int[digit*digit];
        firstNumbers = new int[digit*digit];
        num = new NumberBox[digit*digit];
        
        for(int i = 0; i < digit*digit ; i++) {
            num[i] = new NumberBox(this.indexToPosition(i));
        }
    }
    
    public SudokuProvider(SudokuProvider sp) {
        this(sp.getMaxDigit());
        this.numbers = sp.numbers;
        this.firstNumbers = sp.firstNumbers;
        this.num = sp.num;
    }
    
    /**
     * Returns number on specified coordinates.
     * @param x X position
     * @param y Y position
     * @return NumberBox on specified coordinates
     */
    public NumberBox getNum(int x, int y) {
        if(x < 0 || y < 0 || x >= digit || y >= digit) return null;
        return this.getColumn(x)[y];
    }
    
    /**
     * Converts index number to position object.
     * @param index input index
     * @return position represented by specified index
     */
    public final Position indexToPosition(int index) {
        return new Position(index % digit, index / digit);
    }
    /**
     * Converts index number to Position of Group.
     * Works well only for digit = 9.
     * @param index
     * @return 
     */
    public Position indexToGroupPosition(int index) {
        return new Position(index % 3, index / 3, PosType.GROUP_POS);
    }
    
    /**
     * Returns index of box on specified coordinates.
     * @param x X position
     * @param y Y position
     * @return index of specified position
     */
    int getNumIndex(int x, int y){
        if(x < 0 || y < 0 || x >= digit || y >= digit) return -1;
        return (digit*y+x);
    }
    
    /**
     * Returns array of all boxes in specified row.
     * @param row index of row, top is 0
     * @return array of NumberBox objects in specified row
     */
    public NumberBox[] getRow(int row) {
        if(row < 0 || row >= digit) return null;
        NumberBox[] n = new NumberBox[digit];
        
        for(int i = 0 ; i < digit ; i++ ) {
            n[i] = this.num[row*digit + i];
        }
        return n;
    }
    
    /**
     * Returns array of all boxes in specified column.
     * @param col index of column, most left is 0
     * @return array of NumberBox objects in specified column
     */
    public NumberBox[] getColumn(int col) {
        if(col < 0 || col >= digit) return null;
        NumberBox[] n = new NumberBox[digit];
        
        for(int i = 0 ; i < digit ; i++ ) {
            n[i] = this.num[i*digit + col];
        }
        return n;
    }
    
    /**
     * Returns array of boxes in specified group
     * @param x X position of group
     * @param y Y position of group
     * @return array of NumberBox objects in specified group
     */
    public NumberBox[] getGroup(int x, int y) {
        if(x < 0 || x > 2 || y < 0 || y > 2) return null;
        int digit = this.digit;
        NumberBox[] n = new NumberBox[digit];
        
        for(int i = 0; i < 3 ; i++) {
            n[i] = this.num[x*3+y*3*digit+i*digit];
            n[i+1] = this.num[x*3+y*3*digit+1+i*digit];
            n[i+2] = this.num[x*3+y*3*digit+2+i*digit];
        }
        return n;
    }
    
    /**
     * Returns array of boxes in specified group
     * @param p Position of group
     * @return array of NumberBox objects in specified group
     */
    public NumberBox[] getGroup(Position p) {
        return this.getGroup(p.getX(), p.getY());
    }
    
    /**
     * Sets a number at start of session.
     * @param x X position of number (column)
     * @param y Y position of number (row)
     * @param number number to add in the box
     * @return false if number or position is corrupt
     */
    public boolean initNum(int x, int y, int number) {
        if(number == 0) return false;
        if(this.getNumIndex(x, y) == -1) return false;
        return num[this.getNumIndex(x, y)].setFinally(number);
    }
    
    /**
     * Sets a number at start of session.
     * @param index position of number (index)
     * @param number number to add in the box
     * @return false if number or position is corrupt
     */
    public boolean initNum(int index, int number) {
        if(index < 0 || index >= digit*digit) return false;
        return num[index].setFinally(number);
    }
    
    /**
     * @return variable digit
     */
    public int getMaxDigit() {
        return this.digit;
    }
    
    /**
     * Class representing groups in sudoku.
     */
    public static class Group {
        /**
         * Position of this group.
         */
        private Position pos;
        
        /**
         * Returns position of this group
         * @return position of this group
         */
        public Position getPos() {
            return this.pos;
        }
    }
    
    /**
     * Class representing row of numbers
     */
    public static class Row {
        /**
         * Position of this row, top is 0.
         */
        private int pos;
        
        /**
         * Returns position of this row.
         * @return position of this row
         */
        public int getPos() {
            return this.pos;
        }
    }
    
    /**
     * Class representing column of numbers.
     */
    public static class Column {
        /**
         * Position of this column, most left is 0.
         */
        private int pos;
        
        /**
         * Returns position of this column.
         * @return position of this column
         */
        public int getPos() {
            return this.pos;
        }
    }
    
    /**
     * Class providing better positioning of boxes etc.
     */
    public static class Position {
        /**
         * X position
         */
        final int x;
        
        /**
         * Y position
         */
        final int y;
        
        /**
         * Enum saying what is this position of.
         */
        final PosType type;
        
        /**
         * Creates new Position object with type of BOX_POS.
         * @param x X coordinate
         * @param y Y coordinate
         */
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
            type = PosType.BOX_POS;
        }
        
        /**
         * Creates new position object of specified type.
         * @param x X coordinate
         * @param y Y coordinate
         * @param type type of this position
         */
        public Position(int x, int y, PosType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
        
        /**
         * Returns x coordinate of this Position object.
         * @return x coordinate
         */
        public int getX() {
            return this.x;
        }
        
        /**
         * Returns y coordinate of this Position object.
         * @return y coordinate
         */
        public int getY() {
            return this.y;
        }
    }
    
    /**
     * Enum providing possible types of positions.
     */
    public static enum PosType {
        /**
         * Marks position of Group
         */
        GROUP_POS,
        /**
         * Marks position of NumberBox
         */
        BOX_POS
    }
    
    /**
     * @return SudokuProvider object with same variable values as current one.
     */
    @Override
    public SudokuProvider clone() {
        return new SudokuProvider(this);
    }
}
