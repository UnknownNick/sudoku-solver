/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author JH
 */
public class SuFrame extends Frame {
    protected SuPanel pan;
    int digit = 9;
    public SuFrame() {
        this.setLayout(new GridLayout());
        this.setTitle("Sudoku");
        pan = new SuPanel(digit);
        this.add(pan);
        
        this.setSize(400, 400);
        this.setResizable(false);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    static class SuPanel extends Panel {
       TextBox[] boxes;
       int dig;
       private final int max = 9;
       int[] nums;
       SudokuProvider sp;
       
        SuPanel(int digit) {
            if(digit < 1 || digit >= max) digit = 9;
            this.dig = digit;
            boxes = new TextBox[digit*digit];
            nums = new int[digit];
            for(int u = 1 ; u <= digit ; u++ ) nums[u-1] = u;
            this.setLayout(new GridLayout(digit,digit));
            sp = new SudokuProvider(digit);
           
            for( int i = 0 ; i < digit*digit ; i++ ) {
                boxes[i] = new TextBox();
                boxes[i].index = i;
                this.add(boxes[i]);
                
                boxes[i].addActionListener((ActionEvent e) -> {
                    String s = ((TextBox) e.getSource()).getText();
                    int is = 0;
                    try {is = Integer.parseInt(s);
                    } catch(NumberFormatException ex) {System.err.println("Bad value entered.");}
                    if(is < 1 || is > max) ((TextBox) e.getSource()).setText("");
                    if(!s.equals("")) sp.initNum(((TextBox) e.getSource()).index, is);
                });
                
            }
            this.initColors();
        }
        
        SuPanel(int digit, SudokuProvider supr) {
            if(digit < 1 || digit >= max) digit = 9;
            this.dig = digit;
            boxes = new TextBox[digit*digit];
            nums = new int[digit];
            for(int u = 1 ; u <= digit ; u++ ) nums[u-1] = u;
            this.setLayout(new GridLayout(digit,digit));
            sp = supr;
           
            for( int i = 0 ; i < digit*digit ; i++ ) {
                boxes[i] = new TextBox();
                boxes[i].index = i;
                this.add(boxes[i]);
                
                boxes[i].addActionListener((ActionEvent e) -> {
                    String s = ((TextBox) e.getSource()).getText();
                    int is = 0;
                    try {is = Integer.parseInt(s);
                    } catch(NumberFormatException ex) {System.err.println("Bad value entered.");}
                    if(is < 1 || is > max) ((TextBox) e.getSource()).setText("");
                    if(!s.equals("")) sp.initNum(((TextBox) e.getSource()).index, is);
                });
                
            }
            this.initColors();
        }
        
        /**
         * Initialises colors in the panel.
         */
        private void initColors() {
            int[] i1 = {9,8,7,3,2,1};
            int[] i3 = {4,5,6};
            int[] i2 = {1,2,3,7,8,9};
            for(int u2 : i2) {
                for(int u1 : i1) {
                    boxes[u2*9-u1].setBackground(Color.GRAY);
                }
            }
            for(int u3 : i3) {
                for(int u3_1 : i3) {
                    boxes[u3*9-u3_1].setBackground(Color.GRAY);
                }
            }
        }
        
        public int getDigit() {
            return this.dig;
        }
        
        public void setNumbers(int[] nums) {
            if(nums.length == (dig*dig))
                for(int i = 0; i < this.dig * this.dig; i++) {
                    boxes[i].setText(Integer.toString(nums[i]));
                }
            else {
                System.err.println("Bad int array provided.");
            }
        }
    }
    
    /**
     * Box used in sudoku panel.
     */
    static class TextBox extends TextField {
        protected int index;
    }
    
    /**
     * GUI test main.
     */
    public static void main(String[] args) {
        new SuFrame().setVisible(true);
    }
}
