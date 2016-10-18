/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import sudokusolver.SuFrame.SuPanel;
/**
 *
 * @author pepik
 */
public class MainFrame extends Frame {
    /**
     * MenuBar in this Frame.
     */
    MenuBar mb;
    /**
     * Menu "File"
     */
    Menu menuFile;
    /**
     * Menu Item "Exit", calls MainFrame.exit()
     */
    MenuItem menuItExit,
            /**
             * MenuItem "Export", calls MainFrame.export()
             */
            menuItExport,
            /**
             * MenuItem "Load", calls MainFrame.load()
             */
            menuItLoad;
    /**
     * SuPanel for main widow gathering input.
     */
    SuPanel pan;
    /**
     * First initial SudokuProvider.
     */
    SudokuProvider supr;
    /**
     * input numbers
     */
    int[] input = new int[81];
    /**
     * solution
     */
    int[][] all;
    /**
     * Frame showing solution.
     */
    private SolutionFrame sf;
    /**
     * Creates main window of the program.
     */
    public MainFrame() {
        super("Sudoku");
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                exit();
            }
        });
        
        supr = new SudokuProvider(9);
        mb = new MenuBar();
        menuFile = new Menu("File");
        menuItExit = new MenuItem("Exit");
        menuItExport = new MenuItem("Export");
        menuItLoad = new MenuItem("Load");
        pan = new SuPanel(9, supr);
        
        menuItExit.addActionListener((ActionEvent e) -> {
            exit();
        });
        menuItExport.addActionListener((ActionEvent e) -> {
            try {
                export();
            } catch (FileNotFoundException ex) {
                System.err.println("File not found");
            } catch (IOException ex) {
                System.err.println("An error occured while exporting");
            } 
        });
        menuItLoad.addActionListener((ActionEvent e) -> {
            try {
                load();
            } catch (FileNotFoundException ex) {
                System.err.println("File not found");
            } catch (IOException ex) {
                System.err.println("Invalid file.");
            }
        });
        
        this.setMenuBar(mb);
        mb.add(menuFile);
        menuFile.add(menuItExit);
        menuFile.add(menuItExport);
        menuFile.add(menuItLoad);
        this.add(pan);
        
        this.setSize(400, 450);
    }
    
    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }
    
    /**
     * Method called by "Exit" menu
     */
    protected void exit() {
        ExitDialog ex = new ExitDialog(this);
        ex.setLocationRelativeTo(null);
        ex.setVisible(true);
    }
    
    /**
     * Method called by "Load" menu
     * @throws FileNotFoundException
     * @throws IOException 
     */
    protected void load() throws FileNotFoundException, IOException {
        FileDialog fd = new FileDialog(this, "Load sudoku save file",FileDialog.LOAD);
        fd.setFilenameFilter((File dir, String name) -> {
            return name.endsWith(".bin.sudoku");
        });
        fd.setVisible(true);
        String filename = fd.getFile();
        String dir = fd.getDirectory();
        if(filename == null) return;
        File f = new File(dir + System.getProperty("file.separator") + filename);
        DataInputStream ds = new DataInputStream(new FileInputStream(f));
        for(int i = 0; i < 81; i++) input[i] = ds.readInt();
        int[] j = new int[81];
        for(int k = 0; ; k++){
            try{
                for(int i = 0; i < 81; i++) {
                    j[i] = ds.readInt();
                }
                for(int l = 0; l < 81; l++) {
                    all[k][l] = j[l];
                }
            } catch(EOFException ex) {break;}
        }
        updateNumbersLoad(pan,-1);
    }
    
    /**
     * Method called by "Export" menu
     * @throws FileNotFoundException
     * @throws IOException 
     */
    protected void export() throws FileNotFoundException, IOException {
        updateNumbersRead();
        FileDialog fd = new FileDialog(this, "Create sudoku save file",FileDialog.SAVE);
        fd.setFilenameFilter((File dir, String name) -> {
            return name.endsWith(".bin.sudoku");
        });
        fd.setFile("sudoku.bin.sudoku");
        fd.setVisible(true);
        String filename = fd.getFile();
        String dir = fd.getDirectory();
        if(filename == null) return;
        File f = new File(dir + System.getProperty("file.separator") + filename);
        f.createNewFile();
        DataOutputStream ds;
        ds = new DataOutputStream(new FileOutputStream(f));
        for(int i = 0; i < 81; i++) ds.writeInt(input[i]);
        for(int[] j : all){
            for(int k : j){
                ds.writeInt(k);
            }
        }
    }
    
    /**
     * Sets in GUI numbers saved in array.
     * @param pan affected SuPanel
     */
    void updateNumbersLoad(SuPanel pan, int arrindex) {
        if(arrindex == -1)pan.setNumbers(input);
        if(arrindex == 0)pan.setNumbers(all[0]);
    }
    
    /**
     * Reads numbers from GUI and writes them into input array.
     */
    void updateNumbersRead() {
        for(int i = 0; i < input.length ; i++) {
            input[i] = Integer.parseInt((pan.boxes[i].getText().equals("")) ? ("0") : (pan.boxes[i].getText()));
        }
    }
    
    /**
     * Runs the solver.
     */
    private void run() {
        
    }
    
    public static class ExitDialog extends Dialog {
        protected Label l;
        protected Button btExit, btCancel;
        protected Panel p;
        public ExitDialog(Frame f) {
            super(f,true);
            this.setTitle("Exit sudoku solver");
            this.setLayout(new BorderLayout());
            
            l = new Label("Do you really want to exit?");
            btExit = new Button("Yes");
            btCancel = new Button("No");
            p = new Panel();
            
            this.add(l, BorderLayout.NORTH);
            this.add(p, BorderLayout.CENTER);
            
            p.add(btExit);
            p.add(btCancel);
            
            btExit.addActionListener((ActionEvent e) -> {
                System.exit(0);
            });
            
            btCancel.addActionListener((ActionEvent e) -> {
                this.dispose();
            });
            
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    dispose();
                }
            });
            
            this.setSize(180, 90);
            this.setResizable(false);
        }
    }
    
    public class SolutionFrame extends Frame {
        int digit;
        int[] nums;
        SuPanel pan;
        public SolutionFrame(int digit, int[] numbers, SudokuProvider supr) {
            super("Solution");
            
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e){
                    exit();
                }
            });
            
            this.digit = digit;
            if(numbers.length == digit*digit) this.nums = numbers;
            pan = new SuPanel(digit, supr);
            this.add(pan);
            this.setSize(400, 430);
            this.update();
        }
        
        protected void update() {
            pan.setNumbers(nums);
        }
    }
}
