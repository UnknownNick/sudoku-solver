/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import sudokusolver.SuFrame.SuPanel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author pepik
 */
public class MainFrame extends Frame {
    MenuBar mb;
    Menu menuFile;
    MenuItem menuItExit, menuItExport, menuItLoad;
    SuFrame.SuPanel pan;
    int[] input = new int[81], all = new int[81];
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
        
        mb = new MenuBar();
        menuFile = new Menu("File");
        menuItExit = new MenuItem("Exit");
        menuItExport = new MenuItem("Export");
        menuItLoad = new MenuItem("Load");
        pan = new SuPanel(9);
        
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
    
    protected void exit() {
        ExitDialog ex = new ExitDialog(this);
        ex.setLocationRelativeTo(null);
        ex.setVisible(true);
    }
    
    protected void load() throws FileNotFoundException {
        FileDialog fd = new FileDialog(this, "Load sudoku save file",FileDialog.LOAD);
        fd.setFilenameFilter((File dir, String name) -> {
            return name.endsWith(".bin.sudoku");
        });
        String filename = fd.getFile();
        File f = new File(filename);
        Scanner sc = new Scanner(f);
        for(int i = 0; i < 81; i++) input[i] = sc.nextInt();
        for(int i = 0; i < 81; i++) all[i] = sc.nextInt();
    }
    
    protected void export() throws FileNotFoundException, IOException {
        FileDialog fd = new FileDialog(this, "Load sudoku save file",FileDialog.SAVE);
        fd.setFilenameFilter((File dir, String name) -> {
            return name.endsWith(".bin.sudoku");
        });
        fd.setFile("sudoku.bin.sudoku");
        String filename = fd.getFile();
        File f = new File(filename);
        f.createNewFile();
        DataOutputStream ds;
        ds = new DataOutputStream(new FileOutputStream(f));
        for(int i = 0; i < 81; i++) ds.writeInt(input[i]);
        for(int i = 0; i < 81; i++) ds.writeInt(all[i]);
    }
    
    protected void updateNumbers() {
        
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
}
