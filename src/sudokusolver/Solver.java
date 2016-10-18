/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

import sudokusolver.SudokuProvider.Position;

/**
 *
 * @author JH
 */
public class Solver {
    protected SudokuProvider sp;
    /**
     * Creates new Solver using specified SudokuProvider.
     * @param sp 
     */
    public Solver(SudokuProvider sp) {
        this.sp = sp;
    }
    /**
     * @return SudokuProvider of this Solver.
     */
    public SudokuProvider getProvider(){
        return this.sp;
    }
    /**
     * Finds point where calculation breaks and splits it in two numbers for future
     * evaluation.
     * @return ColisionPoint where calculation splits.
     */
    public ColisionPoint findColisionPoint() {
        Position p = null;
        int c = -1, n = -1;
        //DODELAT
        
        
        return new ColisionPoint(p,c,n);
    }
    /**
     * ID of the last created thread.
     */
    static int lastThreadId = 0;
    
    public class ColisionPoint {
        /**
         * Position of NumberBox causing colision.
         */
        final Position pos;
        /**
         * Old thread's number to continue calculation.
         */
        final int num1;
        /**
         * New thread's number to continue calculation.
         */
        final int num2;
        /**
         * @param p position of NumberBox causing colision.
         * @param c new evaluation number for current thread.
         * @param n new evaluation number for new thread.
         */
        ColisionPoint(Position p, int c, int n) {
            pos = p;
            num1 = c;
            num2 = n;
        }
        
        /**
         * @return number for new thread's future evaluation.
         */
        public int getNewThreadsNumber() {
            return num2;
        }
        
        /**
         * @return nuber for current thread's evaluation.
         */
        public int getOldThreadsNumber() {
            return num1;
        }
        
        /**
         * @return position of NumberBox breaking calculation.
         */
        public Position getPosition() {
            return pos;
        }
        
        /**
         * Similar to ColisionPoint.getPosition().getX
         * @return X coordinate of NumberBox breaking evaluation
         */
        public int getX() {
            return pos.getX();
        }
        
        /**
         * Similar to ColisionPoint.getPosition().getY
         * @return Y coordinate of NumberBox breaking evaluation
         */
        public int getY() {
            return pos.getY();
        }
    }
    
    class Thread extends java.lang.Thread {
        /**
         * Serial number of this thread, first has 1, every new's is Solver.lastThreadId + 1.
         */
        int ID;
        /**
         * Numbers similar to those in local instance of SudokuProvider.
         */
        NumberBox[] nums;
        /**
         * Local SudokuProvider instance used for calculation.
         */
        SudokuProvider sp;
        /**
         * bool marking if last loop changed something in nums or sp
         */
        boolean changed = true;
        /**
         * bool marking if solution was already found in this thread.
         */
        boolean done = false;
        /**
         * Local RowChecker instance used for calculation.
         */
        RowChecker rc;
        /**
         * Local ColumnChecker instance used for calculation.
         */
        ColumnChecker cc;
        /**
         * Local GroupChecker instance used for calculation.
         */
        GroupChecker gc;
        /**
         * Solver instance given to rc, cc and gc.
         */
        Solver s;
        
        Thread(NumberBox[] input, SudokuProvider sp, int id) {
            nums = input.clone();
            this.sp = sp.clone();
            ID = id;
            s = new Solver(this.sp);
            rc = new RowChecker(s);
            cc = new ColumnChecker(s);
            gc = new GroupChecker(s);
        }
        
        @Override
        public void run() {
            while(!done) {
                if(changed) {
                    //Run actual checks.
                    changed = rc.check() | cc.check() | gc.check() | rc.checkPossibles() | cc.checkPossibles() | gc.checkPossibles();
                } else {
                    //Split evaluation when current thread failed.
                    split();
                }
            }
        }
        
        /**
         * Splits the calculation into two threads, current and new.
         */
        Thread split() {
            Thread t = new Thread(nums,sp,++Solver.lastThreadId);
            t.start();
            return t;
        }
        
        /**
         * @return true if thread has found solution of the sudoku.
         */
        boolean calculationDone() {
            boolean finished = true;
            for(NumberBox n : nums) {
                if(!n.found()) finished = false;
            }
            return finished;
        }
    }
}

class RowChecker implements ISolver {
    protected Solver slv;
    protected SudokuProvider sp;
    RowChecker(Solver parentSolver) {
        slv = parentSolver;
        sp = slv.getProvider();
    }

    @Override
    public boolean check() {
        boolean changed = false;
        int tmp;
        NumberBox tmpbox = null;
        int[] found;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getRow(i);
            found = new int[sp.getMaxDigit()];
            for(int u = 0; u < sp.getMaxDigit(); u++) {
                found[u] = n[u].getNum();
            }
            /*
            * Checks if there are still possible numbers on position where they
            * can't be in this Row, because the number has already been found.
            */
            for(NumberBox b:n) {
                for(int f:found) {
                    if(f != 0 && b.isPossible(f)) {
                        b.removePossibleNumber(f);
                        changed = true;
                    }
                }
            }
            /*
             * Checks if any number is possible anly once here and then sets 
             * it finally. 
             */
            for(int u = 1; u <= sp.getMaxDigit(); u++) {
                tmp = 0;
                tmpbox = null;
                for(NumberBox b:n) {
                    if(b.isPossible(u)) {
                        tmp++;
                        if(tmp == 1) tmpbox = b;
                    }
                }
                if(tmp == 1 && tmpbox != null) {
                    tmpbox.setFinally(u);
                    changed = true;
                }
            }
        }
        return changed;
    }
    
    @Override
    public boolean checkPossibles() {
        boolean changed = false;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getRow(i);
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                    changed = true;
                }
            }
        }
        return changed;
    }
    
}

class ColumnChecker implements ISolver {
    protected Solver slv;
    protected SudokuProvider sp;
    ColumnChecker(Solver parentSolver) {
        slv = parentSolver;
        sp = slv.getProvider();
    }
    @Override
    public boolean check() {
        boolean changed = false;
        int tmp;
        NumberBox tmpbox = null;
        int[] found;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getColumn(i);
            found = new int[sp.getMaxDigit()];
            for(int u = 0; u < sp.getMaxDigit(); u++) {
                found[u] = n[u].getNum();
            }
            
            for(NumberBox b:n) {
                for(int f:found) {
                    if(f != 0 && b.isPossible(f)) {
                        b.removePossibleNumber(f);
                        changed = true;
                    }
                }
            }
            /*
             * Checks if any number is possible anly once here and then sets 
             * it finally. 
             */
            for(int u = 1; u <= sp.getMaxDigit(); u++) {
                tmp = 0;
                tmpbox = null;
                for(NumberBox b:n) {
                    if(b.isPossible(u)) {
                        tmp++;
                        if(tmp == 1) tmpbox = b;
                    }
                }
                if(tmp == 1 && tmpbox != null) {
                    tmpbox.setFinally(u);
                    changed = true;
                }
            }
        }
        return changed;
    }
    
    @Override
    public boolean checkPossibles() {
        boolean changed = false;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getColumn(i);
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                    changed = true;
                }
            }
        }
        return changed;
    }
    
}

class GroupChecker implements ISolver {
    protected Solver slv;
    protected SudokuProvider sp;
    GroupChecker(Solver parentSolver) {
        slv = parentSolver;
        sp = slv.getProvider();
    }
    @Override
    public boolean check() {
        boolean changed = false;
        int tmp;
        NumberBox tmpbox = null;
        int[] found;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getGroup(sp.indexToGroupPosition(i));
            found = new int[sp.getMaxDigit()];
            for(int u = 0; u < sp.getMaxDigit(); u++) {
                found[u] = n[u].getNum();
            }
            
            for(NumberBox b:n) {
                for(int f:found) {
                    if(f != 0 && b.isPossible(f)) {
                        b.removePossibleNumber(f);
                        changed = true;
                    }
                }
            }
            /*
             * Checks if any number is possible anly once here and then sets 
             * it finally. 
             */
            for(int u = 1; u <= sp.getMaxDigit(); u++) {
                tmp = 0;
                tmpbox = null;
                for(NumberBox b:n) {
                    if(b.isPossible(u)) {
                        tmp++;
                        if(tmp == 1) tmpbox = b;
                    }
                }
                if(tmp == 1 && tmpbox != null) {
                    tmpbox.setFinally(u);
                    changed = true;
                }
            }
        }
        
        return changed;
    }
    
    @Override
    public boolean checkPossibles() {
        boolean changed = false;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getGroup(sp.indexToGroupPosition(i));
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                    changed = true;
                }
            }
        }
        return changed;
    }
    
}

class NonFullGroupRowChecker implements ISolver {
    protected Solver slv;
    protected SudokuProvider sp;
    NonFullGroupRowChecker(Solver parentSolver) {
        slv = parentSolver;
        sp = slv.getProvider();
    }
    @Override
    public boolean check() {
        boolean changed = false;
        return changed;
    }
    
    @Override
    public boolean checkPossibles() {
        boolean changed = false;
        return changed;
    }
    
}

class NonFullGroupColumnChecker implements ISolver {
    protected Solver slv;
    protected SudokuProvider sp;
    NonFullGroupColumnChecker(Solver parentSolver) {
        slv = parentSolver;
        sp = slv.getProvider();
    }
    @Override
    public boolean check() {
        boolean changed = false;
        return changed;
    }
    
    @Override
    public boolean checkPossibles() {
        boolean changed = false;
        return changed;
    }
    
}