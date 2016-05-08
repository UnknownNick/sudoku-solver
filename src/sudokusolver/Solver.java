/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 *
 * @author JH
 */
public class Solver {
    protected SudokuProvider sp;
    public Solver(SudokuProvider sp) {
        this.sp = sp;
    }
    
    public SudokuProvider getProvider(){
        return this.sp;
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
    public void check() {
        int[] found;
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getRow(i);
            found = new int[sp.getMaxDigit()];
            for(int u = 0; u < sp.getMaxDigit(); u++) {
                found[u] = n[u].getNum();
            }
            
            for(NumberBox b:n) {
                for(int f:found) {
                    if(f != 0 && b.isPossible(f)) {
                        b.removePossibleNumber(f);
                    }
                }
            }
        }
    }
    
    @Override
    public void checkPossibles() {
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getRow(i);
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                }
            }
        }
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
    public void check() {
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
                    }
                }
            }
        }
    }
    
    @Override
    public void checkPossibles() {
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getColumn(i);
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                }
            }
        }
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
    public void check() {
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
                    }
                }
            }
        }
    }
    
    @Override
    public void checkPossibles() {
        for(int i = 0; i < sp.getMaxDigit(); i++) {
            NumberBox[] n = sp.getGroup(sp.indexToGroupPosition(i));
            for(NumberBox b:n) {
                Integer[] ints = b.getPossibleNums();
                if(ints.length==1){
                    b.setFinally(ints[0]);
                }
            }
        }
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
    public void check() {
        
    }
    
    @Override
    public void checkPossibles() {
        
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
    public void check() {
        
    }
    
    @Override
    public void checkPossibles() {
        
    }
    
}